package nari.model.symbol;

import java.util.Iterator;

import nari.model.bean.SubClassDef;
import nari.model.bean.SymbolDef;
import nari.model.device.Device;

public interface SymbolAdapter {

	public static final SymbolAdapter NONE = new SymbolAdapter(){

		@Override
		public Iterator<SymbolDef> search(String modelId) throws Exception {
			return null;
		}

		@Override
		public Iterator<SymbolDef> search(SubClassDef subClassDef) throws Exception {
			return null;
		}

		@Override
		public SymbolDef search(Device device) throws Exception {
			return null;
		}
		
	};
	
	public SymbolDef search(Device device) throws Exception;
	
	public Iterator<SymbolDef> search(String modelId) throws Exception;
	
	public Iterator<SymbolDef> search(SubClassDef subClassDef) throws Exception;
}
