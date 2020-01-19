package usyd.peacock.nodemonitor;

import java.net.InetSocketAddress;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.thrift.async.AsyncMethodCallback;

import usyd.distributed.scheduler.peacock.thrift.SchedulerServices.AsyncClient;
import usyd.peacock.util.LogUtil;
import usyd.peacock.util.ThriftClientPool;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class NodeMonitorRegistrationCallback implements AsyncMethodCallback<Boolean> {

	private String socketStr = null;

	private CopyOnWriteArrayList<String> unregisteredNodeMonitors = null;

	private ThriftClientPool clientPool;

	private InetSocketAddress inetSocketAddress;

	private AsyncClient client;

	public NodeMonitorRegistrationCallback(ThriftClientPool clientPool, InetSocketAddress inetSocketAddress,
			AsyncClient client, String socketStr, CopyOnWriteArrayList<String> unregisteredNodeMonitors) {

		this.socketStr = socketStr;

		this.unregisteredNodeMonitors = unregisteredNodeMonitors;

		this.clientPool = clientPool;

		this.inetSocketAddress = inetSocketAddress;

		this.client = client;
	}

	@Override
	public void onComplete(Boolean response) {

		unregisteredNodeMonitors.remove(socketStr);

		LogUtil.logFunctionCall("Registering node monitor in scheduler " + socketStr + " successfully");

		clientPool.returnClient(inetSocketAddress, client);

	}

	@Override
	public void onError(Exception exception) {

		LogUtil.logFunctionCall("Error in Registering node monitor in scheduler " + socketStr + "  retry in 5 seconds");

		clientPool.returnFailedClient(inetSocketAddress, client);
	}

}
