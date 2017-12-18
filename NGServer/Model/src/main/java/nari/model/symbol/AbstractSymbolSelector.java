package nari.model.symbol;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import nari.model.bean.SymbolDef;

public class AbstractSymbolSelector {

	private final ConcurrentMap<String, List<SymbolDef>> symbolMap = new ConcurrentHashMap<String, List<SymbolDef>>();
	
	public AbstractSymbolSelector(){
		
	}
	
	protected Iterator<SymbolDef> getSymbolFromCache(String modelId){
		List<SymbolDef> symbols = symbolMap.get(modelId);
		if(symbols==null){
			return null;
		}
		List<SymbolDef> list = new ArrayList<SymbolDef>(symbols);
		
		return list.iterator();
	}
	
	protected void cacheSymbol(SymbolDef symbol){
		if(symbolMap.containsKey(symbol.getModelId())){
			symbolMap.get(symbol.getModelId()).add(symbol);
		}else{
			List<SymbolDef> list = new ArrayList<SymbolDef>();
			list.add(symbol);
			symbolMap.putIfAbsent(symbol.getModelId(), list);
		}
	}
	
}
