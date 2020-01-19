package usyd.distributed.scheduling.dataconversion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Convert {

	public static void main(String[] args) {

		BufferedReader br = null;
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {

			br = new BufferedReader(new FileReader(
					"C:\\gsutil\\gsutil\\DATA\\clusterdata-2011-2\\task_events_polished_partitioned_uniquejobs_sorted\\RealExperimentData.txt"));
			int visitedJobs = 0;
			String lastVisistedJobId = " ";
			ArrayList<String> lines = new ArrayList<String>();

			while (true) {

				String currentLine = br.readLine();

				if (currentLine == null || visitedJobs > 3499)
					break;

				String[] slices = currentLine.split(" ");

				if (!slices[0].equals("6256576178") && !slices[0].equals("6256394077")) {
					if (!slices[0].equals(lastVisistedJobId)) {
						lastVisistedJobId = slices[0];
						visitedJobs++;
					}
					lines.add((visitedJobs - 1) + " " + slices[1] + " " + slices[2]);
				}
			}

			fw = new FileWriter(
					"C:\\gsutil\\gsutil\\DATA\\clusterdata-2011-2\\task_events_polished_partitioned_uniquejobs_sorted\\output.txt");
			bw = new BufferedWriter(fw);
			for (String line : lines) {
				bw.write(line);
				bw.newLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
