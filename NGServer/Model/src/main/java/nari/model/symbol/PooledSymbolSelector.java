package nari.model.symbol;

import java.util.Iterator;

import nari.model.bean.SymbolDef;

public class PooledSymbolSelector extends AbstractSymbolSelector implements SymbolSelector {

	private SymbolSelector selector = null;
	
	public PooledSymbolSelector(SymbolSelector selector) {
		this.selector = selector;
	}

	@Override
	public Iterator<SymbolDef> search(String modelId) throws Exception {
		Iterator<SymbolDef> it = getSymbolFromCache(modelId);
		if(it==null){
			it = selector.search(modelId);
			if(it!=null){
				while(it.hasNext()){
					cacheSymbol(it.next());
				}
			}
		}
		it = getSymbolFromCache(modelId);
		return it;
	}
}
