package usyd.distributed.scheduling.simulator.events.Peacock;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import usyd.distributed.scheduling.simulator.Configuration;
import usyd.distributed.scheduling.simulator.Job;
import usyd.distributed.scheduling.simulator.Simulation;
import usyd.distributed.scheduling.simulator.Worker;
import usyd.distributed.scheduling.simulator.concurrent.Peacock.BaseService;
import usyd.distributed.scheduling.simulator.concurrent.Peacock.ConcurrentExecutor;
import usyd.distributed.scheduling.simulator.events.BaseEvent;
import usyd.distributed.scheduling.simulator.events.Event;

public class ProbeEvent extends BaseEvent {

	protected Worker[] workers = null;
	private Job job;

	public ProbeEvent(double time) {
		super(time + Configuration.getConfig().getNetworkDelay());
	}

	public ProbeEvent(double time, Job job, Worker[] workers) {
		this(time);
		this.job = job;
		this.workers = workers;
	}

	@Override
	public void run() {

		ConcurrentHashMap<String, Long> extraInfo = PeacockScheduler.getGlobalInfo();

		for (int i = 0; i < workers.length; i++) {
			workers[i].addProbe(job, time, extraInfo);
		}
	}

	@Override
	public void runConc() throws Exception {

		ConcurrentExecutor.waitForConcEventsExecute();

		// Check if it is coming in the right order

		Event event = Simulation.getEvent();

		Simulation.addEvent(event);

		if (event.getTime() < this.getTime()) {
			Simulation.addEvent(this);
			return;
		}

		////////////////////////////////////////////////////////////////////////
		CountDownLatch latch = new CountDownLatch(workers.length);

		ConcurrentHashMap<String, Long> extraInfo = PeacockScheduler.getGlobalInfo();

		for (int i = 0; i < workers.length; i++)
			ConcurrentExecutor.execute(new ProbeService(workers[i], job, time, extraInfo, latch));

		try

		{
			latch.await();
		} catch (

		InterruptedException e)

		{
			e.printStackTrace();
		}

	}

	public class ProbeService extends BaseService {

		private Job job;
		private ConcurrentHashMap<String, Long> extraInfo;
		private CountDownLatch latch;

		public ProbeService(Worker worker, Job job, double time, ConcurrentHashMap<String, Long> extraInfo) {
			super(time);
			this.worker = worker;
			this.job = job;
			this.extraInfo = extraInfo;
		}

		public ProbeService(Worker worker, Job job, double time, ConcurrentHashMap<String, Long> extraInfo,
				CountDownLatch latch) {
			this(worker, job, time, extraInfo);
			this.latch = latch;
		}

		@Override
		public void run() {

			worker.addProbe(job, time, extraInfo);

			if (latch != null)
				latch.countDown();
		}

	}

}