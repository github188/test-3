package nari.model.device;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

import nari.Dao.interfaces.DbAdaptor;

public class DbProvider implements SelectorProvider {

	private DbAdaptor dbAdaptor = DbAdaptor.NONE;
	
	private final AtomicReference<ModelSelector> ref = new AtomicReference<ModelSelector>(null);
	
	private int priority;
	
	public DbProvider(DbAdaptor dbAdaptor,int priority) {
		this.dbAdaptor = dbAdaptor;
		this.priority = priority;
	}
	
	@Override
	public ModelSelector get() {
		if(ref.get()==null){
			ModelSelector dbSelector = new DbModelSelector(dbAdaptor);
			ModelSelector poolSelector = new PooledModelSelector(dbSelector);
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
		String sql = "select 1 from dual";
		
		int i=0;
		boolean flag = true;
		while(i<3){
			try {
				dbAdaptor.findMap(sql);
			} catch (SQLException e) {
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
