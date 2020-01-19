package usyd.peacock.api;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class SparrowScriptGenerator {

	public static void main(String[] args) throws FileNotFoundException {

		SparrowScriptGenerator script = new SparrowScriptGenerator();

		File file = new File(System.getProperty("user.dir") + "//servers.txt");

		ArrayList<String> nodeMonitor = new ArrayList<String>();

		ArrayList<String> backends = new ArrayList<String>();

		ArrayList<String> plugin = new ArrayList<String>();

		ArrayList<String> serverUpdate = new ArrayList<String>();

		ArrayList<String> stopAll = new ArrayList<String>();

		ArrayList<String> removeSpark = new ArrayList<String>();

		ArrayList<String> schedulers = new ArrayList<String>();

		ArrayList<String> nodemonitors = new ArrayList<String>();

		Scanner scanner = new Scanner(file);

		while (scanner.hasNextLine()) {

			String line = scanner.nextLine();

			nodeMonitor.add(script.generateNodeMonitors(line));

			backends.add(script.generateBackends(line));

			plugin.add(script.generatePluginUpdate(line));

			serverUpdate.add(script.generateServerUpdate(line));

			stopAll.add(script.generateStopAll(line));

			removeSpark.add(script.generateremoveSpark(line));

			schedulers.add(script.generateSchedulers(line));

			nodemonitors.add(script.generateStaticNodeMonitors(line));

		}

		scanner.close();

		script.save(nodeMonitor, "sparrow-launchNodeMonitor.sh", -1);

		script.save(backends, "sparrow-launchBackends.sh", 3);

		script.save(schedulers, "sparrow-launchSchedulers.sh", 1);

		script.save(plugin, "sparrow-pluginServerUpdates.sh", -1);

		script.save(serverUpdate, "sparrow-sparkServerUpdate.sh", -1);

		script.save(stopAll, "sparrow-stopAll.sh", -1);

		script.save(removeSpark, "sparrow-removeSpark.sh", -1);

		script.saveAll(nodemonitors, "sparrow-staticAll.sh");

	}

	public String generateNodeMonitors(String line) {

		String[] items = line.split(" ");

		StringBuffer buffer = new StringBuffer();

		if (items.length == 4) {

			buffer.append("ssh -f -i /home/mkhe8942/sparkkey.pem ubuntu@" + items[0]
					+ " java -XX:+UseConcMarkSweepGC -verbose:gc -XX:+PrintGCTimeStamps -Xmx2046m -XX:+PrintGCDetails -cp /home/ubuntu/spark/external/sparrow-1.0-SNAPSHOT.jar "
					+ "edu.berkeley.sparrow.daemon.SparrowDaemon -c /home/ubuntu/spark/external/configuration -ip " + items[0]);

			return buffer.toString();

		}

		return null;
	}

	public String generateSchedulers(String line) {

		String[] items = line.split(" ");

		StringBuffer buffer = new StringBuffer();

		if (items.length == 2) {

			buffer.append("ssh -f -i /home/mkhe8942/sparkkey.pem ubuntu@" + items[0]
					+ " java -XX:+UseConcMarkSweepGC -verbose:gc -XX:+PrintGCTimeStamps -Xmx2046m -XX:+PrintGCDetails -cp /home/ubuntu/spark/external/sparrow-1.0-SNAPSHOT.jar "
					+ "edu.berkeley.sparrow.daemon.SparrowDaemon -c /home/ubuntu/spark/external/configuration -ip " + items[0]);

			return buffer.toString();

		}
		return null;
	}

	public String generateBackends(String line) {

		String[] items = line.split(" ");

		StringBuffer buffer = new StringBuffer();

		if (items.length == 4) {

			buffer.append("ssh -f -i /home/mkhe8942/sparkkey.pem ubuntu@" + items[0]
					+ " /home/ubuntu/spark/bin/spark-run  -Dspark.scheduler=eagle -Dspark.master.port=7077  -Dspark.hostname="
					+ items[0]
					+ " -Dspark.serializer=org.apache.spark.serializer.KryoSerializer  -Dspark.kryoserializer.buffer=128  -Dspark.driver.host=130.56.251.20"
					+ " -Dspark.driver.port=60501  -Deagle.app.name=spark -Dspark.httpBroadcast.uri=http://130.56.251.20:33644 "
					+ " -Dspark.rpc.message.maxSize=2047  spark.scheduler.sparrow.SparrowExecutorBackend --driver-url spark://SparrowSchedulerBackend@130.56.251.20:60501");

			buffer.append("\n echo @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + items[0]);
			return buffer.toString();

		}

		return null;
	}

	public String generatePluginUpdate(String line) {

		String[] items = line.split(" ");

		StringBuffer buffer = new StringBuffer();

		buffer.append(
				"scp -i  /home/mkhe8942/sparkkey.pem -r /home/spark-sparrow/external/sparrow-1.0-SNAPSHOT.jar ubuntu@"
						+ items[0] + ":/home/ubuntu/spark/external");

		buffer.append("\n scp -i  /home/mkhe8942/sparkkey.pem -r /home/mkhe8942/configuration  ubuntu@" + items[0]
				+ ":/home/ubuntu/spark/external/");

		buffer.append("\n echo @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + items[0]);

		return buffer.toString();
	}

	public String generateServerUpdate(String line) {

		String[] items = line.split(" ");

		StringBuffer buffer = new StringBuffer();

		buffer.append("scp -i  /home/mkhe8942/sparkkey.pem -r /home/spark-sparrow/assembly ubuntu@" + items[0]
				+ ":/home/ubuntu/spark/");

		buffer.append("\n scp -i  /home/mkhe8942/sparkkey.pem -r /home/spark-sparrow/bin ubuntu@" + items[0]
				+ ":/home/ubuntu/spark/");

		buffer.append("\n echo @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + items[0]);

		return buffer.toString();
	}

	public String generateStopAll(String line) {

		String[] items = line.split(" ");

		StringBuffer buffer = new StringBuffer();

		buffer.append("ssh -i  /home/mkhe8942/sparkkey.pem ubuntu@" + items[0] + " killall -9 java");

		buffer.append("\n echo @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + items[0]);

		return buffer.toString();
	}

	public String generateremoveSpark(String line) {

		String[] items = line.split(" ");

		StringBuffer buffer = new StringBuffer();

		buffer.append("ssh -i /home/mkhe8942/sparkkey.pem ubuntu@" + items[0] + " rm -r /home/ubuntu/spark/assembly");

		buffer.append("ssh -i /home/mkhe8942/sparkkey.pem ubuntu@" + items[0] + " rm -r /home/ubuntu/spark/examples");

		buffer.append("\n ssh -i /home/mkhe8942/sparkkey.pem ubuntu@" + items[0]
				+ " rm -r /home/ubuntu/original-spark-examples_2.11-2.0.0-SNAPSHOT.jar");

		buffer.append("\n ssh -i /home/mkhe8942/sparkkey.pem ubuntu@" + items[0] + " rm -r /home/ubuntu/spark/bin");

		buffer.append(
				"\n ssh -i /home/mkhe8942/sparkkey.pem ubuntu@" + items[0] + " rm -r /home/ubuntu/spark/external");

		buffer.append("\n echo @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + items[0]);

		return buffer.toString();
	}

	public String generateStaticNodeMonitors(String line) {

		String[] items = line.split(" ");

		StringBuffer buffer = new StringBuffer();

		if (items.length == 4) {

			buffer.append(items[0] + ":20502");
			return buffer.toString();

		}
		return null;
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