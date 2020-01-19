package usyd.distributed.scheduling.simulator.observers;

import usyd.distributed.scheduling.simulator.Configuration;
import usyd.distributed.scheduling.simulator.Jobs;
import usyd.distributed.scheduling.simulator.SimulationUtil;
import usyd.distributed.scheduling.simulator.Workers;
import usyd.distributed.scheduling.simulator.events.Peacock.PeacockScheduler;

public class ConsolePeriodicPrinter extends PeriodicPrinterBase {

	public ConsolePeriodicPrinter() {

	}

	public ConsolePeriodicPrinter(PeriodicPrinter base) {
		super(base);
	}

	public ConsolePeriodicPrinter(PeriodicPrinter base, int jobCompletedCountInterval) {
		super(base, jobCompletedCountInterval);
	}

	@Override
	public void print() {

		if (base != null)
			base.print();

		System.out.println(Configuration.getTotalWorkers() + ":" + Configuration.getLoad() + "  "
				+ (Configuration.googleNumberOfJobs - Jobs.getJobCompletedCount()));
		// System.out.println(Configuration.getTotalWorkers() + ":" +
		// Configuration.getLoad() + " "
		// + SimulationUtil.getCurrentTime() + " " + Jobs.getRemainingJobs() + "
		// " + Jobs.getJobCompletedCount()
		// + " " + Jobs.numberOfActiveTasks + " " + Workers.calculateFreeSlots()
		// + " "
		// + PeacockScheduler.getGlobalInfo().get(PeacockScheduler.QueueSize) +
		// " "
		// + PeacockScheduler.getGlobalInfo().get(PeacockScheduler.WorkerLoad));

	}

}
