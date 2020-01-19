package usyd.distributed.scheduling.simulator.events;

import java.util.Comparator;

import usyd.distributed.scheduling.simulator.Worker;

public interface Event extends  Runnable {


	public double getTime();

	public Worker getWorker();

	public void setTime(double time);

	@Override
	public void run();

	public void runConc() throws Exception;

	public class EventComparator implements Comparator<Event> {
		public EventComparator() {

		}

		@Override
		public int compare(Event event1, Event event2) {
			return (int) (event1.getTime() - event2.getTime());
		}
	}
}