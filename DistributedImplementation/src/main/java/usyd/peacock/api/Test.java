package usyd.peacock.api;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

//import java.lang.management.GarbageCollectorMXBean;
//import java.lang.management.ManagementFactory;
//import java.lang.reflect.InvocationTargetException;
//import java.nio.ByteBuffer;
//
//import sun.nio.ch.DirectBuffer;

public class Test {

	public static void main(String[] args) throws Exception {

		// CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>(new
		// String[] { "abc", "def" });
		//
		// System.out.println(String.join(",", list));
		//
		// HashMap<String, ByteBuffer> tasks = new HashMap<String,
		// ByteBuffer>();
		//
		// ByteBuffer task1 = ByteBuffer.allocate(8);
		// task1.putLong(10);
		// tasks.put("0", task1);
		// task1.position(0);
		//
		// System.out.println(tasks.get("0").getLong());
		//
		// ByteBuffer task2 = ByteBuffer.allocate(8);
		// task2.putLong(20);
		// tasks.put("1", task2);
		// task2.position(0);
		// System.out.println(tasks.get("1").getLong());
		//
		// ByteBuffer task3 = ByteBuffer.allocate(8);
		// task3.putLong(30);
		// tasks.put("2", task3);
		// task3.position(0);
		// System.out.println(tasks.get("2").getLong());
		//
		// ByteBuffer task4 = ByteBuffer.allocate(8);
		// task4.putLong(40);
		// tasks.put("3", task4);
		// task4.position(0);
		// System.out.println(tasks.get("3").getLong());
		//
		// ByteBuffer task5 = ByteBuffer.allocate(8);
		// task5.putLong(50);
		// tasks.put("4", task5);
		// task5.position(0);
		// System.out.println(tasks.get("4").getLong());

		// long start = System.currentTimeMillis();
		// Test.f();
		//
		// long secondGCCount = Test.getGCCount();
		// long secondGCTime = Test.getGCTime();
		//
		// System.out.println(secondGCTime + " " + secondGCCount);
		//
		// Test.f();
		//
		// // secondGCCount = Test.getGCCount();
		// // secondGCTime = Test.getGCTime();
		// //
		// // System.out.println(secondGCTime + " " + secondGCCount);
		//
		// for (int i = 0; i < 10000; i++) {
		// Test.f();
		// }
		//
		// secondGCCount = Test.getGCCount();
		// secondGCTime = Test.getGCTime();
		//
		// System.out.println(secondGCTime + " " + secondGCCount);
		//
		// System.out.println(System.currentTimeMillis() - start);

		// Test t = new Test();
		//
		// HashMap<Long, Long> cache = new HashMap<Long, Long>();
		//
		// cache.put(0L, 0L);
		//
		// cache.put(1L, 1L);
		//
		// // cache.put(2, 1);
		//
		// // t.fibonachi(5, cache);
		//
		// long start = System.nanoTime();
		// System.out.println(t.fibonachiOptimized(40, cache));
		// long finish = System.nanoTime();
		// System.out.println(finish - start);
		//
		// start = System.nanoTime();
		// System.out.println(t.fibonachi(40));
		// finish = System.nanoTime();
		//
		// System.out.println(finish - start);
		//
		// }
		//
		// public long fibonachiOptimized(long n, HashMap<Long, Long> cache)
		{
			//
			// if (cache.containsKey(n))
			// return cache.get(n);
			//
			// cache.put(n - 1, fibonachiOptimized(n - 1, cache));
			//
			// cache.put(n - 2, fibonachiOptimized(n - 2, cache));
			//
			// return cache.get(n - 1) + cache.get(n - 2);
			//
			// }
			//
			// public int fibonachi(long n) {
			//
			// if (n == 0)
			// return 0;
			//
			// if (n == 1)
			// return 1;
			//
			// return fibonachi(n - 1) + fibonachi(n - 2);
		}

		// public static void f() throws NoSuchMethodException,
		// SecurityException,
		// IllegalAccessException,
		// IllegalArgumentException, InvocationTargetException {
		//
		// // ByteBuffer bb = ByteBuffer.allocateDirect(58720256);
		// ByteBuffer bb = ByteBuffer.wrap(new byte[58720256]);
		//
		// long heapSize = Runtime.getRuntime().totalMemory();
		//
		// // Get maximum size of heap in bytes. The heap cannot grow beyond
		// this
		// // size.// Any attempt will result in an OutOfMemoryException.
		// long heapMaxSize = Runtime.getRuntime().maxMemory();
		//
		// // Get amount of free memory within the heap in bytes. This size will
		// // increase // after garbage collection and decrease as new objects
		// are
		// // created.
		// long heapFreeSize = Runtime.getRuntime().freeMemory();
		//
		// for (int i = 0; i < 58720256; i++) {
		// bb.put(Byte.MAX_VALUE);
		// }
		//
		// heapFreeSize = Runtime.getRuntime().freeMemory();
		//
		// if (bb instanceof DirectBuffer)
		// ((DirectBuffer) bb).cleaner().clean();
		// }

		// public static long getGCCount() {
		//
		// long totalGarbageCollections = 0;
		//
		// for (GarbageCollectorMXBean gc :
		// ManagementFactory.getGarbageCollectorMXBeans()) {
		// long count = gc.getCollectionCount();
		// if (count >= 0) {
		// totalGarbageCollections += count;
		// }
		// }
		// return totalGarbageCollections;
		// }

		// public static long getGCTime() {
		// long garbageCollectionTime = 0;
		//
		// for (GarbageCollectorMXBean gc :
		// ManagementFactory.getGarbageCollectorMXBeans()) {
		// long time = gc.getCollectionTime();
		// if (time >= 0) {
		// garbageCollectionTime += time;
		// }
		// }
		// return garbageCollectionTime;
		// }

		long sum = 0;
		for (int i = 0; i < 1000; i++)
			sum += poisson(1.3) * 1000;

		System.out.println(sum / 1000);

	}

	private static Random randomJobArrival = new Random();

	public static int poisson(final double mean) {

		double L = Math.exp(-mean);
		double p = 1.0;
		int k = 0;

		do {

			k++;
			p *= randomJobArrival.nextDouble();

		} while (p > L);

		return k - 1;
	}
}