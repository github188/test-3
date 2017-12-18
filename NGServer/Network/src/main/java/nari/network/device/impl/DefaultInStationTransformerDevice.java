package nari.network.device.impl;

import nari.network.device.Station;
import nari.network.device.Transformer;

public class DefaultInStationTransformerDevice
	extends DefaultInStationDevice implements Transformer {

	public DefaultInStationTransformerDevice(int modelId, int oid,
                                             int voltageLevel, long[] connectionNodes, Station station) {
		super(modelId, oid, voltageLevel, connectionNodes, station);
	}

	@Override
    public boolean isTransformer() {
    	return true;
    }
}
