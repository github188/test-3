package nari.network.device.concurrent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nari.network.device.TopoDevice;
import nari.network.device.TopoDevices;

public class ConcurrentTopoDevices
	implements TopoDevices {
	
	private List<TopoDevice> devices;
	
	public ConcurrentTopoDevices() {
		devices = new ArrayList<TopoDevice>();
	}

	@Override
	public boolean isEmpty() {
		return devices.isEmpty();
	}

	@Override
	public int size() {
		return devices.size();
	}

	@Override
	public void add(TopoDevice device) {
		synchronized(devices) {
			devices.add(device);
		}
	}

	@Override
	public void remove(TopoDevice device) {
		synchronized(devices) {
			devices.remove(device);
		}
	}

	@Override
	public Iterator<TopoDevice> iterator() {
		List<TopoDevice> _devices = new ArrayList<TopoDevice>();
		synchronized(devices) {
			for (TopoDevice device : devices) {
				_devices.add(device);
			}
		}
		return _devices.iterator();
	}

}
