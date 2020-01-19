package usyd.distributed.scheduling.simulator.events;

import usyd.distributed.scheduling.simulator.Worker;

public class BaseEvent implements Event {

	protected double time;
	protected Worker worker;

	public BaseEvent(double time) {
		this.time = time;
	}

	@Override
	public double getTime() {
		return time;
	}
	
	public Worker getWorker() {
		return worker;
	}

	@Override
	public void setTime(double time) {
		this.time = time;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void runConc() throws Exception {
		// TODO Auto-generated method stub
		
	}

}