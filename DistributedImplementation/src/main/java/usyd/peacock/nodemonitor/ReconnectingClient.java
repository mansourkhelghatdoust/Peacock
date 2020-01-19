package usyd.peacock.nodemonitor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Queue;
import java.util.Set;

import com.google.common.collect.Sets;

import usyd.distributed.scheduler.peacock.thrift.THostPort;
import usyd.peacock.util.LogUtil;

import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ReconnectingClient {

	private static final Logger LOG = LoggerFactory.getLogger(ReconnectingClient.class);

	/**
	 * List of causes which suggest a restart might fix things (defined as
	 * constants in {@link org.apache.thrift.transport.TTransportException}).
	 */
	private static final Set<Integer> RESTARTABLE_CAUSES = Sets.newHashSet(TTransportException.NOT_OPEN,
			TTransportException.END_OF_FILE, TTransportException.TIMED_OUT, TTransportException.UNKNOWN);

	public interface FailureCallback {
		void onFailure(String failedNodeAddr);
	}

	public static class Options {

		private int numRetries;

		private long timeBetweenRetries;

		/**
		 *
		 * @param numRetries
		 *            the maximum number of times to try reconnecting before
		 *            giving up and throwing an exception
		 * @param timeBetweenRetries
		 *            the number of milliseconds to wait in between reconnection
		 *            attempts.
		 */
		public Options(int numRetries, long timeBetweenRetries) {
			this.numRetries = numRetries;
			this.timeBetweenRetries = timeBetweenRetries;
		}

		private int getNumRetries() {
			return numRetries;
		}

		private long getTimeBetweenRetries() {
			return timeBetweenRetries;
		}

		public Options withNumRetries(int numRetries) {
			this.numRetries = numRetries;
			return this;
		}

		public Options withTimeBetweenRetries(long timeBetweenRetries) {
			this.timeBetweenRetries = timeBetweenRetries;
			return this;
		}

	}

	/**
	 * Reflectively wraps a thrift client so that when a call fails due to a
	 * networking error, a reconnect is attempted.
	 *
	 * @param baseClient
	 *            the client to wrap
	 * @param clientInterface
	 *            the interface that the client implements (can be inferred by
	 *            using
	 *            {@link #wrap(org.apache.thrift.TServiceClient, com.ReconnectingClient.spruce_lib.singletons.ReconnectingThriftClient.Options)}
	 * @param options
	 *            options that control behavior of the reconnecting client
	 * @param <T>
	 * @param <C>
	 * @return
	 * @throws TTransportException
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	public static <T extends TServiceClient, C> C wrap(Class<T> baseClient, Class<C> clientInterface, Options options,
			THostPort address, int connectTimeout) throws IllegalArgumentException, TTransportException {

		Object proxyObject = Proxy.newProxyInstance(clientInterface.getClassLoader(),
				new Class<?>[] { clientInterface }, new ReconnectingClientProxy<T>(baseClient, options.getNumRetries(),
						options.getTimeBetweenRetries(), address, connectTimeout));

		return (C) proxyObject;

	}

	/**
	 * Reflectively wraps a thrift client so that when a call fails due to a
	 * networking error, a reconnect is attempted.
	 *
	 * @param baseClient
	 *            the client to wrap
	 * @param options
	 *            options that control behavior of the reconnecting client
	 * @param <T>
	 * @param <C>
	 * @return
	 * @throws TTransportException
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	public static <T extends TServiceClient, C> C wrap(Class<T> baseClient, Options options, THostPort address,
			int connectTimeout) throws IllegalArgumentException, TTransportException {

		Class<?>[] interfaces = baseClient.getInterfaces();

		for (Class<?> iface : interfaces) {
			if (iface.getSimpleName().equals("Iface")
					&& iface.getEnclosingClass().equals(baseClient.getEnclosingClass())) {
				return (C) wrap(baseClient, iface, options, address, connectTimeout);
			}
		}

		throw new RuntimeException("Class needs to implement Iface directly. Use wrap(TServiceClient, Class) instead.");
	}

	/**
	 * Reflectively wraps a thrift client so that when a call fails due to a
	 * networking error, a reconnect is attempted.
	 *
	 * @param baseClient
	 *            the client to wrap
	 * @param clientInterface
	 *            the interface that the client implements (can be inferred by
	 *            using
	 *            {@link #wrap(org.apache.thrift.TServiceClient, com.ReconnectingClient.spruce_lib.singletons.ReconnectingThriftClient.Options)}
	 * @param options
	 *            options that control behavior of the reconnecting client
	 * @param <T>
	 * @param <C>
	 * @return
	 * @throws TTransportException
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	public static <T extends TServiceClient, C> C wrap(Class<T> baseClient, Class<C> clientInterface, Options options,
			Queue<THostPort> addresses, int connectTimeout, FailureCallback callback)
			throws IllegalArgumentException, TTransportException {

		Object proxyObject = Proxy.newProxyInstance(clientInterface.getClassLoader(),
				new Class<?>[] { clientInterface }, new FaultToleranceReconnectingClientProxy<T>(baseClient,
						options.getNumRetries(), options.getTimeBetweenRetries(), addresses, connectTimeout, callback));

		return (C) proxyObject;

	}

	/**
	 * Reflectively wraps a thrift client so that when a call fails due to a
	 * networking error, a reconnect is attempted.
	 *
	 * @param baseClient
	 *            the client to wrap
	 * @param options
	 *            options that control behavior of the reconnecting client
	 * @param <T>
	 * @param <C>
	 * @return
	 * @throws TTransportException
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	public static <T extends TServiceClient, C> C wrap(Class<T> baseClient, Options options, Queue<THostPort> addresses,
			int connectTimeout, FailureCallback callback) throws IllegalArgumentException, TTransportException {

		Class<?>[] interfaces = baseClient.getInterfaces();

		for (Class<?> iface : interfaces) {
			if (iface.getSimpleName().equals("Iface")
					&& iface.getEnclosingClass().equals(baseClient.getEnclosingClass())) {
				return (C) wrap(baseClient, iface, options, addresses, connectTimeout, callback);
			}
		}

		throw new RuntimeException("Class needs to implement Iface directly. Use wrap(TServiceClient, Class) instead.");
	}

	private static <T> T createBlockingClient(Class<T> clientType, THostPort address, int connectTimeout)
			throws Exception {

		TFramedTransport tr = new TFramedTransport(new TSocket(address.host, address.getPort(), connectTimeout));
		tr.open();
		return clientType.getConstructor(TProtocol.class).newInstance(new TCompactProtocol(tr));

	}

	/**
	 * Helper proxy class. Attempts to call method on proxy object wrapped in
	 * try/catch. If it fails, it attempts a reconnect and tries the method
	 * again.
	 *
	 * @param <T>
	 */
	private static class ReconnectingClientProxy<T extends TServiceClient> implements InvocationHandler {

		private T baseClient;
		private final int maxRetries;
		private final long timeBetweenRetries;

		public ReconnectingClientProxy(Class<T> baseClient, int maxRetries, long timeBetweenRetries, THostPort address,
				int connectTimeout) throws TTransportException {

			this.maxRetries = maxRetries;

			this.timeBetweenRetries = timeBetweenRetries;

			try {
				this.baseClient = createBlockingClient(baseClient, address, connectTimeout);
			} catch (Exception e) {
				if (e instanceof TTransportException)
					throw new TTransportException("Failed to connect " + e.getMessage());
			}

		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

			try {

				return method.invoke(baseClient, args);

			} catch (InvocationTargetException e) {

				if (e.getTargetException() instanceof TTransportException) {

					TTransportException cause = (TTransportException) e.getTargetException();

					if (RESTARTABLE_CAUSES.contains(cause.getType())) {

						reconnectOrThrowException(baseClient.getInputProtocol().getTransport(), maxRetries,
								timeBetweenRetries);
						return method.invoke(baseClient, args);

					}

				}
				throw e;
			}

		}

		private static void reconnectOrThrowException(TTransport transport, int maxRetries, long timeBetweenRetries)
				throws TTransportException {

			int errors = 0;
			transport.close();

			while (errors < maxRetries) {

				try {

					LOG.info("Attempting to reconnect...");

					transport.open();

					LOG.info("Reconnection successful");

					break;

				} catch (TTransportException e) {

					LOG.error("Error while reconnecting:", e);

					errors++;

					if (errors < maxRetries) {
						try {
							LOG.info("Sleeping for {} milliseconds before retrying", timeBetweenRetries);
							Thread.sleep(timeBetweenRetries);
						} catch (InterruptedException e2) {
							throw new RuntimeException(e);
						}
					}
				}
			}

			if (errors >= maxRetries) {
				throw new TTransportException("Failed to reconnect");
			}

		}
	}

	private static class FaultToleranceReconnectingClientProxy<T extends TServiceClient> implements InvocationHandler {

		private T baseClient;
		private Class<T> classClient;
		private final int maxRetries;
		private final long timeBetweenRetries;
		private Queue<THostPort> addresses;
		private THostPort address;
		private int connectTimeout;
		private FailureCallback callback;

		public FaultToleranceReconnectingClientProxy(Class<T> classClient, int maxRetries, long timeBetweenRetries,
				Queue<THostPort> addresses, int connectTimeout, FailureCallback callback) throws TTransportException {

			this.maxRetries = maxRetries;

			this.timeBetweenRetries = timeBetweenRetries;

			this.addresses = addresses;

			this.connectTimeout = connectTimeout;

			this.classClient = classClient;

			this.callback = callback;

			establishConnection(Integer.MAX_VALUE, 1000);
		}

		private void establishConnection(int maxRetries, long timeBetweenRetries) {

			address = addresses.poll();
			int error = 0;

			while (error < maxRetries) {

				try {
					Thread.sleep(timeBetweenRetries);
					this.baseClient = createBlockingClient(classClient, address, connectTimeout);
					break;
				} catch (Exception e) {
					LogUtil.logFunctionCall("Failed to connect " + address.toString() + " Retrying ...");
					error++;
				}
			}
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

			try {

				return method.invoke(baseClient, args);

			} catch (InvocationTargetException e) {

				if (e.getTargetException() instanceof TTransportException) {

					TTransportException cause = (TTransportException) e.getTargetException();

					if (RESTARTABLE_CAUSES.contains(cause.getType())) {

						reconnectOrConnectNewOrThrowException(baseClient.getInputProtocol().getTransport(), maxRetries,
								timeBetweenRetries);
						return method.invoke(baseClient, args);

					}

				}
				throw e;
			}

		}

		private void reconnectOrConnectNewOrThrowException(TTransport transport, int maxRetries,
				long timeBetweenRetries) throws TTransportException {

			int errors = 0;
			transport.close();

			while (errors < maxRetries) {

				try {

					LOG.info(
							"Attempting to reconnect.......................................................................");

					transport.open();

					LOG.info(
							"Reconnection successful........................................................................");

					break;

				} catch (TTransportException e) {

					LOG.error("Error while reconnecting:", e);

					errors++;

					if (errors < maxRetries) {
						try {
							LOG.info("Sleeping for {} milliseconds before retrying", timeBetweenRetries);
							Thread.sleep(timeBetweenRetries);
						} catch (InterruptedException e2) {
							throw new RuntimeException(e);
						}
					}
				}
			}

			if (errors >= maxRetries) {

				callback.onFailure(address.getHost() + ":" + address.getPort());

				if (addresses.isEmpty())
					throw new TTransportException("Failed to reconnect");

				establishConnection(maxRetries, timeBetweenRetries);
			}

		}

	}

}