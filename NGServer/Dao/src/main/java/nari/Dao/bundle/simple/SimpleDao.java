package nari.Dao.bundle.simple;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import nari.Dao.bundle.bean.ResultHandler;

/**
 * 数据访问接口定义
 * @author
 * @version
 */
public interface SimpleDao {
	
	/**
	 * 查询一条记录
	 * @param <T>
	 * @param sql
	 * @return
	 */
	Map<String,Object> findMap(String sql) throws SQLException;
	
	/**
	 * 查询一条记录
	 * @param <T>
	 * @param sql
	 * @param clazz
	 * @return
	 */
	<T> T find(String sql, Class<T> clazz) throws SQLException;
	
	/**
	 * 查询一条记录
	 * @param <T>
	 * @param sql
	 * @param params
	 * @return
	 */
	Map<String,Object> findMap(String sql, Object[] params) throws SQLException;
	
	/**
	 * 查询一条记录
	 * @param <T>
	 * @param sql
	 * @param params
	 * @param clazz
	 * @return
	 */
	<T> T find(String sql, Object[] params, Class<T> clazz) throws SQLException;
	
	/**
	 * 查询多条记录
	 * @param <E>
	 * @param sql
	 * @return
	 */
	List<Map<String,Object>> findAllMap(String sql) throws SQLException;
	
	/**
	 * 查询多条记录
	 * @param <E>
	 * @param sql
	 * @param params
	 * @return
	 */
	List<Map<String,Object>> findAllMap(String sql, Object[] params) throws SQLException;
	
	/**
	 * 查询多条记录
	 * @param <E>
	 * @param sql
	 * @param clazz
	 * @return
	 */
	<E> List<E> findAll(String sql, Class<E> clazz) throws SQLException;
	
	/**
	 * 查询多条记录
	 * @param <E>
	 * @param sql
	 * @param params
	 * @param clazz
	 * @return
	 */
	<E> List<E> findAll(String sql, Object[] params, Class<E> clazz) throws SQLException;
	
	/**
	 * 查询数据
	 * @param sql 查询数据时使用的SQL语句
	 * @param params 替换SQL语句中的?参数，其个数与SQL语句中的?符号保持一致
	 * @param handler 对查询结果的处理参数
	 * @return
	 */
	<T> T query(String sql, Object[] params, ResultHandler<T> handler) throws SQLException;
	
	/**
	 * 查询数据
	 * @param sql 查询数据时使用的SQL语句
	 * @param handler 对查询结果的处理参数
	 * @return
	 */
	<T> T query(String sql, ResultHandler<T> handler) throws SQLException;
	
	/**
	 * 保存记录
	 * @param sql
	 * @param params
	 * @return
	 */
	boolean save(String sql, Object[] params) throws SQLException;
	
	/**
	 * 保存记录
	 * @param sql
	 * @return
	 */
	boolean save(String sql) throws SQLException;
	
	/**
	 * 更新记录
	 * @param sql
	 * @param params
	 * @return
	 */
	boolean update(String sql, Object[] params) throws SQLException;
	
	/**
	 * 更新记录
	 * @param sql
	 * @return
	 */
	boolean update(String sql) throws SQLException;
	
	/**
	 * 删除记录
	 * @param sql
	 * @param params
	 * @return
	 */
	boolean delete(String sql, Object[] params) throws SQLException;
	
	/**
	 * 删除记录
	 * @param sql
	 * @return
	 */
	boolean delete(String sql) throws SQLException;
	
	/**
	 * 获取数据库owner
	 * @return
	 */
	String getOwner();
	
	/**
	 * 批处理
	 * @param sql
	 * @param params
	 * @return
	 */
	boolean batch(String sql, List<Object[]> params) throws SQLException;
	
	/**
	 * 事物
	 * @param actions
	 * @param autoCommit
	 * @return
	 */
	boolean execTransaction(TransAction[] actions,boolean autoCommit) throws SQLException;
	
	Connection getConnection() throws SQLException;
	
	boolean delete(Connection conn, String sql) throws SQLException;

	boolean delete(Connection conn, String sql, Object[] params) throws SQLException;

	boolean save(Connection conn, String sql) throws SQLException;

	boolean save(Connection conn, String sql, Object[] params) throws SQLException;

	boolean update(Connection conn, String sql) throws SQLException;

	boolean update(Connection conn, String sql, Object[] params) throws SQLException;
}
