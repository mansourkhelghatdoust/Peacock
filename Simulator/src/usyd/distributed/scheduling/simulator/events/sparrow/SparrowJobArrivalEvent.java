package usyd.distributed.scheduling.simulator.events.sparrow;

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

public class SparrowJobArrivalEvent extends JobArrivalEvent {

	public SparrowJobArrivalEvent(double time, double interarrivalDelay) {
		super((int) time, interarrivalDelay);
	}

	@Override
	protected void sendProbes(Job job, double currentTime) {

		Worker[] workers = Workers.getWorkers();
		List<Integer> listWorkers = Arrays.asList(Worker.getShuffledWorkers());
		Collections.shuffle(listWorkers, new Random());
		Integer[] indexes = (Integer[]) listWorkers.toArray();

		long numProbes = 2 * job.getNumTasks();
		int workerLength = Workers.getWorkers().length;
		Jobs.numberOfActiveTasks.addAndGet(job.getNumTasks());

		for (int i = 0; i < numProbes; i++) {
			int j = numProbes > workerLength ? i % workerLength : i;
			Simulation.addEvent(new ProbeEvent(currentTime, workers[indexes[j]], job));
		}
	}

	@Override
	protected JobArrivalEvent getJobArrivalEvent() {
		return new SparrowJobArrivalEvent(Math.round(time + SimulationUtil.poisson(interArrivalDelay)),
				interArrivalDelay);
	}
}