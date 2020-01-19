package usyd.distributed.scheduling.simulator.observers;

public class FinalResultBase implements FinalResult {

	protected FinalResultBase finalResultBase;
	protected ResultPrinterBase printer;

	public FinalResultBase(ResultPrinterBase printer) {
		this.printer = printer;
	}

	public FinalResultBase(FinalResultBase finalResultBase, ResultPrinterBase printer) {
		this(printer);
		this.finalResultBase = finalResultBase;
	}

	@Override
	public void save() {

	}

}
