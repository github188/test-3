package nari.Dao.bundle.simple;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;


/**
 * SimpleDao实现的模板类
 * @author 
 * @version
 */
public abstract class SimpleDaoSupport implements SimpleDao{
	
	private QueryRunner queryRunner;
	
	private String owner;
	
	public QueryRunner getQueryRunner() {
		return queryRunner;
	}

	public void setQueryRunner(QueryRunner queryRunner) {
		this.queryRunner = queryRunner;
	}

	
	public final String getOwner() {
		return owner;
	}

	public final void setOwner(String owner) {
		this.owner = owner;
	}

	/** DML操作 */
	protected boolean executeUpdate(String sql, Object[] params) {
		try {
			queryRunner.update(sql, params);
			return true;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return false;
		}	
	}
	
	protected boolean executeUpdate(Connection conn,String sql, Object[] params) {
		try {
			queryRunner.update(conn,sql, params);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}	
	}
	
	/** 批量执行数据更新 */
	protected boolean executeBatch(String sql, List<Object[]> paramList) {	
		Object[][] params = new Object[paramList.size()][];
		for (int i = 0; i < paramList.size(); i++) {
			params[i] = paramList.get(i);
		}
		try {
			queryRunner.batch(sql, params);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/** sql查询 */
	protected <T> T executeQuery(String sql, Object[] params, ResultSetHandler<T> handler) {
		T result = null;
		try {
			result = queryRunner.query(sql, handler, params);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 多个表批量提交
	 * @param batchs
	 * @return
	 */
	protected boolean batchAndCommit(Connection conn ,String sql, List<Object[]> paramList){
		Object[][] params = new Object[paramList.size()][];
		for (int i = 0; i < paramList.size(); i++) {
			params[i] = paramList.get(i);
		}
		try {
			queryRunner.batch(conn, sql, params);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
