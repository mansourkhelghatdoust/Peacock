package usyd.distributed.scheduling.simulator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import usyd.distributed.scheduling.simulator.events.hawk.HawkQueue;
import usyd.distributed.scheduling.simulator.events.hawk.HawkWorker;
import usyd.distributed.scheduling.simulator.observers.SimulationObserver;
import usyd.distributed.scheduling.simulator.observers.Visitable;
import usyd.distributed.scheduling.simulator.observers.Visitor;

public class Job implements Visitable {

	private long jobId;

	private int numTasks = 0;
	// Time when this job is entered to data center (Peacock) or Time when this
	// job is added to the queue (Sparrow)
	private double startTime = 0;

	private double endTime = 0;

	private int taskCompletedCount = 0;

	private long avgExecutionTime = 0;

	private double maximumWaitingTime = 0;

	private AtomicInteger moveCount = new AtomicInteger(0);

	private ArrayList<Long> unscheduledTasks = new ArrayList<Long>();

	private ReentrantLock perJobLock = new ReentrantLock(true);

	public Job(long jobId) {
		this.setJobId(jobId);
	}

	public ArrayList<Long> unscheduledTasks() {
		return this.unscheduledTasks;
	}

	public int getNumTasks() {
		return numTasks;
	}

	public void setNumTasks(int numTasks) {
		this.numTasks = numTasks;
	}

	public double getStartTime() {
		return startTime;
	}

	public long getTaskCompletedCount() {
		return taskCompletedCount;
	}

	public boolean isCompleted() {
		return getNumTasks() - getTaskCompletedCount() == 0;
	}

	public void setTaskCompletedCount(int taskCompletedCount) {
		this.taskCompletedCount = taskCompletedCount;
	}

	public void addTaskCompletionTime(double taskEndTime) {

		try {
			perJobLock.lock();
			if (this.taskCompleted(taskEndTime)) {
				// SimulationObserver.jobCompleted(jobId, this);
				Jobs.increaseJobCompletedCount();
				SimulationObserver.printState();
			}
		} finally {
			perJobLock.unlock();
		}
	}

	public double getTaskDuration() {

		try {
			perJobLock.lock();
			if (unscheduledTasks.size() > 0)
				return unscheduledTasks.remove(0);
			return -1;
		} finally {
			perJobLock.unlock();
		}
	}

	public double getEndTime() {
		return endTime;
	}

	public void setStartTime(double startTime) {
		this.startTime = startTime;
	}

	public boolean taskCompleted(double completionTime) {

		taskCompletedCount++;
		endTime = Math.max(completionTime, endTime);
		return numTasks == taskCompletedCount;

	}

	public long getJobId() {
		return jobId;
	}

	public void setJobId(long jobId) {
		this.jobId = jobId;
	}

	public long getAvgExecutionTime() {
		return avgExecutionTime;
	}

	public void setAvgExecutionTime(long avgExecutionTime) {
		this.avgExecutionTime = avgExecutionTime;
	}

	public void setEstimationExecutionTime(long estimationExecutionTime) {
		this.avgExecutionTime = estimationExecutionTime;
	}

	public double getMaximumWaitingTime() {
		return maximumWaitingTime;
	}

	public void setMaximumWaitingTime(double maximumWaitingTime) {
		this.maximumWaitingTime = maximumWaitingTime;
	}

	public void increaseMove() {
		moveCount.incrementAndGet();
	}

	public int getMoveCount() {
		return moveCount.get();
	}

	public void addTask(long taskDuration) {

		unscheduledTasks.add(taskDuration);
		numTasks++;
		long sum = unscheduledTasks.stream().reduce(0L, (a, b) -> a + b);
		setAvgExecutionTime(sum / unscheduledTasks.size());

	}

	public static class StartTimeComparator implements Comparator<Job>, Serializable {

		private static final long serialVersionUID = 1L;

		private static final StartTimeComparator instance = new StartTimeComparator();
		int workerId = -1;
		Class<? extends Worker> clz = null;

		private StartTimeComparator() {

		}

		public StartTimeComparator(int workerId, Class<? extends Worker> clz) {
			this.workerId = workerId;
			this.clz = clz;
		}

		@Override
		public int compare(Job job1, Job job2) {

			HawkQueue<Job> queue = null;
			if (workerId != -1 && clz.equals(HawkWorker.class))
				queue = Workers.getWorker(HawkWorker.class, workerId).getHawkQueue();

			double diff = job1.getStartTime() - job2.getStartTime();

			if (diff < 0 || diff > 0)
				return (int) diff;
			else if (queue != null && job1.hashCode() == job2.hashCode() && !queue.isAddMode())
				return 0;
			else
				return -1;

		}

		public static StartTimeComparator getInstance() {
			return instance;
		}
	}

	public class TaskCompletionService implements Runnable {

		private Job job = null;
		private long taskEndTime = 0;

		public TaskCompletionService(Job job, long taskEndTime) {
			this.job = job;
			this.taskEndTime = taskEndTime;
		}

		@Override
		public void run() {
			job.addTaskCompletionTime(taskEndTime);
		}
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}