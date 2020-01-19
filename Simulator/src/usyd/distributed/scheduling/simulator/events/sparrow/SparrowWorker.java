package usyd.distributed.scheduling.simulator.events.sparrow;

import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

import usyd.distributed.scheduling.simulator.Configuration;
import usyd.distributed.scheduling.simulator.Job;
import usyd.distributed.scheduling.simulator.Simulation;
import usyd.distributed.scheduling.simulator.Worker;
import usyd.distributed.scheduling.simulator.Workers;
import usyd.distributed.scheduling.simulator.events.TaskEndEvent;

public class SparrowWorker extends Worker {

	private static final long serialVersionUID = 1L;

	public SparrowWorker(int id, int numOfSlots) {

		super(id, numOfSlots);
		this.setQueuedProbes(new PriorityQueue<Job>(10, new Job.StartTimeComparator(id, SparrowWorker.class)));

	}

	public void addProbe(Job job, double time, ConcurrentHashMap<String, Long> extraInfo) {

		queue.add(job);
		mayBeGetTask(time);

	}

	public void mayBeGetTask(double time) {

		if (!queue.isEmpty() && this.freeSlots > 0) {

			Job job = queue.poll();
			this.freeSlots--;
			double taskDuration = job.getTaskDuration();
			if (taskDuration >= 0) {
				double taskEndTime = time + (2 * Configuration.getConfig().getNetworkDelay()) + taskDuration;
				Simulation.addEvent(new TaskEndEvent((int) taskEndTime, this, new JobTaskInfo(job, taskEndTime)));
			} else
				Simulation.addEvent(new NoopGetTaskResponseEvent(time, this));

		}
	}

	public static void setWorkers() {

		Worker.setShuffledWorkers(new Integer[Configuration.getTotalWorkers()]);
		Worker[] workers = new SparrowWorker[Configuration.getTotalWorkers()];

		for (int idx = 0; idx < workers.length; idx++) {
			workers[idx] = new SparrowWorker(idx, Configuration.getNumOfSlotsPerWorker());
			Worker.getShuffledWorkers()[idx] = idx;
		}

		Workers.setWorkers(workers);

	}
}