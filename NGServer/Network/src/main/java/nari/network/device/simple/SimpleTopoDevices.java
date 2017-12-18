package nari.network.device.simple;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nari.network.device.TopoDevice;
import nari.network.device.TopoDevices;

public class SimpleTopoDevices
	implements TopoDevices {

	private List<TopoDevice> devices;
	
	public SimpleTopoDevices() {
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
		devices.add(device);
	}

	@Override
	public void remove(TopoDevice device) {
		devices.remove(device);
	}

	@Override
	public Iterator<TopoDevice> iterator() {
		return devices.iterator();
	}
	
}
