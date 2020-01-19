package usyd.distributed.scheduling.simulator.events.eagle;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import usyd.distributed.scheduling.simulator.Configuration;
import usyd.distributed.scheduling.simulator.Job;
import usyd.distributed.scheduling.simulator.Simulation;
import usyd.distributed.scheduling.simulator.Worker;
import usyd.distributed.scheduling.simulator.Workers;

public class EagleWorker extends Worker {

	private static final long serialVersionUID = 1L;

	private LinkedList<Job> queue = new LinkedList<Job>();

	private SuccinctStateSharing state;

	private int hasLongJob;

	private HashMap<Long, Double> byPassCount = new HashMap<Long, Double>();

	public EagleWorker(int id, int numOfSlots) {
		super(id, numOfSlots);
	}

	public void addProbe(Job job, double time, ConcurrentHashMap<String, Long> extraInfo) {

		if (job.getAvgExecutionTime() < 2000 && containsLongJob()) {

			Simulation.addEvent(new RejectedProbeEvent((int) time, new EagleProbeEvent(time, this, job, state)));
			return;

		}

		queue.addLast(job);

		mayBeGetTask(time);
	}

	public void addProbe(Job job, double time, SuccinctStateSharing state) {

		hasLongJob++;

		queue.addLast(job);

		if (this.state == null || state.timeStamp > this.state.timeStamp)
			this.state = state;

		mayBeGetTask(time);
	}

	public void mayBeGetTask(double time) {

		while (!queue.isEmpty() && this.freeSlots > 0) {

			int jobIdx = getProbeFromQueue();
			double taskDuration = queue.get(jobIdx).getTaskDuration();

			if (taskDuration >= 0) {
				this.freeSlots--;
				if (taskDuration > 2000)
					hasLongJob--;
				double taskEndTime = time + (2 * Configuration.getConfig().getNetworkDelay()) + taskDuration;
				Simulation.addEvent(new EagleTaskEndEvent((int) taskEndTime, this,
						new JobTaskInfo(queue.get(jobIdx), taskEndTime)));
				updateJobsWaitingTimes(jobIdx, queue.get(jobIdx).getAvgExecutionTime());
			} else {
				this.freeSlots--;
				Simulation.addEvent(new NoopGetTaskResponseEvent(time, this));
				byPassCount.remove(queue.get(jobIdx));
				queue.remove(jobIdx);
			}
		}

	}

	private void updateJobsWaitingTimes(int jobIdx, double addedWaitingTime) {

		for (int idx = 0; idx < jobIdx; idx++) {
			if (byPassCount.get(queue.get(idx).getJobId()) == null)
				byPassCount.put(queue.get(idx).getJobId(), 0.0);
			byPassCount.put(queue.get(idx).getJobId(), byPassCount.get(queue.get(idx).getJobId()) + addedWaitingTime);
		}

	}

	private int getProbeFromQueue() {

		int jobIdx = -1;
		double shortestWaitingTime = Long.MAX_VALUE;

		for (int idx = 0; idx < queue.size(); idx++) {

			Job job = queue.get(idx);
			if (job.getAvgExecutionTime() > 2000) {
				if (jobIdx == -1)
					return 0;
				else
					return jobIdx;
			}
			if (byPassCount.get(job.getJobId()) != null && byPassCount.get(job.getJobId()) >= 1000) {
				if (jobIdx == -1)
					return 0;
				else
					return jobIdx;
			}
			
			double waitingTime = job.getAvgExecutionTime() * job.getNumTasks();
			
			if (waitingTime < shortestWaitingTime) {
				jobIdx = idx;
				shortestWaitingTime = waitingTime;
			}

		}
		return jobIdx;
	}

	private boolean containsLongJob() {
		return hasLongJob > 0;
	}

	public static void setWorkers() {

		Worker.setShuffledWorkers(new Integer[Configuration.getTotalWorkers()]);
		Worker[] workers = new EagleWorker[Configuration.getTotalWorkers()];

		for (int idx = 0; idx < workers.length; idx++) {
			workers[idx] = new EagleWorker(idx, Configuration.getNumOfSlotsPerWorker());
			Worker.getShuffledWorkers()[idx] = idx;
		}

		Workers.setWorkers(workers);

	}
}