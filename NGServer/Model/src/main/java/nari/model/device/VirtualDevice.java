package nari.model.device;

import java.util.Iterator;

public interface VirtualDevice {

	public static final VirtualDevice NONE = new VirtualDevice(){

		@Override
		public Iterator<Device> containDevice() {
			return null;
		}
		
	};
	
	public Iterator<Device> containDevice();
	
}
