package nari.Dao.bundle.bean;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultHandler <T> {
	
	T handle(ResultSet resultSet) throws SQLException;

}
