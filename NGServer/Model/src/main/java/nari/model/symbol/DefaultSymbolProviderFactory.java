package nari.model.symbol;

import java.util.concurrent.atomic.AtomicReference;

public class DefaultSymbolProviderFactory implements SymbolProviderFactory {

	private final AtomicReference<SymbolProvider> ref = new AtomicReference<SymbolProvider>();
	
	@Override
	public SymbolProvider getProvider() {
//		if(ref.get()==null){
//			SymbolProvider provider = new DbSymbolProvider();
//			ref.compareAndSet(null, provider);
//		}
		return ref.get();
	}

}
