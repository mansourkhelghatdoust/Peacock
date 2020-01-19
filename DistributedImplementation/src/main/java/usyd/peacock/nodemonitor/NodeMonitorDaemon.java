package usyd.peacock.nodemonitor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.Scanner;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import usyd.distributed.scheduler.peacock.thrift.NodeMonitorServices;
import usyd.distributed.scheduler.peacock.thrift.ProbesRotationServices;
import usyd.distributed.scheduler.peacock.thrift.SchedulerServices;
import usyd.peacock.common.ClientConnector;
import usyd.peacock.common.SchedulerMessage;
import usyd.peacock.common.SchedulerServiceCaller;
import usyd.peacock.util.Network;
import usyd.peacock.util.TClients;
import usyd.peacock.util.TServers;
import usyd.peacock.util.ThriftClientPool;

public class NodeMonitorDaemon {

	private final static Logger LOG = Logger.getLogger(NodeMonitorDaemon.class);

	public final static Level DEFAULT_LOG_LEVEL = Level.DEBUG;

	private final static Integer SELECTOR_THREADS = 2;

	private final static Integer WORKER_THREADS = 2;

	private String schedulers = null;

	private Integer listeningPort = 0;

	private InetSocketAddress neighbor = null;

	private InetSocketAddress[] successors = null;

	private int rotationInterval = 0;

	private int rotationListeningPort = 0;

	private int getTaskPort = 0;
	
	private String hostIPAddr = null;

	public static void main(String[] args) {

		BasicConfigurator.configure();

		NodeMonitorDaemon daemon = new NodeMonitorDaemon();

		daemon.run(args);

	}

	public void run(String[] args) {

		Scanner scanner = new Scanner(System.in);

		try {

			LOG.info("Launching a node monitor instance");

			validate(args);

			extractArguments(args);

			initialize(hostIPAddr, schedulers, getTaskPort, listeningPort, rotationListeningPort, neighbor, successors,
					rotationInterval);

			LOG.info("Type 'Exit' to terminate Node Monitor ...");

			while (scanner.hasNext()) {

				if (scanner.nextLine().toUpperCase().equals("EXIT")) {
					LOG.info("Node Monitor server has been terminated ...");
					TServers.stop();
					TClients.stop();
					break;
				}

				LOG.info("Type 'Exit' to terminate Node Monitor ...");

			}

		} catch (Exception e) {

			e.printStackTrace();

			System.exit(-1);

		} finally {
			scanner.close();
		}

	}

	private void extractArguments(String[] args) {

		for (int i = 0; i < args.length; i = i + 2) {

			if (args[i].equals("--schedulers")) {

				schedulers = args[i + 1];

			} else if (args[i].equals("--listeningPort")) {

				listeningPort = Integer.parseInt(args[i + 1]);

			} else if (args[i].equals("--hostIPAddr")) {

				hostIPAddr = args[i + 1];

			} else if (args[i].equals("--neighbors")) {

				String[] neighbors = args[i + 1].split(",");

				successors = new InetSocketAddress[neighbors.length - 1];

				Optional<InetSocketAddress> neighborAddr = Network.strToSocket(neighbors[0]);

				if (!neighborAddr.isPresent()) {
					LOG.error("Invalid Neighbor address");
					System.exit(-1);
				} else {
					neighbor = neighborAddr.get();
				}

				for (int ii = 0; ii < neighbors.length - 1; ii++)
					successors[ii] = Network.strToSocket(neighbors[ii + 1]).get();

			} else if (args[i].equals("--rotationInterval")) {

				rotationInterval = Integer.parseInt(args[i + 1]);

			} else if (args[i].equals("--rotationListeningPort")) {

				rotationListeningPort = Integer.parseInt(args[i + 1]);

			} else if (args[i].equals("--getTaskPort")) {

				getTaskPort = Integer.parseInt(args[i + 1]);

			} else {
				LOG.error(args[i] + " not known");
				System.exit(-1);
			}

		}

	}

	private static void validate(String[] args) {

		if (args == null || args.length < 12) {

			LOG.error("The number of input parameters " + args.length + " is wrong,  terminating ...");

			LOG.info("--schedulers, --listeningPort, --rotationListeningPort, --hostIPAddr, --neighbor, --rotationInterval");

			System.exit(-1);

		}
	}

	private void initialize(String hostIPAddr, String schedulers, Integer getTaskPort, Integer listeningPort,
			Integer rotationListeningPort, InetSocketAddress neighbor, InetSocketAddress[] successors,
			int rotationInterval) {

		Level logLevel = Level.toLevel("log_level", DEFAULT_LOG_LEVEL);

		Logger.getRootLogger().setLevel(logLevel);

		ClientConnector<SchedulerServices.AsyncClient, SchedulerMessage> schedulerConnector = new ClientConnector<SchedulerServices.AsyncClient, SchedulerMessage>(
				new ThriftClientPool.SchedulerServicesFactory(), new SchedulerServiceCaller());

		TClients.launchConnector("schedulerConnector", schedulerConnector);

		NodeMonitorService nodeMonitor = new NodeMonitorService(hostIPAddr, schedulers, getTaskPort, schedulerConnector,
				listeningPort, neighbor, successors, rotationInterval);

		launchServers(nodeMonitor, listeningPort, rotationListeningPort);

	}

	private void launchServers(NodeMonitorService nodeMonitor, Integer listeningPort, Integer rotationListeningPort) {

		try {

			// Launch multi-threaded non blocking server for handling requests
			TServers.launchThreadedThriftServer("NodeMonitorServer", listeningPort, SELECTOR_THREADS, WORKER_THREADS,
					new NodeMonitorServices.AsyncProcessor<NodeMonitorServices.AsyncIface>(nodeMonitor));

			TServers.launchSingleThreadThriftServer("ProbesRotationServer", rotationListeningPort,
					new ProbesRotationServices.Processor<ProbesRotationServices.Iface>(nodeMonitor));

		} catch (IOException e) {

			LOG.error("Error Launching node Monitor listening sockets " + e.getMessage());

			e.printStackTrace();

			System.exit(-1);

		}

	}

}
