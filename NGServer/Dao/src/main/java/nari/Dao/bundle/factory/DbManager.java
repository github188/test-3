package nari.Dao.bundle.factory;

import java.util.concurrent.atomic.AtomicReference;

import nari.Dao.bundle.bean.Jdbc;
import nari.Dao.bundle.bean.Jndi;
import nari.Dao.bundle.factory.proxy.DataSourceProxy;
import nari.Dao.bundle.factory.proxy.JdbcDataSourceProxy;
import nari.Dao.bundle.factory.proxy.JndiDataSourceProxy;
import nari.Dao.bundle.simple.SimpleDao;

/**
 * dao实现的工厂类
 * @author 
 * @version 
 */
public class DbManager {
	
	private final AtomicReference<SimpleDao> ref = new AtomicReference<SimpleDao>(null);
	
	public DbManager() {
		
	}
	
	public SimpleDao initJdbcDataBase(Jdbc jdbc){
		return getJdbcInstance(jdbc);
	}
	
	public SimpleDao initJndiDataBase(Jndi jndi){
		return getJndiInstance(jndi);
	}
	
	public SimpleDao getJdbcInstance(Jdbc jdbc) {
		if(ref.get()==null){
			SimpleDao dao = createJdbcDataSourcePool(jdbc);
			ref.compareAndSet(null, dao);
		}
		return ref.get();
	}
	
	public SimpleDao getJndiInstance(Jndi jndi) {
		if(ref.get()==null){
			SimpleDao dao = createJndiDataSourcePool(jndi);
			ref.compareAndSet(null, dao);
		}
		return ref.get();
	}
	
	private SimpleDao createJdbcDataSourcePool(Jdbc jdbc){
		if(jdbc==null){
			return null;
		}
		
		DataSourceProxy proxy = null;
		if(jdbc.isActive()){
			proxy = new JdbcDataSourceProxy();
		}
		if(proxy==null){
			return null;
		}
		return proxy.getJdbcDataBaseInstance(jdbc);
	}
	
	private SimpleDao createJndiDataSourcePool(Jndi jndi){
		if(jndi==null){
			return null;
		}
		
		DataSourceProxy proxy = null;
		if(jndi.isActive()){
			proxy = new JndiDataSourceProxy();
		}
		if(proxy==null){
			return null;
		}
		return proxy.getJndiDataBaseInstance(jndi);
	}
}
