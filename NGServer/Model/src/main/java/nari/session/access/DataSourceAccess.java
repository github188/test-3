package nari.session.access;

public interface DataSourceAccess {

	public String getDataSourceName() throws Exception;
	
	public ModelDataSource getDataSource() throws Exception;
	
}
