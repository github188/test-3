package nari.network.device.impl;

import nari.network.device.InStationDevice;
import nari.network.device.Station;

public class DefaultInStationDevice
	extends DefaultTopoDevice implements InStationDevice {
	
	private Station station;

	public DefaultInStationDevice(int modelId, int oid, int voltageLevel,
								  long[] connectionNodes, Station station) {
		super(modelId, oid, voltageLevel, connectionNodes);
		this.station = station;
	}
	
	@Override
    public boolean isInStation() {
    	return true;
    }

	@Override
	public Station getStation() {
		return station;
	}

	@Override
	public void setStation(Station station) {
		this.station = station;
	}
}
