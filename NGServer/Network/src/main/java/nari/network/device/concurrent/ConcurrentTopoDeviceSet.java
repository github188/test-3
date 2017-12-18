package nari.network.device.concurrent;


import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import nari.network.device.TopoDevice;
import nari.network.device.TopoDeviceSet;

/**
 * 拓扑设备集合实现类
 * Created by Birderyu on 2017/4/22.
 */
public class ConcurrentTopoDeviceSet
        implements TopoDeviceSet {

    private Map<Integer, Map<Integer, TopoDevice>> devices;
    private AtomicInteger count;

    public ConcurrentTopoDeviceSet() {
        this.devices = new ConcurrentHashMap<Integer, Map<Integer, TopoDevice>>();
        this.count = new AtomicInteger(0);
    }
    
    public boolean isEmpty() {
    	return devices.isEmpty();
    }
    
    @Override
    public int size() {
        return count.intValue();
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
            count.incrementAndGet();
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
    	Map<Integer, TopoDevice> device = devices.get(modelId);
    	if (null == device) {
    		return;
    	}
        if (null != device.remove(oid)) {
        	count.decrementAndGet();
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
        Map<Integer, TopoDevice> device = devices.get(modelId);
        if (null == device) {
            return null;
        }
        return device.values().iterator();
    }
}
