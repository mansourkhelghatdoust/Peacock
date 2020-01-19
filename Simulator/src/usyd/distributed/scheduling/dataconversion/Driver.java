package usyd.distributed.scheduling.dataconversion;

import java.io.BufferedReader;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import usyd.distributed.scheduling.dataconversion.Tasks.Aggregate;
import usyd.distributed.scheduling.dataconversion.Tasks.AggregateNumOfJobs;
import usyd.distributed.scheduling.dataconversion.Tasks.Average;
import usyd.distributed.scheduling.dataconversion.Tasks.LongAverage;
import usyd.distributed.scheduling.dataconversion.Tasks.MedianTaskDuration;
import usyd.distributed.scheduling.dataconversion.Tasks.MedianTaskPerJob;
import usyd.distributed.scheduling.dataconversion.Tasks.PartitionJobs;
import usyd.distributed.scheduling.dataconversion.Tasks.RemoveCorruptedJobsTasks;
import usyd.distributed.scheduling.dataconversion.Tasks.TaskEventPolish;
import usyd.distributed.scheduling.dataconversion.Tasks.sortTasksOfPartitionedJobs;
import usyd.distributed.scheduling.dataconversion.input.BunchTextFileScanner;
import usyd.distributed.scheduling.dataconversion.input.TextFileScanner;
import usyd.distributed.scheduling.dataconversion.output.BunchTextOutputRenderer;
import usyd.distributed.scheduling.dataconversion.output.LongOutputRenderer;
import usyd.distributed.scheduling.dataconversion.output.TextOutputRenderer;

public class Driver {

	private static String TASK_EVENTS_INPUT_PATH = "C:\\gsutil\\gsutil\\DATA\\clusterdata-2011-2\\task_events\\";
	private static String TASK_EVENTS_OUTPUT_PATH = "C:\\gsutil\\gsutil\\DATA\\clusterdata-2011-2\\task_events_polished\\";

	private static String TASK_EVENTS_POLISHED_INPUT_PATH = "C:\\gsutil\\gsutil\\DATA\\clusterdata-2011-2\\task_events_polished\\";
	private static String TASK_EVENTS_POLISHED_OUTPUT_PATH = "C:\\gsutil\\gsutil\\DATA\\clusterdata-2011-2\\task_events_polished_partitioned\\";

	private static String TASK_EVENTS_POLISHED_PARTITIONED_INPUT_PATH = "C:\\gsutil\\gsutil\\DATA\\clusterdata-2011-2\\task_events_polished_partitioned\\";
	private static String TASK_EVENTS_POLISHED_PARTITIONED_OUTPUT_PATH = "C:\\gsutil\\gsutil\\DATA\\clusterdata-2011-2\\task_events_polished_partitioned_uniquejobs\\";

	private static String TASK_EVENTS_POLISHED_PARTITIONED_SORTED_INPUT_PATH = "C:\\gsutil\\gsutil\\DATA\\clusterdata-2011-2\\task_events_polished_partitioned_uniquejobs\\";
	private static String TASK_EVENTS_POLISHED_PARTITIONED_SORTED_OUTPUT_PATH = "C:\\gsutil\\gsutil\\DATA\\clusterdata-2011-2\\task_events_polished_partitioned_uniquejobs_sorted\\";

//	private static String TASK_EVENTS_POLISHED_PARTITIONED_REDUCED_INPUT_PATH = "C:\\gsutil\\gsutil\\DATA\\clusterdata-2011-2\\task_events_polished_partitioned_uniquejobs_sorted\\";
//	private static String TASK_EVENTS_POLISHED_PARTITIONED_REDUCED_INPUT_PATH = "//home//DistributedScheduling//DistributedSchedulingSpark//Data//";

	private static String TASK_EVENTS_POLISHED_PARTITIONED_REDUCED_INPUT_PATH = "D:\\DistributedScheduling\\DistributedSchedulingSpark\\Data\\";
	
	private static String TASK_EVENTS_POLISHED_PARTITIONED_REDUCED_OUTPUT_PATH = "D:\\DistributedScheduling\\DistributedSchedulingSpark\\Output\\";
//	private static String TASK_EVENTS_POLISHED_PARTITIONED_REDUCED_OUTPUT_PATH = "C:\\gsutil\\gsutil\\DATA\\clusterdata-2011-2\\task_events_polished_partitioned_uniquejobs_reduced\\";
//	private static String TASK_EVENTS_POLISHED_PARTITIONED_REDUCED_OUTPUT_PATH = "//home//DistributedScheduling//DistributedSchedulingSpark//Output//";

	public static void main(String[] args) {
		new Driver().runReduceMedianTaskPerJob();
		new Driver().runReduceMedianTaskDuration();
		new Driver().runReduceNumOfJobs();
	}
	
	public void runTaskEventPolishJob() {

		ExecutorService executor = Executors.newFixedThreadPool(3);
		DirectoryScanner scanner = new DirectoryScanner(TASK_EVENTS_INPUT_PATH);
		while (scanner.hasNext()) {
			String fileName = scanner.next();
			executor.execute(new Mapper<String, String>(new TaskEventPolish(','),
					new TextFileScanner(TASK_EVENTS_INPUT_PATH + fileName),
					new TextOutputRenderer(TASK_EVENTS_OUTPUT_PATH, fileName + ".txt")));
		}
		executor.shutdown();

	}

