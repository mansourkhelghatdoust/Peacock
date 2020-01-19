package usyd.distributed.scheduling.dataconversion;

import java.util.ArrayList;
import java.util.List;

public class Reducer<T, U, K> implements Runnable {

	private Function<T, U> func;
	private InputScanner<T> input;
	private OutputRenderer<K> output;
	private Function<List<U>, K> finalFunc;

	public Reducer(Function<T, U> func, InputScanner<T> input, OutputRenderer<K> output,
			Function<List<U>, K> finalFunc) {

		this.func = func;
		this.input = input;
		this.output = output;
		this.finalFunc = finalFunc;

	}

	@Override
	public void run() {

		List<U> tmpOutput = new ArrayList<U>();

		while (input.hasNext()) {
			
			U u = func.apply(input.next());
			if (u != null)
				tmpOutput.add(u);
		
		}

		List<K> list = new ArrayList<K>();

		list.add(finalFunc.apply(tmpOutput));

		output.render(list);

	}

}