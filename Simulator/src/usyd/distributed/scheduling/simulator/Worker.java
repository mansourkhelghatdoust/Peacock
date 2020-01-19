package usyd.distributed.scheduling.simulator;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Worker implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Integer[] shuffledWorkers;

	protected int id = -1;

	protected int numOfSlots = 0;

	protected int freeSlots = 0;

	protected Queue<Job> queue = null;

	public Worker(int id, int numOfSlots) {
		this.setId(id);
		this.setNumOfSlots(numOfSlots);
		this.setFreeSlots(numOfSlots);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumOfSlots() {
		return numOfSlots;
	}

	public void setNumOfSlots(int numOfSlots) {
		this.numOfSlots = numOfSlots;
	}

	public int getFreeSlots() {
		return freeSlots;
	}

	private void setFreeSlots(int freeSlots) {
		this.freeSlots = freeSlots;
	}

	protected void setQueuedProbes(Queue<Job> queue) {
		this.queue = queue;
	}

	protected Queue<Job> getQueuedProbes() {
		return this.queue;
	}

	public abstract void addProbe(Job job, double time, ConcurrentHashMap<String, Long> extraInfo);

	public void freeSlot(double time, JobTaskInfo job) {
		this.freeSlots++;
		this.mayBeGetTask(time);
	}

	public static Integer[] getShuffledWorkers() {
		return shuffledWorkers;
	}

	public static void setShuffledWorkers(Integer[] shuffledWorkers) {
		Worker.shuffledWorkers = shuffledWorkers;
	}

	protected abstract void mayBeGetTask(double time);

	public class JobTaskInfo {

		private Job job;
		// Time to start execution of task or expected waiting Time
		private double time;

		private int numOfCores;

		public JobTaskInfo(Job job, double time) {
			this.setJob(job);
			this.setTime(time);
		}

		public Job getJob() {
			return job;
		}

		public void setJob(Job job) {
			this.job = job;
		}

		public double getTime() {
			return time;
		}

		public void setTime(double time) {
			this.time = time;
		}

		public void increaseTimeBy(double time) {
			this.time += time;
		}

		public void decreaseTimeBy(double time) {
			this.time -= time;
		}

		public double getRemainingEstimationTime(double time) {
			double retVal = this.time + job.getAvgExecutionTime() - time;
			return retVal > 0 ? retVal : 0;
		}

		@SuppressWarnings("unchecked")
		public boolean hasBeenScheduledLaterThan(JobTaskInfo job, @SuppressWarnings("rawtypes") Comparator comparator) {
			return comparator.compare(this, job) >= 0;
		}

		public boolean hasBeenScheduledLaterThanAndCannotSwap(JobTaskInfo otherJob,
				@SuppressWarnings("rawtypes") Comparator comparator) {

			if (!hasBeenScheduledLaterThan(otherJob, comparator))
				return false;

			if (this.hasLessExecutionTimeThan(otherJob))
				if (otherJob.waitInQueueLessThanItsMaximumThresholdAfterCrossing(this)
						&& !otherJob.isMaximumThresholdPassed())
					return false;

			return true;

		}

		public boolean waitInQueueLessThanItsMaximumThreshold() {
			return this.getJob().getMaximumWaitingTime() > this.getTime();
		}

		public boolean waitInQueueLessThanItsMaximumThresholdAfterCrossing(JobTaskInfo crossingJob) {
			return this.getTime() + crossingJob.getJob().getAvgExecutionTime() < this.getJob().getMaximumWaitingTime();
		}

		public boolean waitInQueueMoreThanItsMaximumThreshold() {
			return this.getTime() > this.getJob().getMaximumWaitingTime();
		}

		public boolean hasLessExecutionTimeThan(JobTaskInfo otherJob) {
			return job.getAvgExecutionTime() < otherJob.getJob().getAvgExecutionTime();
		}

		public boolean isMaximumThresholdPassed() {
			return this.getJob().getMaximumWaitingTime() < this.time;
		}

		public int getNumOfCores() {
			return numOfCores;
		}

		public void setNumOfCores(int numOfCores) {
			this.numOfCores = numOfCores;
		}
	}
}