package usyd.distributed.scheduling.simulator;

import java.util.concurrent.PriorityBlockingQueue;

import usyd.distributed.scheduling.simulator.concurrent.Peacock.ConcurrentExecutor;
import usyd.distributed.scheduling.simulator.events.Event;
import usyd.distributed.scheduling.simulator.events.Peacock.HopTaskEvent;
import usyd.distributed.scheduling.simulator.events.Peacock.PeacockJobArrivalEvent;
import usyd.distributed.scheduling.simulator.events.Peacock.PeacockWorker;
import usyd.distributed.scheduling.simulator.events.eagle.EagleCentralizedScheduler;
import usyd.distributed.scheduling.simulator.events.eagle.EagleJobArrivalEvent;
import usyd.distributed.scheduling.simulator.events.eagle.EagleWorker;
import usyd.distributed.scheduling.simulator.events.hawk.HawkCentralizedScheduler;
import usyd.distributed.scheduling.simulator.events.hawk.HawkJobArrivalEvent;
import usyd.distributed.scheduling.simulator.events.hawk.HawkWorker;
import usyd.distributed.scheduling.simulator.events.sparrow.SparrowJobArrivalEvent;
import usyd.distributed.scheduling.simulator.events.sparrow.SparrowWorker;
import usyd.distributed.scheduling.simulator.observers.SimulationObserver;

public class Simulation {

	private static final Simulation simulation = new Simulation();

	private PriorityBlockingQueue<Event> eventsQueue = new PriorityBlockingQueue<Event>(1, new Event.EventComparator());

	private Simulation() {

	}

	public static Simulation instance() {
		return simulation;
	}

	public void run() throws Exception {

		SimulationObserver.simulationStarted();

		addInitialEvent();

		while (!eventsQueue.isEmpty()) {

			Event event = eventsQueue.poll();
			SimulationUtil.setCurrentTime(event.getTime());
			event.run();

		}

		ConcurrentExecutor.terminate();

		SimulationObserver.printState();

		SimulationObserver.simulationEnded();

	}

	public static Event getEvent() {
		return simulation.eventsQueue.poll();
	}

	public static void addEvent(Event event) {
		if (event != null)
			simulation.eventsQueue.add(event);
	}

	private void addInitialEvent() {

		if (Configuration.getAlgorithmType().equals(SparrowJobArrivalEvent.class)) {

			SparrowWorker.setWorkers();
			Simulation.addEvent(new SparrowJobArrivalEvent(0, Configuration.getInterArrivalDelay()));

		} else if (Configuration.getAlgorithmType().equals(PeacockJobArrivalEvent.class)) {

			PeacockWorker.setWorkers();
			Simulation.addEvent(new PeacockJobArrivalEvent(0, Configuration.getInterArrivalDelay()));
			Simulation.addEvent(new HopTaskEvent(0));

		} else if (Configuration.getAlgorithmType().equals(HawkJobArrivalEvent.class)) {

			HawkWorker.setWorkers();
			HawkCentralizedScheduler.initialize();
			Simulation.addEvent(new HawkJobArrivalEvent(0, Configuration.getInterArrivalDelay()));

		} else if (Configuration.getAlgorithmType().equals(EagleJobArrivalEvent.class)) {

			EagleWorker.setWorkers();
			EagleCentralizedScheduler.initialize();
			Simulation.addEvent(new EagleJobArrivalEvent(0, Configuration.getInterArrivalDelay()));

		} else {

			System.out.println("JobArrivalEvent is not valid. Terminating ...");
			System.exit(-1);

		}

	}

	public static void main(String[] args) throws Exception {

		System.out.println("Test updated");
		Configuration.setUP(Integer.parseInt(args[0]), 1, 0.005, Double.parseDouble(args[1]),
				PeacockJobArrivalEvent.class);
		Simulation.instance().run();

	}
}