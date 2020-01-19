package usyd.peacock.common;

import java.net.InetSocketAddress;

public class SchedulerMessage extends ClientConnectorMessageBase {

	public SchedulerMessage(InetSocketAddress socket) {
		super(socket);
	}
}
