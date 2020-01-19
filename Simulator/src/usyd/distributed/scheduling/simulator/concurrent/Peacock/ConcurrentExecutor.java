package usyd.distributed.scheduling.simulator.concurrent.Peacock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import usyd.distributed.scheduling.simulator.Configuration;
import usyd.distributed.scheduling.simulator.concurrent.Peacock.BaseService.IRandomRunnable;
import usyd.distributed.scheduling.simulator.events.Event;

public class ConcurrentExecutor {

	private ExecutorService[] fifoTaskExecutors = new ExecutorService[Configuration.fifoParallelismDegree];
	private ExecutorService randomTaskExecutors = new ThreadPoolExecutor(5, Configuration.parallelismDegree, 5,
			TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

	private static final ConcurrentExecutor parallelExecutor = new ConcurrentExecutor();

	private ConcEventsSynchronizer seqSynch = new ConcEventsSynchronizer();

	private ConcurrentExecutor() {
		initialize();
	}

	private static ConcurrentExecutor getInstance() {
		return parallelExecutor;
	}

	public static void execute(Runnable event) {
		getInstance().doExecute(event);
	}

	private void doExecute(Runnable event) {

		if (event instanceof IRandomRunnable) {
			randomTaskExecutors.execute(event);
		} else if (event instanceof Event) {
			fifoTaskExecutors[getExecutor(event)].execute(event);
		} else {
			System.out.println("Wrong input parameter, terminating...");
		}

	}

	private int getExecutor(Runnable event) {

		int slotsCnt = Configuration.getTotalWorkers() / Configuration.fifoParallelismDegree;

		int idx = ((Event) event).getWorker().getId() / slotsCnt;

		return idx > 0 ? idx : 0;
	}

	public ConcEventsSynchronizer getConcSynch() {
		return seqSynch;
	}

	public void setConcSynch(ConcEventsSynchronizer seqSynch) {
		this.seqSynch = seqSynch;
	}

	public static synchronized void concEventsExecute(Runnable event) {
		getInstance().doExecute(event);
		getInstance().getConcSynch().acquire();
	}

	public static void notifyConcEventsExecute() {
		try {
			getInstance().getConcSynch().release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void waitForConcEventsExecute() {
		try {
			getInstance().getConcSynch().waitForConcEventsExecute();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void terminate() {

		for (ExecutorService service : getInstance().fifoTaskExecutors) {
			service.shutdown();
			try {
				service.awaitTermination(0, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		getInstance().randomTaskExecutors.shutdown();
		try {
			getInstance().randomTaskExecutors.awaitTermination(0, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public class ConcEventsSynchronizer {

		private int numOfEvents = 0;

		public ConcEventsSynchronizer() {

		}

		private synchronized void acquire() {
			numOfEvents++;
		}

		public synchronized void release() throws InterruptedException {
			numOfEvents--;
			notify();
		}

		public synchronized void waitForConcEventsExecute() throws InterruptedException {

			while (numOfEvents > 0)
				wait();
		}
	}

	public static void reset() {
		getInstance().initialize();
	}

	private void initialize() {

		for (int i = 0; i < fifoTaskExecutors.length; i++)
			fifoTaskExecutors[i] = new ThreadPoolExecutor(1, 1, Long.MAX_VALUE, TimeUnit.HOURS,
					new LinkedBlockingQueue<Runnable>());

		randomTaskExecutors = new ThreadPoolExecutor(5, Configuration.parallelismDegree, 5, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>());
	}

}
