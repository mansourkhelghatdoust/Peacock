package usyd.distributed.scheduling.simulator.events.hawk;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import usyd.distributed.scheduling.simulator.Job;
import usyd.distributed.scheduling.simulator.Jobs;
import usyd.distributed.scheduling.simulator.Simulation;
import usyd.distributed.scheduling.simulator.SimulationUtil;
import usyd.distributed.scheduling.simulator.Worker;
import usyd.distributed.scheduling.simulator.Workers;
import usyd.distributed.scheduling.simulator.events.JobArrivalEvent;
import usyd.distributed.scheduling.simulator.events.sparrow.ProbeEvent;

public class HawkJobArrivalEvent extends JobArrivalEvent {

	public HawkJobArrivalEvent(double time, double interarrivalDelay) {
		super(time, interarrivalDelay);
	}

	@Override
	protected void sendProbes(Job job, double currentTime) {

		Jobs.numberOfActiveTasks.addAndGet(job.getNumTasks());

		if (job.getAvgExecutionTime() > 1000)
			scheduleLongJob(job, currentTime);
		else
			scheduleShortJob(job, currentTime);

	}

	private void scheduleShortJob(Job job, double currentTime) {

		Worker[] workers = Workers.getWorkers();
		List<Integer> listWorkers = Arrays.asList(Worker.getShuffledWorkers());
		Collections.shuffle(listWorkers, new Random());
		Integer[] indexes = (Integer[]) listWorkers.toArray();

		long numProbes = 2 * job.getNumTasks();

		int workerLength = Workers.getWorkers().length;

		for (int i = 0; i < numProbes; i++) {
			int j = numProbes > workerLength ? i % workerLength : i;
			Simulation.addEvent(new ProbeEvent(currentTime, workers[indexes[j]], job));
		}

	}

	private void scheduleLongJob(Job job, double currentTime) {

		long numOfTasks = job.getNumTasks();
		for (int i = 0; i < numOfTasks; i++) {
			int workerId = HawkCentralizedScheduler.getWorkerId(job);
			Simulation.addEvent(new ProbeEvent(currentTime, Workers.getWorker(HawkWorker.class, workerId), job));
		}

	}

	@Override
	protected JobArrivalEvent getJobArrivalEvent() {
		return new HawkJobArrivalEvent(Math.round(time + SimulationUtil.poisson(interArrivalDelay)), interArrivalDelay);
	}
}