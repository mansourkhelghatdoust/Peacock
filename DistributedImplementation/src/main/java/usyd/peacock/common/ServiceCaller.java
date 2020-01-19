package usyd.peacock.common;

import org.apache.thrift.TException;
import org.apache.thrift.async.TAsyncClient;

import usyd.peacock.util.ThriftClientPool;


public interface ServiceCaller<T extends TAsyncClient ,U extends Message> {

	@SuppressWarnings("rawtypes")
	public void doCallService(T client, U message, ThriftClientPool clientPool) throws TException;
}
