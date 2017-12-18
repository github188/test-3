package nari.model.symbol;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

import nari.Dao.interfaces.DbAdaptor;

public class DbSymbolProvider implements SymbolProvider {

	private final AtomicReference<SymbolSelector> ref = new AtomicReference<SymbolSelector>();
	
	private DbAdaptor dbAdapter;
	
	private int priority;
	
	public DbSymbolProvider(DbAdaptor dbAdapter,int priority) {
		this.dbAdapter = dbAdapter;
		this.priority = priority;
	}
	
	@Override
	public SymbolSelector get() {
		if(ref.get()==null){
			SymbolSelector dbSelector = new DbSymbolSelector(dbAdapter);
			SymbolSelector poolSelector = new PooledSymbolSelector(dbSelector);
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
				dbAdapter.findMap(sql);
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
		
		return flag;
	}

}
