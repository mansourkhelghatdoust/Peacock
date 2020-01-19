package usyd.distributed.scheduling.simulator.events.Peacock;

import java.util.HashSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CountDownLatch;

import usyd.distributed.scheduling.simulator.Configuration;
import usyd.distributed.scheduling.simulator.Job;
import usyd.distributed.scheduling.simulator.Jobs;
import usyd.distributed.scheduling.simulator.Simulation;
import usyd.distributed.scheduling.simulator.Workers;
import usyd.distributed.scheduling.simulator.concurrent.Peacock.BaseService;
import usyd.distributed.scheduling.simulator.concurrent.Peacock.ConcurrentExecutor;
import usyd.distributed.scheduling.simulator.events.BaseEvent;
import usyd.distributed.scheduling.simulator.events.Event;

public class HopTaskEvent extends BaseEvent {

	private boolean movingMode = true;
	private static ConcurrentSkipListSet<Integer> movingWorkersSet = new ConcurrentSkipListSet<Integer>();
	private static ConcurrentSkipListSet<Integer> executeWorkersSet = new ConcurrentSkipListSet<Integer>();

	public HopTaskEvent(double time) {
		super(time);
	}

	public static void reset() {
		movingWorkersSet.clear();
		executeWorkersSet.clear();
	}

	public static void addExecuteWorker(int workerId) {
		executeWorkersSet.add(workerId);
	}

	public static void addMovingWorker(int workerId) {
		movingWorkersSet.add(workerId);
	}

	@Override
	public void run() {

		if (movingMode) {
			move();
			this.setTime(time + Configuration.getConfig().getNetworkDelay() + Configuration.getConfig().getRotationInterval());
		} else {
			execute();
			this.setTime(time);
		}
		movingMode = !movingMode;

		if (Jobs.getJobCompletedCount() < Configuration.googleNumberOfJobs)
			Simulation.addEvent(this);

	}

	@Override
	public void runConc() throws Exception {

		ConcurrentExecutor.waitForConcEventsExecute();

		Event event = Simulation.getEvent();

		if (event != null) {
			Simulation.addEvent(event);

			if (event.getTime() < this.getTime()) {
				Simulation.addEvent(this);
				return;
			}
		}

		if (movingMode) {
			moveConc();
			this.setTime(time + Configuration.getConfig().getNetworkDelay() + Configuration.getConfig().getRotationInterval());
		} else {
			executeConc();
			this.setTime(time);
		}
		movingMode = !movingMode;

		if (Jobs.getJobCompletedCount() < Configuration.googleNumberOfJobs)
			Simulation.addEvent(this);

	}

	private void execute() {

		while (executeWorkersSet.size() > 0)
			new HopTaskExecuteService(time, executeWorkersSet.pollFirst()).run();

	}

	private void move() {
		while (movingWorkersSet.size() > 0)
			new HopTaskMovingService(time, movingWorkersSet.pollFirst()).run();

	}

	private void executeConc() {

		CountDownLatch latch = new CountDownLatch(executeWorkersSet.size());

		while (executeWorkersSet.size() > 0)
			ConcurrentExecutor.execute(new HopTaskExecuteService(time, executeWorkersSet.pollFirst(), latch));

		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private void moveConc() {

		CountDownLatch latch = new CountDownLatch(movingWorkersSet.size());

		while (movingWorkersSet.size() > 0)
			ConcurrentExecutor.execute(new HopTaskMovingService(time, movingWorkersSet.pollFirst(), latch));

		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public class HopTaskExecuteService extends BaseService implements BaseService.IRandomRunnable {

		private PeacockWorker worker;
		private CountDownLatch latch;

		public HopTaskExecuteService(double time, int workerId) {

			super(time);
			worker = Workers.getWorker(PeacockWorker.class, workerId);

		}

		public HopTaskExecuteService(double time, int workerId, CountDownLatch latch) {

			this(time, workerId);
			this.latch = latch;

		}

		@Override
		public void run() {

			HopTaskData data = worker.getHopTaskData();

			data.receiveQueueSize();

			while (!data.getOnTheWayJob().isEmpty()) {

				worker.addProbe(data.getOnTheWayJob().pollFirst(), time, worker.getLastUpdatedGlobalInfo());

			}

			if (latch != null)
				latch.countDown();
		}

	}

	public class HopTaskMovingService extends BaseService implements BaseService.IRandomRunnable {

		private CountDownLatch latch;
		private PeacockWorker worker;
		
		public HopTaskMovingService(double time, int workerId) {
			super(time);
			worker = Workers.getWorker(PeacockWorker.class, workerId);
		}

		public HopTaskMovingService(double time, int workerId, CountDownLatch latch) {
			this(time, workerId);
			this.latch = latch;
		}

		@Override
		public void run() {

			PeacockWorker neighbor = Workers.getWorker(PeacockWorker.class, worker.getNeighbor());

			neighbor.getHopTaskData().updateOnTheFlyGlobalData();

			HopTaskData workerData = worker.getHopTaskData();

			HopTaskData neighborData = neighbor.getHopTaskData();

			Jobs.increaseMove(new HashSet<Job>(workerData.getWaitForMovingJob()));

			while (!workerData.getWaitForMovingJob().isEmpty())
				neighborData.getOnTheWayJob().add(workerData.getWaitForMovingJob().pollFirst());

			HopTaskEvent.addExecuteWorker(neighbor.getId());

			if (latch != null)
				latch.countDown();
		}

	}

}