package nari.model.device;

import java.util.concurrent.atomic.AtomicReference;

import nari.MongoClient.interfaces.MongoAdaptor;

public class MongoProvider implements SelectorProvider {

	private final AtomicReference<ModelSelector> ref = new AtomicReference<ModelSelector>(null);
	
	private MongoAdaptor mongoAdapter;
	
	private int priority;
	
	public MongoProvider(MongoAdaptor mongoAdapter,int priority) {
		this.mongoAdapter = mongoAdapter;
	}
	
	@Override
	public ModelSelector get() {
		if(ref.get()==null){
			ModelSelector mongoSelector = new MongoModelSelector(mongoAdapter);
			ModelSelector poolSelector = new PooledModelSelector(mongoSelector);
			ref.compareAndSet(null, poolSelector);
		}
		return ref.get();
	}

	@Override
	public int getPriority() {
		return priority;
	}

	@Override
	public boolean selfCheck() {
		int i=0;
		boolean flag = true;
		while(i<3){
			try {
				mongoAdapter.find("dual", null);
			} catch (Exception e) {
				flag = false;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			} finally{
				i++;
			}
			if(flag){
				break;
			}
		}
		
		if(!flag){
			reset();
		}
		
		return flag;
	}
	
	@SuppressWarnings("unused")
	private void reset(){
		ModelSelector selector = ref.getAndSet(null);
		selector = null;
	}

}
