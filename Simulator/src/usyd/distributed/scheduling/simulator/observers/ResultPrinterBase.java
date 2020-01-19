package usyd.distributed.scheduling.simulator.observers;

import java.util.List;

public class ResultPrinterBase implements ResultPrinter {

	protected ResultPrinterBase base;

	public ResultPrinterBase() {
	}

	public ResultPrinterBase(ResultPrinterBase base) {
		this.base = base;
	}

	@Override
	public void print(List<String> list) {

	}

}
