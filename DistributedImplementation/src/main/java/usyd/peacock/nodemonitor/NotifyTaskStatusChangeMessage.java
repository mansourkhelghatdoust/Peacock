package usyd.peacock.nodemonitor;

import java.net.InetSocketAddress;

import usyd.distributed.scheduler.peacock.thrift.TLightProbe;
import usyd.distributed.scheduler.peacock.thrift.TTaskExecutionResult;
import usyd.peacock.common.SchedulerMessage;

public class NotifyTaskStatusChangeMessage extends SchedulerMessage {

	private TLightProbe probe;
	private TTaskExecutionResult executionResult;

	public NotifyTaskStatusChangeMessage(InetSocketAddress socket, TLightProbe probe,
			TTaskExecutionResult executionResult) {
		super(socket);
		this.probe = probe;
		this.executionResult = executionResult;
	}

	public TLightProbe getProbe() {
		return probe;
	}

	public TTaskExecutionResult getExecutionResult() {
		return executionResult;
	}

}
