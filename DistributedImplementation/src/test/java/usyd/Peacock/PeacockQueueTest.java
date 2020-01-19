package usyd.Peacock;

import junit.framework.TestCase;
import usyd.distributed.scheduler.peacock.thrift.TFullProbe;
import usyd.distributed.scheduler.peacock.thrift.TGlobalStateInfo;
import usyd.distributed.scheduler.peacock.thrift.TLocalFullProbe;
import usyd.peacock.nodemonitor.PeacockQueue;
import usyd.peacock.nodemonitor.SharedState;

public class PeacockQueueTest extends TestCase {

	PeacockQueue queue = new PeacockQueue();
	
	protected void setUp() {

		SharedState sharedState = new SharedState(queue);
		queue.setSharedState(sharedState);

	}

	protected void tearDown() {

		queue = new PeacockQueue();

	}

	public PeacockQueueTest(String testName) {
		super(testName);
	}

	public void testFilterQueueSize() {

		queue.getSharedState().updateGlobalState(new TGlobalStateInfo(2, 100000, System.currentTimeMillis()));

		queue.setRunningJob(new TFullProbe("1", "1", 10000, 100000, System.currentTimeMillis(), null, false),
				System.currentTimeMillis());

		assertEquals(true, queue.getRotatingProbes().isEmpty());

		queue.add(new TLocalFullProbe(new TFullProbe("2", "1", 10000, 100000, System.currentTimeMillis(), null, false),
				System.currentTimeMillis(), 0));

		assertEquals(true, queue.getRotatingProbes().isEmpty());

		queue.add(new TLocalFullProbe(new TFullProbe("3", "1", 10000, 100000, System.currentTimeMillis(), null, false),
				System.currentTimeMillis(), 0));

		assertEquals(true, queue.getRotatingProbes().isEmpty());

		queue.add(new TLocalFullProbe(new TFullProbe("4", "1", 10000, 100000, System.currentTimeMillis(), null, false),
				System.currentTimeMillis(), 0));

		assertEquals(false, queue.getRotatingProbes().isEmpty());

	}

	public void testFilterAverageWorkload() {

		queue.getSharedState().updateGlobalState(new TGlobalStateInfo(3, 35000, System.currentTimeMillis()));

		queue.setRunningJob(new TFullProbe("1", "1", 10000, 100000, System.currentTimeMillis(), null, false),
				System.currentTimeMillis());

		assertEquals(true, queue.getRotatingProbes().isEmpty());

		queue.add(new TLocalFullProbe(new TFullProbe("2", "1", 10000, 100000, System.currentTimeMillis(), null, false),
				System.currentTimeMillis(), 0));

		assertEquals(true, queue.getRotatingProbes().isEmpty());

		queue.add(new TLocalFullProbe(new TFullProbe("3", "1", 10000, 100000, System.currentTimeMillis(), null, false),
				System.currentTimeMillis(), 0));

		assertEquals(true, queue.getRotatingProbes().isEmpty());

		queue.add(new TLocalFullProbe(new TFullProbe("4", "1", 10000, 100000, System.currentTimeMillis(), null, false),
				System.currentTimeMillis(), 0));

		assertEquals(false, queue.getRotatingProbes().isEmpty());

	}

	// When there is no running task and queue is empty, adding probe shoud not
	// be marked for rotating
	public void testFilterAddToEmptyQueue() {

		queue.getSharedState().updateGlobalState(new TGlobalStateInfo(1, 35000, System.currentTimeMillis()));

		queue.add(new TLocalFullProbe(new TFullProbe("1", "1", 10, 0, System.currentTimeMillis(), null, false),
				System.currentTimeMillis(), 0));
		queue.add(new TLocalFullProbe(new TFullProbe("2", "1", 10, 0, System.currentTimeMillis(), null, false),
				System.currentTimeMillis(), 0));

		assertEquals(2, queue.size());
	}

	public void testProbeReorderingJobArrivalTime() {

		queue.getSharedState().updateGlobalState(new TGlobalStateInfo(4, 40000, System.currentTimeMillis()));

		queue.setRunningJob(new TFullProbe("1", "1", 10000, 50000, System.currentTimeMillis() - 5000, null, false),
				System.currentTimeMillis());

		queue.add(new TLocalFullProbe(
				new TFullProbe("1", "3", 10000, 50000, System.currentTimeMillis() - 3000, null, false),
				System.currentTimeMillis(), 0));

		queue.add(new TLocalFullProbe(
				new TFullProbe("1", "4", 10000, 50000, System.currentTimeMillis() - 2000, null, false),
				System.currentTimeMillis(), 0));

		queue.add(new TLocalFullProbe(
				new TFullProbe("1", "2", 10000, 50000, System.currentTimeMillis() - 4000, null, false),
				System.currentTimeMillis(), 0));

		assertEquals("2", queue.peek().getFullProbe().jobId);

	}

