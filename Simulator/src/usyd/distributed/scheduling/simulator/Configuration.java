package usyd.distributed.scheduling.simulator;

import usyd.distributed.scheduling.simulator.concurrent.Peacock.ConcurrentExecutor;
import usyd.distributed.scheduling.simulator.events.JobArrivalEvent;
import usyd.distributed.scheduling.util.DataHelper;

public class Configuration {

	private static final Configuration config = new Configuration();

	public static int googleNumberOfJobs = 200822; // 504882;

	public static int googleMedianTaskPerJob = 36;// 35;
	// microsecond to second

	public static long googleMedianTaskDuration = 71;// 634;

	public static int fifoParallelismDegree = 25;

	public static int parallelismDegree = 25;

	private int totalWorkers = 0;

	private int numOfSlotsPerWorker = 0;

	private double networkDelay = 0.005;

	private double rotationInterval = 1;

	private double load = 0;

	private double averageUsedSlots = 0;

	private double interArrivalDelay = 0;

	private Class<? extends JobArrivalEvent> algorithmType = null;

	public static Configuration setUP(int totalWorkers, int slotPerWorker, double networkDelay, double load,
			Class<? extends JobArrivalEvent> algorithmType) {

		config.setTotalWorkers(totalWorkers);
		config.setNumOfSlotsPerWroker(slotPerWorker);
		config.setNetworkDelay(networkDelay);
		config.setLoad(load);
		config.averageUsedSlots = Configuration.getLoad() * Configuration.getNumOfSlotsPerWorker()
				* Configuration.getTotalWorkers();
		config.setInterArrivalDelay(
				DataHelper.round((1 * Configuration.googleMedianTaskDuration * Configuration.googleMedianTaskPerJob)
						/ config.averageUsedSlots, 2));

		config.setAlgorithmType(algorithmType);
		Jobs.setRemainingJobs(Configuration.googleNumberOfJobs);

		Jobs.getInstance().reset();
		Workers.reset();
		ConcurrentExecutor.reset();

		return config;
	}

	public static int getTotalWorkers() {
		return getConfig().totalWorkers;
	}

	public void setTotalWorkers(int total_workers) {
		this.totalWorkers = total_workers;
	}

	public static int getNumOfSlotsPerWorker() {
		return getConfig().numOfSlotsPerWorker;
	}

	public void setNumOfSlotsPerWroker(int num_of_slots_per_wroker) {
		this.numOfSlotsPerWorker = num_of_slots_per_wroker;
	}

	public double getNetworkDelay() {
		return networkDelay;
	}

	public void setNetworkDelay(double network_delay) {
		this.networkDelay = network_delay;
	}

	public static double getLoad() {
		return getConfig().load;
	}

	public void setLoad(double load) {
		this.load = load;
	}

	public static Configuration getConfig() {
		return config;
	}

	public static double getInterArrivalDelay() {
		return getConfig().interArrivalDelay;
	}

	public void setInterArrivalDelay(double interArrivalDelay) {
		this.interArrivalDelay = interArrivalDelay;
	}

	public static Class<? extends JobArrivalEvent> getAlgorithmType() {
		return getConfig().algorithmType;
	}

	public void setAlgorithmType(Class<? extends JobArrivalEvent> algorithmType) {
		this.algorithmType = algorithmType;
	}

	public double getRotationInterval() {
		return rotationInterval;
	}

	public void setRotationInterval(double rotationInterval) {
		this.rotationInterval = rotationInterval;
	}
}