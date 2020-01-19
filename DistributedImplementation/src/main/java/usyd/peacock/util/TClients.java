package usyd.peacock.util;

import java.util.ArrayList;

import usyd.peacock.common.ClientConnector;
import usyd.peacock.nodemonitor.InternalConcurrentService;

/**
 * Helper functions for creating Thrift clients for various Eagle interfaces.
 */
public class TClients {

	@SuppressWarnings("rawtypes")
	private static ArrayList<ClientConnector> connectors = new ArrayList<ClientConnector>();

	private static ArrayList<InternalConcurrentService> internalServices = new ArrayList<InternalConcurrentService>();

	@SuppressWarnings("rawtypes")
	public static void launchConnector(String name, ClientConnector connector) {

		new Thread(connector, name).start();

		connectors.add(connector);
	}

	public static void createInternalConcurrentService(String name, InternalConcurrentService internalService,
			boolean newThread) {

		if (newThread)
			new Thread(internalService, name).start();

		internalServices.add(internalService);
	}

	@SuppressWarnings("rawtypes")
	public static void stop() {

		for (ClientConnector client : connectors)
			client.stop();

		for (InternalConcurrentService client : internalServices)
			client.stop();

	}
}