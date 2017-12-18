package nari.network.device;

public interface Device {

	/**
	 * 合法性检查，判断当前的拓扑设备是否有效
	 * @return
	 */
	boolean validate();
	
	/**
	 * 获取当前设备的设备子类型
	 * @return
	 */
	int getModelId();

	/**
	 * 获取当前设备的OID
	 * @return
	 */
	int getOid();

	/**
	 * 获取当前设备的电压等级
	 * @return
	 */
	int getVoltageLevel();

	/**
	 * 是否是拓扑设备
	 * @return
	 */
	boolean isTopoDevice();

	/**
	 * 是否是站房类设备
	 *
	 * @return
	 */
	boolean isStation();

	/**
	 * 是否是站内设备
	 *
	 * @return 若是站内设备则返回true，否则（站外设备）返回false
	 */
	boolean isInStation();

	/**
	 * 是否是大馈线下的设备
	 *
	 * @return 若是大馈线下的设备则返回true，否则返回false
	 */
	boolean isInBigFeeder();

	void setInBigFeeder(boolean inBigFeeder);

	BigFeeder getBigFeeder();

	void setBigFeeder(BigFeeder bigFeeder);

	/**
	 * 转换为拓扑设备
	 *
	 * @return
	 */
	TopoDevice asTopoDevice();

	/**
	 * 转换为站房类设备
	 *
	 * @return
	 */
	Station asStation();

	/**
	 * 转换为站内设备
	 *
	 * @return
	 */
	InStationDevice asInStation();

}
