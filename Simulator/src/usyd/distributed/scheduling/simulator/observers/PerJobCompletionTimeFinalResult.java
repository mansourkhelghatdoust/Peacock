package usyd.distributed.scheduling.simulator.observers;

import usyd.distributed.scheduling.simulator.Job;
import usyd.distributed.scheduling.simulator.Jobs;

public class PerJobCompletionTimeFinalResult extends FinalResultBase {

	public PerJobCompletionTimeFinalResult(ResultPrinterBase printer) {
		super(printer);
	}

	public PerJobCompletionTimeFinalResult(FinalResultBase finalResult, ResultPrinterBase printer) {
		super(finalResult, printer);
	}

	@Override
	public void save() {

		if (finalResultBase != null)
			finalResultBase.save();

		PerJobCompletionTimeVisitor visitor = new PerJobCompletionTimeVisitor();

		for (Job job : Jobs.getJobs().values())
			job.accept(visitor);

		printer.print(visitor.getJobsCompletionTimes());
	}

}
