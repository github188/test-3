package nari.Dao.interfaces.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import nari.Dao.bundle.bean.ResultHandler;
import nari.Dao.bundle.sequence.Sequence;
import nari.Dao.bundle.sequence.SequenceFactory;
import nari.Dao.bundle.simple.SimpleDao;
import nari.Dao.bundle.transaction.Transaction;
import nari.Dao.interfaces.DbAdaptor;

public class DbAdaptorHandler implements DbAdaptor {

	private SimpleDao dao = null;
	
	public DbAdaptorHandler(SimpleDao dao){
		this.dao = dao;
	}
	
	public boolean batch(String sql, List<Object[]> params) throws SQLException{
		return dao.batch(sql, params);
	}

	public boolean delete(String sql, Object[] params) throws SQLException{
		return dao.delete(sql, params);
	}

	public boolean delete(String sql) throws SQLException{
		return dao.delete(sql);
	}

	public <T> T find(String sql, Class<T> clazz) throws SQLException{
		return dao.find(sql, clazz);
	}

	public <T> T find(String sql, Object[] params, Class<T> clazz) throws SQLException{
		return dao.find(sql, params, clazz);
	}

	public <E> List<E> findAll(String sql, Class<E> clazz) throws SQLException{
		return dao.findAll(sql, clazz);
	}

	public <E> List<E> findAll(String sql, Object[] params, Class<E> clazz) throws SQLException{
		return dao.findAll(sql, params, clazz);
	}

	public List<Map<String,Object>> findAllMap(String sql) throws SQLException{
		return dao.findAllMap(sql);
	}

	public List<Map<String,Object>> findAllMap(String sql, Object[] params) throws SQLException{
		return dao.findAllMap(sql, params);
	}

	public Map<String,Object> findMap(String sql) throws SQLException{
		return dao.findMap(sql);
	}

	public Map<String,Object> findMap(String sql, Object[] params) throws SQLException{
		return dao.findMap(sql, params);
	}
	
	@Override
	public <T> T query(String sql, Object[] params, ResultHandler<T> handler) throws SQLException {
		return dao.query(sql, params, handler);
	}
	
	@Override
	public <T> T query(String sql, ResultHandler<T> handler) throws SQLException {
		return dao.query(sql, handler);
	}

	public String getOwner() {
		return dao.getOwner();
	}

	public boolean save(String sql, Object[] params) throws SQLException{
		return dao.save(sql, params);
	}

	public boolean save(String sql) throws SQLException{
		return dao.save(sql);
	}

	public boolean update(String sql, Object[] params) throws SQLException{
		return dao.update(sql, params);
	}

	public boolean update(String sql) throws SQLException{
		return dao.update(sql);
	}

	public int getSequence(String tableName) throws SQLException{
		Sequence seq = SequenceFactory.getFactory(dao).createSequence();
		if(seq==null){
			return -1;
		}
		return seq.getSequence(tableName);
	}
	
	public Connection getConnection() throws SQLException{
		return dao.getConnection();
	}

	public boolean delete(Connection conn, String sql) throws SQLException{
		return dao.delete(conn, sql);
	}

	public boolean delete(Connection conn, String sql, Object[] params) throws SQLException{
		return dao.delete(conn, sql, params);
	}

	public boolean save(Connection conn, String sql) throws SQLException{
		return dao.save(conn, sql);
	}

	public boolean save(Connection conn, String sql, Object[] params) throws SQLException{
		return dao.save(conn, sql, params);
	}

	public boolean update(Connection conn, String sql) throws SQLException{
		return dao.update(conn, sql);
	}

	public boolean update(Connection conn, String sql, Object[] params) throws SQLException{
		return dao.update(conn, sql, params);
	}
	
	public Transaction getTransactionManager() {
		return null;
	}

	@Override
	public boolean isActive() {
		return true;
	}
}
