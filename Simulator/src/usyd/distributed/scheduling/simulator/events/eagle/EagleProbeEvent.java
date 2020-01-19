package usyd.distributed.scheduling.simulator.events.eagle;

import usyd.distributed.scheduling.simulator.Configuration;
import usyd.distributed.scheduling.simulator.Job;
import usyd.distributed.scheduling.simulator.Worker;
import usyd.distributed.scheduling.simulator.events.BaseEvent;

public class EagleProbeEvent extends BaseEvent {

	private Job job;
	private Worker worker;
	private SuccinctStateSharing state;

	public EagleProbeEvent(double time, Worker worker, Job job, SuccinctStateSharing state) {

		super((int) time + Configuration.getConfig().getNetworkDelay());
		this.setJob(job);
		this.setWorker(worker);
		this.setState(state);

	}

	@Override
	public void run() {

		((EagleWorker) this.worker).addProbe(job, time, state);
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

	public SuccinctStateSharing getState() {
		return state;
	}

	public void setState(SuccinctStateSharing state) {
		this.state = state;
	}

}
