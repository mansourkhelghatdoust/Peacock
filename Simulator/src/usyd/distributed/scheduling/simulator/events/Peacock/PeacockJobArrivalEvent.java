package usyd.distributed.scheduling.simulator.events.Peacock;

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
import usyd.distributed.scheduling.simulator.concurrent.Peacock.ConcurrentExecutor;
import usyd.distributed.scheduling.simulator.events.JobArrivalEvent;
import usyd.distributed.scheduling.simulator.events.Peacock.ProbeEvent;
import usyd.distributed.scheduling.util.DataHelper;

public class PeacockJobArrivalEvent extends JobArrivalEvent {

	public PeacockJobArrivalEvent(double time, double interarrivalDelay) {
		super(time, interarrivalDelay);
	}

	@Override
	protected JobArrivalEvent getJobArrivalEvent() {
		return new PeacockJobArrivalEvent(Math.round(time + SimulationUtil.poisson(interArrivalDelay)),
				interArrivalDelay);
	}

	@Override
	protected void sendProbes(Job job, double currentTime) {

		job.setMaximumWaitingTime(currentTime + PeacockScheduler.getGlobalInfo().get(PeacockScheduler.WorkerLoad));

		int numProbes = job.getNumTasks();

		Worker[] workersList = Workers.getWorkers();

		List<Integer> listWorkers = Arrays.asList(Worker.getShuffledWorkers());

		Collections.shuffle(listWorkers, new Random());

		Integer[] indexes = (Integer[]) listWorkers.toArray();

		PeacockWorker[] workers = new PeacockWorker[numProbes];

		for (int i = 0; i < numProbes; i++) {

			int j = numProbes > workersList.length ? i % workersList.length : i;

			Jobs.numberOfActiveTasks.incrementAndGet();

			Jobs.estimationExecutionTimeOfActiveTasks.addAndGet((long) DataHelper.round(job.getAvgExecutionTime(), 0));

			workers[i] = Workers.getWorker(PeacockWorker.class, workersList[indexes[j]].getId());

		}

		ProbeEvent probeEvent = new ProbeEvent(currentTime, job, workers);

		Simulation.addEvent(probeEvent);

	}

	@Override
	public void runConc() throws Exception {

		ConcurrentExecutor.waitForConcEventsExecute();
		run();
	}

}