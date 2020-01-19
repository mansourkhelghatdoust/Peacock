package usyd.peacock.common;

import org.apache.thrift.TException;

import usyd.distributed.scheduler.peacock.thrift.NodeMonitorServices;
import usyd.distributed.scheduler.peacock.thrift.NodeMonitorServices.AsyncClient;
import usyd.peacock.scheduler.EnqueueProbeRequestMessage;
import usyd.peacock.util.ThriftClientPool;

public class NodeMonitorServiceCaller implements ServiceCaller<NodeMonitorServices.AsyncClient, NodeMonitorMessage> {

	@SuppressWarnings("rawtypes")
	@Override
	public void doCallService(AsyncClient client, NodeMonitorMessage message, ThriftClientPool clientPool)
			throws TException {

		if (message instanceof EnqueueProbeRequestMessage) {

			EnqueueProbeRequestMessage msg = (EnqueueProbeRequestMessage) message;

			client.enqueueTaskProbes(msg.getRequest(),
					new FunctionCallBack<Boolean>(clientPool, message.getSocket(), client));

		}
	}
}