	public void testLessExecutionTimePriorityForAddingProbe() {

		queue.getSharedState().updateGlobalState(new TGlobalStateInfo(4, 40000, System.currentTimeMillis()));

		queue.setRunningJob(new TFullProbe("1", "1", 10000, 50000, System.currentTimeMillis() - 5000, null, false),
				System.currentTimeMillis());

		queue.add(new TLocalFullProbe(
				new TFullProbe("1", "3", 10000, 50000, System.currentTimeMillis() - 3000, null, false),
				System.currentTimeMillis(), 0));

		queue.add(new TLocalFullProbe(
				new TFullProbe("1", "4", 3000, 50000, System.currentTimeMillis() - 2000, null, false),
				System.currentTimeMillis(), 0));

		queue.add(new TLocalFullProbe(
				new TFullProbe("1", "2", 10000, 50000, System.currentTimeMillis() - 4000, null, false),
				System.currentTimeMillis(), 0));

		assertEquals("4", queue.poll().fullProbe.jobId);
		assertEquals("2", queue.poll().fullProbe.jobId);
		assertEquals("3", queue.poll().fullProbe.jobId);

	}

	public void testExceedingThresholdCurrentProbe() {

		queue.getSharedState().updateGlobalState(new TGlobalStateInfo(4, 40000, System.currentTimeMillis()));

		queue.setRunningJob(new TFullProbe("1", "1", 10000, 50000, System.currentTimeMillis() - 5000, null, false),
				System.currentTimeMillis());

		queue.add(new TLocalFullProbe(
				new TFullProbe("1", "2", 10000, 50000, System.currentTimeMillis() - 4000, null, false),
				System.currentTimeMillis(), 0));

		queue.add(new TLocalFullProbe(
				new TFullProbe("1", "3", 10000, 24000, System.currentTimeMillis() - 3000, null, false),
				System.currentTimeMillis(), 0));

		queue.add(new TLocalFullProbe(
				new TFullProbe("1", "4", 9000, 50000, System.currentTimeMillis() - 2000, null, false),
				System.currentTimeMillis(), 0));

		assertEquals("2", queue.poll().getFullProbe().jobId);
		assertEquals("3", queue.poll().getFullProbe().jobId);
		assertEquals("4", queue.poll().getFullProbe().jobId);

	}

	public void testLessExecutionTimePriorityForCurrentProbe() {

		queue.getSharedState().updateGlobalState(new TGlobalStateInfo(4, 40000, System.currentTimeMillis()));

		queue.setRunningJob(new TFullProbe("1", "1", 10000, 50000, System.currentTimeMillis() - 5000, null, false),
				System.currentTimeMillis());

		queue.add(new TLocalFullProbe(
				new TFullProbe("1", "2", 10000, 50000, System.currentTimeMillis() - 4000, null, false),
				System.currentTimeMillis(), 0));

		queue.add(new TLocalFullProbe(
				new TFullProbe("1", "3", 10000, 50000, System.currentTimeMillis() - 3000, null, false),
				System.currentTimeMillis(), 0));

		queue.add(new TLocalFullProbe(
				new TFullProbe("1", "4", 3000, 50000, System.currentTimeMillis() - 2000, null, false),
				System.currentTimeMillis(), 0));

		assertEquals("4", queue.peek().getFullProbe().jobId);

	}

	public void testExceedingThresholdAddingProbe() {

		queue.getSharedState().updateGlobalState(new TGlobalStateInfo(4, 45000, System.currentTimeMillis()));

		queue.setRunningJob(new TFullProbe("1", "1", 10000, 50000, System.currentTimeMillis() - 5000, null, false),
				System.currentTimeMillis());

		queue.add(new TLocalFullProbe(
				new TFullProbe("1", "3", 10000, 50000, System.currentTimeMillis() - 3000, null, false),
				System.currentTimeMillis(), 0));

		queue.add(new TLocalFullProbe(
				new TFullProbe("1", "4", 10000, 50000, System.currentTimeMillis() - 2000, null, false),
				System.currentTimeMillis(), 0));

		queue.add(new TLocalFullProbe(
				new TFullProbe("1", "2", 11000, 25000, System.currentTimeMillis() - 4000, null, false),
				System.currentTimeMillis(), 0));

		assertEquals("3", queue.poll().fullProbe.jobId);
		assertEquals("2", queue.poll().fullProbe.jobId);
		assertEquals("4", queue.poll().fullProbe.jobId);

	}

