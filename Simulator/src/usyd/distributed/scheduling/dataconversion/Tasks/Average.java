package usyd.distributed.scheduling.dataconversion.Tasks;

import java.math.BigInteger;
import java.util.List;

import usyd.distributed.scheduling.dataconversion.Function;

public class Average implements Function<List<List<BigInteger>>, Long> {

	public Average() {

	}

	@Override
	public Long apply(List<List<BigInteger>> list) {

		BigInteger result = BigInteger.ZERO;
		int count = 0;
		for (List<BigInteger> val : list) {
			for (BigInteger innerVal : val) {
				result = result.add(innerVal);
				count++;
			}
		}

		BigInteger retVal = result.divide(BigInteger.valueOf(count));

		return retVal.longValue();
	}

}
