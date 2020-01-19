package usyd.peacock.nodemonitor;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import usyd.distributed.scheduler.peacock.thrift.ProbesRotationServices;
import usyd.distributed.scheduler.peacock.thrift.SchedulerServices;
import usyd.distributed.scheduler.peacock.thrift.TFullProbe;
import usyd.distributed.scheduler.peacock.thrift.TGlobalStateInfo;
import usyd.distributed.scheduler.peacock.thrift.THostPort;
import usyd.distributed.scheduler.peacock.thrift.TRotateProbesRequest;
import usyd.peacock.common.ClientConnector;
import usyd.peacock.common.SchedulerMessage;
import usyd.peacock.nodemonitor.ReconnectingClient.FailureCallback;
import usyd.peacock.util.LogUtil;
import usyd.peacock.util.Network;

public class ProbeRotationService implements InternalConcurrentService, FailureCallback {

	private PeacockQueue queue;

	private ScheduledExecutorService probeRotationService = null;

	private Queue<THostPort> neighbors = null;

	private SharedState sharedState = null;

	private int rotationInterval = 0;

	private ProbesRotationServices.Iface proxy = null;

	private String schedulers = null;

	private ClientConnector<SchedulerServices.AsyncClient, SchedulerMessage> schedulerConnector;

	public ProbeRotationService(PeacockQueue queue, Queue<THostPort> neighbors, SharedState sharedState,
			int rotationInterval, String schedulers,
			ClientConnector<SchedulerServices.AsyncClient, SchedulerMessage> schedulerConnector) {

		this.queue = queue;

		this.neighbors = neighbors;

		this.sharedState = sharedState;

		this.rotationInterval = rotationInterval;

		this.schedulers = schedulers;

		this.schedulerConnector = schedulerConnector;
	}

	@Override
	public void run() {

		probeRotationService = Executors.newScheduledThreadPool(1, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "probeRotationService");
			}
		});

		probeRotationService.scheduleAtFixedRate(() -> {

			try {

				Optional<TGlobalStateInfo> state = sharedState.getToForwardGlobalStateInfo();

				ArrayList<TFullProbe> rotatingProbes = queue.getRotatingProbes();

				if (!rotatingProbes.isEmpty() || state.isPresent()) {

					if (proxy == null)
						proxy = ReconnectingClient.wrap(ProbesRotationServices.Client.class,
								new ReconnectingClient.Options(5, 1000), neighbors, Integer.MAX_VALUE, this);

					proxy.rotateProbes(
							new TRotateProbesRequest(rotatingProbes, state.isPresent() ? state.get() : null));

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}, 0, rotationInterval, TimeUnit.MILLISECONDS);

	}

	@Override
	public void stop() {
		probeRotationService.shutdown();
	}

	private void unRegister(String failedNodeMonitorAddress) {

		try {

			String[] schedulerList = schedulers.split(",");

			List<String> schedulers = Arrays.asList(schedulerList);

			schedulers.stream().forEach((socketStr) -> {

				Optional<InetSocketAddress> schedulerSocket = Network.strToSocket(socketStr);

				if (schedulerSocket.isPresent()) {

					schedulerConnector.sendMessage(
							new UnRegisterNodeMonitorMessage(schedulerSocket.get(), failedNodeMonitorAddress));

				}
			});

		} catch (Exception ex) {

			LogUtil.logFunctionCall("Error UnRegistering Node Monitor listening sockets ");

			ex.printStackTrace();

			System.exit(-1);

		}

		LogUtil.logFunctionCall("node monitor has been unregistered");

	}

	@Override
	public void onFailure(String failedNodeAddr) {
		unRegister(failedNodeAddr);
	}

}