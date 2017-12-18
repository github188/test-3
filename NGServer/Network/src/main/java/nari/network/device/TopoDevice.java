package nari.network.device;

import nari.network.bean.NetworkConnection;

/**
 * 拓扑设备
 */
public interface TopoDevice
	extends Device {

	/**
	 * 获取端子的数量
	 * 
	 * @return
	 */
	int getTerminalCount();
	
	/**
	 * 是否是单端子设备
	 * 
	 * @return 若是单端子设备则返回true，否则（双端子设备）则返回false
	 */
	boolean isSingleTerminal();
	
	/**
	 * 获取当前设备的拓扑连接点
	 * 
	 * @return
	 */
	long[] getConnectionNodes();
	
	/**
	 * 获取与另一个设备的邻接关系
	 * 
	 * @param device
	 * @return
	 */
	NetworkConnection relateTo(TopoDevice device);
	
	/**
	 * 获取端子前进的方向
	 * 
	 * @param comingTerminal 传输过来的端子
	 * @return
	 */
	int[] forward(int comingTerminal);
	
	/**
	 * 是否是母线类设备
	 * 
	 * @return
	 */
	boolean isBus();
	
	/**
	 * 是否是导体类设备（导线段、电缆段、连接线等）
	 * 
	 * @return
	 */
	boolean isConductor();
	
	/**
	 * 是否是杆塔类设备（运行杆）
	 * 
	 * @return
	 */
	boolean isPole();
	
	/**
	 * 是否是开关类设备
	 * 
	 * @return
	 */
	boolean isSwitch();
	
	/**
	 * 是否是变压器类设备
	 * 
	 * @return
	 */
	boolean isTransformer();

	/**
	 * 转换为母线类设备
	 * 
	 * @return
	 */
	Bus asBus();
	
	/**
	 * 转换为导体类设备（导线段、电缆段、连接线等）
	 * 
	 * @return
	 */
	Conductor asConductor();
	
	/**
	 * 转换为杆塔类设备（运行杆）
	 * 
	 * @return
	 */
	Pole asPole();
	
	/**
	 * 转换为开关类设备
	 * 
	 * @return
	 */
	Switch asSwitch();
	
	/**
	 * 转换为变压器类设备
	 * 
	 * @return
	 */
	Transformer asTransformer();
}
