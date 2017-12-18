package nari.Dao.bundle.handler;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

/**
 * 按照Map结构保存的缺省结果集处理器
 * @author
 * @since
 */
public class DefaultMapResultHandler extends MapResultHandler<String,Object> {
	
	@Override
	protected String createKey(ResultSet rs, ResultSetMetaData metaData, int currentColumn) throws SQLException {
		return metaData.getColumnLabel(currentColumn).toLowerCase();
	}

	@Override
	protected Object createValue(ResultSet rs, ResultSetMetaData metaData, int currentColumn) throws SQLException {
		int columnType = metaData.getColumnType(currentColumn);
		if (columnType == Types.BLOB) {
			return rs.getBlob(currentColumn);
		}
		if (columnType == Types.CLOB) {
			return rs.getClob(currentColumn);
		}
		if (columnType == Types.DATE&&rs.getTimestamp(currentColumn)!=null) {
			Timestamp timestamp = rs.getTimestamp(currentColumn);
			return new Date(timestamp.getTime());
		}
		return rs.getObject(currentColumn);
	}

}
