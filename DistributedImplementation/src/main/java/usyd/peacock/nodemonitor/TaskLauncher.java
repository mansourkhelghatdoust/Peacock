package usyd.peacock.nodemonitor;

import java.util.HashMap;

import org.apache.thrift.transport.TTransportException;

import usyd.distributed.scheduler.peacock.thrift.BackendServices;
import usyd.distributed.scheduler.peacock.thrift.SchedulerGetTaskService;
import usyd.distributed.scheduler.peacock.thrift.SchedulerServices;
import usyd.distributed.scheduler.peacock.thrift.THostPort;
import usyd.distributed.scheduler.peacock.thrift.TLightProbe;
import usyd.distributed.scheduler.peacock.thrift.TLocalFullProbe;
import usyd.distributed.scheduler.peacock.thrift.TTaskExecutionResult;
import usyd.distributed.scheduler.peacock.thrift.TTaskLaunchSpec;
import usyd.peacock.common.ClientConnector;
import usyd.peacock.common.SchedulerMessage;
import usyd.peacock.util.LogUtil;
import usyd.peacock.util.Network;

public class TaskLauncher implements InternalConcurrentService {

	private PeacockQueue queue;

	private volatile boolean stop = false;

	private SharedState sharedState;

	private ClientConnector<SchedulerServices.AsyncClient, SchedulerMessage> schedulerConnector;

	private HashMap<String, SchedulerGetTaskService.Iface> schedulerProxies = new HashMap<String, SchedulerGetTaskService.Iface>();

	private BackendServices.Iface backendProxy = null;

	private Integer getTaskPort = 0;

	private THostPort getTaskAddress = new THostPort("", -1);

	private String hostIPAddr = null;

	public TaskLauncher(String hostIPAddr,
			ClientConnector<SchedulerServices.AsyncClient, SchedulerMessage> schedulerConnector, PeacockQueue queue,
			SharedState sharedState, THostPort backendAddress, Integer getTaskPort) {

		this.queue = queue;

		this.sharedState = sharedState;

		this.schedulerConnector = schedulerConnector;

		this.getTaskPort = getTaskPort;

		this.hostIPAddr = hostIPAddr;

		try {

			backendProxy = ReconnectingClient.wrap(BackendServices.Client.class,
					new ReconnectingClient.Options(5, 1000), backendAddress, Integer.MAX_VALUE);

		} catch (IllegalArgumentException | TTransportException e) {
			e.printStackTrace();
			LogUtil.logFunctionCall(
					"Task Launcher cannot be established due to backend conneting error, terminating...");
			System.exit(-1);
		}

	}

	@Override
	public void run() {

		try {

			TLocalFullProbe localFullProbe = null;

			TTaskLaunchSpec taskLaunchSpec = null;

			while (!stop) {

				localFullProbe = queue.remove();

				if (localFullProbe != null) {

					queue.setWaitingForRunningTask(true);
					
					getTaskAddress.setHost(localFullProbe.fullProbe.getSchedulerAddress().getHost());

					getTaskAddress.setPort(getTaskPort);

					SchedulerGetTaskService.Iface schedulerProxy = schedulerProxies.get(getTaskAddress.toString());

					if (schedulerProxy == null) {

						schedulerProxy = ReconnectingClient.wrap(SchedulerGetTaskService.Client.class,
								new ReconnectingClient.Options(5, 1000), getTaskAddress, Integer.MAX_VALUE);

						schedulerProxies.put(getTaskAddress.toString(), schedulerProxy);

					}

					taskLaunchSpec = schedulerProxy.getTask(localFullProbe.fullProbe.getJobId(),
							localFullProbe.fullProbe.taskIds);

					sendAck(launchTask(taskLaunchSpec, localFullProbe), localFullProbe, taskLaunchSpec);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void sendAck(TTaskExecutionResult executionResult, TLocalFullProbe localFullProbe,
			TTaskLaunchSpec taskLaunchSpec) {

		if (executionResult.getStatus() > 0)
			queue.removeRunningJob();

		schedulerConnector.sendMessage(new NotifyTaskStatusChangeMessage(
				Network.strToSocket(localFullProbe.fullProbe.getSchedulerAddress().host + ":"
						+ localFullProbe.fullProbe.getSchedulerAddress().port).get(),
				new TLightProbe(localFullProbe.fullProbe.taskIds, localFullProbe.fullProbe.jobId), executionResult));

	}

	private TTaskExecutionResult launchTask(TTaskLaunchSpec taskLaunchSpec, TLocalFullProbe localFullProbe) {

		try {

			taskLaunchSpec.globalInfo.time = System.currentTimeMillis();

			sharedState.updateGlobalState(taskLaunchSpec.globalInfo);

			if (taskLaunchSpec.taskId == null || taskLaunchSpec.jobId == null) {

				LogUtil.logFunctionCall("Cannot get task " + localFullProbe.fullProbe.getTaskIds().split(",")[0]
						+ " Job " + localFullProbe.fullProbe.getJobId() + " from Scheduler "
						+ localFullProbe.fullProbe.schedulerAddress.getHost() + ":"
						+ localFullProbe.fullProbe.schedulerAddress.getPort() + ". It is ignored and try to get next");

				return new TTaskExecutionResult(-1, null);

			}

			queue.setRunningJob(localFullProbe.fullProbe, System.currentTimeMillis());
			
			queue.setWaitingForRunningTask(false);

			return backendProxy.launchTask(taskLaunchSpec.message,
					new TLightProbe(taskLaunchSpec.taskId, taskLaunchSpec.jobId));

		} catch (Exception e) {

			LogUtil.logFunctionCall("Error in launching task " + taskLaunchSpec.taskId + " of job "
					+ taskLaunchSpec.jobId + " by node monitor  " + hostIPAddr);
			e.printStackTrace();

			queue.removeRunningJob();

			return null;
		}

	}

	@Override
	public void stop() {
		stop = true;
	}

}