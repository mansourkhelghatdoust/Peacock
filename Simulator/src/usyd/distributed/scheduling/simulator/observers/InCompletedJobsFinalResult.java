package usyd.distributed.scheduling.simulator.observers;

import java.util.ArrayList;
import java.util.List;

import usyd.distributed.scheduling.simulator.Job;
import usyd.distributed.scheduling.simulator.Jobs;

public class InCompletedJobsFinalResult extends FinalResultBase {

	public InCompletedJobsFinalResult(ResultPrinterBase printer) {
		super(printer);
	}

	public InCompletedJobsFinalResult(FinalResultBase finalResultBase, ResultPrinterBase printer) {
		super(finalResultBase, printer);
	}

	@Override
	public void save() {

		if (finalResultBase != null)
			finalResultBase.save();

		InCompletedJobsVisitor visitor = new InCompletedJobsVisitor();

		for (Job job : Jobs.getJobs().values())
			job.accept(visitor);

		List<String> output = new ArrayList<String>();
		output.add("InCompleted Jobs : ");
		output.addAll(visitor.getInCompletedJobs());
		printer.print(output);

	}
}
