package usyd.peacock.nodemonitor;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.thrift.TException;

import usyd.distributed.scheduler.peacock.thrift.TEnqueueProbeRequest;
import usyd.distributed.scheduler.peacock.thrift.THostPort;
import usyd.distributed.scheduler.peacock.thrift.TLocalFullProbe;
import usyd.distributed.scheduler.peacock.thrift.TRotateProbesRequest;
import usyd.distributed.scheduler.peacock.thrift.SchedulerServices;
import usyd.peacock.common.ClientConnector;
import usyd.peacock.common.SchedulerMessage;
import usyd.peacock.util.LogUtil;
import usyd.peacock.util.Network;
import usyd.peacock.util.ObjectCloner;
import usyd.peacock.util.TClients;

public class NodeMonitor {

	private SharedState sharedstate = null;

	private PeacockQueue queue = null;

	private ClientConnector<SchedulerServices.AsyncClient, SchedulerMessage> schedulerConnector = null;

	private Integer listeningPort;

	private Integer getTaskPort;
	
	private String hostIPAddr;

	public NodeMonitor(String hostIPAddr, String schedulers, Integer getTaskPort,
			ClientConnector<SchedulerServices.AsyncClient, SchedulerMessage> schedulerConnector, Integer listeningPort,
			Queue<THostPort> neighbors, int rotationInterval) {

		this.queue = new PeacockQueue();

		this.sharedstate = new SharedState(queue);

		this.queue.setSharedState(sharedstate);

		this.schedulerConnector = schedulerConnector;

		this.listeningPort = listeningPort;

		this.getTaskPort = getTaskPort;
		
		this.hostIPAddr = hostIPAddr;
		
		register(schedulers);

		TClients.createInternalConcurrentService("ProbeRotationService", new ProbeRotationService(queue, neighbors,
				sharedstate, rotationInterval, schedulers, schedulerConnector), true);

	}

	private void register(String schedulers) {

		String nodeMonitorAddress;

		try {

			nodeMonitorAddress = hostIPAddr + ":" + listeningPort;

			String[] schedulerList = schedulers.split(",");

			CopyOnWriteArrayList<String> unregisteredSchedulers = new CopyOnWriteArrayList<String>();

			unregisteredSchedulers.addAll(Arrays.asList(schedulerList));

			while (!unregisteredSchedulers.isEmpty()) {

				unregisteredSchedulers.stream().forEach((socketStr) -> {

					Optional<InetSocketAddress> schedulerSocket = Network.strToSocket(socketStr);

					if (schedulerSocket.isPresent()) {

						schedulerConnector.sendMessage(
								new RegisterNodeMonitorMessage(schedulerSocket.get(), nodeMonitorAddress, socketStr,
										unregisteredSchedulers, new RegisterNodeMonitorErrorCallback(socketStr)));

					}
				});
				Thread.sleep(5000);

			}

		} catch (Exception ex) {
			LogUtil.logFunctionCall("Error Launching Node Monitor listening sockets ");

			ex.printStackTrace();

			System.exit(-1);

		}

		LogUtil.logFunctionCall("node monitor has gone up successfully");

	}

	public void registerBackend(String backendPort) throws TException {

		TClients.createInternalConcurrentService("TaskLauncher", new TaskLauncher(hostIPAddr, schedulerConnector, queue,
				sharedstate, new THostPort("localhost", Integer.parseInt(backendPort)), getTaskPort), true);
	}

	public void enqueueTaskProbes(TEnqueueProbeRequest request) throws TException {

		try {

			request.globalInfo.time = System.currentTimeMillis();

			sharedstate.updateGlobalState(request.globalInfo);

			String[] taskIds = request.probe.taskIds.split(",");

			for (String taskId : taskIds) {

				TEnqueueProbeRequest clonedProbeRequest = (TEnqueueProbeRequest) ObjectCloner.deepCopy(request);

				clonedProbeRequest.probe.taskIds = taskId;

				queue.add(new TLocalFullProbe(clonedProbeRequest.probe, System.currentTimeMillis(), 0));

			}

		} catch (Exception e) {

			LogUtil.logFunctionCall(
					"Error in adding new probe into the PeacockQueue for job " + request.probe.jobId + " and task ",
					request.probe.taskIds);

			e.printStackTrace();

		}

	}

	public void rotateProbes(TRotateProbesRequest request) throws TException {

		sharedstate.updateGlobalState(request.globalInfo);

		request.fullProbeIds.stream().forEach(probe -> {
			queue.add(new TLocalFullProbe(probe, System.currentTimeMillis(), 0));
		});

	}

}