package usyd.distributed.scheduling.simulator.events.Peacock;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

import usyd.distributed.scheduling.simulator.Worker.JobTaskInfo;
import usyd.distributed.scheduling.simulator.Workers;

public class PeacockQueue implements Queue<JobTaskInfo>, Filterable<JobTaskInfo> {

	private int workerId;
	private long totalEstimationTimeOfQueuedJobs;
	private Comparator<JobTaskInfo> comparator;
	private CopyOnWriteArrayList<JobTaskInfo> runningJobs;
	private LinkedList<JobTaskInfo> queuedJobs;

	public PeacockQueue(int workerId, Comparator<JobTaskInfo> comparator) {
		this.setWorkerId(workerId);
		this.setComparator(comparator);
		this.runningJobs = new CopyOnWriteArrayList<JobTaskInfo>();
		this.queuedJobs = new LinkedList<JobTaskInfo>();
	}

	@Override
	public boolean addAll(Collection<? extends JobTaskInfo> jobTasks) {

		for (JobTaskInfo jobTask : jobTasks)
			add(jobTask);
		return true;

	}

	@Override
	public void clear() {
		queuedJobs.clear();
		totalEstimationTimeOfQueuedJobs = 0;
	}

	@Override
	public boolean contains(Object jobTask) {
		return queuedJobs.contains(jobTask);
	}

	@Override
	public boolean containsAll(Collection<?> jobTasks) {
		return queuedJobs.containsAll(jobTasks);
	}

	@Override
	public boolean isEmpty() {
		return queuedJobs.isEmpty();
	}

	@Override
	public Iterator<JobTaskInfo> iterator() {
		return queuedJobs.iterator();
	}

	@Override
	public boolean remove(Object currentJob) {
		queuedJobs.remove(currentJob);
		totalEstimationTimeOfQueuedJobs -= ((JobTaskInfo) currentJob).getJob().getAvgExecutionTime();
		move((JobTaskInfo) currentJob);
		return false;
	}

	private void move(JobTaskInfo currentJob) {
		Workers.getHopTaskData(workerId).addWaitingForJob(((JobTaskInfo) currentJob).getJob());
		HopTaskEvent.addMovingWorker(workerId);
	}

	@Override
	public boolean removeAll(Collection<?> col) {
		for (Object obj : col)
			queuedJobs.remove(obj);
		return true;
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		return queuedJobs.retainAll(arg0);
	}

	@Override
	public int size() {
		return queuedJobs.size();
	}

	@Override
	public Object[] toArray() {
		return queuedJobs.toArray();
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		return arg0;
	}

	@Override
	public boolean add(JobTaskInfo addingJob) {

		// boolean decided = false;

		// addingJob.setTime(addingJob.getTime() +
		// getRemainingTimeRunningJobs(addingJob.getTime())
		// + totalEstimationTimeOfQueuedJobs);
		//
		// for (int i = queuedJobs.size() - 1; i >= 0; i--) {
		//
		// JobTaskInfo currentJob = queuedJobs.get(i);
		//
		// if (hasBeenScheduledLaterThan(addingJob, currentJob)) {
		//
		// if (hasLessExecutionTime(addingJob, currentJob) &&
		// notExceedsThresholdIfByPass(currentJob, addingJob)) {
		//
		// crossingOverCurrentJob(addingJob, currentJob);
		//
		// } else {
		// addOrMove(addingJob, i + 1);
		//
		// decided = true;
		//
		// break;
		// }
		//
		// } else {
		// if (hasLessExecutionTime(currentJob, addingJob) &&
		// notExceedsThreasholdIfStay(addingJob, currentJob)) {
		//
		// addOrMove(addingJob, i + 1);
		//
		// decided = true;
		//
		// break;
		//
		// } else {
		// crossingOverCurrentJob(addingJob, currentJob);
		// }
		// }
		// }
		//
		// if (!decided) {
		addOrMove(addingJob, 0);
		// decided = true;
		// }

		this.filter();
		return true;

	}

	public boolean hasLessExecutionTime(JobTaskInfo addingProbe, JobTaskInfo otherProbe) {
		return addingProbe.getJob().getAvgExecutionTime() < otherProbe.getJob().getAvgExecutionTime();
	}

	private boolean hasBeenScheduledLaterThan(JobTaskInfo probe, JobTaskInfo otherProbe) {
		return probe.getJob().getStartTime() > otherProbe.getJob().getStartTime();
	}

	private boolean notExceedsThreasholdIfStay(JobTaskInfo addingProbe, JobTaskInfo currentProbe) {
		return addingProbe.getTime() < addingProbe.getJob().getMaximumWaitingTime();
	}

	private boolean notExceedsThresholdIfByPass(JobTaskInfo currentProbe, JobTaskInfo addingProbe) {
		return currentProbe.getTime() + addingProbe.getJob().getAvgExecutionTime() < currentProbe.getJob()
				.getMaximumWaitingTime();
	}

