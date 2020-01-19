package usyd.distributed.scheduling.dataconversion.Tasks;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import usyd.distributed.scheduling.dataconversion.Function;
import usyd.distributed.scheduling.dataconversion.Utility;

public class MedianTaskDuration implements Function<String, List<BigInteger>> {

	private String path;

	public MedianTaskDuration(String path) {
		this.path = path;
	}

	@Override
	public List<BigInteger> apply(String fileName) {

		List<BigInteger> retVals = new ArrayList<BigInteger>();

		long lastSeenJobId = -1;
		long sum = 0;
		int taskCount = 0;

		String currentLine = null;
		BufferedReader br;

		try {
			br = new BufferedReader(new FileReader(path + fileName));

			while (true) {

				try {

					currentLine = br.readLine();
					if (currentLine == null) {
						retVals.add(BigInteger.valueOf(sum / taskCount));
						break;
					}
					if (!currentLine.split(" ")[0].equals(Long.toString(lastSeenJobId)) && lastSeenJobId != -1) {
						retVals.add(BigInteger.valueOf(sum / taskCount));
						sum = 0;
						taskCount = 0;
					}
					lastSeenJobId = Long.parseLong(currentLine.split(" ")[0]);
					taskCount++;
					sum = sum + (new BigInteger(currentLine.split(" ")[2]).longValue() / 10000000);

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