package usyd.peacock.nodemonitor;

import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

import usyd.distributed.scheduler.peacock.thrift.TGlobalStateInfo;

public class SharedState {

	private ReentrantLock lock = new ReentrantLock(true);

	private int queueSize;

	private long avgWorkload;

	private long updateTime;

	private long sentTime;

	private PeacockQueue queue;

	public SharedState(PeacockQueue queue) {
		this.queue = queue;
	}

	public void updateGlobalState(TGlobalStateInfo globalState) {

		lock.lock();

		if (globalState != null) {

			if (globalState.time > updateTime) {

				updateTime = globalState.time;

				queueSize = globalState.getQueueSize();

				avgWorkload = globalState.getWorkLoad();

				queue.filter();
			}
		}

		lock.unlock();

	}

	public Optional<TGlobalStateInfo> getToForwardGlobalStateInfo() {

		try {
			lock.lock();

			if (sentTime >= updateTime)
				return Optional.empty();

			sentTime = System.currentTimeMillis();

			return Optional.of(new TGlobalStateInfo(queueSize, avgWorkload, updateTime));

		} finally {

			lock.unlock();

		}
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public int getQueueSize() {
		return queueSize;
	}

	public long getAvgWorkload() {
		return avgWorkload;
	}
}
