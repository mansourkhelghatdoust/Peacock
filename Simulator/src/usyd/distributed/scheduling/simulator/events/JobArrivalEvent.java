package usyd.distributed.scheduling.simulator.events;


import usyd.distributed.scheduling.simulator.Job;
import usyd.distributed.scheduling.simulator.Jobs;
import usyd.distributed.scheduling.simulator.Simulation;

public abstract class JobArrivalEvent extends BaseEvent {

	protected double interArrivalDelay;

	public JobArrivalEvent(double time) {

		super(time);
		Jobs.decreaseRemainingJobs();

	}

	public JobArrivalEvent(double time, double interarrivalDelay) {

		super(time);
		this.interArrivalDelay = interarrivalDelay;
		Jobs.decreaseRemainingJobs();

	}

	public void run() {

		Job job = Jobs.getInstance().getNextJob(time);
		
		sendProbes(job, time);

		if (Jobs.isJobNeeded()) 
			Simulation.addEvent(getJobArrivalEvent());

	}

	protected abstract JobArrivalEvent getJobArrivalEvent();

	protected abstract void sendProbes(Job job, double currentTime);
}