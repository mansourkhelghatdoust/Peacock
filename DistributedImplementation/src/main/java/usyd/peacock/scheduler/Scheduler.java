package usyd.peacock.scheduler;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import org.apache.thrift.TException;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import usyd.distributed.scheduler.peacock.thrift.FrontendServices;
import usyd.distributed.scheduler.peacock.thrift.NodeMonitorServices;
import usyd.distributed.scheduler.peacock.thrift.SchedulerServices;
import usyd.distributed.scheduler.peacock.thrift.TEnqueueProbeRequest;
import usyd.distributed.scheduler.peacock.thrift.TGlobalStateInfo;
import usyd.distributed.scheduler.peacock.thrift.TLightProbe;
import usyd.distributed.scheduler.peacock.thrift.TSchedulingRequest;
import usyd.distributed.scheduler.peacock.thrift.TTaskLaunchSpec;
import usyd.peacock.common.ClientConnector;
import usyd.peacock.common.FrontendMessage;
import usyd.peacock.common.NodeMonitorMessage;
import usyd.peacock.common.SchedulerMessage;
import usyd.peacock.util.LogUtil;
import usyd.peacock.util.Network;
import usyd.peacock.util.TClients;
import usyd.distributed.scheduler.peacock.thrift.TGlobalStateUpdateRequest;

public class Scheduler {

	private CopyOnWriteArrayList<InetSocketAddress> schedulerSockets = Lists.newCopyOnWriteArrayList();

	private CopyOnWriteArrayList<InetSocketAddress> nodeMonitorSockets = Lists.newCopyOnWriteArrayList();

	private ScheduledJobs scheduledJobs = new ScheduledJobs();

	private SharedState sharedState = null;

	private ClientConnector<FrontendServices.AsyncClient, FrontendMessage> frontendConnector;

	private ClientConnector<SchedulerServices.AsyncClient, SchedulerMessage> schedulerConnector;

	private ClientConnector<NodeMonitorServices.AsyncClient, NodeMonitorMessage> nodeMonitorConnector;

	private Integer listeningPort;

	private String hostIPAddr;

	public Scheduler(String hostIPAddr, String schedulers, SharedState sharedState,
			ClientConnector<FrontendServices.AsyncClient, FrontendMessage> frontendConnector,
			ClientConnector<SchedulerServices.AsyncClient, SchedulerMessage> schedulerConnector,
			ClientConnector<NodeMonitorServices.AsyncClient, NodeMonitorMessage> nodeMonitorConnector,
			Integer listeningPort) {

		Optional<List<InetSocketAddress>> addresses = Network.strToSockets(schedulers);

		if (addresses.isPresent())
			schedulerSockets.addAll(addresses.get());

		this.sharedState = sharedState;

		this.frontendConnector = frontendConnector;

		this.schedulerConnector = schedulerConnector;

		this.nodeMonitorConnector = nodeMonitorConnector;

		this.listeningPort = listeningPort;

		this.hostIPAddr = hostIPAddr;

		//TClients.createInternalConcurrentService("MissedProbesReAllocator",
		//		new MissedProbesReallocator(hostIPAddr, this), true);

	}

	public void submitJob(TSchedulingRequest request) throws TException {

		sharedState.updateGlobalInfo(request);

		request.enterTime = System.currentTimeMillis();

		request.maximumWaitingTime = sharedState.getAvgWorkload();

		scheduledJobs.addSchedulingRequest(request);

		FairProbesAllocator allocator = new FairProbesAllocator(hostIPAddr, request, nodeMonitorSockets, sharedState,
				listeningPort, false);

		while (allocator.hasNext()) {

			Map.Entry<InetSocketAddress, TEnqueueProbeRequest> entry = allocator.next();

			nodeMonitorConnector.sendMessage(new EnqueueProbeRequestMessage(entry.getKey(), entry.getValue()));

		}

		updateSchedulers(request.getTasksSize(), request.estimatedDuration, true);

	}

	public void notifyTaskStatusChange(TLightProbe probe, ByteBuffer message, int status) throws TException {

		try {

			sharedState.updateGlobalInfo(1, scheduledJobs.getJobEstimationExecutionTime(probe.jobId));

			updateSchedulers(1 , scheduledJobs.getJobEstimationExecutionTime(probe.jobId), false);

			// Job already has been finished. It may be because of probes
			// submitted by fault-tolerance services.
			if (scheduledJobs.isJobFinished(probe.jobId))
				return;

			if (status > 0) {

				Optional<InetSocketAddress> fronendSocket = Network
						.strToSocket(scheduledJobs.getJobFrontendAddress(probe.jobId));

				frontendConnector
						.sendMessage(new NotifyTaskStatusChangeMessage(fronendSocket.get(), probe, message, status));

			}

			// task finished
			if (status == 2) {
				scheduledJobs.markTaskAsFinished(probe.jobId, probe.taskId);
				// Job finished
				scheduledJobs.markJobIfIsFinished(probe.jobId);
			}

		} catch (Exception e) {
			LogUtil.logFunctionCall(e.getMessage() + " " + e.getStackTrace());
		}

	}

