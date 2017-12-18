package nari.model.device;

import nari.model.cluster.ProviderCluster;

public class DefaultModelService extends AbstractModelService{

	private ProviderCluster cluster;
	
	public DefaultModelService(ProviderCluster cluster){
		this.cluster = cluster;
		
		// 为了加载配置缓存，先get一下
		getProvider().get();
	}
	
	@Override
	protected SelectorProvider getProvider() {
		return cluster.getProvider();
	}
	
}
