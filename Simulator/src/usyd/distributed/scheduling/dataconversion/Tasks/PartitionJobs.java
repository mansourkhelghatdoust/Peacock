package usyd.distributed.scheduling.dataconversion.Tasks;

import usyd.distributed.scheduling.dataconversion.Function;

public class PartitionJobs implements Function<String, String> {

	int categoryIdx = 0;
	int upIdx = 0;
	long[] ranges = null;

	public PartitionJobs(int categoryIdx, long[] ranges) {

		this.categoryIdx = categoryIdx;
		this.ranges = ranges;
		upIdx = categoryIdx + 1 >= ranges.length ? categoryIdx : categoryIdx + 1;

	}

	@Override
	public String apply(String str) {

		long jobId = Long.parseLong(str.split(" ")[0]);
		if (jobId >= ranges[categoryIdx] && jobId <= ranges[upIdx])
			return str;
		return null;

	}
}