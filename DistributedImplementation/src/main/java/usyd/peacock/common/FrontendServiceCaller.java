package usyd.peacock.common;

import org.apache.thrift.TException;

import usyd.distributed.scheduler.peacock.thrift.FrontendServices;
import usyd.distributed.scheduler.peacock.thrift.FrontendServices.AsyncClient;
import usyd.peacock.scheduler.NotifyTaskStatusChangeMessage;
import usyd.peacock.util.ThriftClientPool;

public class FrontendServiceCaller implements ServiceCaller<FrontendServices.AsyncClient, FrontendMessage> {

	public FrontendServiceCaller() {

	}

	@SuppressWarnings({ "rawtypes"})
	@Override
	public void doCallService(AsyncClient client, FrontendMessage message, ThriftClientPool clientPool)
			throws TException {

		if (message instanceof NotifyTaskStatusChangeMessage) {

			NotifyTaskStatusChangeMessage msg = (NotifyTaskStatusChangeMessage) message;

			client.notifyTaskStatusChange(msg.getProbe(), msg.getMessage(), msg.getStatus(),
					new FunctionCallBack<Void>(clientPool, message.getSocket(), client));
		}

	}
}