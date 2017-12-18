package org.Invoker.rpc.registry;

public interface StateListener {

	int DISCONNECTED = 0;

	int CONNECTED = 1;

	int RECONNECTED = 2;

	public void stateChanged(int connected);
}
