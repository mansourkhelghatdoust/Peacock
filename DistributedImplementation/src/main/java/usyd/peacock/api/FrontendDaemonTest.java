
package usyd.peacock.api;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.HashMap;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;

import usyd.distributed.scheduler.peacock.thrift.FrontendServices;
import usyd.distributed.scheduler.peacock.thrift.SchedulerServices;
import usyd.distributed.scheduler.peacock.thrift.SchedulerServices.AsyncClient;
import usyd.distributed.scheduler.peacock.thrift.TSchedulingRequest;
import usyd.peacock.common.FunctionCallBack;
import usyd.peacock.util.TServers;
import usyd.peacock.util.ThriftClientPool;

public class FrontendDaemonTest {

	public final static Level DEFAULT_LOG_LEVEL = Level.DEBUG;

	public static void main(String[] args) throws IOException {

		FrontendDaemonTest frontEndDaemon = new FrontendDaemonTest();

		BasicConfigurator.configure();

		TServers.launchThreadedThriftServer("FrontendServer", 5000, 1, 1,
				new FrontendServices.AsyncProcessor<FrontendServices.AsyncIface>(new FrontendService()));

		frontEndDaemon.initialize();

	}

	public void initialize() {

		try {

			ThriftClientPool<SchedulerServices.AsyncClient> schedulerClientPool = new ThriftClientPool<SchedulerServices.AsyncClient>(
					new ThriftClientPool.SchedulerServicesFactory());

			AsyncClient client = schedulerClientPool.borrowClient(new InetSocketAddress("127.0.0.1", 4001));

			client.submitJob(createJob(schedulerClientPool, 3, 10, "111111", "127.0.0.1:5000"),
					new FunctionCallBack<>(schedulerClientPool, new InetSocketAddress("127.0.0.1", 5000), client));

			Thread.sleep(3000);

			client = schedulerClientPool.borrowClient(new InetSocketAddress("127.0.0.1", 4001));

			client.submitJob(createJob(schedulerClientPool, 5, 15, "222222", "127.0.0.1:5000"),
					new FunctionCallBack<>(schedulerClientPool, new InetSocketAddress("127.0.0.1", 5000), client));

			Thread.sleep(3000);

			client = schedulerClientPool.borrowClient(new InetSocketAddress("127.0.0.1", 4001));

			client.submitJob(createJob(schedulerClientPool, 7, 5, "333333", "127.0.0.1:5000"),
					new FunctionCallBack<>(schedulerClientPool, new InetSocketAddress("127.0.0.1", 5000), client));

			Thread.sleep(3000);

			client = schedulerClientPool.borrowClient(new InetSocketAddress("127.0.0.1", 4001));

			client.submitJob(createJob(schedulerClientPool, 9, 8, "444444", "127.0.0.1:5000"),
					new FunctionCallBack<>(schedulerClientPool, new InetSocketAddress("127.0.0.1", 5000), client));

			Thread.sleep(300000);

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public static TSchedulingRequest createJob(ThriftClientPool<SchedulerServices.AsyncClient> schedulerClientPool,
			int taskCount, int estimatedDuration, String jobId, String frontendAddress) throws Exception {

		HashMap<String, ByteBuffer> tasks = new HashMap<String, ByteBuffer>();

		for (int i = 0; i < taskCount; i++) {

			ByteBuffer task = ByteBuffer.allocate(8);

			task.putLong(estimatedDuration);

			tasks.put(i + "", task);

			task.position(0);

		}

		return new TSchedulingRequest(tasks, estimatedDuration, jobId, frontendAddress, -1, System.currentTimeMillis());

	}

}