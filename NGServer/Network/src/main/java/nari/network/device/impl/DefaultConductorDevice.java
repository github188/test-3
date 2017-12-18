package nari.network.device.impl;

import nari.network.device.Conductor;

public class DefaultConductorDevice
	extends DefaultTopoDevice implements Conductor {

	public DefaultConductorDevice(int modelId, int oid, int voltageLevel,
								  long[] connectionNodes) {
		super(modelId, oid, voltageLevel, connectionNodes);
	}

	@Override
    public boolean isConductor() {
    	return true;
    }
}
