package usyd.distributed.scheduling.simulator.events.Peacock;

import java.util.concurrent.ConcurrentSkipListSet;

import usyd.distributed.scheduling.simulator.Job;
import usyd.distributed.scheduling.simulator.Workers;
import usyd.distributed.scheduling.simulator.Job.StartTimeComparator;

public class HopTaskData {

	private long onTheFlyLastTimeSet = 0;
	private long onTheFlyQueueSize = 0;
	private long onTheFlyWorkerLoad = 0;
	private long onTheFlyAverageExecutionTime = 0;
	private ConcurrentSkipListSet<Job> waitingForMoveJobs = null;
	private ConcurrentSkipListSet<Job> onTheWayJobs = null;

	private int inNeighbor;
	private int workerId;

	public HopTaskData(int inNeighbor, int workerId) {

		this.inNeighbor = inNeighbor;
		this.workerId = workerId;
		waitingForMoveJobs = new ConcurrentSkipListSet<Job>(StartTimeComparator.getInstance());
		onTheWayJobs = new ConcurrentSkipListSet<Job>(StartTimeComparator.getInstance());

	}

	public ConcurrentSkipListSet<Job> getWaitForMovingJob() {
		return waitingForMoveJobs;
	}

	public ConcurrentSkipListSet<Job> getOnTheWayJob() {
		return onTheWayJobs;
	}

	public void addWaitingForJob(Job job) {
		waitingForMoveJobs.add(job);
	}

	public void updateOnTheFlyGlobalData() {

		PeacockWorker neighbor = ((PeacockWorker) Workers.getWorkers()[inNeighbor]);
		onTheFlyQueueSize = neighbor.getGlobalData().getQueueSize();
		onTheFlyLastTimeSet = neighbor.getGlobalData().getLastTimeSet();
		onTheFlyWorkerLoad = neighbor.getGlobalData().getWorkerLoad();
		onTheFlyAverageExecutionTime = neighbor.getGlobalData().getAvgExecutionTime();

	}

	public void receiveQueueSize() {

		PeacockWorker worker = ((PeacockWorker) Workers.getWorkers()[workerId]);
		GlobalData data = worker.getGlobalData();

		if (onTheFlyLastTimeSet > data.getLastTimeSet()) {
			data.setQueueSize(onTheFlyQueueSize);
			data.setLastTimeSet(onTheFlyLastTimeSet);
			data.setWorkerLoad(onTheFlyWorkerLoad);
			data.setAvgExecutionTime(onTheFlyAverageExecutionTime);
			HopTaskEvent.addMovingWorker(workerId);
		}
	}

	public long getOnTheFlyAverageExecutionTime() {
		return onTheFlyAverageExecutionTime;
	}

	public void setOnTheFlyAverageExecutionTime(long onTheFlyAverageExecutionTime) {
		this.onTheFlyAverageExecutionTime = onTheFlyAverageExecutionTime;
	}

}
