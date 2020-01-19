package usyd.distributed.scheduling.simulator.observers;

import java.util.UUID;

import usyd.distributed.scheduling.simulator.Configuration;
import usyd.distributed.scheduling.simulator.Workers;
import usyd.distributed.scheduling.util.DataHelper;

public class SimulationObserver {

	private static final SimulationObserver observer = new SimulationObserver();

	PeriodicPrinter printer = null;

	public static void printState() {
		getObserver().printer.print();
	}

	public static SimulationObserver getObserver() {
		return observer;
	}

	public static void simulationStarted() {

		getObserver().printer = new ConsolePeriodicPrinter();
	}

	public static void simulationEnded() {

		String moveFile = DataHelper.getOutputPath() + "move-" + Workers.getWorkers().length + "-"
				+ Configuration.getLoad() + "-" + UUID.randomUUID() + ".txt";

		String medianJobCompletionTime = DataHelper.getOutputPath() + "ajct-" + Workers.getWorkers().length + "-"
				+ Configuration.getLoad() + "-" + UUID.randomUUID() + ".txt";

		String perJobCompletionTime = DataHelper.getOutputPath() + "pjct-" + Workers.getWorkers().length + "-"
				+ Configuration.getLoad() + "-" + UUID.randomUUID() + ".txt";

		InCompletedJobsFinalResult incompleted = new InCompletedJobsFinalResult(new ConsoleResultPrinter());

		JobMedianTimeFinalResult jobMedianTime = new JobMedianTimeFinalResult(incompleted,
				new ConsoleResultPrinter(new FileResultPrinter(medianJobCompletionTime)));

		JobsMovingCountFinalResult jobsMoving = new JobsMovingCountFinalResult(jobMedianTime,
				new FileResultPrinter(moveFile));

		PerJobCompletionTimeFinalResult perJobCompletion = new PerJobCompletionTimeFinalResult(jobsMoving,
				new FileResultPrinter(perJobCompletionTime));

		perJobCompletion.save();
	}
}