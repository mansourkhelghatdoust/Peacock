package usyd.distributed.scheduling.dataconversion.Tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import usyd.distributed.scheduling.dataconversion.Function;
import usyd.distributed.scheduling.dataconversion.Utility;

public class sortTasksOfPartitionedJobs implements Function<BufferedReader, List<String>> {

	HashMap<String, List<String>> jobs = new HashMap<String, List<String>>();
	String fileName = null;

	public sortTasksOfPartitionedJobs(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public List<String> apply(BufferedReader br) {

		String currentLine = null;
		while (true) {

			try {

				currentLine = br.readLine();
				if (currentLine == null)
					break;
				String[] str = currentLine.split(" ");

				if (!jobs.containsKey(str[0]))
					jobs.put(str[0], new ArrayList<>());
				jobs.get(str[0]).add(currentLine);

			} catch (IOException e) {
				Utility.handleIOException(br, e);
			}

		}

		List<String> returnValue = new ArrayList<String>();
		for (Entry<String, List<String>> entry : jobs.entrySet())
			returnValue.addAll(entry.getValue());

		return returnValue;

	}

}
