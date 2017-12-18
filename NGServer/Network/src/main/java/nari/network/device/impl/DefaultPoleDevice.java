package nari.network.device.impl;

import nari.network.device.Pole;

public class DefaultPoleDevice
	extends DefaultTopoDevice implements Pole {

	public DefaultPoleDevice(int modelId, int oid, int voltageLevel,
                             long[] connectionNodes) {
		super(modelId, oid, voltageLevel, connectionNodes);
	}

	@Override
    public boolean isPole() {
    	return true;
    }
}
