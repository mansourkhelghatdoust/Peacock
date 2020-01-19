package usyd.peacock.scheduler;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import usyd.distributed.scheduler.peacock.thrift.TEnqueueProbeRequest;
import usyd.distributed.scheduler.peacock.thrift.TFullProbe;
import usyd.distributed.scheduler.peacock.thrift.TGlobalStateInfo;
import usyd.distributed.scheduler.peacock.thrift.THostPort;
import usyd.distributed.scheduler.peacock.thrift.TSchedulingRequest;
import usyd.peacock.util.LogUtil;

public class FairProbesAllocator implements ProbeAllocator {

	HashMap<InetSocketAddress, TEnqueueProbeRequest> probesReqs = new HashMap<InetSocketAddress, TEnqueueProbeRequest>();

	Iterator<Entry<InetSocketAddress, TEnqueueProbeRequest>> iterator = null;

	public FairProbesAllocator(String hostIPAddr, TSchedulingRequest request, List<InetSocketAddress> workerNodesCount,
			SharedState sharedState, int listeningPort, boolean immediateExecution) {
		allocate(hostIPAddr, request, workerNodesCount, sharedState, listeningPort, immediateExecution);
	}

	private void allocate(String hostIPAddr, TSchedulingRequest request, List<InetSocketAddress> workerNodesCount,
			SharedState sharedState, int listeningPort, boolean immediateExecution) {

		try {

			ArrayList<Integer> indexes = getShuffledIndexes(workerNodesCount);

			HashMap<InetSocketAddress, TFullProbe> probes = new HashMap<InetSocketAddress, TFullProbe>();

			String[] keys = request.tasks.keySet().toArray(new String[0]);

			for (int i = 0; i < request.tasks.size(); i++) {

				InetSocketAddress nodeMonitor = workerNodesCount.get(indexes.get(i % workerNodesCount.size()));

				if (probes.containsKey(nodeMonitor)) {

					TFullProbe fullTaskId = probes.get(nodeMonitor);
					fullTaskId.taskIds = fullTaskId.taskIds.concat("," + keys[i]);

				} else {

					TFullProbe fullProbe = new TFullProbe("" + keys[i], request.jobId, request.estimatedDuration,
							request.getMaximumWaitingTime(), request.getEnterTime(),
							new THostPort(hostIPAddr, listeningPort), immediateExecution);

					probes.put(nodeMonitor, fullProbe);
				}

			}

			TGlobalStateInfo sharedInfo = new TGlobalStateInfo(sharedState.getQueueSize(), sharedState.getAvgWorkload(),
					-1);

			for (Entry<InetSocketAddress, TFullProbe> entry : probes.entrySet()) {

				TEnqueueProbeRequest probeReq = new TEnqueueProbeRequest(entry.getValue(), sharedInfo);

				probesReqs.put(entry.getKey(), probeReq);
			}

		} catch (Exception e) {

			LogUtil.logFunctionCall("Error in submitting Probes " + e.getMessage() + " ");

			e.printStackTrace();

		}

	}

	public boolean hasNext() {

		if (iterator == null)
			iterator = probesReqs.entrySet().iterator();
		return iterator.hasNext();

	}

	public Map.Entry<InetSocketAddress, TEnqueueProbeRequest> next() {

		if (iterator == null)
			iterator = probesReqs.entrySet().iterator();
		return iterator.next();

	}

	private ArrayList<Integer> getShuffledIndexes(List<InetSocketAddress> workerNodesCount) {

		ArrayList<Integer> shuffledIdx = new ArrayList<Integer>();

		for (int i = 0; i < workerNodesCount.size(); i++)
			shuffledIdx.add(i);

		Collections.shuffle(shuffledIdx);

		return shuffledIdx;

	}

}