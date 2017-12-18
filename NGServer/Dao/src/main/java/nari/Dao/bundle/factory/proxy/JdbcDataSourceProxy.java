package nari.Dao.bundle.factory.proxy;

import java.beans.PropertyVetoException;

import javax.naming.NamingException;
import javax.sql.DataSource;

import nari.Dao.bundle.bean.Jdbc;
import nari.Dao.bundle.bean.Jndi;
import nari.decrypt.Decrypt;
import nari.eds.EDSUtils;

import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class JdbcDataSourceProxy extends DataSourceProxy {

	@Override
	public DataSource getJdbcDataSource(Jdbc jdbc) throws PropertyVetoException {
		if(jdbc==null){
			return null;
		}
		
		ComboPooledDataSource pool = new ComboPooledDataSource();
		pool.setDriverClass(jdbc.getDriverClass());
		pool.setJdbcUrl(jdbc.getJdbcUrl());
		pool.setUser(jdbc.getUser());
		Decrypt d = new Decrypt();
		pool.setPassword(d.getDESDecrpty(jdbc.getPassword(), EDSUtils.getKey()));
		
		String minPoolSize = jdbc.getMinPoolSize();
		if(minPoolSize!=null && !"".equals(minPoolSize)){
			int size = Integer.parseInt(minPoolSize);
			if(size>0){
				pool.setMinPoolSize(Integer.parseInt(minPoolSize));
			}
		}
		
		String maxPoolSize = jdbc.getMaxPoolSize();
		if(maxPoolSize!=null && !"".equals(maxPoolSize)){
			int size = Integer.parseInt(maxPoolSize);
			if(size>0){
				pool.setMaxPoolSize(size);
			}
		}
		
		String maxAdministrativeTaskTime = jdbc.getMaxAdministrativeTaskTime();
		if(maxAdministrativeTaskTime!=null && !"".equals(maxAdministrativeTaskTime)){
			int size = Integer.parseInt(maxAdministrativeTaskTime);
			if(size>0){
				pool.setMaxAdministrativeTaskTime(size);
			}
		}
		
		String numHelperThreads = jdbc.getNumHelperThreads();
		if(numHelperThreads!=null && !"".equals(numHelperThreads)){
			int size = Integer.parseInt(numHelperThreads);
			if(size>0){
				pool.setNumHelperThreads(size);
			}
		}
		
		DataSource ds = new TransactionAwareDataSourceProxy(pool);
		return ds;
	}

	@Override
	public DataSource getJndiDataSource(Jndi jndi) throws IllegalArgumentException, NamingException, PropertyVetoException {
		return null;
	}

}
