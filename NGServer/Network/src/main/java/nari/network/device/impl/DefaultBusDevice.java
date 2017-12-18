package nari.network.device.impl;

import nari.network.device.Bus;
import nari.network.device.Station;

public class DefaultBusDevice
	extends DefaultInStationDevice implements Bus {

	public DefaultBusDevice(int modelId, int oid, int voltageLevel,
							long[] connectionNodes, Station station) {
		super(modelId, oid, voltageLevel, connectionNodes, station);
	}

	@Override
    public boolean isBus() {
    	return true;
    }
}
