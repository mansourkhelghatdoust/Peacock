package usyd.peacock.nodemonitor;

import java.net.InetSocketAddress;

import usyd.peacock.common.SchedulerMessage;

public class UnRegisterNodeMonitorMessage extends SchedulerMessage {

	private String failedNodeMonitorAddress = null;

	public UnRegisterNodeMonitorMessage(InetSocketAddress socket, String failedNodeMonitorAddress) {

		super(socket);

		this.failedNodeMonitorAddress = failedNodeMonitorAddress;

	}

	public String getFailedNodeMonitorAddress() {
		return failedNodeMonitorAddress;
	}

	public void setFailedNodeMonitorAddress(String failedNodeMonitorAddress) {
		this.failedNodeMonitorAddress = failedNodeMonitorAddress;
	}

}
