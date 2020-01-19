package usyd.peacock.common;

import org.apache.thrift.TException;

import usyd.distributed.scheduler.peacock.thrift.SchedulerServices;
import usyd.distributed.scheduler.peacock.thrift.SchedulerServices.AsyncClient;
import usyd.peacock.nodemonitor.NodeMonitorRegistrationCallback;
import usyd.peacock.nodemonitor.NotifyTaskStatusChangeMessage;
import usyd.peacock.nodemonitor.RegisterNodeMonitorMessage;
import usyd.peacock.nodemonitor.UnRegisterNodeMonitorMessage;
import usyd.peacock.scheduler.BroadCastSharedStateMessage;
import usyd.peacock.util.ThriftClientPool;

@SuppressWarnings("rawtypes")
public class SchedulerServiceCaller implements ServiceCaller<SchedulerServices.AsyncClient, SchedulerMessage> {

	public SchedulerServiceCaller() {

	}

	@Override
	public void doCallService(AsyncClient client, SchedulerMessage message, ThriftClientPool clientPool)
			throws TException {

		if (message instanceof BroadCastSharedStateMessage) {

			client.broadCastGlobalState(((BroadCastSharedStateMessage) message).getGlobalStateUpdateRequest(),
					new FunctionCallBack<Boolean>(clientPool, message.getSocket(), client));

		} else if (message instanceof NotifyTaskStatusChangeMessage) {

			client.notifyTaskStatusChange(((NotifyTaskStatusChangeMessage) message).getProbe(),
					((NotifyTaskStatusChangeMessage) message).getExecutionResult().bufferForMessage(),
					((NotifyTaskStatusChangeMessage) message).getExecutionResult().getStatus(),
					new FunctionCallBack<Void>(clientPool, message.getSocket(), client));

		} else if (message instanceof RegisterNodeMonitorMessage) {

			client.registerNodeMonitor(((RegisterNodeMonitorMessage) message).getNodeMonitorAddr(),
					new NodeMonitorRegistrationCallback(clientPool, message.getSocket(), client,
							((RegisterNodeMonitorMessage) message).getSchedulerSocketStr(),
							((RegisterNodeMonitorMessage) message).getUnregisteredSchedulers()));

		} else if (message instanceof UnRegisterNodeMonitorMessage) {

			UnRegisterNodeMonitorMessage msg = (UnRegisterNodeMonitorMessage) message;
			client.unRegisterNodeMonitor(msg.getFailedNodeMonitorAddress(),
					new FunctionCallBack<>(clientPool, message.getSocket(), client));

		}
	}

}