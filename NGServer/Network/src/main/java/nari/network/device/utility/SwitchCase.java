package nari.network.device.utility;

import nari.network.device.TopoDevice;

/**
 * 针对开关状态的处理方法
 */
public class SwitchCase {
	
	public static final int OPEN_CASE = 536870912;
	
	public static final int CLOSE_CASE = 536870913;

	public static int[] forward(TopoDevice device, int switchCase, int comingTerminal) {
		if (SwitchCase.OPEN_CASE == switchCase) {
			// 拉开状态
			return null;
		} else if (SwitchCase.CLOSE_CASE == switchCase) {
			// 闭合状态
			return device.forward(comingTerminal);
		}
		// 其他状态
		return device.forward(comingTerminal);
	}
}
