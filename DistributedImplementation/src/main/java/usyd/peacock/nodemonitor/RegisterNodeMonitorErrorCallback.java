package usyd.peacock.nodemonitor;

import org.apache.thrift.TException;

import usyd.peacock.common.ClientConnectorErrorCallback;
import usyd.peacock.util.LogUtil;

public class RegisterNodeMonitorErrorCallback implements ClientConnectorErrorCallback {

	private String socketStr = null;
	
	public RegisterNodeMonitorErrorCallback(String socketStr) {
		this.socketStr = socketStr;
	}
	@Override
	public void onError(TException exception) {

		LogUtil.logFunctionCall("Error in Registering node monitor in scheduler " + socketStr + "  retry in 5 seconds");

	}

}
