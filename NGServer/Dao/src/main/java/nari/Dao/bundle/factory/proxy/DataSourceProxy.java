package nari.Dao.bundle.factory.proxy;

import java.beans.PropertyVetoException;

import javax.naming.NamingException;
import javax.sql.DataSource;

import nari.Dao.bundle.bean.Jdbc;
import nari.Dao.bundle.bean.Jndi;
import nari.Dao.bundle.simple.SimpleDao;
import nari.Dao.bundle.simple.SimpleDaoImpl;

import org.apache.commons.dbutils.QueryRunner;

public abstract class DataSourceProxy {
	
	public SimpleDao getJdbcDataBaseInstance(Jdbc jdbc){
		DataSource ds = null;
		try {
			ds = getJdbcDataSource(jdbc);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		QueryRunner runner = new QueryRunner(ds);
		SimpleDao dao = new SimpleDaoImpl(runner,"scyw");
		return dao;
	}
	
	public SimpleDao getJndiDataBaseInstance(Jndi jndi){
		DataSource ds = null;
		try {
			ds = getJndiDataSource(jndi);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		QueryRunner runner = new QueryRunner(ds);
		SimpleDao dao = new SimpleDaoImpl(runner,"scyw");
		return dao;
	}
	
	public abstract DataSource getJdbcDataSource(Jdbc jdbc) throws IllegalArgumentException, NamingException,PropertyVetoException;
	
	public abstract DataSource getJndiDataSource(Jndi jndi) throws IllegalArgumentException, NamingException,PropertyVetoException;
}