	public void runPartitioningJob() {

		long[] ranges = new long[] { 0, 500000000, 510000000, 520000000, 530000000, 540000000, 550000000, 560000000,
				570000000, 580000000, 590000000, 6000000000L, 6100000000L, 6200000000L, 6210000000L, 6220000000L,
				6220500000L, 6221000000L, 6221500000L, 6221700000L, 6221900000L, 6222000000L, 6224000000L, 6225000000L,
				6230000000L, 6240000000L, 6250000000L, 6255000000L, 6260000000L, 6280000000L, 6290000000L, 6300000000L,
				6310000000L, 6320000000L, 6330000000L, 6340000000L, 6350000000L, 6360000000L, 6370000000L, 6380000000L,
				6390000000L, 6400000000L, 6410000000L, 6420000000L, 6430000000L, 6440000000L, 6450000000L, 6460000000L,
				6470000000L, 6480000000L, 6490000000L, 6500000000L, 6600000000L, 6700000000L, 6800000000L, 6900000000L,
				7000000000L, 100000000000L };

		ExecutorService executor = Executors.newFixedThreadPool(3);
		for (int categoryIdx = 0; categoryIdx < ranges.length; categoryIdx++) {
			DirectoryScanner scanner = new DirectoryScanner(TASK_EVENTS_POLISHED_INPUT_PATH);
			while (scanner.hasNext()) {
				executor.execute(new Mapper<String, String>(new PartitionJobs(categoryIdx, ranges),
						new TextFileScanner(TASK_EVENTS_POLISHED_INPUT_PATH + scanner.next()), new TextOutputRenderer(
								TASK_EVENTS_POLISHED_OUTPUT_PATH, "partitioned" + categoryIdx + ".txt")));
			}
		}
		executor.shutdown();

	}

	public void runRemoveCorruptedJobsTasks() {

		ExecutorService executor = Executors.newSingleThreadExecutor();
		DirectoryScanner scanner = new DirectoryScanner(TASK_EVENTS_POLISHED_PARTITIONED_INPUT_PATH);
		while (scanner.hasNext()) {
			String fileName = scanner.next();
			executor.execute(new Mapper<BufferedReader, List<String>>(new RemoveCorruptedJobsTasks(fileName),
					new BunchTextFileScanner(TASK_EVENTS_POLISHED_PARTITIONED_INPUT_PATH + fileName),
					new BunchTextOutputRenderer(TASK_EVENTS_POLISHED_PARTITIONED_OUTPUT_PATH, fileName)));
		}
		executor.shutdown();

	}

	public void runSortedPartitionedJobsTasks() {

		ExecutorService executor = Executors.newSingleThreadExecutor();
		DirectoryScanner scanner = new DirectoryScanner(TASK_EVENTS_POLISHED_PARTITIONED_SORTED_INPUT_PATH);
		while (scanner.hasNext()) {
			String fileName = scanner.next();
			executor.execute(new Mapper<BufferedReader, List<String>>(new sortTasksOfPartitionedJobs(fileName),
					new BunchTextFileScanner(TASK_EVENTS_POLISHED_PARTITIONED_SORTED_INPUT_PATH + fileName),
					new BunchTextOutputRenderer(TASK_EVENTS_POLISHED_PARTITIONED_SORTED_OUTPUT_PATH, fileName)));
		}
		executor.shutdown();

	}

	public void runReduceNumOfJobs() {

		ExecutorService executor = Executors.newSingleThreadExecutor();

		DirectoryScanner scanner = new DirectoryScanner(TASK_EVENTS_POLISHED_PARTITIONED_REDUCED_INPUT_PATH);

		executor.execute(new Reducer<String, Long, Long>(
				new AggregateNumOfJobs(TASK_EVENTS_POLISHED_PARTITIONED_REDUCED_INPUT_PATH), scanner,
				new LongOutputRenderer(TASK_EVENTS_POLISHED_PARTITIONED_REDUCED_OUTPUT_PATH, "numOfJobs"),
				new Aggregate()));

		executor.shutdown();

	}

	public void runReduceMedianTaskDuration() {

		ExecutorService executor = Executors.newSingleThreadExecutor();

		DirectoryScanner scanner = new DirectoryScanner(TASK_EVENTS_POLISHED_PARTITIONED_REDUCED_INPUT_PATH);

		executor.execute(new Reducer<String, List<BigInteger>, Long>(
				new MedianTaskDuration(TASK_EVENTS_POLISHED_PARTITIONED_REDUCED_INPUT_PATH), scanner,
				new LongOutputRenderer(TASK_EVENTS_POLISHED_PARTITIONED_REDUCED_OUTPUT_PATH, "medianTaskDuration"),
				new Average()));

		executor.shutdown();

	}

	public void runReduceMedianTaskPerJob() {

		ExecutorService executor = Executors.newSingleThreadExecutor();

		DirectoryScanner scanner = new DirectoryScanner(TASK_EVENTS_POLISHED_PARTITIONED_REDUCED_INPUT_PATH);

		executor.execute(new Reducer<String, List<Long>, Long>(
				new MedianTaskPerJob(TASK_EVENTS_POLISHED_PARTITIONED_REDUCED_INPUT_PATH), scanner,
				new LongOutputRenderer(TASK_EVENTS_POLISHED_PARTITIONED_REDUCED_OUTPUT_PATH, "medianTaskPerJob"),
				new LongAverage()));

		executor.shutdown();

	}

}
