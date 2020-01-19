package usyd.distributed.scheduling.dataconversion.Tasks;

import java.io.BufferedReader;
import java.util.List;

import usyd.distributed.scheduling.dataconversion.Function;

public class MergeSortPartitionedJobs implements Function<BufferedReader, List<String>> {

	String fileName = null;

	public MergeSortPartitionedJobs(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public List<String> apply(BufferedReader t) {
		return null;
	}

}
