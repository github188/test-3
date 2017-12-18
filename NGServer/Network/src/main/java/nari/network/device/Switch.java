package nari.network.device;

/**
 * 开关设备 开关设备是拓扑设备的一种实现，包含了开关状态信息 
 * Created by Birderyu on 2017/4/22.
 */
public interface Switch
	extends TopoDevice {

	int getSwitchCase();

	void setSwitchCase(int switchCase);
	
	int getUsualSwitchCase();
	
	void setUsualSwitchCase(int usualSwitchCase);
	
	/**
	 * 考虑开关状态的前进算法
	 * 
	 * @param comingTerminal
	 * @return
	 */
	int[] switchForward(int comingTerminal);
	
	/**
	 * 模拟开关状态的前进算法
	 * 
	 * @param mockSwitchCase 模拟开关状态
	 * @param comingTerminal 前进端子
	 * @return
	 */
	int[] mockSwitchForward(int mockSwitchCase, int comingTerminal);
}
