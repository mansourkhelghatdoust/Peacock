package usyd.peacock.scheduler;

import java.nio.ByteBuffer;

import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;

import usyd.distributed.scheduler.peacock.thrift.SchedulerServices;
import usyd.distributed.scheduler.peacock.thrift.FrontendServices;
import usyd.distributed.scheduler.peacock.thrift.NodeMonitorServices;
import usyd.distributed.scheduler.peacock.thrift.SchedulerGetTaskService;
import usyd.distributed.scheduler.peacock.thrift.TGlobalStateUpdateRequest;
import usyd.distributed.scheduler.peacock.thrift.TLightProbe;
import usyd.distributed.scheduler.peacock.thrift.TSchedulingRequest;
import usyd.distributed.scheduler.peacock.thrift.TTaskLaunchSpec;
import usyd.peacock.common.ClientConnector;
import usyd.peacock.common.FrontendMessage;
import usyd.peacock.common.NodeMonitorMessage;
import usyd.peacock.common.SchedulerMessage;
import usyd.peacock.util.LogUtil;

public class SchedulerService implements SchedulerServices.AsyncIface, SchedulerGetTaskService.Iface {

	private Scheduler scheduler = null;

	private SharedState sharedState = new SharedState();

	public SchedulerService(String hostIPAddr, String schedulers,
			ClientConnector<FrontendServices.AsyncClient, FrontendMessage> frontendConnector,
			ClientConnector<SchedulerServices.AsyncClient, SchedulerMessage> schedulerConnector,
			ClientConnector<NodeMonitorServices.AsyncClient, NodeMonitorMessage> nodeMonitorConnector,
			Integer listeningPort) {

		scheduler = new Scheduler(hostIPAddr, schedulers, sharedState, frontendConnector, schedulerConnector, nodeMonitorConnector,
				listeningPort);

	}

	@Override
	public void submitJob(TSchedulingRequest request, AsyncMethodCallback<Void> resultHandler) throws TException {

		LogUtil.logFunctionCall(request);

		scheduler.submitJob(request);

		resultHandler.onComplete(null);

	}

	@Override
	public void notifyTaskStatusChange(TLightProbe probe, ByteBuffer message, int status,
			AsyncMethodCallback<Void> resultHandler) throws TException {

		LogUtil.logFunctionCall(probe, message);

		scheduler.notifyTaskStatusChange(probe, message, status);

		resultHandler.onComplete(null);

	}

	@Override
	public void registerNodeMonitor(String nodeMonitorAddress, AsyncMethodCallback<Boolean> resultHandler)
			throws TException {

		LogUtil.logFunctionCall(nodeMonitorAddress);

		scheduler.registerNodeMonitor(nodeMonitorAddress);

		resultHandler.onComplete(true);

	}

	@Override
	public void unRegisterNodeMonitor(String nodeMonitorAddress, AsyncMethodCallback<Boolean> resultHandler)
			throws TException {

		LogUtil.logFunctionCall(nodeMonitorAddress);

		scheduler.unRegisterNodeMonitor(nodeMonitorAddress);

		resultHandler.onComplete(true);

	}

	@Override
	public void broadCastGlobalState(TGlobalStateUpdateRequest request, AsyncMethodCallback<Boolean> resultHandler)
			throws TException {

		LogUtil.logFunctionCall(request);

		sharedState.updateGlobalInfo(request);

		resultHandler.onComplete(true);

	}

	@Override
	public void registerScheduler(String schedulerAddress, AsyncMethodCallback<Void> resultHandler) throws TException {

		LogUtil.logFunctionCall(schedulerAddress);

		scheduler.registerScheduler(schedulerAddress);

		resultHandler.onComplete(null);

	}

	@Override
	public void unRegisterScheduler(String schedulerAddress, AsyncMethodCallback<Void> resultHandler)
			throws TException {

		LogUtil.logFunctionCall(schedulerAddress);

		scheduler.unRegisterScheduler(schedulerAddress);

		resultHandler.onComplete(null);

	}

	@Override
	public TTaskLaunchSpec getTask(String jobId, String taskId) throws TException {

		LogUtil.logFunctionCall(jobId);

		return scheduler.getTask(jobId, taskId);

	}

}