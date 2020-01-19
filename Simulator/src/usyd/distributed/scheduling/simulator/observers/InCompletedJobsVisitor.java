package usyd.distributed.scheduling.simulator.observers;

import java.util.ArrayList;
import java.util.List;

import usyd.distributed.scheduling.simulator.Job;

public class InCompletedJobsVisitor implements Visitor {

	ArrayList<String> inCompletedJobs = new ArrayList<String>();

	public InCompletedJobsVisitor() {

	}

	@Override
	public void visit(Job job) {
		if (!job.isCompleted())
			inCompletedJobs.add(Long.toString(job.getJobId()));
	}

	public List<String> getInCompletedJobs() {
		return inCompletedJobs;
	}

}
