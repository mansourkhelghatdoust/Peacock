package usyd.peacock.api;

import java.nio.ByteBuffer;

import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;

import usyd.distributed.scheduler.peacock.thrift.FrontendServices;
import usyd.distributed.scheduler.peacock.thrift.TLightProbe;

public class FrontendService implements FrontendServices.AsyncIface {

	@Override
	public void notifyTaskStatusChange(TLightProbe probe, ByteBuffer message, int status,
			AsyncMethodCallback<Void> resultHandler) throws TException {

		System.out.println("Job " + probe.getJobId() + " Task " + probe.getTaskId() + " executed successfully");
		resultHandler.onComplete(null);

	}

}
