package nari.network.device.impl;

import nari.network.device.Transformer;

public class DefaultTransformerDevice
	extends DefaultTopoDevice implements Transformer {

	public DefaultTransformerDevice(int modelId, int oid, int voltageLevel,
                                    long[] connectionNodes) {
		super(modelId, oid, voltageLevel, connectionNodes);
	}

	@Override
    public boolean isTransformer() {
    	return true;
    }
}
