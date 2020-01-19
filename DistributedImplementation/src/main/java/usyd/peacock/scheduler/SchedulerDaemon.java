package usyd.peacock.scheduler;

import java.io.IOException;
import java.util.Scanner;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import usyd.distributed.scheduler.peacock.thrift.FrontendServices;
import usyd.distributed.scheduler.peacock.thrift.NodeMonitorServices;
import usyd.distributed.scheduler.peacock.thrift.SchedulerGetTaskService;
import usyd.distributed.scheduler.peacock.thrift.SchedulerServices;
import usyd.peacock.common.ClientConnector;
import usyd.peacock.common.FrontendMessage;
import usyd.peacock.common.FrontendServiceCaller;
import usyd.peacock.common.NodeMonitorMessage;
import usyd.peacock.common.NodeMonitorServiceCaller;
import usyd.peacock.common.SchedulerMessage;
import usyd.peacock.common.SchedulerServiceCaller;
import usyd.peacock.util.TClients;
import usyd.peacock.util.TServers;
import usyd.peacock.util.ThriftClientPool;

public class SchedulerDaemon {

	private final static Logger LOG = Logger.getLogger(SchedulerDaemon.class);

	public final static Level DEFAULT_LOG_LEVEL = Level.DEBUG;

	private final static Integer SELECTOR_THREADS = 2;

	private final static Integer WORKER_THREADS = 2;

	private String schedulers = null;

	private Integer listeningPort = 0;

	private Integer getTaskPort = 0;
	
	private String hostIPAddr = null;

	public static void main(String[] args) {

		BasicConfigurator.configure();

		SchedulerDaemon daemon = new SchedulerDaemon();

		daemon.run(args);

	}

	public void run(String[] args) {

		Scanner scanner = new Scanner(System.in);

		try {

			LOG.info("Launching a scheduler instance");

			validate(args);

			extractArguments(args);

			initialize(schedulers, listeningPort, getTaskPort);

			LOG.info("Type 'Exit' to terminate Scheduler ...");

			while (scanner.hasNext()) {

				if (scanner.nextLine().toUpperCase().equals("EXIT")) {
					LOG.info("Scheduler server has been terminated ...");
					TServers.stop();
					TClients.stop();
					break;
				}

				LOG.info("Type 'Exit' to terminate Scheduler ...");

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

			} else if (args[i].equals("--getTaskPort")) {

				getTaskPort = Integer.parseInt(args[i + 1]);

			} else if (args[i].equals("--hostIPAddr")) {

				hostIPAddr = args[i + 1];

			} else {

				LOG.error(args[i] + " not known");
				System.exit(-1);
			}

		}

	}

	private static void validate(String[] args) {

		if (args == null || args.length < 6) {

			LOG.error("The number of input parameters " + args.length + " is wrong,  terminating ...");

			LOG.info("--schedulers, --listeningPort, --getTaskPort");

			System.exit(-1);

		}

	}

	private void initialize(String schedulers, Integer listeningPort, Integer getTaskPort) {

		Level logLevel = Level.toLevel("log_level", DEFAULT_LOG_LEVEL);

		Logger.getRootLogger().setLevel(logLevel);

		ClientConnector<FrontendServices.AsyncClient, FrontendMessage> frontendConnector = new ClientConnector<FrontendServices.AsyncClient, FrontendMessage>(
				new ThriftClientPool.FrontendServicesFactory(), new FrontendServiceCaller());

		ClientConnector<SchedulerServices.AsyncClient, SchedulerMessage> schedulerConnector = new ClientConnector<SchedulerServices.AsyncClient, SchedulerMessage>(
				new ThriftClientPool.SchedulerServicesFactory(), new SchedulerServiceCaller());

		ClientConnector<NodeMonitorServices.AsyncClient, NodeMonitorMessage> nodeMonitorConnector = new ClientConnector<NodeMonitorServices.AsyncClient, NodeMonitorMessage>(
				new ThriftClientPool.NodeMonitorServicesFactory(), new NodeMonitorServiceCaller());

		TClients.launchConnector("frontendConnector", frontendConnector);

		TClients.launchConnector("schedulerConnector", schedulerConnector);

		TClients.launchConnector("nodeMonitorConnector", nodeMonitorConnector);

		SchedulerService scheduler = new SchedulerService(hostIPAddr, schedulers, frontendConnector, schedulerConnector,
				nodeMonitorConnector, listeningPort);

		launchServers(scheduler, listeningPort, getTaskPort);

	}

	private void launchServers(SchedulerService scheduler, Integer listeningPort, Integer getTaskPort) {

		try {

			// Launch multi-threaded non blocking server for handling
			// requests
			TServers.launchThreadedThriftServer("SchedulerServer", listeningPort, SELECTOR_THREADS, WORKER_THREADS,
					new SchedulerServices.AsyncProcessor<SchedulerServices.AsyncIface>(scheduler));

			// Launch server for handling get task requests
			TServers.launchSingleThreadThriftServer("GetTaskServer", getTaskPort,
					new SchedulerGetTaskService.Processor<SchedulerGetTaskService.Iface>(scheduler));

		} catch (IOException e) {

			LOG.error("Error Launching Scheduler listening sockets " + e.getMessage());

			e.printStackTrace();

			System.exit(-1);

		}

	}

}
