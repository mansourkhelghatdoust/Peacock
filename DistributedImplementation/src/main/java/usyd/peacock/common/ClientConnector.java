package usyd.peacock.common;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.thrift.TException;
import org.apache.thrift.async.TAsyncClient;

import usyd.peacock.util.LogUtil;
import usyd.peacock.util.ThriftClientPool;
import usyd.peacock.util.ThriftClientPool.MakerFactory;

public class ClientConnector<T extends TAsyncClient, U extends ClientConnectorMessageBase> implements Runnable {

	private volatile boolean stop = false;

	private LinkedBlockingQueue<U> queue = new LinkedBlockingQueue<U>();

	private ThriftClientPool<T> connectorClientPool = null;

	private ServiceCaller<T, U> caller = null;

	public ClientConnector(MakerFactory<T> factory, ServiceCaller<T, U> caller) {

		this.connectorClientPool = new ThriftClientPool<T>(factory);

		this.caller = caller;
	}

	@Override
	public void run() {

		T client = null;
		U message = null;

		while (!stop) {

			try {
				message = queue.poll(10000, TimeUnit.MILLISECONDS);

				if (message != null) {

					if (connectorClientPool.getNumIdle(message.getSocket()) > 0) {

						client = connectorClientPool.borrowClient(message.getSocket());

						caller.doCallService(client, message, connectorClientPool);


					} else {
						// add message to queue to be sent next time when there
						// is a client available for the front end
						queue.add(message);
					}
				}

				if (queue.isEmpty() && stop)
					return;

			} catch (TException e) {

				LogUtil.logFunctionCall("Exception happened while sending message. " + message);

				if (message.getErrorCallback() != null)
					message.getErrorCallback().onError(e);

			} catch (InterruptedException e) {

				LogUtil.logFunctionCall("Client Connector Thread has been interrupted");

			} catch (Exception e) {

				LogUtil.logFunctionCall("Exception happened during borrowing object from thread pool. " + message);

			}

		}

		System.out.println("Terminating Connector ...");
	}

	public void stop() {
		stop = true;
	}

	public void sendMessage(U message) {
		queue.add(message);
	}

}
