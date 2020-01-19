package usyd.peacock.common;

import java.net.InetSocketAddress;

import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.async.TAsyncClient;

import usyd.peacock.util.ThriftClientPool;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class FunctionCallBack<T> implements AsyncMethodCallback<T> {

	private ThriftClientPool clientPool;

	private InetSocketAddress socket;

	private TAsyncClient client;

	private boolean terminateOnError = false;

	public FunctionCallBack(ThriftClientPool clientPool, InetSocketAddress socket, TAsyncClient client) {
		this.clientPool = clientPool;
		this.socket = socket;
		this.client = client;
	}

	public FunctionCallBack(ThriftClientPool clientPool, InetSocketAddress socket, TAsyncClient client,
			boolean terminateOnError) {
		this(clientPool, socket, client);
		this.terminateOnError = terminateOnError;
	}

	@Override
	public void onComplete(T response) {
		clientPool.returnClient(socket, client);
	}

	@Override
	public void onError(Exception exception) {

		clientPool.returnFailedClient(socket, client);
		if (terminateOnError)
			System.exit(-1);
	}

}
