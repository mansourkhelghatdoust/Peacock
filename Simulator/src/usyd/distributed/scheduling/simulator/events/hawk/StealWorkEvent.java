package usyd.distributed.scheduling.simulator.events.hawk;

import usyd.distributed.scheduling.simulator.Job;
import usyd.distributed.scheduling.simulator.Workers;
import usyd.distributed.scheduling.simulator.events.BaseEvent;

public class StealWorkEvent extends BaseEvent {

	private int senderId;
	private int recipientId;

	public StealWorkEvent(double time, int senderId, int recipientId) {
		super(time);
		this.setSenderId(senderId);
		this.setRecipientId(recipientId);
	}

	@Override
	public void run() {

		Job job = Workers.getWorker(HawkWorker.class, recipientId).getHighestPriorityShortJobId();
		if (job != null)
			Workers.getWorker(HawkWorker.class, senderId).addProbe(job, time, null);

	}

	public int getSenderId() {
		return senderId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}

	public int getRecipientId() {
		return recipientId;
	}

	public void setRecipientId(int recipientId) {
		this.recipientId = recipientId;
	}

}
