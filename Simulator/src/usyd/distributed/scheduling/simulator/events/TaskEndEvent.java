package usyd.distributed.scheduling.simulator.events;

import usyd.distributed.scheduling.simulator.Jobs;
import usyd.distributed.scheduling.simulator.Worker;
import usyd.distributed.scheduling.simulator.Worker.JobTaskInfo;
import usyd.distributed.scheduling.simulator.concurrent.Peacock.ConcurrentExecutor;

public class TaskEndEvent extends BaseEvent implements Runnable {

	protected Worker worker;
	protected JobTaskInfo jobTaskInfo;
	protected boolean notify = false;

	public TaskEndEvent(int time, Worker worker, JobTaskInfo jobTaskInfo) {
		super(time);
		this.setWorker(worker);
		this.setJobId(jobTaskInfo);
	}

	@Override
	public void run() {

		this.worker.freeSlot(time, jobTaskInfo);

		Jobs.numberOfActiveTasks.decrementAndGet();

		Jobs.estimationExecutionTimeOfActiveTasks.set((long) (Jobs.estimationExecutionTimeOfActiveTasks.doubleValue()
				- jobTaskInfo.getJob().getAvgExecutionTime()));

		if (notify) {
			jobTaskInfo.getJob().addTaskCompletionTime(time);
			ConcurrentExecutor.notifyConcEventsExecute();
		} else {
			jobTaskInfo.getJob().addTaskCompletionTime(time);
		}
	}

	public Worker getWorker() {
		return worker;
	}

	@Override
	public void runConc() throws Exception {
		notify = true;
		ConcurrentExecutor.concEventsExecute(this);
	}

	public void setWorker(Worker worker) {
		this.worker = worker;
	}

	public JobTaskInfo getJobId() {
		return jobTaskInfo;
	}

	public void setJobId(JobTaskInfo job) {
		this.jobTaskInfo = job;
	}

}