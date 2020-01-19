package usyd.distributed.scheduling.dataconversion.Tasks;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import usyd.distributed.scheduling.dataconversion.Function;
import usyd.distributed.scheduling.dataconversion.Utility;

public class AggregateNumOfJobs implements Function<String, Long> {

	private String path;

	public AggregateNumOfJobs(String path) {
		this.path = path;
	}

	@Override
	public Long apply(String fileName) {

		long lastSeenJobId = -1;
		long jobCount = 0;

		String currentLine = null;
		BufferedReader br;

		try {
			br = new BufferedReader(new FileReader(path + fileName));

			while (true) {

				try {
					currentLine = br.readLine();
					if (currentLine == null)
						break;
					if (!currentLine.split(" ")[0].equals(Long.toString(lastSeenJobId))) {
						jobCount++;
						lastSeenJobId = Long.parseLong(currentLine.split(" ")[0]);
					}
				} catch (IOException e) {
					Utility.handleIOException(br, e);
				}

			}

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		return jobCount;
	}

}