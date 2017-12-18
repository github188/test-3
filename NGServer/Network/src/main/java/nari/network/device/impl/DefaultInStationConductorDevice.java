package nari.network.device.impl;

import nari.network.device.Conductor;
import nari.network.device.Station;

public class DefaultInStationConductorDevice
	extends DefaultInStationDevice implements Conductor {

	public DefaultInStationConductorDevice(int modelId, int oid,
										   int voltageLevel, long[] connectionNodes, Station station) {
		super(modelId, oid, voltageLevel, connectionNodes, station);
	}

	@Override
    public boolean isConductor() {
    	return true;
    }
}
