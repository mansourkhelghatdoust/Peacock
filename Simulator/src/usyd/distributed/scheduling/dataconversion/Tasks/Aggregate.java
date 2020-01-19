package usyd.distributed.scheduling.dataconversion.Tasks;

import java.util.List;

import usyd.distributed.scheduling.dataconversion.Function;

public class Aggregate implements Function<List<Long>, Long> {

	public Aggregate() {

	}

	@Override
	public Long apply(List<Long> list) {

		long result = 0;

		for (Long val : list) {
			result += val;
		}

		return result;
	}

}
