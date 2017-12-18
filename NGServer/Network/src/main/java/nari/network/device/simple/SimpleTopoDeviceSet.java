package nari.network.device.simple;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import nari.network.device.TopoDevice;
import nari.network.device.TopoDeviceSet;

public class SimpleTopoDeviceSet
	implements TopoDeviceSet {
	
	private Map<Integer, Map<Integer, TopoDevice>> devices;
	private int count;
	
	public SimpleTopoDeviceSet() {
        this.devices = new HashMap<Integer, Map<Integer, TopoDevice>>();
        this.count = 0;
    }
    
    public boolean isEmpty() {
    	return devices.isEmpty();
    }
    
    @Override
    public int size() {
        return count;
    }

    @Override
    public void add(TopoDevice device) {

        if (null == device) {
            return;
        }
        int modelId = device.getModelId();
        Map<Integer, TopoDevice> oid_device = devices.get(modelId);
        if (null == oid_device) {
            oid_device = new ConcurrentHashMap<Integer, TopoDevice>();
            devices.put(modelId, oid_device);
        }
        if (null == oid_device.put(device.getOid(), device)) {
        	count++;
        }
    }

    @Override
    public void remove(TopoDevice device) {
    	if (null == device) {
            return;
        }
    	remove(device.getModelId(), device.getOid());
    }
    
    @Override
    public void remove(int modelId, int oid) {
    	Map<Integer, TopoDevice> oid_device = devices.get(modelId);
    	if (null == oid_device) {
    		return;
    	}
        if (null != oid_device.remove(oid)) {
        	count--;
        }
    }
    
    @Override
    public TopoDevice get(int modelId, int oid) {
        Map<Integer, TopoDevice> oid_device = devices.get(modelId);
        if (null == oid_device) {
            return null;
        }
        return oid_device.get(oid);
    }

    @Override
    public Iterator<TopoDevice> get(int modelId) {
        Map<Integer, TopoDevice> oid_device = devices.get(modelId);
        if (null == oid_device) {
            return null;
        }
        return oid_device.values().iterator();
    }
}
