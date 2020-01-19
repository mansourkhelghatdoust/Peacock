package usyd.peacock.nodemonitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

import usyd.distributed.scheduler.peacock.thrift.TFullProbe;
import usyd.distributed.scheduler.peacock.thrift.TLocalFullProbe;
import usyd.peacock.util.ObjectCloner;

public class PeacockQueue implements Queue<TLocalFullProbe> {

	private long totalEstimationTimeOfQueuedJobs;

	private TFullProbe runningTask = null;

	private boolean waitingForRunningTask = false;

	private long executionStartTime = 0;

	private LinkedList<TLocalFullProbe> queuedJobs = new LinkedList<TLocalFullProbe>();

	private ArrayList<TFullProbe> rotatingProbes = new ArrayList<TFullProbe>();

	private ReentrantLock lock = new ReentrantLock(true);

	private SharedState sharedState = null;

	public PeacockQueue() {

	}

	public void setSharedState(SharedState sharedState) {
		this.sharedState = sharedState;
	}

	@Override
	public boolean addAll(Collection<? extends TLocalFullProbe> probes) {

		lock.lock();

		for (TLocalFullProbe probe : probes)
			add(probe);

		lock.unlock();

		return true;
	}

	@Override
	public void clear() {

		lock.lock();

		queuedJobs.clear();

		totalEstimationTimeOfQueuedJobs = 0;

		lock.unlock();
	}

	@SuppressWarnings("unchecked")
	public ArrayList<TFullProbe> getRotatingProbes() {

		lock.lock();

		ArrayList<TFullProbe> probes = (ArrayList<TFullProbe>) ObjectCloner.deepCopy(rotatingProbes);

		rotatingProbes.clear();

		lock.unlock();

		return probes;
	}

	@Override
	public boolean contains(Object probe) {
		return queuedJobs.contains(probe);
	}

	@Override
	public boolean containsAll(Collection<?> probes) {
		return queuedJobs.containsAll(probes);
	}

	@Override
	public boolean isEmpty() {
		return queuedJobs.isEmpty();
	}

	@Override
	public Iterator<TLocalFullProbe> iterator() {
		return queuedJobs.iterator();
	}

	@Override
	public boolean remove(Object probe) {

		lock.lock();

		queuedJobs.remove(probe);

		totalEstimationTimeOfQueuedJobs -= ((TLocalFullProbe) probe).fullProbe.estimatedDuration;

		lock.unlock();

		return true;
	}

	public TLocalFullProbe removeLast() {

		lock.lock();

		TLocalFullProbe lastProbe = queuedJobs.removeLast();

		totalEstimationTimeOfQueuedJobs -= lastProbe.fullProbe.estimatedDuration;

		lock.unlock();

		return lastProbe;
	}

