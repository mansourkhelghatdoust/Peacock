package usyd.peacock.common;

import org.apache.thrift.TException;

public interface ClientConnectorErrorCallback {

	void onError(TException exception);

}
