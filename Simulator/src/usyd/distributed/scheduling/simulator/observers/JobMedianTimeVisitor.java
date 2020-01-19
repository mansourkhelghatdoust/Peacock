package usyd.distributed.scheduling.simulator.observers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import usyd.distributed.scheduling.simulator.Job;

public class JobMedianTimeVisitor implements Visitor {

	int jobCount = 0;
	BigInteger jobMedianTime = BigInteger.ZERO;

	@Override
	public void visit(Job job) {

		if (job.isCompleted()) {
			jobCount++;
			jobMedianTime = jobMedianTime
					.add(new BigInteger(Integer.toString((int) (job.getEndTime() - job.getStartTime()))));
		}
	}

	public List<String> getJobMedianTime() {

		List<String> list = new ArrayList<String>();

		list.add(new String("Median Job Completion Time : "
				+ jobMedianTime.divide(new BigInteger(Integer.toString(jobCount))).toString()));

		return list;
	}

}
