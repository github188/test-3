package nari.model.symbol;

import java.util.Iterator;

import nari.model.bean.SymbolDef;

public interface SymbolSelector {

	public Iterator<SymbolDef> search(String modelId) throws Exception;
	
}
