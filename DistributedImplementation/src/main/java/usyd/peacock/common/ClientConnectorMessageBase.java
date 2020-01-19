package usyd.peacock.common;

import java.net.InetSocketAddress;

public class ClientConnectorMessageBase implements Message {

	protected InetSocketAddress socket;

	protected ClientConnectorErrorCallback errorCallback = null;

	public ClientConnectorMessageBase(InetSocketAddress socket) {
		super();
		this.socket = socket;
	}

	public ClientConnectorMessageBase(InetSocketAddress socket, ClientConnectorErrorCallback errorCallback) {
		this(socket);
		this.errorCallback = errorCallback;
	}

	@Override
	public InetSocketAddress getSocket() {
		return this.socket;
	}

	public ClientConnectorErrorCallback getErrorCallback() {
		return errorCallback;
	}

}
