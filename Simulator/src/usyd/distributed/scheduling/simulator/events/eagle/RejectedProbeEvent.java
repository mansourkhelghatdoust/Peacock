package usyd.distributed.scheduling.simulator.events.eagle;

import java.util.Random;

import usyd.distributed.scheduling.simulator.Configuration;
import usyd.distributed.scheduling.simulator.Simulation;
import usyd.distributed.scheduling.simulator.Workers;
import usyd.distributed.scheduling.simulator.events.BaseEvent;

public class RejectedProbeEvent extends BaseEvent {

	private EagleProbeEvent probe;

	public RejectedProbeEvent(int time, EagleProbeEvent probe) {
		super(time + Configuration.getConfig().getNetworkDelay());
		this.probe = probe;
	}

	@Override
	public void run() {

		EagleCentralizedScheduler.updateState(probe.getState());

		int idx = (Configuration.getTotalWorkers() * 80) / 100;

		Random r = new Random();

		int workerId = r.nextInt(Configuration.getTotalWorkers() - idx - 1);

		probe.setWorker(Workers.getWorkers()[workerId]);

		probe.setState(EagleCentralizedScheduler.getState());

		probe.setTime(time + Configuration.getConfig().getNetworkDelay());

		Simulation.addEvent(probe);

	}
}