	public void testImmediateExecutionAddingProbe() {

		queue.getSharedState().updateGlobalState(new TGlobalStateInfo(4, 45000, System.currentTimeMillis()));

		queue.setRunningJob(new TFullProbe("1", "1", 10000, 50000, System.currentTimeMillis() - 5000, null, false),
				System.currentTimeMillis());

		queue.add(new TLocalFullProbe(
				new TFullProbe("1", "3", 10000, 50000, System.currentTimeMillis() - 3000, null, false),
				System.currentTimeMillis(), 0));

		queue.add(new TLocalFullProbe(
				new TFullProbe("1", "4", 10000, 50000, System.currentTimeMillis() - 2000, null, false),
				System.currentTimeMillis(), 0));

		queue.add(new TLocalFullProbe(
				new TFullProbe("1", "2", 11000, 25000, System.currentTimeMillis() - 4000, null, true),
				System.currentTimeMillis(), 0));

		assertEquals("2", queue.poll().fullProbe.jobId);
		assertEquals("3", queue.poll().fullProbe.jobId);
		assertEquals("4", queue.poll().fullProbe.jobId);

	}

	public void testImmediateExecutionCurrentProbe() {

		queue.getSharedState().updateGlobalState(new TGlobalStateInfo(4, 40000, System.currentTimeMillis()));

		queue.setRunningJob(new TFullProbe("1", "1", 10000, 50000, System.currentTimeMillis() - 5000, null, false),
				System.currentTimeMillis());

		queue.add(new TLocalFullProbe(
				new TFullProbe("1", "2", 10000, 50000, System.currentTimeMillis() - 4000, null, false),
				System.currentTimeMillis(), 0));

		queue.add(new TLocalFullProbe(
				new TFullProbe("1", "3", 10000, 50000, System.currentTimeMillis() - 3000, null, true),
				System.currentTimeMillis(), 0));

		queue.add(new TLocalFullProbe(
				new TFullProbe("1", "4", 9000, 50000, System.currentTimeMillis() - 2000, null, false),
				System.currentTimeMillis(), 0));

		assertEquals("3", queue.poll().getFullProbe().jobId);
		assertEquals("4", queue.poll().getFullProbe().jobId);
		assertEquals("2", queue.poll().getFullProbe().jobId);

	}

	public void testImmediateExecutionBothProbesLessExecution() {

		queue.getSharedState().updateGlobalState(new TGlobalStateInfo(4, 40000, System.currentTimeMillis()));

		queue.setRunningJob(new TFullProbe("1", "1", 10000, 50000, System.currentTimeMillis() - 5000, null, false),
				System.currentTimeMillis());

		queue.add(new TLocalFullProbe(
				new TFullProbe("1", "2", 10000, 50000, System.currentTimeMillis() - 4000, null, false),
				System.currentTimeMillis(), 0));

		queue.add(new TLocalFullProbe(
				new TFullProbe("1", "3", 10000, 50000, System.currentTimeMillis() - 3000, null, true),
				System.currentTimeMillis(), 0));

		queue.add(new TLocalFullProbe(
				new TFullProbe("1", "4", 9000, 50000, System.currentTimeMillis() - 2000, null, true),
				System.currentTimeMillis(), 0));

		assertEquals("3", queue.poll().getFullProbe().jobId);
		assertEquals("4", queue.poll().getFullProbe().jobId);
		assertEquals("2", queue.poll().getFullProbe().jobId);

	}

	public void testImmediateExecutionBothProbesExceedingThreshold() {

		queue.getSharedState().updateGlobalState(new TGlobalStateInfo(4, 40000, System.currentTimeMillis()));

		queue.setRunningJob(new TFullProbe("1", "1", 10000, 50000, System.currentTimeMillis() - 5000, null, false),
				System.currentTimeMillis());

		queue.add(new TLocalFullProbe(
				new TFullProbe("1", "2", 10000, 50000, System.currentTimeMillis() - 4000, null, false),
				System.currentTimeMillis(), 0));

		queue.add(new TLocalFullProbe(
				new TFullProbe("1", "3", 10000, 5000, System.currentTimeMillis() - 3000, null, true),
				System.currentTimeMillis(), 0));

		queue.add(new TLocalFullProbe(
				new TFullProbe("1", "4", 9000, 50000, System.currentTimeMillis() - 2000, null, true),
				System.currentTimeMillis(), 0));

		assertEquals("3", queue.poll().getFullProbe().jobId);
		assertEquals("4", queue.poll().getFullProbe().jobId);
		assertEquals("2", queue.poll().getFullProbe().jobId);

	}

}