	private void updateSchedulers(int numOfTasks, long estimationExecutionTime, boolean added) {

		schedulerSockets.stream().forEach(new Consumer<InetSocketAddress>() {

			@Override
			public void accept(InetSocketAddress schedulerSocket) {

				try {

					schedulerConnector.sendMessage(new BroadCastSharedStateMessage(schedulerSocket,
							new TGlobalStateUpdateRequest(numOfTasks, estimationExecutionTime, added)));

				} catch (Exception e) {

					LogUtil.logFunctionCall("Error in broadcasting Global Info, but continue executing");

					e.printStackTrace();

				}
			}
		});

	}

	public TTaskLaunchSpec getTask(String jobId, String taskId) throws TException {

		if (scheduledJobs.markJobIfIsFinished(jobId) || scheduledJobs.ifTaskHasBeenFinished(jobId, taskId)
				|| scheduledJobs.taskIsBeingExecuted(jobId, taskId)) {

			LogUtil.logInfo(jobId + " " + taskId, "NoopTasks.txt");
			
			return new TTaskLaunchSpec(null, null, null,
					new TGlobalStateInfo(sharedState.getQueueSize(), sharedState.getAvgWorkload(), -1));
		}

		ByteBuffer spec = scheduledJobs.getTaskData(jobId, taskId);

		return new TTaskLaunchSpec(taskId, jobId, spec, sharedState.getGlobalStateInfo());

	}

	public void registerScheduler(String schedulerAddress) {

		Optional<InetSocketAddress> address = Network.strToSocket(schedulerAddress);
		if (address.isPresent())
			schedulerSockets.add(address.get());

	}

	public void unRegisterScheduler(String schedulerAddress) throws TException {

		Optional<InetSocketAddress> address = Network.strToSocket(schedulerAddress);

		if (address.isPresent())
			schedulerSockets.remove(address.get());

	}

	public void registerNodeMonitor(String nodeMonitorAddress) {

		Optional<InetSocketAddress> address = Network.strToSocket(nodeMonitorAddress);

		if (address.isPresent())
			nodeMonitorSockets.add(address.get());

		sharedState.setNumberOfWorkerNodes(nodeMonitorSockets.size());

	}

	public void unRegisterNodeMonitor(String nodeMonitorAddress) {

		Optional<InetSocketAddress> address = Network.strToSocket(nodeMonitorAddress);

		if (address.isPresent())
			nodeMonitorSockets.remove(address.get());

		sharedState.setNumberOfWorkerNodes(nodeMonitorSockets.size());

	}

	public ScheduledJobs scheduledJobs() {
		return scheduledJobs;
	}

	public Integer getListeningPort() {
		return listeningPort;
	}

	public CopyOnWriteArrayList<InetSocketAddress> getNodeMonitorSockets() {
		return nodeMonitorSockets;
	}

	public SharedState getSharedState() {
		return sharedState;
	}

	public ClientConnector<NodeMonitorServices.AsyncClient, NodeMonitorMessage> getNodeMonitorConnector() {
		return nodeMonitorConnector;
	}

	public class ScheduledJobs {

		private ConcurrentMap<String, TSchedulingRequest> scheduledJobs = Maps.newConcurrentMap();

		private ConcurrentMap<String, Long> executingTasks = Maps.newConcurrentMap();

		public ScheduledJobs() {

		}

		public boolean taskCanBeResubmitted(String jobId, String taskId) {
			return !taskIsBeingExecuted(jobId, taskId) || executionOfTheTaskIsTakingMoreThanUsual(jobId, taskId);
		}

		public void clearExecutingTask(String jobId, String taskId) {
			executingTasks.remove(jobId + ":" + taskId);
		}

		public boolean executionOfTheTaskIsTakingMoreThanUsual(String jobId, String taskId) {
			return executingTasks.get(jobId + ":" + taskId) * 2 > System.currentTimeMillis();
		}

		public void addSchedulingRequest(TSchedulingRequest request) {
			scheduledJobs.put(request.jobId, request);
		}

		public boolean isJobFinished(String jobIb) {
			return scheduledJobs.get(jobIb) == null;
		}

		public int getJobEstimationExecutionTime(String jobIb) {
			return scheduledJobs.get(jobIb).getEstimatedDuration();
		}

		public String getJobFrontendAddress(String jobIb) {
			return scheduledJobs.get(jobIb).getFrontendAddress();
		}

		public boolean markJobIfIsFinished(String jobId) {

			if (scheduledJobs.get(jobId) == null || scheduledJobs.get(jobId).getTasks().size() == 0) {
				scheduledJobs.remove(jobId);
				return true;
			}
			return false;

		}

		public void markTaskAsFinished(String jobId, String taskId) {

			executingTasks.remove(jobId + ":" + taskId);

			if (scheduledJobs.get(jobId) != null)
				scheduledJobs.get(jobId).getTasks().remove(taskId);

		}

		public boolean taskIsBeingExecuted(String jobId, String taskId) {
			return executingTasks.containsKey(jobId + ":" + taskId);
		}

		public boolean ifTaskHasBeenFinished(String jobId, String taskId) {
			return scheduledJobs.get(jobId).getTasks().get(taskId) == null;
		}

		public ByteBuffer getTaskData(String jobId, String taskId) {

			executingTasks.put(jobId + ":" + taskId,
					System.currentTimeMillis() + (scheduledJobs.get(jobId).estimatedDuration));

			return scheduledJobs.get(jobId).getTasks().get(taskId);

		}

		public ConcurrentMap<String, TSchedulingRequest> getScheduledJobs() {
			return scheduledJobs;
		}

	}
}
