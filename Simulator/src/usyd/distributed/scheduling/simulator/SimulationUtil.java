package usyd.distributed.scheduling.simulator;

import java.util.Random;

public class SimulationUtil {

	private static Random randomJobArrival = new Random();
	private static Random randomTaskPerJob = new Random();
	private static Random randomTaskDuration = new Random();
	private static double currentTime = 0;

	public static int poisson(final double mean) {

		double L = Math.exp(-mean);
		double p = 1.0;
		int k = 0;

		do {

			k++;
			p *= randomJobArrival.nextDouble();

		} while (p > L);

		return k - 1;
	}

	public static long getNumberOfTasksPerJob(final int mean) {

		return randomTaskPerJob.nextInt(mean * 2) + 1;

	}

	public static long getNextTaskDuration(final long mean) {

		return randomTaskDuration.nextInt((int) mean * 2) + 1;

	}

	public static void setCurrentTime(double time) {
		currentTime = time;
	}

	public static double getCurrentTime() {
		return currentTime;
	}

}