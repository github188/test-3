package nari.Dao.bundle.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import nari.Dao.bundle.factory.HandlerFactory;

import org.apache.commons.dbutils.ResultSetHandler;


/**
 * 按照List形式处理保存结果集的抽象基类
 * @author
 * @since
 */
public abstract class ListResultHandler <T> implements ResultSetHandler<List<T>> {
	
	private int resultType;
	
	public ListResultHandler() {
		this.resultType = HandlerFactory.ARRAY_LIST;
	}

	public int getResultType() {
		return resultType;
	}

	public void setResultType(int resultType) {
		this.resultType = resultType;
	}

	public List<T> handle(ResultSet rs) throws SQLException {
		List<T> result = getResultList();
		while (rs.next()) {
			result.add(processRow(rs));
		}
		return result;
	}
	
	private List<T> getResultList() {
		return HandlerFactory.getResultCollection(resultType);
	}
	
	protected abstract T processRow(ResultSet rs) throws SQLException;
}
