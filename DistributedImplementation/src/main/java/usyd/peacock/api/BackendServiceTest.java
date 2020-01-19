package usyd.peacock.api;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;

import usyd.peacock.common.FunctionCallBack;
import usyd.peacock.nodemonitor.NodeMonitorDaemon;
import usyd.peacock.util.LogUtil;
import usyd.peacock.util.TServers;
import usyd.peacock.util.ThriftClientPool;

import usyd.distributed.scheduler.peacock.thrift.BackendServices;
import usyd.distributed.scheduler.peacock.thrift.NodeMonitorServices;
import usyd.distributed.scheduler.peacock.thrift.TLightProbe;
import usyd.distributed.scheduler.peacock.thrift.TTaskExecutionResult;

public class BackendServiceTest implements BackendServices.Iface {

	private final static Logger LOG = Logger.getLogger(NodeMonitorDaemon.class);

	private ThriftClientPool<NodeMonitorServices.AsyncClient> nodeMonitorClientPool = new ThriftClientPool<NodeMonitorServices.AsyncClient>(
			new ThriftClientPool.NodeMonitorServicesFactory());

	public void launchServers(InetSocketAddress nodeMonitor, int listeningPort) {

		try {

			LogUtil.logFunctionCall(nodeMonitor, listeningPort);

			NodeMonitorServices.AsyncClient client = nodeMonitorClientPool.borrowClient(nodeMonitor);

			client.registerBackend("" + listeningPort,
					new FunctionCallBack<Boolean>(nodeMonitorClientPool, nodeMonitor, client));

			BackendServices.Processor<BackendServices.Iface> backendProcessor = new BackendServices.Processor<BackendServices.Iface>(
					this);

			TServers.launchThreadedThriftServer("Backend Server", listeningPort, 1, 1, backendProcessor);

		} catch (IOException e) {

			LOG.error("Error Launching Node Monitor listening sockets ");

			e.printStackTrace();

			System.exit(-1);

		} catch (Exception e) {

			LOG.error("Error in registering back end");

			e.printStackTrace();

			System.exit(-1);
		}

	}

	@Override
	public TTaskExecutionResult launchTask(ByteBuffer message, TLightProbe probe) throws TException {

		LogUtil.logFunctionCall(message, toString());

		try {

			long estimationDuration = message.getLong();

			System.out.println("Executing service with duration " + estimationDuration);

			Thread.sleep(estimationDuration * 1000);

			return new TTaskExecutionResult(2, message);

		} catch (Exception e) {

			e.printStackTrace();

			return new TTaskExecutionResult(0, message);

		}

	}

}
