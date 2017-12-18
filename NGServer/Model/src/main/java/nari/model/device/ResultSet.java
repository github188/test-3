package nari.model.device;

import java.util.Iterator;

public interface ResultSet {

	public static final ResultSet NONE = new ResultSet(){

		@Override
		public Device getSingle() {
			return null;
		}

		@Override
		public Iterator<Device> resultList() {
			return null;
		}
		
	};
	
	public Device getSingle();
	
	public Iterator<Device> resultList();
	
}
