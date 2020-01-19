package usyd.distributed.scheduling.simulator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import usyd.distributed.scheduling.dataconversion.DirectoryScanner;
import usyd.distributed.scheduling.simulator.events.Peacock.HopTaskEvent;
import usyd.distributed.scheduling.util.DataHelper;
//import usyd.distributed.scheduling.util.ProcessSynchronizer;

public class Jobs {

	private final String TASK_EVENTS_POLISHED_PARTITIONED_SORTED_OUTPUT_PATH;

	private DirectoryScanner scanner = null;

	private static final Jobs instance = new Jobs();

	private static ArrayList<Job> unscheduledJobs = new ArrayList<Job>();

	private static ConcurrentHashMap<Long, Job> jobs = new ConcurrentHashMap<Long, Job>();

	private static AtomicInteger remainingJobs = new AtomicInteger(0);

	private static AtomicInteger jobCompletedCount = new AtomicInteger(0);

	public static AtomicInteger numberOfActiveTasks = new AtomicInteger(0);

	public static AtomicLong estimationExecutionTimeOfActiveTasks = new AtomicLong(0);

	private Jobs() {
		TASK_EVENTS_POLISHED_PARTITIONED_SORTED_OUTPUT_PATH = DataHelper.getInputPath();
		scanner = new DirectoryScanner(TASK_EVENTS_POLISHED_PARTITIONED_SORTED_OUTPUT_PATH);
	}

	public void reset() {
		scanner = new DirectoryScanner(TASK_EVENTS_POLISHED_PARTITIONED_SORTED_OUTPUT_PATH);
		remainingJobs = new AtomicInteger(Configuration.googleNumberOfJobs);
		jobCompletedCount = new AtomicInteger(0);
		numberOfActiveTasks = new AtomicInteger(0);
		estimationExecutionTimeOfActiveTasks.set(0);
		jobs.clear();
		HopTaskEvent.reset();
	}

	public static Jobs getInstance() {
		return instance;
	}

	public Job getNextJob(double time) {

		if (!unscheduledJobs.isEmpty())
			return fetchJob(time);

		if (scanner.hasNext()) {
			String fileName = scanner.next();
			BufferedReader br = null;
			try {
				// locker.lock();
				br = new BufferedReader(new FileReader(TASK_EVENTS_POLISHED_PARTITIONED_SORTED_OUTPUT_PATH + fileName));
				String jobId = " ";
				while (true) {
					String currentLine = br.readLine();
					if (currentLine == null)
						break;

					String[] slices = currentLine.split(" ");
					if (!slices[0].equals(jobId)) {
						jobId = slices[0];
						Job job = new Job(Long.parseLong(slices[0]));
						jobs.put(Long.parseLong(slices[0]), job);
						unscheduledJobs.add(job);
					}
					double val = Double.parseDouble(slices[2]) / 10000000;

					jobs.get(Long.parseLong(slices[0])).addTask(val < 0 ? 1 : (long) val);
				}
				return fetchJob(time);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					br.close();
					// locker.unlock();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;

	}

	Random dr = new Random();

	private Job fetchJob(double time) {

		Job job = unscheduledJobs.remove(0);
		job.setStartTime(time);
		return job;
	}

	public static void setRemainingJobs(int remainingJobs) {
		Jobs.remainingJobs = new AtomicInteger(remainingJobs);
	}

	public static int getRemainingJobs() {
		return remainingJobs.intValue();
	}

	public static void decreaseRemainingJobs() {
		remainingJobs.decrementAndGet();
	}

	public static ConcurrentHashMap<Long, Job> getJobs() {
		return jobs;
	}

	public static Job getJob(long jobId) {
		return Jobs.getJobs().get(jobId);
	}

	public static int getJobCompletedCount() {
		return jobCompletedCount.intValue();
	}

	public static int increaseJobCompletedCount() {
		return jobCompletedCount.incrementAndGet();
	}

	public static boolean isJobNeeded() {
		return remainingJobs.intValue() > 0;
	}

	public static synchronized void increaseMove(HashSet<Job> jobs) {
		for (Job job : jobs)
			job.increaseMove();
	}
}
