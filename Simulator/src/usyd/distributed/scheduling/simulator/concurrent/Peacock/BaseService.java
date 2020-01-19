package usyd.distributed.scheduling.simulator.concurrent.Peacock;

import usyd.distributed.scheduling.simulator.events.BaseEvent;

public class BaseService extends BaseEvent  {


	public BaseService(double time) {
		super(time);
	}

	@Override
	public void run() {

	}

	public interface IRandomRunnable {

	}

}
