package usyd.distributed.scheduling.simulator.events.Peacock;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import usyd.distributed.scheduling.simulator.Configuration;
import usyd.distributed.scheduling.simulator.Job;
import usyd.distributed.scheduling.simulator.Simulation;
import usyd.distributed.scheduling.simulator.Worker;
import usyd.distributed.scheduling.simulator.Workers;
import usyd.distributed.scheduling.simulator.events.TaskEndEvent;
import usyd.distributed.scheduling.simulator.events.Peacock.PeacockQueue.ScheduleTimeComparator;

public class PeacockWorker extends Worker {

	private static final long serialVersionUID = 1L;

	private int neighbor = -1;

	private HopTaskData hopTaskData = null;

	private GlobalData globalData = null;

	private PeacockQueue workerJobs;

	private ReentrantLock perWorkerLock = new ReentrantLock(true);

	public PeacockWorker(int id, int numOfSlots) {

		super(id, numOfSlots);
		setNeighbor();
		this.hopTaskData = new HopTaskData(getInNeighbor(), id);
		this.globalData = new GlobalData(id);
		workerJobs = new PeacockQueue(id, ScheduleTimeComparator.getInstance());

	}

	@Override
	public void addProbe(Job job, double time, ConcurrentHashMap<String, Long> globalInfo) {

		try {

			perWorkerLock.lock();

			globalData.updateGlobalInfo(globalInfo);

			if (existFreeSlotToExecuteImmediately())
				executeTask(new JobTaskInfo(job, time));
			else
				wait(job, time);

		} finally {
			perWorkerLock.unlock();
		}

	}

	public void checkForMoving() {
		workerJobs.filter();
	}

	@Override
	public void freeSlot(double time, JobTaskInfo job) {

		try {

			perWorkerLock.lock();

			workerJobs.removeRunningJob(job);

			super.freeSlot(time, job);

		} finally {
			perWorkerLock.unlock();
		}
	}

	@Override
	protected void mayBeGetTask(double time) {

		while (existFreeSlotToExecuteImmediately() && existWaitingTask()) {
			executeTask(pickupWaitingTask(time));
			globalData.updateGlobalInfo(PeacockScheduler.getGlobalInfo());
			checkForMoving();
		}
	}

	private JobTaskInfo pickupWaitingTask(double time) {
		JobTaskInfo jobTask = workerJobs.remove();
		jobTask.setTime(time);
		return jobTask;
	}

	private boolean existWaitingTask() {
		return !workerJobs.isEmpty();
	}

	public ConcurrentHashMap<String, Long> getLastUpdatedGlobalInfo() {

		ConcurrentHashMap<String, Long> globalInfo = new ConcurrentHashMap<String, Long>();

		globalInfo.put(PeacockScheduler.QueueSize, globalData.getQueueSize());
		globalInfo.put(PeacockScheduler.WorkerLoad, globalData.getWorkerLoad());
		globalInfo.put(PeacockScheduler.TimeSet, globalData.getLastTimeSet());

		return globalInfo;

	}

	public int getInNeighbor() {
		return this.id > 0 ? this.id - 1 : Configuration.getNumOfSlotsPerWorker() - 1;
	}

	public int getNeighbor() {
		return neighbor;
	}

	private void executeTask(JobTaskInfo jobTaskInfo) {

		this.freeSlots--;
		double taskEndTime = jobTaskInfo.getTime() + (2 * Configuration.getConfig().getNetworkDelay())
				+ jobTaskInfo.getJob().getTaskDuration();
		workerJobs.addRunningJob(jobTaskInfo);
		Simulation.addEvent(new TaskEndEvent((int) taskEndTime, this, jobTaskInfo));

	}

	private void wait(Job job, double time) {
		workerJobs.add(new JobTaskInfo(job, time));
	}

	private void setNeighbor() {
		neighbor = (id + 1) % Configuration.getTotalWorkers();
	}

	private boolean existFreeSlotToExecuteImmediately() {
		return this.freeSlots > 0;
	}

	public static void setWorkers() {

		Worker.setShuffledWorkers(new Integer[Configuration.getTotalWorkers()]);
		Worker[] workers = new PeacockWorker[Configuration.getTotalWorkers()];

		for (int idx = 0; idx < workers.length; idx++) {
			workers[idx] = new PeacockWorker(idx, Configuration.getNumOfSlotsPerWorker());
			Worker.getShuffledWorkers()[idx] = idx;
		}

		Workers.setWorkers(workers);

	}

	public HopTaskData getHopTaskData() {
		return hopTaskData;
	}

	public GlobalData getGlobalData() {
		return globalData;
	}

	public PeacockQueue getQueuedJobs() {
		return workerJobs;
	}
}