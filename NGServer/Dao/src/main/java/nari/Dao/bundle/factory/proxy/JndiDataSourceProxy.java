package nari.Dao.bundle.factory.proxy;

import java.beans.PropertyVetoException;
import java.util.Properties;

import javax.naming.NamingException;
import javax.sql.DataSource;

import nari.Dao.bundle.bean.Jdbc;
import nari.Dao.bundle.bean.Jndi;

import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.jndi.JndiObjectFactoryBean;

public class JndiDataSourceProxy extends DataSourceProxy {

	@Override
	public DataSource getJndiDataSource(Jndi jndiObject) throws IllegalArgumentException, NamingException {
		JndiObjectFactoryBean jndi = new JndiObjectFactoryBean();
		if(jndiObject==null){
			return null;
		}
		
		jndi.setJndiName(jndiObject.getJndiName());
		jndi.setResourceRef(jndiObject.isResourceRef());
		
		Properties jndiEnvironment = new Properties();
		jndiEnvironment.put("java.naming.provider.url", jndiObject.getUrl());
		jndiEnvironment.put("java.naming.factory.initial", jndiObject.getInitial());
		
		jndi.setJndiEnvironment(jndiEnvironment);
		
		jndi.afterPropertiesSet();
		DataSource pool = (DataSource)jndi.getObject();
		DataSource ds = new TransactionAwareDataSourceProxy(pool);
		return ds;
	}

	@Override
	public DataSource getJdbcDataSource(Jdbc jdbc) throws IllegalArgumentException, NamingException, PropertyVetoException {
		return null;
	}

}
