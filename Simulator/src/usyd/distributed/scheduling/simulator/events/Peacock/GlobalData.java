package usyd.distributed.scheduling.simulator.events.Peacock;

import java.util.concurrent.ConcurrentHashMap;

public class GlobalData {

	private long lastTimeSet = 0;
	private long queueSize = 0;
	private long workerLoad = 0;
	private long avgExecutionTime = 0;

	private int workerId;

	public GlobalData(int workerId) {
		this.workerId = workerId;
	}

	public void updateGlobalInfo(ConcurrentHashMap<String, Long> globalInfo) {

		setQueueSize(globalInfo.get(PeacockScheduler.QueueSize));
		setWorkerLoad(globalInfo.get(PeacockScheduler.WorkerLoad));
		setLastTimeSet(globalInfo.get(PeacockScheduler.TimeSet));
		HopTaskEvent.addMovingWorker(workerId);

	}

	public long getQueueSize() {
		return queueSize;
	}

	public void setQueueSize(long queueSize) {
		this.queueSize = queueSize;
	}

	public long getWorkerLoad() {
		return workerLoad;
	}

	public void setWorkerLoad(long workerLoad) {
		this.workerLoad = workerLoad;
	}

	public long getLastTimeSet() {
		return lastTimeSet;
	}

	public void setLastTimeSet(long lastTimeSet) {
		this.lastTimeSet = lastTimeSet;
	}

	public long getAvgExecutionTime() {
		return avgExecutionTime;
	}

	public void setAvgExecutionTime(long avgExecutionTime) {
		this.avgExecutionTime = avgExecutionTime;
	}

}
