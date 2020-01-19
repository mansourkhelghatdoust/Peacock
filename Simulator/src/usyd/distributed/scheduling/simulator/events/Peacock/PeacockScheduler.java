package usyd.distributed.scheduling.simulator.events.Peacock;

import java.util.concurrent.ConcurrentHashMap;

import usyd.distributed.scheduling.simulator.Configuration;
import usyd.distributed.scheduling.simulator.Jobs;
import usyd.distributed.scheduling.simulator.SimulationUtil;

public class PeacockScheduler {

	public static final String QueueSize = "queueSize";
	public static final String WorkerLoad = "workerLoad";
	public static final String TimeSet = "timeSet";

	public static ConcurrentHashMap<String, Long> getGlobalInfo() {

		ConcurrentHashMap<String, Long> queueInfo = new ConcurrentHashMap<String, Long>();

		queueInfo.put(PeacockScheduler.QueueSize, currentQueueSize());
		queueInfo.put(PeacockScheduler.WorkerLoad, currentLoadPerWorker());
		queueInfo.put(PeacockScheduler.TimeSet, (long) SimulationUtil.getCurrentTime());

		return queueInfo;
	}

	private static Long currentQueueSize() {

		int capacity = (Configuration.getTotalWorkers() * Configuration.getNumOfSlotsPerWorker());
		int minWaitingTasks = Jobs.numberOfActiveTasks.intValue() - capacity;

		if (minWaitingTasks < 0)
			return 0L;

		return (long) minWaitingTasks / Configuration.getTotalWorkers();
	}

	private static long currentLoadPerWorker() {

		long load = (long) Jobs.estimationExecutionTimeOfActiveTasks.doubleValue() / Configuration.getTotalWorkers();
		return load < 0 ? 0 : load;

	}

}
