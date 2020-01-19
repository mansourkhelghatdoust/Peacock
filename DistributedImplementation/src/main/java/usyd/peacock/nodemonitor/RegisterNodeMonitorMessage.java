package usyd.peacock.nodemonitor;

import java.net.InetSocketAddress;
import java.util.concurrent.CopyOnWriteArrayList;

import usyd.peacock.common.SchedulerMessage;

public class RegisterNodeMonitorMessage extends SchedulerMessage {

	private String nodeMonitorAddr = null;

	private String schedulerSocketStr = null;

	private CopyOnWriteArrayList<String> unregisteredSchedulers = new CopyOnWriteArrayList<String>();

	public RegisterNodeMonitorMessage(InetSocketAddress socket, String nodeMonitorAddr, String schedulerSocketStr,
			CopyOnWriteArrayList<String> unregisteredSchedulers, RegisterNodeMonitorErrorCallback errorCallback) {

		super(socket);

		this.nodeMonitorAddr = nodeMonitorAddr;

		this.schedulerSocketStr = schedulerSocketStr;

		this.unregisteredSchedulers = unregisteredSchedulers;

		this.errorCallback = errorCallback;

	}

	public String getNodeMonitorAddr() {
		return nodeMonitorAddr;
	}

	public void setNodeMonitorAddr(String nodeMonitorAddr) {
		this.nodeMonitorAddr = nodeMonitorAddr;
	}

	public String getSchedulerSocketStr() {
		return schedulerSocketStr;
	}

	public void setSchedulerSocketStr(String schedulerSocketStr) {
		this.schedulerSocketStr = schedulerSocketStr;
	}

	public CopyOnWriteArrayList<String> getUnregisteredSchedulers() {
		return unregisteredSchedulers;
	}

	public void setUnregisteredSchedulers(CopyOnWriteArrayList<String> unregisteredSchedulers) {
		this.unregisteredSchedulers = unregisteredSchedulers;
	}

}
