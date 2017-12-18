package nari.network.device.impl;

import nari.network.device.utility.SwitchCase;
import nari.network.device.Switch;

public class DefaultSwitchDevice
	extends DefaultTopoDevice implements Switch {
	
	/**
	 * 开关状态
	 */
	private int switchCase;
	
	/**
	 * 常开状态
	 */
	private int usualSwitchCase = 2097151; // 常闭

	public DefaultSwitchDevice(int modelId, int oid, int voltageLevel,
                               long[] connectionNodes, int switchCase) {
		super(modelId, oid, voltageLevel, connectionNodes);
		this.switchCase = switchCase;
	}
	
	@Override
    public boolean isSwitch() {
    	return true;
    }

	@Override
	public int getSwitchCase() {
		return switchCase;
	}

	@Override
	public void setSwitchCase(int switchCase) {
		this.switchCase = switchCase;
	}
	
	@Override
	public int getUsualSwitchCase() {
		return usualSwitchCase;
	}
	
	@Override
	public void setUsualSwitchCase(int usualSwitchCase) {
		this.usualSwitchCase = usualSwitchCase;
	}
	
	@Override
	public int[] switchForward(int comingTerminal) {
		return SwitchCase.forward(this, switchCase, comingTerminal);
	}

	@Override
	public int[] mockSwitchForward(int mockSwitchCase, int comingTerminal) {
		return SwitchCase.forward(this, mockSwitchCase, comingTerminal);
	}
}
