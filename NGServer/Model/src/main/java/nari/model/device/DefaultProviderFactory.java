package nari.model.device;

import java.util.concurrent.atomic.AtomicReference;

public class DefaultProviderFactory implements ProviderFactory {

	private final AtomicReference<SelectorProvider> ref = new AtomicReference<>();
	
	@Override
	public SelectorProvider getProvider() {
//		if(ref.get()==null){
//			Provider provider = new DbProvider();
//			ref.compareAndSet(null, provider);
//		}
		return ref.get();
	}

}
