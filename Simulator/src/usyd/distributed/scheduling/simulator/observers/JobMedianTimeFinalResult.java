package usyd.distributed.scheduling.simulator.observers;

import usyd.distributed.scheduling.simulator.Job;
import usyd.distributed.scheduling.simulator.Jobs;

public class JobMedianTimeFinalResult extends FinalResultBase {

	public JobMedianTimeFinalResult(ResultPrinterBase printer) {
		super(printer);
	}

	public JobMedianTimeFinalResult(FinalResultBase finalResultBase, ResultPrinterBase printer) {
		super(finalResultBase, printer);
	}

	@Override
	public void save() {

		if (finalResultBase != null)
			finalResultBase.save();

		JobMedianTimeVisitor visitor = new JobMedianTimeVisitor();

		for (Job job : Jobs.getJobs().values())
			job.accept(visitor);

		printer.print(visitor.getJobMedianTime());

	}

}
