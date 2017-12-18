package nari.model.device;

import java.util.concurrent.atomic.AtomicReference;

public class ServerProvider implements SelectorProvider {
	
	private final AtomicReference<ModelSelector> ref = new AtomicReference<>();
	
	@Override
	public ModelSelector get() {
		if(ref.get()==null){
			ModelSelector svSelector = new ServerModelSelector();
			ModelSelector poolSelector = new PooledModelSelector(svSelector);
			ref.compareAndSet(null, poolSelector);
		}
		return ref.get();
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public boolean selfCheck() {
		return false;
	}

}
