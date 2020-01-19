package usyd.distributed.scheduling.util;

import java.util.Arrays;

public class MathUtils {

	public static int[] get2PowerOfM(int totalWorkers) {

		int[] result = new int[0];

		for (int i = 1; i <= totalWorkers; i++) {
			
			if ((i & (i - 1)) == 0) {
				result = Arrays.copyOf(result, result.length + 1);
				result[result.length - 1] = i;
			}
			
		}

		return result;

	}
}