	// @Override
	// public boolean add(JobTaskInfo addingJob) {
	//
	// boolean decided = false;
	//
	// addingJob.setTime(addingJob.getTime() +
	// getRemainingTimeRunningJobs(addingJob.getTime())
	// + totalEstimationTimeOfQueuedJobs);
	//
	// for (int i = queuedJobs.size() - 1; i >= 0; i--) {
	// JobTaskInfo currentJob = queuedJobs.get(i);
	//
	// if (addingJob.hasBeenScheduledLaterThanAndCannotSwap(currentJob,
	// comparator)) {
	// addOrMove(addingJob, i + 1);
	// decided = true;
	// break;
	// } else
	// crossingOverCurrentJob(addingJob, currentJob);
	// }
	//
	// if (!decided) {
	// addOrMove(addingJob, 0);
	// decided = true;
	// }
	//
	// this.filter();
	// return true;
	//
	// }

	private void addOrMove(JobTaskInfo addingJob, int i) {
		// if (addingJob.waitInQueueLessThanItsMaximumThreshold() ||
		// addingJob.isMaximumThresholdPassed())
		add2Queue(i, addingJob);
		// else
		// move(addingJob);
	}

	// private void addOrMove(JobTaskInfo addingProbe, long currentTime, int i)
	// {
	//
	// if (waitInQueueLessThanItsMaximumThreshold(addingProbe, currentTime)
	// || isMaximumThresholdPassed(addingProbe, currentTime) || i == 0)
	// add2Queue(i, addingProbe);
	// else {
	// move(addingProbe);
	// }
	// }

	// private boolean waitInQueueLessThanItsMaximumThreshold(JobTaskInfo
	// addingProbe, long currentTime) {
	//
	// return currentTime + addingProbe.getTime() <
	// addingProbe.getJob().getStartTime()
	// + addingProbe.getJob().getMaximumWaitingTime();
	//
	// }

	// public boolean isMaximumThresholdPassed(JobTaskInfo probe, long
	// currentTime) {
	// return probe.getJob().getStartTime() +
	// probe.getJob().getMaximumWaitingTime() < currentTime;
	// }

	private void crossingOverCurrentJob(JobTaskInfo passingJob, JobTaskInfo passedJob) {

		passingJob.decreaseTimeBy(passedJob.getJob().getAvgExecutionTime());
		passedJob.increaseTimeBy(passingJob.getJob().getAvgExecutionTime());

	}

	private void add2Queue(int index, JobTaskInfo addingJob) {
		queuedJobs.add(index, addingJob);
		totalEstimationTimeOfQueuedJobs += addingJob.getJob().getAvgExecutionTime();
	}

	@Override
	public JobTaskInfo element() {
		return queuedJobs.element();
	}

	@Override
	public boolean offer(JobTaskInfo jobTaskInfo) {
		return add(jobTaskInfo);
	}

	@Override
	public JobTaskInfo peek() {
		return queuedJobs.peek();
	}

	@Override
	public JobTaskInfo poll() {
		return this.remove();
	}

	@Override
	public JobTaskInfo remove() {

		JobTaskInfo jobTaskInfo = queuedJobs.remove();
		if (jobTaskInfo != null)
			totalEstimationTimeOfQueuedJobs -= jobTaskInfo.getJob().getAvgExecutionTime();
		return jobTaskInfo;

	}

	@Override
	public void filter() {

		GlobalData data = Workers.getGlobalData(workerId);

		while (queuedJobs.size() > data.getQueueSize() || totalEstimationTimeOfQueuedJobs > data.getWorkerLoad())
			this.remove(queuedJobs.removeLast());
	}

	public Comparator<JobTaskInfo> getComparator() {
		return comparator;
	}

	public void setComparator(Comparator<JobTaskInfo> comparator) {
		this.comparator = comparator;
	}

	public void removeRunningJob(JobTaskInfo runningJob) {
		runningJobs.remove(runningJob);
	}

	public void addRunningJob(JobTaskInfo runningJob) {
		runningJobs.add(runningJob);
	}

	private double getRemainingTimeRunningJobs(double currentTime) {

		double remainingTime = 0;

		for (JobTaskInfo jobTaskInfo : runningJobs)
			remainingTime += jobTaskInfo.getRemainingEstimationTime(currentTime);

		return remainingTime;

	}

	public int getWorkerId() {
		return workerId;
	}

	public void setWorkerId(int workerId) {
		this.workerId = workerId;
	}

	// Time when the job has entered to the data center
	public static class ScheduleTimeComparator implements Comparator<JobTaskInfo>, Serializable {

		private static final long serialVersionUID = 1L;
		private static final ScheduleTimeComparator comparator = new ScheduleTimeComparator();

		private ScheduleTimeComparator() {
		}

		public static ScheduleTimeComparator getInstance() {
			return comparator;
		}

		@Override
		public int compare(JobTaskInfo job1, JobTaskInfo job2) {

			double diff = job1.getJob().getStartTime() - job2.getJob().getStartTime();

			if (diff < 0 || diff > 0)
				return (int) diff;
			else
				return -1;

		}
	}

}
