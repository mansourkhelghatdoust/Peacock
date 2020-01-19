package usyd.distributed.scheduling.util;

import usyd.distributed.scheduling.simulator.Workers;
import usyd.distributed.scheduling.simulator.events.Peacock.PeacockWorker;
import usyd.distributed.scheduling.simulator.events.hawk.HawkWorker;
import usyd.distributed.scheduling.simulator.events.sparrow.SparrowWorker;

public class DataHelper {

	public static String getInputPath() {

		if (isWindows())
			return getCurrentWorkingDirectory() + "\\Data\\";
		else if (isLinux())
			return getCurrentWorkingDirectory() + "/Data/";

		System.out.println("Error in finding Data Path, Terminating....");
		System.exit(-1);
		return null;
	}

	public static String getOutputPath() {

		if (isWindows())
			return getCurrentWorkingDirectory() + "\\Output\\";
		else if (isLinux())
			return getCurrentWorkingDirectory() + "/Output/";

		System.out.println("Error in finding Output Path, Terminating....");
		System.exit(-1);
		return null;
	}

	public static String getDefaultOutputPath() {

		if (isWindows())
			return getCurrentWorkingDirectory() + "\\Output\\";
		else if (isLinux())
			return getCurrentWorkingDirectory() + "/Output/";

		System.out.println("Error in finding Output Path, Terminating....");
		System.exit(-1);
		return null;
	}

	private static String getAlgorithmName() {

		if (Workers.getWorkers()[0] instanceof SparrowWorker)
			return "Sparrow";
		else if (Workers.getWorkers()[0] instanceof PeacockWorker)
			return "Peacock";
		else if (Workers.getWorkers()[0] instanceof HawkWorker)
			return "Hawk";
		else
			return "Eagle";

	}

	private static String getCurrentWorkingDirectory() {
		return System.getProperty("user.dir");
	}

	private static String getCurrentOS() {
		return System.getProperty("os.name");
	}

	private static boolean isWindows() {
		return getCurrentOS().indexOf("Win") >= 0;
	}

	private static boolean isLinux() {
		String currentOS = getCurrentOS();
		return currentOS.indexOf("nix") >= 0 || currentOS.indexOf("nux") >= 0 || currentOS.indexOf("aix") > 0;
	}

	public static double round(double value, int places) {
		
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

}
