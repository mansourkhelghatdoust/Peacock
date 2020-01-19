package usyd.distributed.scheduling.simulator.events.hawk;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import usyd.distributed.scheduling.simulator.Configuration;
import usyd.distributed.scheduling.simulator.Job;
import usyd.distributed.scheduling.simulator.Simulation;
import usyd.distributed.scheduling.simulator.Worker;
import usyd.distributed.scheduling.simulator.Workers;
import usyd.distributed.scheduling.simulator.events.TaskEndEvent;

public class HawkWorker extends Worker {

	private static final long serialVersionUID = 1L;
	private HawkQueue<Job> queue = null;

	public HawkWorker(int id, int numOfSlots) {
		super(id, numOfSlots);
		queue = new HawkQueue<Job>(new Job.StartTimeComparator(id, HawkWorker.class));
	}

	public void addProbe(Job job, double time, ConcurrentHashMap<String, Long> extraInfo) {

		queue.add(job);
		mayBeGetTask(time);
	}

	public HawkQueue<Job> getHawkQueue() {
		return this.queue;
	}

	public void mayBeGetTask(double time) {

		if (this.freeSlots > 0) {

			if (queue.isEmpty()) {
				Simulation.addEvent(new StealWorkEvent((double) time + (2 * Configuration.getConfig().getNetworkDelay()),
						this.getId(), new Random().nextInt(Configuration.getTotalWorkers() * 90 / 100)));
			} else {

				Job job = queue.pollFirst();
				this.freeSlots--;
				double taskDuration = job.getTaskDuration();
				if (taskDuration >= 0) {
					double taskEndTime = time + (2 * Configuration.getConfig().getNetworkDelay()) + taskDuration;
					Simulation.addEvent(new TaskEndEvent((int) taskEndTime, this, new JobTaskInfo(job, taskEndTime)));
				} else
					Simulation.addEvent(new NoopGetTaskResponseEvent(time, this));

			}
		}
	}

	public static void setWorkers() {

		Worker.setShuffledWorkers(new Integer[Configuration.getTotalWorkers()]);
		Worker[] workers = new HawkWorker[Configuration.getTotalWorkers()];

		for (int idx = 0; idx < workers.length; idx++) {
			workers[idx] = new HawkWorker(idx, Configuration.getNumOfSlotsPerWorker());
			Worker.getShuffledWorkers()[idx] = idx;
		}

		Workers.setWorkers(workers);

	}

	public Job getHighestPriorityShortJobId() {

		Job retVal = null;

		Iterator<Job> iterator = queue.iterator();

		while (iterator.hasNext()) {

			Job current = iterator.next();
			if (current.getAvgExecutionTime() <= 1000) {
				queue.remove(current);
				retVal = current;
				break;
			}
		}

		return retVal;
	}
}