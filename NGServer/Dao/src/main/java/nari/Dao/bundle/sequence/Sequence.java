package nari.Dao.bundle.sequence;

import java.sql.SQLException;

public interface Sequence {
	
	public int getSequence(String tableName) throws SQLException;
}
