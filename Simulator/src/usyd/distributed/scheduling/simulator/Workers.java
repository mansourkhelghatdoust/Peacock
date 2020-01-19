package usyd.distributed.scheduling.simulator;

import usyd.distributed.scheduling.simulator.events.Peacock.GlobalData;
import usyd.distributed.scheduling.simulator.events.Peacock.HopTaskData;
import usyd.distributed.scheduling.simulator.events.Peacock.PeacockWorker;

public class Workers {

	private static Worker[] workers = null;

	public static Worker[] getWorkers() {
		return workers;
	}

	public static void setWorkers(Worker[] workers) {
		Workers.workers = workers;
	}

	public static synchronized HopTaskData getHopTaskData(int workerId) {
		return ((PeacockWorker) Workers.getWorkers()[workerId]).getHopTaskData();
	}

	public static GlobalData getGlobalData(int workerId) {
		return ((PeacockWorker) Workers.getWorkers()[workerId]).getGlobalData();
	}

	@SuppressWarnings("unchecked")
	public static <T extends Worker> T getWorker(Class<T> type, int workerId) {
		return (T) Workers.getWorkers()[workerId];
	}

	public static int calculateFreeSlots() {

		int sumFreeSlots = 0;
		for (int i = 0; i < workers.length; i++) {
			sumFreeSlots += workers[i].freeSlots;
		}
		return sumFreeSlots;

	}

	public static void reset() {
		workers = null;
	}

}
