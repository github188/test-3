package nari.network.device;

public interface InStationDevice
	extends TopoDevice {

	/**
	 * 获取所属电站设备的设备
	 * 
	 * @return
	 */
	Station getStation();

	void setStation(Station station);

}
