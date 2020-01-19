package usyd.peacock.util;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TNonblockingServer.Args;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransportException;

public class TServers {

	private final static Logger LOG = Logger.getLogger(TServers.class);

	private static ArrayList<TServer> servers = new ArrayList<TServer>();

	/**
	 * Launch a single threaded nonblocking IO server. All requests to this
	 * server will be handled in a single thread, so its requests should not
	 * contain blocking functions.
	 */
	public static void launchSingleThreadThriftServer(String serverName, int port, TProcessor processor)
			throws IOException {

		LOG.info("Staring async thrift server of type: " + processor.getClass().toString() + " on port " + port);

		TNonblockingServerTransport serverTransport;

		try {

			serverTransport = new TNonblockingServerSocket(port);

		} catch (TTransportException e) {

			throw new IOException(e);
		}

		Args serverArgs = new TNonblockingServer.Args(serverTransport);

		serverArgs.transportFactory(new TFramedTransport.Factory());

		serverArgs.protocolFactory(new TCompactProtocol.Factory());

		serverArgs.processor(processor);

		TServer server = new TNonblockingServer(serverArgs);

		new Thread(new TServerRunnable(server), serverName).start();

		servers.add(server);
	}

	/**
	 * Launch a multi-threaded Thrift server with the given {@code processor}.
	 * Note that internally this creates an expanding thread pool of at most
	 * {@code threads} threads, and requests are queued whenever that thread
	 * pool is saturated.
	 */
	public static void launchThreadedThriftServer(String serverName, int port, int selectorThreads, int workerThreads,
			TProcessor processor) throws IOException {

		LOG.info("Staring async thrift server of type: " + processor.getClass().toString() + " on port " + port);

		TNonblockingServerTransport serverTransport;

		try {

			serverTransport = new TNonblockingServerSocket(port);

		} catch (TTransportException e) {

			throw new IOException(e);

		}

		TThreadedSelectorServer.Args serverArgs = new TThreadedSelectorServer.Args(serverTransport);

		serverArgs.transportFactory(new TFramedTransport.Factory());

		serverArgs.protocolFactory(new TCompactProtocol.Factory());

		serverArgs.processor(processor);

		serverArgs.selectorThreads(selectorThreads);

		serverArgs.workerThreads(workerThreads);

		TServer server = new TThreadedSelectorServer(serverArgs);

		new Thread(new TServerRunnable(server), serverName).start();

		servers.add(server);
	}

	public static void stop() {

		for (TServer server : servers)
			server.stop();

	}

	/**
	 * Runnable class to wrap thrift servers in their own thread.
	 */
	private static class TServerRunnable implements Runnable {

		private TServer server;

		public TServerRunnable(TServer server) {
			this.server = server;
		}

		public void run() {
			this.server.serve();
		}

	}

}
