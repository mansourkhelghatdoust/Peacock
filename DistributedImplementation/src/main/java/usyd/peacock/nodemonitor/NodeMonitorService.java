package usyd.peacock.nodemonitor;

import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;

import usyd.distributed.scheduler.peacock.thrift.NodeMonitorServices;
import usyd.distributed.scheduler.peacock.thrift.ProbesRotationServices;
import usyd.distributed.scheduler.peacock.thrift.SchedulerServices.AsyncClient;
import usyd.distributed.scheduler.peacock.thrift.TEnqueueProbeRequest;
import usyd.distributed.scheduler.peacock.thrift.THostPort;
import usyd.distributed.scheduler.peacock.thrift.TRotateProbesRequest;
import usyd.peacock.common.ClientConnector;
import usyd.peacock.common.SchedulerMessage;
import usyd.peacock.util.LogUtil;

public class NodeMonitorService implements NodeMonitorServices.AsyncIface, ProbesRotationServices.Iface {

	private NodeMonitor nodeMonitor = null;

	public NodeMonitorService(String hostIPAddr, String schedulers, Integer getTaskPort,
			ClientConnector<AsyncClient, SchedulerMessage> schedulerConnector, Integer listeningPort,
			InetSocketAddress neighbor, InetSocketAddress[] successors, int rotationInterval) {

		Queue<THostPort> neighbors = new LinkedList<THostPort>();

		neighbors.add(new THostPort(neighbor.getHostName(), neighbor.getPort()));

		for (InetSocketAddress addr : successors)
			neighbors.add(new THostPort(addr.getHostName(), addr.getPort()));

		nodeMonitor = new NodeMonitor(hostIPAddr, schedulers, getTaskPort, schedulerConnector, listeningPort, neighbors,
				rotationInterval);

	}

	@Override
	public void registerBackend(String listenSocket, AsyncMethodCallback<Boolean> resultHandler) throws TException {

		LogUtil.logFunctionCall(listenSocket);

		nodeMonitor.registerBackend(listenSocket);

		resultHandler.onComplete(true);

	}

	@Override
	public void enqueueTaskProbes(TEnqueueProbeRequest request, AsyncMethodCallback<Boolean> resultHandler)
			throws TException {

		LogUtil.logFunctionCall(request);

		nodeMonitor.enqueueTaskProbes(request);

		resultHandler.onComplete(true);

	}

	@Override
	public void rotateProbes(TRotateProbesRequest request) throws TException {

		LogUtil.logFunctionCall(request);

		nodeMonitor.rotateProbes(request);

	}

}