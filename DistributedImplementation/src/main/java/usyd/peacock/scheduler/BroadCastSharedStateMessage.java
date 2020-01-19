package usyd.peacock.scheduler;

import java.net.InetSocketAddress;

import usyd.distributed.scheduler.peacock.thrift.TGlobalStateUpdateRequest;
import usyd.peacock.common.SchedulerMessage;

public class BroadCastSharedStateMessage extends SchedulerMessage {

	private TGlobalStateUpdateRequest statusUpdateReq;

	public BroadCastSharedStateMessage(InetSocketAddress socket, TGlobalStateUpdateRequest statusUpdateReq) {
		super(socket);
		this.statusUpdateReq = statusUpdateReq;
	}

	public TGlobalStateUpdateRequest getGlobalStateUpdateRequest() {
		return statusUpdateReq;
	}

}
