package nari.Dao.bundle.transaction;

import java.sql.Connection;
import java.sql.SQLException;

public interface Transaction {
	
	public void commit()  throws SQLException;
	
//	public void startTransaction() throws SQLException;
//	
//	public void stopTransaction() throws SQLException;
	
	public void start() throws SQLException;
	
	public void stop() throws SQLException;
	
//	public void stopTransactionAndCommit() throws SQLException;
	
	public void addRollbackPoint(String pointName) throws SQLException;
	
	public void rollback() throws SQLException;
	
	public void rollback(String pointName) throws SQLException;
	
	public Connection getConnection() throws SQLException;
	
//	public void execTransaction(String sql) throws SQLException;
//	
//	public void execTransaction(String sql, Object[] params) throws SQLException;
//	
//	public boolean isSuccess() throws SQLException;
//	
//	public boolean isStarted() throws SQLException;
//	
//	public boolean delete(String sql) throws SQLException;
//
//	public boolean delete(String sql, Object[] params) throws SQLException;
//
//	public boolean save(String sql) throws SQLException;
//
//	public boolean save(String sql, Object[] params) throws SQLException;
//	
//	public boolean update(String sql) throws SQLException;
//
//	public boolean update(String sql, Object[] params) throws SQLException;
//	
//	public boolean batch(String sql, List<Object[]> params) throws SQLException;
//	
//	public Map<String,Object> findMap(String sql) throws SQLException;
//	
//	public <T> T find(String sql, Class<T> clazz) throws SQLException;
//	
//	public Map<String,Object> findMap(String sql, Object[] params) throws SQLException;
//	
//	public <T> T find(String sql, Object[] params, Class<T> clazz) throws SQLException;
//	
//	public List<Map<String,Object>> findAllMap(String sql) throws SQLException;
//	
//	public List<Map<String,Object>> findAllMap(String sql, Object[] params) throws SQLException;
//	
//	public <E> List<E> findAll(String sql, Class<E> clazz) throws SQLException;
//	
//	public <E> List<E> findAll(String sql, Object[] params, Class<E> clazz) throws SQLException;
}
