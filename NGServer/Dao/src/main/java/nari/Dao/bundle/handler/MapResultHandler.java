package nari.Dao.bundle.handler;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nari.Dao.bundle.factory.HandlerFactory;

import org.apache.commons.dbutils.ResultSetHandler;

/**
 * 按照Map形式处理保存结果集的抽象基类
 * @author
 * @since
 */
public abstract class MapResultHandler <K, V> implements ResultSetHandler<List<Map<K, V>>> {
	
	private int resultType;
	
	public MapResultHandler() {
		this.resultType = HandlerFactory.ARRAY_LIST;
	}

	public int getResultType() {
		return resultType;
	}

	public void setResultType(int resultType) {
		this.resultType = resultType;
	}

	public List<Map<K, V>> handle(ResultSet rs) throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();
		List<Map<K, V>> result = getResultList();
		while (rs.next()) {
			Map<K, V> map = new HashMap<K, V>();
			for (int i = 1; i <= columnCount; i++) {
				map.put(createKey(rs, metaData, i), createValue(rs, metaData, i));
			}			
			result.add(map);
		}
		return result;
	}
	
	private List<Map<K, V>> getResultList() {
		return HandlerFactory.getResultCollection(resultType);
	}
	
	protected abstract K createKey(ResultSet rs, ResultSetMetaData metaData, int currentColumn) throws SQLException;
	
	protected abstract V createValue(ResultSet rs, ResultSetMetaData metaData, int currentColumn) throws SQLException;
}
