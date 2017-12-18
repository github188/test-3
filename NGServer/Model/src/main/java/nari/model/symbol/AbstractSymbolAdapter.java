package nari.model.symbol;

import java.util.Iterator;

import nari.model.bean.SubClassDef;
import nari.model.bean.SymbolDef;
import nari.model.device.Device;

public abstract class AbstractSymbolAdapter implements SymbolAdapter {

	@Override
	public SymbolDef search(Device device) throws Exception {
		
		Iterator<SymbolDef> it = getProvider().get().search(String.valueOf(device.getValue("SBZLX")));
		
		SymbolDef defaultSymbol = null;
		
		SymbolDef symbol = null;
		if(it == null){
			return null;
		}
		while(it.hasNext()){
			SymbolDef def = it.next();
			String value = def.getSymbolValue();
			if(value==null || "".equals(value) || "null".equals(value)){
				defaultSymbol = def;
				continue;
			}
			
			if(value.endsWith(";")){
				value = value.substring(0, value.length()-2);
			}
			String[] conditions = value.split(";");
			boolean b = true;
			for(String condition:conditions){
				String devVal = device.getValue(condition.split("=")[0])==null?"":String.valueOf(device.getValue(condition.split("=")[0]));
				if("".equals(devVal)){
					continue;
				}
				
				String val = condition.split("=")[1];
				if(devVal.equalsIgnoreCase(val)){
					b = b && true;
				}else{
					b = b && false;
					break;
				}
			}
			
			if(b){
				symbol = def;
				break;
			}
		}
		
		return symbol==null?defaultSymbol:symbol;
	}

	@Override
	public Iterator<SymbolDef> search(String modelId) throws Exception {
		return getProvider().get().search(modelId);
	}

	@Override
	public Iterator<SymbolDef> search(SubClassDef subClassDef) throws Exception {
		return getProvider().get().search(subClassDef.getModelId());
	}

	protected abstract SymbolProvider getProvider();
}
