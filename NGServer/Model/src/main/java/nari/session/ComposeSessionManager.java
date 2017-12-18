package nari.session;

import org.apache.commons.lang.StringUtils;

import nari.session.access.AccessCluster;
import nari.session.access.DataSourceAccess;

public class ComposeSessionManager extends AbstractSessionManager {

	private AccessCluster cluster = null;
	
	public ComposeSessionManager(AccessCluster cluster) {
		this.cluster = cluster;
	}

	@Override
	public DataSourceAccess selectDataSource(String session) throws Exception {
		if(cluster==null){
			return null;
		}
		return StringUtils.isEmpty(session)?cluster.getDefault():cluster.find(session);
	}
	
	@Override
	public DataSourceAccess[] selectAllDataSource() throws Exception {
		if(cluster==null){
			return null;
		}
		return cluster.getAll();
	}
}
