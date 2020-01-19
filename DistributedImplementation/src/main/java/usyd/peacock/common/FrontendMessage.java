package usyd.peacock.common;

import java.net.InetSocketAddress;

public class FrontendMessage extends ClientConnectorMessageBase {

	public FrontendMessage(InetSocketAddress socket) {
		super(socket);
	}

}