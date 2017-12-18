package nari.model.cluster;

import java.util.concurrent.atomic.AtomicReference;

import nari.MongoClient.interfaces.MongoAdaptor;
import nari.model.symbol.MongoSymbolSelector;
import nari.model.symbol.PooledSymbolSelector;
import nari.model.symbol.SymbolProvider;
import nari.model.symbol.SymbolSelector;

public class MongoSymbolProvider implements SymbolProvider {

	private final AtomicReference<SymbolSelector> ref = new AtomicReference<SymbolSelector>(null);
	
	private MongoAdaptor mongoAdapter;
	
	private int priority;
	
	public MongoSymbolProvider(MongoAdaptor mongoAdapter,int priority) {
		this.mongoAdapter = mongoAdapter;
		this.priority = priority;
	}
	
	@Override
	public SymbolSelector get() {
		if(ref.get()==null){
			SymbolSelector mongoSelector = new MongoSymbolSelector(mongoAdapter);
			SymbolSelector poolSelector = new PooledSymbolSelector(mongoSelector);
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
				mongoAdapter.find("dual",null);
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
		
		return flag;
	}

}
