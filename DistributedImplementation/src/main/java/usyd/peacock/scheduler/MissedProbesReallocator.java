package usyd.peacock.scheduler;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import usyd.distributed.scheduler.peacock.thrift.TEnqueueProbeRequest;
import usyd.distributed.scheduler.peacock.thrift.TSchedulingRequest;
import usyd.peacock.nodemonitor.InternalConcurrentService;
import usyd.peacock.util.LogUtil;

public class MissedProbesReallocator implements InternalConcurrentService {

	private Scheduler scheduler;

	private ScheduledExecutorService missedProbesReAllocatorService = null;

	private String hostIPAddr = null;

	private List<String> suspectedMissedProbes = new ArrayList<String>();

	public MissedProbesReallocator(String hostIPAddr, Scheduler scheduler) {
		super();
		this.scheduler = scheduler;
		this.hostIPAddr = hostIPAddr;
	}

	public void run() {

		missedProbesReAllocatorService = Executors.newScheduledThreadPool(1, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "MissedProbesReAllocator");
			}
		});

		missedProbesReAllocatorService.scheduleAtFixedRate(() -> {

			for (TSchedulingRequest request : scheduler.scheduledJobs().getScheduledJobs().values()) {

				if (jobShouldBeReSubmitted(request)) {

					FairProbesAllocator allocator = new FairProbesAllocator(hostIPAddr, request,
							scheduler.getNodeMonitorSockets(), scheduler.getSharedState(), scheduler.getListeningPort(),
							true);

					while (allocator.hasNext()) {

						Map.Entry<InetSocketAddress, TEnqueueProbeRequest> entry = allocator.next();

						filterTasks(entry.getValue());

						scheduler.getNodeMonitorConnector()
								.sendMessage(new EnqueueProbeRequestMessage(entry.getKey(), entry.getValue()));

						suspectedMissedProbes.add(entry.getValue().probe.jobId + "  " + entry.getValue().probe.taskIds);

					}

				}

			}

			if (!suspectedMissedProbes.isEmpty()) {
				LogUtil.logInfo(suspectedMissedProbes, "reallocate.txt");
				suspectedMissedProbes.clear();
			}

		}, 0, 5, TimeUnit.SECONDS);

	}

	private void filterTasks(TEnqueueProbeRequest entry) {

		CopyOnWriteArrayList<String> taskIds = new CopyOnWriteArrayList<String>(
				entry.getProbe().getTaskIds().split(","));

		for (String taskId : taskIds) {

			if (scheduler.scheduledJobs().taskCanBeResubmitted(entry.probe.jobId, taskId))
				scheduler.scheduledJobs().clearExecutingTask(entry.probe.jobId, taskId);
			else
				taskIds.remove(taskId);

		}

		entry.probe.taskIds = String.join(",", taskIds);
	}

	private boolean jobShouldBeReSubmitted(TSchedulingRequest request) {

		if (request.enterTime + request.estimatedDuration + request.maximumWaitingTime < System.currentTimeMillis()) {
			
			request.maximumWaitingTime += scheduler.getSharedState().getAvgWorkload();
			
			return true;
		}

		return false;
	}

	public void stop() {
		missedProbesReAllocatorService.shutdown();
	}
}