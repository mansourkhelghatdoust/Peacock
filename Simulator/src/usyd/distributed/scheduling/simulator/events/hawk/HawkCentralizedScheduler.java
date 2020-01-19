package usyd.distributed.scheduling.simulator.events.hawk;

import java.io.Serializable;
import java.util.Comparator;
import java.util.PriorityQueue;

import usyd.distributed.scheduling.simulator.Configuration;
import usyd.distributed.scheduling.simulator.Job;
import usyd.distributed.scheduling.simulator.SimulationUtil;

public class HawkCentralizedScheduler {

	private static PriorityQueue<WorkerLongJobsInfo> queue = new PriorityQueue<WorkerLongJobsInfo>(10 , 
			new HawkCentralizedScheduler.LongJobWaitingTimeComparator());

	public static void initialize() {

		for (int workerId = 0; workerId < Configuration.getTotalWorkers() * 90 / 100; workerId++)
			queue.add(new WorkerLongJobsInfo(workerId));

	}

	public static int getWorkerId(Job job) {
		WorkerLongJobsInfo worker = queue.poll();
		worker.addJob(job.getAvgExecutionTime());
		queue.add(worker);
		return worker.getWorkerId();
	}

	public static class WorkerLongJobsInfo {

		private int workerId;
		private double waitingTime = 0;
		private double registeredTime = 0;

		public WorkerLongJobsInfo(int workerId) {
			this.setWorkerId(workerId);
		}

		public void addJob(double executionTime) {
			waitingTime -= SimulationUtil.getCurrentTime() - registeredTime;
			waitingTime = waitingTime > 0 ? waitingTime : 0;
			waitingTime += executionTime;
			registeredTime = SimulationUtil.getCurrentTime();
		}

		public void update() {
			waitingTime -= SimulationUtil.getCurrentTime() - registeredTime;
			waitingTime = waitingTime > 0 ? waitingTime : 0;
			registeredTime = SimulationUtil.getCurrentTime();
		}

		public int getWorkerId() {
			return workerId;
		}

		public void setWorkerId(int workerId) {
			this.workerId = workerId;
		}

		public double getWaitingTime() {
			return waitingTime;
		}

		public void setWaitingTime(long waitingTime) {
			this.waitingTime = waitingTime;
		}

		public double getRegisteredTime() {
			return registeredTime;
		}

		public void setRegisteredTime(long registeredTime) {
			this.registeredTime = registeredTime;
		}
	}

	public static class LongJobWaitingTimeComparator implements Comparator<WorkerLongJobsInfo>, Serializable {

		private static final long serialVersionUID = 1L;

		public LongJobWaitingTimeComparator() {

		}

		@Override
		public int compare(WorkerLongJobsInfo worker1, WorkerLongJobsInfo worker2) {

			worker1.update();
			worker2.update();

			double diff = worker1.getWaitingTime() - worker2.getWaitingTime();

			return (int) diff;
		}
	}
}