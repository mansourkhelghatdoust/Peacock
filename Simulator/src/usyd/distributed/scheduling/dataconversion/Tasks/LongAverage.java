package usyd.distributed.scheduling.dataconversion.Tasks;

import java.util.List;

import usyd.distributed.scheduling.dataconversion.Function;

public class LongAverage implements Function<List<List<Long>>, Long> {


	public LongAverage() {
	}

	@Override
	public Long apply(List<List<Long>> list) {

		Long result = 0L;
		int count = 0;
		for (List<Long> val : list) {
			for (Long innerVal : val) {
				result += innerVal;
				count++;
			}
		}

		return result / count;
	}

}
