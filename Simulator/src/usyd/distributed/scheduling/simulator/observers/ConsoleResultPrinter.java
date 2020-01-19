package usyd.distributed.scheduling.simulator.observers;

import java.util.List;

public class ConsoleResultPrinter extends ResultPrinterBase {

	public ConsoleResultPrinter() {
	}

	public ConsoleResultPrinter(ResultPrinterBase base) {
		super(base);
	}

	@Override
	public void print(List<String> list) {
		if (base != null)
			base.print(list);

		list.stream().forEach(System.out::println);
	}
}
