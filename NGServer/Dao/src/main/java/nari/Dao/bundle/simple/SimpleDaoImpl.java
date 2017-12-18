package nari.Dao.bundle.simple;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import nari.Dao.bundle.bean.ResultHandler;
import nari.Dao.bundle.factory.HandlerFactory;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 * 数据库操作实现类
 * @author
 *
 */
public class SimpleDaoImpl extends SimpleDaoSupport {

	public SimpleDaoImpl(QueryRunner queryRunner,String owner){
		setQueryRunner(queryRunner);
		setOwner(owner);
	}
	
	/**
	 * 参数化数据库保存
	 */
	public boolean save(String sql, Object[] params) {
		if (sql.trim().toLowerCase().matches("insert(\\s+.+)")) {
			return executeUpdate(sql, params);
		}
		return false;
	}
	
	/**
	 * 数据库保存
	 */
	public boolean save(String sql) {
		return save(sql, null);
	}
	
	/**
	 * 删除
	 */
	public boolean delete(String sql, Object[] params) {
		if (sql.trim().toLowerCase().matches("delete(\\s+.+)")) {
			return executeUpdate(sql, params);
		}
		return false;
	}
	
	/**
	 * 删除
	 */
	public boolean delete(String sql) {
		return delete(sql, null);
	}
	
	/**
	 * 更新
	 */
	public boolean update(String sql, Object[] params) {
		if (sql.trim().toLowerCase().matches("update(\\s+.+)")) {
			return executeUpdate(sql, params);
		}
		return false;
	}
	
	/**
	 * 更新
	 */
	public boolean update(String sql) {
		return update(sql, null);
	}
	
	/**
	 * 单条查询
	 */
	public Map<String,Object> findMap(String sql) {
		return findMap(sql, null);
	}
	
	/**
	 * 单条查询
	 */
	public Map<String,Object> findMap(String sql, Object[] params) {
		List<Map<String,Object>> result = findAllMap(sql, params);	
		return getSingleResult(result);
	}
	
	/**
	 * 单条查询
	 */
	public <T> T find(String sql, Class<T> clazz) {
		return find(sql, null, clazz);
	}
	
	/**
	 * 单条查询
	 */
	public <T> T find(String sql, Object[] params, Class<T> clazz) {
		List<T> result = findAll(sql, params, clazz);
		return getSingleResult(result);
	}
	
	/**
	 * 查询
	 */
	public List<Map<String,Object>> findAllMap(String sql) {
		return findAllMap(sql, null);
	}
	
	
	public List<Map<String,Object>> findAllMap(String sql, Object[] params) {
		return executeQuery(sql, params, getDefaultHandler());
	}
	
	public <E> List<E> findAll(String sql, Class<E> clazz) {
		return findAll(sql, null, clazz);
	}
	
	/**
	 * 查询
	 */
	public <E> List<E> findAll(String sql, Object[] params, Class<E> clazz) {
		BeanListHandler<E> handler = new BeanListHandler<E>(clazz);
		return executeQuery(sql, params, getCustomBeanHandler(handler));
	}
	
	@Override
	public <T> T query(String sql, Object[] params, final ResultHandler<T> handler) {
		
		return executeQuery(sql, params, new ResultSetHandler<T>() {

			@Override
			public T handle(ResultSet resultSet) throws SQLException {
				return handler.handle(resultSet);
			}
			
		});
	}
	
	@Override
	public <T> T query(String sql, final ResultHandler<T> handler) {
		
		return query(sql, null, handler);
	}
	
	/**
	 * 批量处理
	 */
	public boolean batch(String sql, List<Object[]> params) {
		return this.executeBatch(sql, params);
	}
	
	/**
	 * 事物处理
	 */
	public boolean execTransaction(TransAction[] actions,boolean autoCommit){
		Connection conn = getConnection();
		try {
			conn.setAutoCommit(false);
			boolean bb = true;
			for(TransAction action:actions){
				if(action==null){
					continue;
				}
				boolean b = batchAndCommit(conn,action.getAction(),action.getParams());
				bb = b && bb;
			}
			if(bb){
				if(autoCommit){
					conn.commit();
					conn.setAutoCommit(true);
				}else{
					conn.rollback();
					conn.setAutoCommit(true);
				}
				conn.close();
				return true;
			}else{
				conn.rollback();
				conn.setAutoCommit(true);
				conn.close();
				return false;
			}
		} catch (SQLException e) {
			try {
				conn.rollback();
				conn.setAutoCommit(true);
				conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			e.printStackTrace();
			return false;
		}
	}
	
	private ResultSetHandler<List<Map<String,Object>>> getDefaultHandler() {
		return HandlerFactory.getMapHandler();
	}
	
	private <E> ResultSetHandler<List<E>> getCustomBeanHandler(BeanListHandler<E> resultHandler) {
		return resultHandler;
	}
	
	private <E> E getSingleResult(List<E> result) {
		if (result == null || result.size() == 0) {
			return null;
		}
		return result.get(0);
	}
	
	public Connection getConnection() {
		Connection conn = null;
		if(isConnected()){
			try {
				conn = getQueryRunner().getDataSource().getConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return conn;
	}
	
	private boolean isConnected() {
		boolean canConnected = false;
		try{
			canConnected = this.getQueryRunner() != null;
		}
		catch (Exception e) {
			return false;
		}
		return canConnected;
	}

	public boolean delete(Connection conn, String sql) {
		return delete(conn,sql,null);
	}

	public boolean delete(Connection conn, String sql, Object[] params) {
		if (sql.trim().toLowerCase().matches("delete(\\s+.+)")) {
			return executeUpdate(conn, sql, params);
		}
		return false;
	}

	public boolean save(Connection conn, String sql) {
		return save(conn,sql,null);
	}

	public boolean save(Connection conn, String sql, Object[] params) {
		if (sql.trim().toLowerCase().matches("insert(\\s+.+)")) {
			return executeUpdate(conn, sql, params);
		}
		return false;
	}

	public boolean update(Connection conn, String sql) {
		return update(conn,sql,null);
	}

	public boolean update(Connection conn, String sql, Object[] params) {
		if (sql.trim().toLowerCase().matches("update(\\s+.+)")) {
			return executeUpdate(conn, sql, params);
		}
		return false;
	}
}
