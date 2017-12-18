package nari.Dao.bundle.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.ResultSetHandler;

import nari.Dao.bundle.bean.ResultHandler;
import nari.Dao.interfaces.DbAdaptor;

public class TransactionDbAdapter implements DbAdaptor {

	private DbAdaptor dbAdapter;
	
	private Transaction tr = null;
	
	public TransactionDbAdapter(DbAdaptor dbAdapter) {
		this.dbAdapter = dbAdapter;
		tr = new TransactionAdaptor(dbAdapter);
	}
	
	@Override
	public Map<String, Object> findMap(String sql) throws SQLException {
		return dbAdapter.findMap(sql);
	}

	@Override
	public <T> T find(String sql, Class<T> clazz) throws SQLException {
		return dbAdapter.find(sql, clazz);
	}

	@Override
	public Map<String, Object> findMap(String sql, Object[] params) throws SQLException {
		return dbAdapter.findMap(sql, params);
	}

	@Override
	public <T> T find(String sql, Object[] params, Class<T> clazz) throws SQLException {
		return dbAdapter.find(sql, params, clazz);
	}

	@Override
	public List<Map<String, Object>> findAllMap(String sql) throws SQLException {
		return dbAdapter.findAllMap(sql);
	}

	@Override
	public List<Map<String, Object>> findAllMap(String sql, Object[] params) throws SQLException {
		return dbAdapter.findAllMap(sql, params);
	}

	@Override
	public <E> List<E> findAll(String sql, Class<E> clazz) throws SQLException {
		return dbAdapter.findAll(sql, clazz);
	}

	@Override
	public <E> List<E> findAll(String sql, Object[] params, Class<E> clazz) throws SQLException {
		return dbAdapter.findAll(sql, params, clazz);
	}
	
	@Override
	public <T> T query(String sql, Object[] params, ResultHandler<T> handler) throws SQLException {
		return dbAdapter.query(sql, params, handler);
	}
	
	@Override
	public <T> T query(String sql, ResultHandler<T> handler) throws SQLException {
		return dbAdapter.query(sql, handler);
	}

	@Override
	public boolean save(String sql, Object[] params) throws SQLException {
		return false;
	}

	@Override
	public boolean save(String sql) throws SQLException {
		Connection conn = tr.getConnection();
		if(conn==null){
			return dbAdapter.save(sql);
		}
		return dbAdapter.save(tr.getConnection(), sql);
	}

	@Override
	public boolean save(Connection conn, String sql) throws SQLException {
		return dbAdapter.save(conn, sql);
	}

	@Override
	public boolean save(Connection conn, String sql, Object[] params) throws SQLException {
		return dbAdapter.save(conn, sql, params);
	}

	@Override
	public boolean update(String sql, Object[] params) throws SQLException {
		Connection conn = tr.getConnection();
		if(conn==null){
			return dbAdapter.update(sql, params);
		}
		return dbAdapter.update(conn, sql, params);
	}

	@Override
	public boolean update(String sql) throws SQLException {
		Connection conn = tr.getConnection();
		if(conn==null){
			return dbAdapter.update(sql);
		}
		
		return dbAdapter.update(conn, sql);
	}

	@Override
	public boolean update(Connection conn, String sql) throws SQLException {
		return dbAdapter.update(conn, sql);
	}

	@Override
	public boolean update(Connection conn, String sql, Object[] params) throws SQLException {
		return dbAdapter.update(conn, sql, params);
	}

	@Override
	public boolean delete(String sql, Object[] params) throws SQLException {
		Connection conn = tr.getConnection();
		if(conn==null){
			return dbAdapter.delete(sql, params);
		}
		return dbAdapter.delete(conn, sql, params);
	}

	@Override
	public boolean delete(String sql) throws SQLException {
		Connection conn = tr.getConnection();
		if(conn==null){
			return dbAdapter.delete(sql);
		}
		return dbAdapter.delete(conn, sql);
	}

	@Override
	public boolean delete(Connection conn, String sql) throws SQLException {
		return dbAdapter.delete(conn, sql);
	}

	@Override
	public boolean delete(Connection conn, String sql, Object[] params) throws SQLException {
		return dbAdapter.delete(conn, sql, params);
	}

	@Override
	public String getOwner() throws SQLException {
		return dbAdapter.getOwner();
	}

	@Override
	public boolean batch(String sql, List<Object[]> params) throws SQLException {
		return dbAdapter.batch(sql, params);
	}

	@Override
	public int getSequence(String tableName) throws SQLException {
		return dbAdapter.getSequence(tableName);
	}

	@Override
	public Connection getConnection() throws SQLException {
		return dbAdapter.getConnection();
	}

	@Override
	public Transaction getTransactionManager() throws SQLException {
		return tr;
	}

	@Override
	public boolean isActive() {
		return dbAdapter.isActive();
	}

}
