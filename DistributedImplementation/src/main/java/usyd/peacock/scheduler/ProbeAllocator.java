package usyd.peacock.scheduler;

import java.net.InetSocketAddress;
import java.util.Map;

import usyd.distributed.scheduler.peacock.thrift.TEnqueueProbeRequest;

public interface ProbeAllocator {

	boolean hasNext();

	Map.Entry<InetSocketAddress, TEnqueueProbeRequest> next();
}
