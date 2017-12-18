package nari.network.device.impl;

import nari.network.bean.NetworkConnection;
import nari.network.device.*;

/**
 * 基础拓扑设备
 * Created by Birderyu on 2017/4/22.
 */
public class DefaultTopoDevice
     extends DefaultDevice implements TopoDevice {
    
    private long[] connectionNodes = null;

    public DefaultTopoDevice(int modelId, int oid, int voltageLevel,
							 long[] connectionNodes) {
        super(modelId, oid, voltageLevel);
        this.connectionNodes = connectionNodes;
    }

    @Override
	public int getTerminalCount() {
    	return connectionNodes.length;
    }
    
    @Override
	public boolean isSingleTerminal() {
    	return getTerminalCount() == 1;
    }

    @Override
	public long[] getConnectionNodes() {
    	return connectionNodes;
    }
    
    @Override
	public NetworkConnection relateTo(TopoDevice device) {
    	if (null == device) {
    		return null;
    	}
    	long[] _connectionNodes = device.getConnectionNodes();
    	if (null == connectionNodes || null == _connectionNodes) {
    		return null;
    	}
    	for (int i = 0; i < connectionNodes.length; i++) {
    		for (int j = 0; j < _connectionNodes.length; j++) {
    			if (connectionNodes[i] == _connectionNodes[j]) {
    				return new NetworkConnection(i, j);
    			}
    		}
    	}
    	return null;
    }
    
    @Override
    public int[] forward(int comingTerminal) {
    	if (null == connectionNodes || comingTerminal >= connectionNodes.length) {
    		return null;
    	}
    	if (connectionNodes.length == 1) {
    		return new int[0];
    	}
    	int[] forwardTerminals = new int[connectionNodes.length - 1];
    	int index = 0;
    	for (int i = 0; i < connectionNodes.length; i++) {
    		if (i == comingTerminal) {
    			continue;
    		}
    		forwardTerminals[index++] = i;
    	}
    	return forwardTerminals;
    }
    
    @Override
    public boolean isBus() {
    	return false;
    }
	
    @Override
    public boolean isConductor() {
    	return false;
    }
	
    @Override
    public boolean isPole() {
    	return false;
    }
	
    @Override
    public boolean isSwitch() {
    	return false;
    }
	
    @Override
    public boolean isTransformer() {
    	return false;
    }
	
    @Override
    public boolean isInStation() {
    	return false;
    }

    @Override
    public Bus asBus() {
    	if (!isBus()) {
    		return null;
    	}
    	return (Bus) this;
    }
	
    @Override
    public Conductor asConductor() {
    	if (!isConductor()) {
    		return null;
    	}
    	return (Conductor) this;
    }
	
    @Override
    public Pole asPole() {
    	if (!isPole()) {
    		return null;
    	}
    	return (Pole) this;
    }
	
    @Override
    public Switch asSwitch() {
    	if (!isSwitch()) {
    		return null;
    	}
    	return (Switch) this;
    }
	
    @Override
    public Transformer asTransformer() {
    	if (!isTransformer()) {
    		return null;
    	}
    	return (Transformer) this;
    }
    
    @Override
	public InStationDevice asInStation() {
    	if (!isInStation()) {
    		return null;
    	}
    	return (InStationDevice) this;
	}
}
