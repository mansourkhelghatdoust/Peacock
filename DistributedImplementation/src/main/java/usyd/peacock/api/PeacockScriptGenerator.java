package usyd.peacock.api;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class PeacockScriptGenerator {

	public static void main(String[] args) throws FileNotFoundException {

		PeacockScriptGenerator script = new PeacockScriptGenerator();

		File file = new File(System.getProperty("user.dir") + "//servers.txt");

		ArrayList<String> nodeMonitor = new ArrayList<String>();

		ArrayList<String> backends = new ArrayList<String>();

		ArrayList<String> schedulers = new ArrayList<String>();

		ArrayList<String> plugin = new ArrayList<String>();

		ArrayList<String> serverUpdate = new ArrayList<String>();

		ArrayList<String> stopAll = new ArrayList<String>();

		Scanner scanner = new Scanner(file);

		while (scanner.hasNextLine()) {

			String line = scanner.nextLine();

			nodeMonitor.add(script.generateNodeMonitors(line));

			backends.add(script.generateBackends(line));

			schedulers.add(script.generateSchedulers(line));

			plugin.add(script.generatePluginUpdate(line));

			serverUpdate.add(script.generateServerUpdate(line));

			stopAll.add(script.generateStopAll(line));

		}

		scanner.close();

		script.save(nodeMonitor, "launchNodeMonitor.sh", 4);

		script.save(backends, "launchBackends.sh", 3);

		script.save(schedulers, "launchSchedulers.sh", 1);

		script.save(plugin, "pluginServerUpdates.sh", -1);

		script.save(serverUpdate, "sparkServerUpdate.sh", -1);

		script.save(stopAll, "stopAll.sh", -1);

	}

	public String generateNodeMonitors(String line) {

		String[] items = line.split(" ");

		StringBuffer buffer = new StringBuffer();

		if (items.length == 4) {

			buffer.append("ssh -f -i /home/mkhe8942/peacockkey.pem ubuntu@" + items[0]
					+ " /home/ubuntu/spark/bin/spark-run usyd.peacock.nodemonitor.NodeMonitorDaemon --neighbors "
					+ items[1] + " --schedulers " + items[2] + " --hostIPAddr " + items[0]
					+ " --getTaskPort 4002 --listeningPort 4003 --rotationInterval 100 --rotationListeningPort 4004");
			buffer.append("\n echo @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + items[0]);

			return buffer.toString();

		}

		return null;
	}

	public String generateBackends(String line) {

		String[] items = line.split(" ");

		StringBuffer buffer = new StringBuffer();

		if (items.length == 4) {
			buffer.append("ssh -f -i /home/mkhe8942/peacockkey.pem ubuntu@" + items[0]
					+ " /home/ubuntu/spark/bin/spark-run org.apache.spark.scheduler.peacock.PeacockExecutorBackendLauncher --driver-url spark://PeacockSchedulerBackend@"
					+ items[3] + ":60501" + " --nodeMonitorListeningPort 4003 --backendListeningPort 33333");

			buffer.append("\n echo @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + items[0]);
			return buffer.toString();

		}

		return null;
	}

	public String generateSchedulers(String line) {

		String[] items = line.split(" ");

		StringBuffer buffer = new StringBuffer();

		if (items.length == 2) {
			buffer.append("ssh -f -i /home/mkhe8942/peacockkey.pem -t ubuntu@" + items[0]
					+ " /home/ubuntu/spark/bin/spark-run usyd.peacock.scheduler.SchedulerDaemon --schedulers "
					+ items[1] + " --listeningPort 4001 --getTaskPort 4002" + " --hostIPAddr " + items[0]);

			buffer.append("\n echo @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + items[0]);
			return buffer.toString();

		}
		return null;
	}

	public String generatePluginUpdate(String line) {

		String[] items = line.split(" ");

		StringBuffer buffer = new StringBuffer();

		buffer.append(
				"scp -i  /home/mkhe8942/peacockkey.pem -r /home/mkhe8942/Peacock/Peacock/target/Peacock-0.0.1-PROTOTYPE.jar ubuntu@"
						+ items[0] + ":/home/ubuntu/spark/external");
		buffer.append("\n echo @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + items[0]);

		return buffer.toString();
	}

	public String generateServerUpdate(String line) {

		String[] items = line.split(" ");

		StringBuffer buffer = new StringBuffer();

		buffer.append("scp -i  /home/mkhe8942/peacockkey.pem -r /home/spark/assembly ubuntu@" + items[0]
				+ ":/home/ubuntu/spark/");

		buffer.append("\n scp -i  /home/mkhe8942/peacockkey.pem -r /home/spark/bin ubuntu@" + items[0]
				+ ":/home/ubuntu/spark/");

		// buffer.append("\n scp -i /home/mkhe8942/peacockkey.pem -r
		// /home/spark/external ubuntu@" + items[0]
		// + ":/home/ubuntu/spark/");

		buffer.append("\n echo @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + items[0]);

		return buffer.toString();
	}

	public String generateStopAll(String line) {

		String[] items = line.split(" ");

		StringBuffer buffer = new StringBuffer();

		buffer.append("ssh -i  /home/mkhe8942/peacockkey.pem ubuntu@" + items[0] + " killall -9 java");
		
		buffer.append("\n echo @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + items[0]);

		return buffer.toString();
	}

	public void save(ArrayList<String> list, String fileName, int sleepInterval) {

		FileWriter fw = null;
		BufferedWriter bw = null;

		try {

			fw = new FileWriter(fileName);

			bw = new BufferedWriter(fw);

			for (String line : list) {
				if (line != null) {
					bw.write(line);
					bw.newLine();
					if (sleepInterval > 0) {
						bw.write("sleep " + sleepInterval);
						bw.newLine();
					}
				}
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}

	}

	public void saveAll(ArrayList<String> list, String fileName) {

		FileWriter fw = null;
		BufferedWriter bw = null;

		try {

			fw = new FileWriter(fileName);

			bw = new BufferedWriter(fw);

			for (String line : list) {
				if (line != null) {
					bw.write(line + ",");
				}
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}

	}

}