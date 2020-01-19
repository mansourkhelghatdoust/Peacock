package usyd.distributed.scheduling.simulator.observers;

import usyd.distributed.scheduling.simulator.Job;
import usyd.distributed.scheduling.simulator.Jobs;

public class JobsMovingCountFinalResult extends FinalResultBase {

	public JobsMovingCountFinalResult(ResultPrinterBase printer) {
		super(printer);
	}

	public JobsMovingCountFinalResult(FinalResultBase finalResultBase, ResultPrinterBase printer) {
		super(finalResultBase, printer);
	}

	@Override
	public void save() {
		
		if (finalResultBase != null)
			finalResultBase.save();

		JobsMovingCountVisitor visitor = new JobsMovingCountVisitor();

		for (Job job : Jobs.getJobs().values())
			job.accept(visitor);

		printer.print(visitor.getJobsMovingCount());

	}
}
