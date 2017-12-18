package nari.session;

import java.io.InputStream;

import nari.Xml.impl.ConfigSearchService;
import nari.Xml.interfaces.ConfigSearch;
import nari.session.access.AccessCluster;
import nari.session.config.DataSourceConfig;

public class Configuration {

	public SessionManager build(String name,InputStream stream) throws Exception{
		ConfigSearch searcher = new ConfigSearchService();
		DataSourceConfig ds = searcher.loadConfigCache(name,stream,"xml",DataSourceConfig.class);
		if(ds==null){
			return null;
		}
		AccessCluster dsAccess = initDataSource(ds);
		return new ComposeSessionManager(dsAccess);
	}
	
	private AccessCluster initDataSource(DataSourceConfig ds){
		
		
		
		
		
		return new AccessCluster();
	}
}
