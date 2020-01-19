package usyd.distributed.scheduling.simulator.observers;

public class PeriodicPrinterBase implements PeriodicPrinter {

	protected PeriodicPrinter base;
	protected int jobCompletedCountInterval = 100;

	public PeriodicPrinterBase() {
	}

	public PeriodicPrinterBase(PeriodicPrinter base) {
		this.base = base;
	}

	public PeriodicPrinterBase(PeriodicPrinter base, int jobCompletedCountInterval) {
		this(base);
		this.jobCompletedCountInterval = jobCompletedCountInterval;
	}

	@Override
	public void print() {

	}

}
