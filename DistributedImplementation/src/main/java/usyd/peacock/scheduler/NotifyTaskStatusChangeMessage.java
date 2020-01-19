package usyd.peacock.scheduler;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import usyd.distributed.scheduler.peacock.thrift.TLightProbe;
import usyd.peacock.common.FrontendMessage;

public class NotifyTaskStatusChangeMessage extends FrontendMessage {

	private TLightProbe probe;
	private int status;
	private ByteBuffer message;

	public NotifyTaskStatusChangeMessage(InetSocketAddress socket, TLightProbe probe, ByteBuffer message, int status) {
		super(socket);
		this.probe = probe;
		this.status = status;
		this.message = message;
	}

	public TLightProbe getProbe() {
		return probe;
	}

	public int getStatus() {
		return status;
	}

	public ByteBuffer getMessage() {
		return message;
	}
}
