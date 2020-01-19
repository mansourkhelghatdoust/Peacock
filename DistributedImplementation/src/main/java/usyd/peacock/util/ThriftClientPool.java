package usyd.peacock.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;

import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.apache.commons.pool.impl.GenericKeyedObjectPool.Config;
import org.apache.log4j.Logger;
import org.apache.thrift.async.TAsyncClient;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TNonblockingSocket;
import org.apache.thrift.transport.TNonblockingTransport;

import usyd.distributed.scheduler.peacock.thrift.BackendServices;
import usyd.distributed.scheduler.peacock.thrift.FrontendServices;
import usyd.distributed.scheduler.peacock.thrift.NodeMonitorServices;
import usyd.distributed.scheduler.peacock.thrift.SchedulerGetTaskService;
import usyd.distributed.scheduler.peacock.thrift.SchedulerServices;

/** A pool of nonblocking thrift async connections. */
public class ThriftClientPool<T extends TAsyncClient> {

	// Default configurations for underlying pool
	/** See {@link GenericKeyedObjectPool.Config} */
	public static int MIN_IDLE_CLIENTS_PER_ADDR = 0;
	/** See {@link GenericKeyedObjectPool.Config} */
	public static int EVICTABLE_IDLE_TIME_MILLIS = 1000;
	/** See {@link GenericKeyedObjectPool.Config} */
	public static int TIME_BETWEEN_EVICTION_RUNS_MILLIS = -1;
	/** See {@link GenericKeyedObjectPool.Config} */
	public static int MAX_ACTIVE_CLIENTS_PER_ADDR = 128;

	private static final Logger LOG = Logger.getLogger(ThriftClientPool.class);

	/** Get the configuration parameters used on the underlying client pool. */
	protected static Config getPoolConfig() {
		Config conf = new Config();
		conf.minIdle = MIN_IDLE_CLIENTS_PER_ADDR;
		conf.maxIdle = -1;
		conf.minEvictableIdleTimeMillis = EVICTABLE_IDLE_TIME_MILLIS;
		conf.timeBetweenEvictionRunsMillis = TIME_BETWEEN_EVICTION_RUNS_MILLIS;
		conf.maxActive = MAX_ACTIVE_CLIENTS_PER_ADDR;
		conf.whenExhaustedAction = GenericKeyedObjectPool.WHEN_EXHAUSTED_BLOCK;
		return conf;
	}

	/**
	 * Clients need to provide an instance of this factory which is capable of
	 * creating the a thrift client of type <T>.
	 */
	public interface MakerFactory<T> {
		public T create(TNonblockingTransport tr, TAsyncClientManager mgr, TProtocolFactory factory);
	}

	public static class SchedulerServicesFactory implements MakerFactory<SchedulerServices.AsyncClient> {
		@Override
		public SchedulerServices.AsyncClient create(TNonblockingTransport tr, TAsyncClientManager mgr,
				TProtocolFactory factory) {
			return new SchedulerServices.AsyncClient(factory, mgr, tr);
		}
	}

	public static class SchedulerGetTaskServiceFactory implements MakerFactory<SchedulerGetTaskService.AsyncClient> {
		@Override
		public SchedulerGetTaskService.AsyncClient create(TNonblockingTransport tr, TAsyncClientManager mgr,
				TProtocolFactory factory) {
			return new SchedulerGetTaskService.AsyncClient(factory, mgr, tr);
		}
	}

	public static class NodeMonitorServicesFactory implements MakerFactory<NodeMonitorServices.AsyncClient> {
		@Override
		public NodeMonitorServices.AsyncClient create(TNonblockingTransport tr, TAsyncClientManager mgr,
				TProtocolFactory factory) {
			return new NodeMonitorServices.AsyncClient(factory, mgr, tr);
		}
	}

	public static class BackendServicesFactory implements MakerFactory<BackendServices.AsyncClient> {
		@Override
		public BackendServices.AsyncClient create(TNonblockingTransport tr, TAsyncClientManager mgr,
				TProtocolFactory factory) {
			return new BackendServices.AsyncClient(factory, mgr, tr);
		}
	}

	public static class FrontendServicesFactory implements MakerFactory<FrontendServices.AsyncClient> {
		@Override
		public FrontendServices.AsyncClient create(TNonblockingTransport tr, TAsyncClientManager mgr,
				TProtocolFactory factory) {
			return new FrontendServices.AsyncClient(factory, mgr, tr);
		}
	}

	private class PoolFactory implements KeyedPoolableObjectFactory<InetSocketAddress, T> {
		// Thrift clients do not expose their underlying transports, so we track
		// them
		// separately here to let us call close() on the transport associated
		// with a
		// particular client.
		private HashMap<T, TNonblockingTransport> transports = new HashMap<T, TNonblockingTransport>();
		private MakerFactory<T> maker;

		public PoolFactory(MakerFactory<T> maker) {
			this.maker = maker;
		}

		@Override
		public void destroyObject(InetSocketAddress socket, T client) throws Exception {
			transports.get(client).close();
			transports.remove(client);
		}

		@Override
		public T makeObject(InetSocketAddress socket) throws Exception {
			TNonblockingTransport nbTr = new TNonblockingSocket(socket.getAddress().getHostAddress(), socket.getPort());
			TProtocolFactory factory = new TCompactProtocol.Factory();
			T client = maker.create(nbTr, clientManager, factory);
			transports.put(client, nbTr);
			return client;
		}

		@Override
		public boolean validateObject(InetSocketAddress socket, T client) {
			return transports.get(client).isOpen();
		}

		@Override
		public void activateObject(InetSocketAddress socket, T client) throws Exception {
			// Nothing to do here
		}

		@Override
		public void passivateObject(InetSocketAddress socket, T client) throws Exception {
			// Nothing to do here
		}
	}

	/** Pointer to shared selector thread. */
	TAsyncClientManager clientManager;

	/** Underlying object pool. */
	private GenericKeyedObjectPool<InetSocketAddress, T> pool;

	public ThriftClientPool(MakerFactory<T> maker) {
		pool = new GenericKeyedObjectPool<InetSocketAddress, T>(new PoolFactory(maker), getPoolConfig());
		try {
			clientManager = new TAsyncClientManager();
		} catch (IOException e) {
			LOG.fatal(e);
		}
	}

	/** Constructor (for unit tests) which overrides default configuration. */
	protected ThriftClientPool(MakerFactory<T> maker, Config conf) {
		this(maker);
		pool.setConfig(conf);
	}

	/** Borrows a client from the pool. */
	public T borrowClient(InetSocketAddress socket) throws Exception {
		return pool.borrowObject(socket);
	}

	/** Returns a client to the pool. */
	public void returnClient(InetSocketAddress socket, T client) {
		try {
			pool.returnObject(socket, client);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void returnFailedClient(InetSocketAddress socket, T client) {
		try {
			pool.invalidateObject(socket, client);
			pool.addObject(socket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getNumActive(InetSocketAddress socket) {
		return pool.getNumActive(socket);
	}

	public int getNumIdle(InetSocketAddress socket) {
		return MAX_ACTIVE_CLIENTS_PER_ADDR - pool.getNumActive(socket);
	}

}