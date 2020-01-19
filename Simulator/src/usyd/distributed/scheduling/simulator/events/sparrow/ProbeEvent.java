package usyd.distributed.scheduling.simulator.events.sparrow;

import usyd.distributed.scheduling.simulator.Configuration;
import usyd.distributed.scheduling.simulator.Job;
import usyd.distributed.scheduling.simulator.Worker;
import usyd.distributed.scheduling.simulator.events.BaseEvent;

public class ProbeEvent extends BaseEvent {

	private Job job;
	private Worker worker;

	public ProbeEvent(double time, Worker worker, Job job) {
		super((int)time + Configuration.getConfig().getNetworkDelay());
		this.setJob(job);
		this.setWorker(worker);
	}

	@Override
	public void run() {

		this.worker.addProbe(job, time, null);
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public Worker getWorker() {
		return worker;
	}

	public void setWorker(Worker worker) {
		this.worker = worker;
	}

}