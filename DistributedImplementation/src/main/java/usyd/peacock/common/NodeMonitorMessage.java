package usyd.peacock.common;

import java.net.InetSocketAddress;

public class NodeMonitorMessage extends ClientConnectorMessageBase {

	public NodeMonitorMessage(InetSocketAddress socket) {
		super(socket);
	}


}
