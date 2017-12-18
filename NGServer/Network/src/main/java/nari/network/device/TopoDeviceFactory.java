package nari.network.device;

import nari.network.device.impl.*;

public class TopoDeviceFactory {

	/**
	 * 创建一个站房类设备
	 * @param modelId 设备子类型
	 * @param oid 设备主键ID
	 * @param voltageLevel 电压等级
	 * @return
	 */
	public Station createStation(int modelId, int oid, int voltageLevel) {
		return new DefaultStationDevice(modelId, oid, voltageLevel);
	}

	/**
	 * 创建一个大馈线设备
	 *
	 * @param modelId 设备子类型
	 * @param oid 设备主键ID
	 * @param voltageLevel 电压等级
	 * @param outletSwitchType 出线开关类型
	 * @param outletSwitchId 出线开关ID
	 * @param startStation 起始电站
	 * @return
	 */
	public BigFeeder createBigFeeder(int modelId, int oid, int voltageLevel,
									 int outletSwitchType, int outletSwitchId,
									 Station startStation) {
		return new DefaultBigFeeder(modelId, oid, voltageLevel, outletSwitchType,
				outletSwitchId, startStation);
	}

	public TopoDevice createTopoDevice(int modelId, int oid, int voltageLevel,
									   long[] connectionNodes,
									   boolean inStation, boolean inBigFeeder) {

		return null;
	}
	
	/**
	 * 创建一个拓扑设备
	 * @param modelId 设备子类型
	 * @param oid 设备主键ID
	 * @param voltageLevel 电压等级
	 * @param connectionNodes 拓扑连接点
	 * @return
	 */
	public TopoDevice createTopoDevice(int modelId, int oid, int voltageLevel,
									   long[] connectionNodes) {
		return new DefaultTopoDevice(modelId, oid, voltageLevel, connectionNodes);
	}

	/**
	 * 创建一个站内的拓扑设备
	 * @param modelId 设备子类型
	 * @param oid 设备主键ID
	 * @param voltageLevel 电压等级
	 * @param connectionNodes 拓扑连接点
	 * @param station 站房设备
	 * @return
	 */
	public TopoDevice createInStationDevice(int modelId, int oid, int voltageLevel,
											long[] connectionNodes, Station station) {
		return new DefaultInStationDevice(modelId, oid, voltageLevel, connectionNodes,
				station);
	}
	
	/**
	 * 创建一个母线设备
	 * @param modelId 设备子类型
	 * @param oid 设备主键ID
	 * @param voltageLevel 电压等级
	 * @param connectionNodes 拓扑连接点
	 * @param station 站房设备
	 * @return
	 */
	public TopoDevice createBusDevice(int modelId, int oid, int voltageLevel,
									  long[] connectionNodes, Station station) {
		return new DefaultBusDevice(modelId, oid, voltageLevel, connectionNodes,
				station);
	}
	
	/**
	 * 创建一个变压器设备
	 * @param modelId 设备子类型
	 * @param oid 设备主键ID
	 * @param voltageLevel 电压等级
	 * @param connectionNodes 拓扑连接点
	 * @return
	 */
	public TopoDevice createTransformerDevice(int modelId, int oid, int voltageLevel,
											  long[] connectionNodes) {
		return new DefaultTransformerDevice(modelId, oid, voltageLevel, connectionNodes);
	}
	
	/**
	 * 创建一个站内变压器设备
	 * @param modelId 设备子类型
	 * @param oid 设备主键ID
	 * @param voltageLevel 电压等级
	 * @param connectionNodes 拓扑连接点
	 * @param station 站房设备
	 * @return
	 */
	public TopoDevice createInStationTransformerDevice(int modelId, int oid, int voltageLevel,
													   long[] connectionNodes, Station station) {
		return new DefaultInStationTransformerDevice(modelId, oid, voltageLevel, connectionNodes,
				station);
	}
	
	/**
	 * 创建一个开关设备
	 * @param modelId 设备子类型
	 * @param oid 设备主键ID
	 * @param voltageLevel 电压等级
	 * @param connectionNodes 拓扑连接点
	 * @param switchCase 开关状态
	 * @return
	 */
	public TopoDevice createSwitchDevice(int modelId, int oid, int voltageLevel,
										 long[] connectionNodes, int switchCase) {
		return new DefaultSwitchDevice(modelId, oid, voltageLevel, connectionNodes, switchCase);
	}
	
	/**
	 * 创建一个站内开关设备
	 * @param modelId 设备子类型
	 * @param oid 设备主键ID
	 * @param voltageLevel 电压等级
	 * @param connectionNodes 拓扑连接点
	 * @param station 站房设备
	 * @param switchCase 开关状态
	 * @return
	 */
	public TopoDevice createInStationSwitchDevice(int modelId, int oid, int voltageLevel,
												  long[] connectionNodes, Station station,
												  int switchCase) {
		return new DefaultInStationSwitchDevice(modelId, oid, voltageLevel, connectionNodes,
				station, switchCase);
	}

	/**
	 * 创建一个导线类设备
	 * @param modelId
	 * @param oid
	 * @param voltageLevel
	 * @param connectionNodes
	 * @return
	 */
	public TopoDevice createConductorDevice(int modelId, int oid, int voltageLevel,
											long[] connectionNodes) {
		return new DefaultConductorDevice(modelId, oid, voltageLevel, connectionNodes);
	}
}