	@Override
	public boolean removeAll(Collection<?> col) {

		lock.lock();

		for (Object obj : col)
			queuedJobs.remove(obj);

		lock.unlock();

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

	public SharedState getSharedState() {
		return sharedState;
	}

	@Override
	public boolean add(TLocalFullProbe addingProbe) {

		lock.lock();

		boolean decided = false;

		addingProbe.setLocalWaitingTime(getRemainingTimeRunningTask() + totalEstimationTimeOfQueuedJobs);

		for (int i = queuedJobs.size() - 1; i >= 0; i--) {

			TLocalFullProbe currentProbe = queuedJobs.get(i);

			if (hasBeenScheduledLaterThan(addingProbe, currentProbe)) {

				if (hasLessExecutionTime(addingProbe, currentProbe)
						&& notExceedsThresholdIfByPass(currentProbe, addingProbe)) {

					byPassOverCurrentJob(addingProbe, currentProbe);

				} else {
					addOrMove(addingProbe, i + 1);

					decided = true;

					break;
				}

			} else {

				if (hasLessExecutionTime(currentProbe, addingProbe)
						&& notExceedsThresholdIfStay(addingProbe, currentProbe)) {

					addOrMove(addingProbe, i + 1);

					decided = true;

					break;

				} else {
					byPassOverCurrentJob(addingProbe, currentProbe);
				}

			}
		}

		if (!decided) {

			addOrMove(addingProbe, 0);

			decided = true;
		}

		this.filter();

		lock.unlock();

		return true;

	}

	private boolean notExceedsThresholdIfStay(TLocalFullProbe addingProbe, TLocalFullProbe currentProbe) {

		if (addingProbe.fullProbe.immediateExecution)
			return false;

		if (currentProbe.fullProbe.immediateExecution)
			return true;

		return System.currentTimeMillis() + addingProbe.localWaitingTime < addingProbe.fullProbe.enterTime
				+ addingProbe.fullProbe.maximumWaitingTime;
	}

	private boolean notExceedsThresholdIfByPass(TLocalFullProbe currentProbe, TLocalFullProbe addingProbe) {

		if (currentProbe.fullProbe.immediateExecution)
			return false;

		return System.currentTimeMillis() + currentProbe.localWaitingTime
				+ addingProbe.fullProbe.estimatedDuration < currentProbe.fullProbe.enterTime
						+ currentProbe.fullProbe.maximumWaitingTime;

	}

	private void addOrMove(TLocalFullProbe addingProbe, int i) {

		if (waitInQueueLessThanItsMaximumThreshold(addingProbe) || isMaximumThresholdPassed(addingProbe) || i == 0)
			add2Queue(i, addingProbe);
		else {
			move(addingProbe);
		}
	}

	private void move(TLocalFullProbe probe) {

		rotatingProbes.add(probe.fullProbe);

	}

	private boolean waitInQueueLessThanItsMaximumThreshold(TLocalFullProbe addingProbe) {

		return System.currentTimeMillis() + addingProbe.localWaitingTime < addingProbe.fullProbe.enterTime
				+ addingProbe.fullProbe.maximumWaitingTime;

	}

	private boolean hasBeenScheduledLaterThan(TLocalFullProbe probe, TLocalFullProbe otherProbe) {

		if (probe.fullProbe.immediateExecution && !otherProbe.fullProbe.immediateExecution)
			return false;

		return probe.fullProbe.enterTime > otherProbe.fullProbe.enterTime;
	}

	public boolean waitInQueueLessThanItsMaximumThresholdAfterCrossing(TLocalFullProbe otherProbe,
			TLocalFullProbe addingProbe) {
		return otherProbe.localEnterTime + otherProbe.localWaitingTime
				+ addingProbe.fullProbe.estimatedDuration < otherProbe.fullProbe.enterTime
						+ otherProbe.fullProbe.maximumWaitingTime;
	}

	public boolean isMaximumThresholdPassed(TLocalFullProbe probe) {
		return probe.fullProbe.enterTime + probe.fullProbe.maximumWaitingTime < System.currentTimeMillis();
	}

	public boolean hasLessExecutionTime(TLocalFullProbe addingProbe, TLocalFullProbe otherProbe) {

		if (addingProbe.fullProbe.immediateExecution && !otherProbe.fullProbe.immediateExecution)
			return true;

		return addingProbe.fullProbe.estimatedDuration < otherProbe.fullProbe.estimatedDuration;
	}

	private void byPassOverCurrentJob(TLocalFullProbe passingProbe, TLocalFullProbe passedProbe) {

		passingProbe.setLocalWaitingTime(passingProbe.getLocalWaitingTime() - passedProbe.fullProbe.estimatedDuration);

		passedProbe.setLocalWaitingTime(passedProbe.getLocalWaitingTime() + passingProbe.fullProbe.estimatedDuration);

	}

	private void add2Queue(int index, TLocalFullProbe addingProbe) {

		queuedJobs.add(index, addingProbe);

		totalEstimationTimeOfQueuedJobs += addingProbe.fullProbe.estimatedDuration;

	}

	@Override
	public TLocalFullProbe element() {

		lock.lock();

		TLocalFullProbe probe = queuedJobs.element();

		lock.unlock();

		return probe;
	}

	@Override
	public boolean offer(TLocalFullProbe probe) {

		lock.lock();

		boolean added = add(probe);

		lock.unlock();

		return added;

	}

	@Override
	public TLocalFullProbe peek() {

		lock.lock();

		TLocalFullProbe probe = queuedJobs.peek();

		lock.unlock();

		return probe;
	}

	@Override
	public TLocalFullProbe poll() {

		lock.lock();

		TLocalFullProbe probe = this.remove();

		lock.unlock();

		return probe;
	}

	@Override
	public TLocalFullProbe remove() {

		lock.lock();

		TLocalFullProbe probe = null;

		if (queuedJobs != null && !queuedJobs.isEmpty()) {

			probe = queuedJobs.remove();

			if (probe != null)
				totalEstimationTimeOfQueuedJobs -= probe.fullProbe.estimatedDuration;

		}
		lock.unlock();

		return probe;

	}

	public void filter() {

		lock.lock();

		int maxQueueSize = sharedState.getQueueSize();
		
		if (runningTask == null && !isWaitingForRunningTask())
			maxQueueSize++;

		while (queuedJobs.size() > 0
				&& (queuedJobs.size() > maxQueueSize || totalWaitingTime() > sharedState.getAvgWorkload())) {

			TLocalFullProbe probe = this.removeLast();
			totalEstimationTimeOfQueuedJobs -= probe.fullProbe.estimatedDuration;
			move(probe);

		}

		lock.unlock();

	}

	public void removeRunningJob() {
		this.runningTask = null;
	}

	public void setRunningJob(TFullProbe runningTask, long time) {

		this.runningTask = runningTask;
		this.executionStartTime = time;

	}

	private long getRemainingTimeRunningTask() {

		if (runningTask == null)
			return 0;
		long retVal = this.executionStartTime + runningTask.estimatedDuration - System.currentTimeMillis();
		return retVal > 0 ? retVal : 0;

	}

	private long totalWaitingTime() {
		return getRemainingTimeRunningTask() + totalEstimationTimeOfQueuedJobs;
	}

	public boolean isWaitingForRunningTask() {
		return waitingForRunningTask;
	}

	public void setWaitingForRunningTask(boolean waitingForRunningTask) {
		this.waitingForRunningTask = waitingForRunningTask;
	}
}