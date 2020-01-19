package usyd.distributed.scheduling.dataconversion;

import java.util.ArrayList;
import java.util.List;

public class Mapper<T, U> implements Runnable {

	private Function<T, U> func;
	private InputScanner<T> input;
	private OutputRenderer<U> output;

	public Mapper(Function<T, U> func, InputScanner<T> input, OutputRenderer<U> output) {
		this.func = func;
		this.input = input;
		this.output = output;
	}

	@Override
	public void run() {

		List<U> outputBuffer = new ArrayList<U>();

		while (input.hasNext()) {
			U u = func.apply(input.next());
			if (u != null)
				outputBuffer.add(u);
		}

		if (!outputBuffer.isEmpty())
			output.render(outputBuffer);
		
	}
}