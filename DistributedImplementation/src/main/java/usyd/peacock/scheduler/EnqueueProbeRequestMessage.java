package usyd.peacock.scheduler;

import java.net.InetSocketAddress;

import usyd.distributed.scheduler.peacock.thrift.TEnqueueProbeRequest;
import usyd.peacock.common.NodeMonitorMessage;

public class EnqueueProbeRequestMessage extends NodeMonitorMessage {

	private TEnqueueProbeRequest request;

	public EnqueueProbeRequestMessage(InetSocketAddress socket, TEnqueueProbeRequest request) {
		super(socket);
		this.setRequest(request);
	}

	public TEnqueueProbeRequest getRequest() {
		return request;
	}

	public void setRequest(TEnqueueProbeRequest request) {
		this.request = request;
	}

}
