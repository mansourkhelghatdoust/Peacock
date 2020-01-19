package usyd.distributed.scheduling.simulator.events.eagle;

import usyd.distributed.scheduling.simulator.Configuration;
import usyd.distributed.scheduling.simulator.Worker;
import usyd.distributed.scheduling.simulator.events.BaseEvent;

public class NoopGetTaskResponseEvent extends BaseEvent {

	private Worker worker;

	public NoopGetTaskResponseEvent(double time, Worker worker) {
		super((int) time + 2 * Configuration.getConfig().getNetworkDelay());
		this.setWorker(worker);
	}

	@Override
	public void run() {

		this.worker.freeSlot(time, null);

	}

	public Worker getWorker() {
		return this.worker;
	}

	public void setWorker(Worker worker) {
		this.worker = worker;
	}

}