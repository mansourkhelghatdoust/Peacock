package usyd.distributed.scheduling.dataconversion.Tasks;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import usyd.distributed.scheduling.dataconversion.Function;
import usyd.distributed.scheduling.dataconversion.Utility;

public class MedianTaskPerJob implements Function<String, List<Long>> {

	private String path;

	public MedianTaskPerJob(String path) {
		this.path = path;
	}

	@Override
	public List<Long> apply(String fileName) {

		List<Long> retVals = new ArrayList<Long>();

		long lastSeenJobId = -1;
		long taskCount = 0;

		String currentLine = null;
		BufferedReader br;

		try {
			br = new BufferedReader(new FileReader(path + fileName));
			while (true) {
				try {
					currentLine = br.readLine();
					if (currentLine == null) {
						retVals.add(taskCount);
						break;
					}
					if (!currentLine.split(" ")[0].equals(Long.toString(lastSeenJobId)) && lastSeenJobId != -1) {

						retVals.add(taskCount);
						taskCount = 0;
					}
					lastSeenJobId = Long.parseLong(currentLine.split(" ")[0]);
					taskCount++;
				} catch (IOException e) {
					Utility.handleIOException(br, e);
				}
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		return retVals;
	}

}