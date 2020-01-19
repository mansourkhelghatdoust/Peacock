package usyd.distributed.scheduling.simulator.observers;

import java.util.ArrayList;

import usyd.distributed.scheduling.simulator.Job;

public class PerJobCompletionTimeVisitor implements Visitor {

	private ArrayList<String> jobCompletionTimes = new ArrayList<String>();

	@Override
	public void visit(Job job) {
		if (job.isCompleted())
			jobCompletionTimes.add(job.getJobId() + ":" + (job.getEndTime() - job.getStartTime()));
	}

	public ArrayList<String> getJobsCompletionTimes() {
		return jobCompletionTimes;
	}

}