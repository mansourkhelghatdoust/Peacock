package usyd.peacock.util;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import usyd.distributed.scheduler.peacock.thrift.THostPort;

public class Network {

	public static THostPort socketAddressToThrift(InetSocketAddress address) {

		return new THostPort(address.getAddress().getHostAddress(), address.getPort());

	}

	public static Optional<InetSocketAddress> strToSocket(String in) {

		String[] parts = in.split(":");

		if (parts.length != 2)
			return Optional.empty();

		String host = parts[0];

		if (parts[0].contains("/"))
			host = parts[0].split("/")[1];

		try {

			return Optional.of(new InetSocketAddress(host, Integer.parseInt(parts[1])));

		} catch (NumberFormatException e) {
			return Optional.empty();
		}

	}

	public static Optional<List<InetSocketAddress>> strToSockets(String in) {

		List<InetSocketAddress> sockets = new ArrayList<InetSocketAddress>();

		if (in == null)
			return Optional.empty();

		String[] parts = in.split(",");

		for (String part : parts) {

			Optional<InetSocketAddress> address = strToSocket(part);

			if (address.isPresent())
				sockets.add(address.get());
		}

		return sockets.isEmpty() ? Optional.empty() : Optional.of(sockets);

	}

}
