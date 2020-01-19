package usyd.distributed.scheduling.util;

import java.util.Random;

public class ConsistentHashing {

	public static int getNode(long key, int num_servers) {

		Random r = new Random(key);
		int b = -1, j = 0;

		while (j < num_servers) {
			b = j;
			j = (int) Math.floor((b + 1) / r.nextDouble());
		}

		return b;
	}
}
