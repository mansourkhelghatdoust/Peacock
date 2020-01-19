package usyd.peacock.scheduler;

import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import usyd.distributed.scheduler.peacock.thrift.TGlobalStateInfo;
import usyd.distributed.scheduler.peacock.thrift.TGlobalStateUpdateRequest;
import usyd.distributed.scheduler.peacock.thrift.TSchedulingRequest;
import usyd.peacock.util.LogUtil;

public class SharedState {

	private final static Logger LOG = Logger.getLogger(SharedState.class);

	private ReentrantLock lock = new ReentrantLock(true);

	private int numberOfActiveTasks;

	private long estimationExecutionTimeOfActiveTasks;

	private int queueSize;

	private long avgWorkload;

	private int numberOfWorkerNodes;

	public SharedState() {

	}

	public int getQueueSize() {
		return queueSize;
	}

	public long getAvgWorkload() {
		return avgWorkload;
	}

	public void updateGlobalInfo(TSchedulingRequest request) {

		lock.lock();

		try {

			numberOfActiveTasks += request.tasks.size();
			estimationExecutionTimeOfActiveTasks += (request.getEstimatedDuration() * request.tasks.size());

			updateQueueSize();

			updateAvgWorkload();

		} catch (Exception ex) {

			LOG.error("Error in updating global Info, but continue executing");

			ex.printStackTrace();

		} finally {

			LogUtil.logFunctionCall(numberOfActiveTasks, estimationExecutionTimeOfActiveTasks, queueSize, avgWorkload);

			lock.unlock();

		}
	}

	public void updateGlobalInfo() {

		lock.lock();

		try {

			updateQueueSize();

			updateAvgWorkload();

		} catch (Exception ex) {

			LOG.error("Error in updating global Info sent by another scheduler, but continue executing");

			ex.printStackTrace();

		} finally {

			LogUtil.logFunctionCall(numberOfActiveTasks, estimationExecutionTimeOfActiveTasks, queueSize, avgWorkload);

			lock.unlock();

		}

	}

	public void updateGlobalInfo(TGlobalStateUpdateRequest request) {

		lock.lock();

		try {

			if (request.isAdded()) {

				numberOfActiveTasks += request.numTasks;
				estimationExecutionTimeOfActiveTasks += (request.avgExecutionTime * request.numTasks);

			} else {

				numberOfActiveTasks -= request.numTasks;
				estimationExecutionTimeOfActiveTasks -= (request.avgExecutionTime * request.numTasks);

			}

			updateQueueSize();

			updateAvgWorkload();

		} catch (Exception ex) {

			LOG.error("Error in updating global Info sent by another scheduler, but continue executing");

			ex.printStackTrace();

		} finally {

			LogUtil.logFunctionCall(numberOfActiveTasks, estimationExecutionTimeOfActiveTasks, queueSize, avgWorkload);

			lock.unlock();

		}

	}

	public TGlobalStateInfo getGlobalStateInfo() {
		return new TGlobalStateInfo(queueSize, avgWorkload, -1);
	}

	private void updateAvgWorkload() {

		if (numberOfWorkerNodes == 0)
			return;

		avgWorkload = (long) Math.ceil(estimationExecutionTimeOfActiveTasks / numberOfWorkerNodes);
		avgWorkload = avgWorkload < 0 ? 0 : avgWorkload;

	}

	private void updateQueueSize() {

		if (numberOfWorkerNodes == 0)
			return;

		int minWaitingTasks = numberOfActiveTasks - numberOfWorkerNodes;

		queueSize = minWaitingTasks < 0 ? 0 : (int) Math.round((double) minWaitingTasks / numberOfWorkerNodes);

	}

	public void updateGlobalInfo(int numOfTask, int estimatedDurationPerTask) {

		lock.lock();

		numberOfActiveTasks -= numOfTask;
		estimationExecutionTimeOfActiveTasks -= (numOfTask * estimatedDurationPerTask);

		updateQueueSize();

		updateAvgWorkload();

		LogUtil.logFunctionCall(numberOfActiveTasks, estimationExecutionTimeOfActiveTasks, queueSize, avgWorkload);

		lock.unlock();

	}

	public void setNumberOfWorkerNodes(int numberOfWorkerNodes) {
		this.numberOfWorkerNodes = numberOfWorkerNodes;
		updateGlobalInfo();
	}

}
