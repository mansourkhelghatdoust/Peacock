package usyd.distributed.scheduling.simulator.observers;

import java.util.ArrayList;

import usyd.distributed.scheduling.simulator.Job;

public class JobsMovingCountVisitor implements Visitor {

	private ArrayList<String> movingInfo = new ArrayList<String>();

	@Override
	public void visit(Job job) {
		if (job.isCompleted())
			movingInfo.add(job.getJobId() + ":" + job.getNumTasks() + ":" + job.getMoveCount());
	}

	public ArrayList<String> getJobsMovingCount() {
		return movingInfo;
	}
}
