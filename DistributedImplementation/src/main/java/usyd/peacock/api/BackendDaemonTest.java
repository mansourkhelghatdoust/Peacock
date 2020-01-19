package usyd.peacock.api;

import java.net.InetSocketAddress;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class BackendDaemonTest {

	private final static Logger LOG = Logger.getLogger(BackendDaemonTest.class);

	public final static Level DEFAULT_LOG_LEVEL = Level.DEBUG;

	public static void main(String[] args) {

		LOG.info("Launching Backend");

		// if (args == null || args.length != 2) {
		//
		// LOG.error(
		// "Configuration information should be given completely as command line
		// arguments, terminating ...");
		//
		// System.exit(-1);
		//
		// }

		InetSocketAddress nodeMonitorAddress = new InetSocketAddress("localhost", 4005); // Network.strToSocket(args[0]);

		// if (!nodeMonitorAddress.isPresent()) {
		//
		// LOG.error("Node Monitor address has not been set correctly,
		// terminating ...");
		//
		// System.exit(-1);
		//
		// }

		int listeningPort = 4008;

		// try {
		//
		// listeningPort = Integer.parseInt(args[1]);
		//
		// } catch (NumberFormatException ex) {
		//
		// LOG.error("Listening port has not been set correctly, terminating
		// ...");
		//
		// System.exit(-1);
		// }

		BackendDaemonTest backendDaemon = new BackendDaemonTest();

		backendDaemon.initialize(nodeMonitorAddress, listeningPort);

	}

	public void initialize(InetSocketAddress nodeMonitorAddr, int listeningPort) {

		Level logLevel = Level.toLevel("", DEFAULT_LOG_LEVEL);

		Logger.getRootLogger().setLevel(logLevel);

		BackendServiceTest backendSrv = new BackendServiceTest();

		backendSrv.launchServers(nodeMonitorAddr, listeningPort);
	}

}
