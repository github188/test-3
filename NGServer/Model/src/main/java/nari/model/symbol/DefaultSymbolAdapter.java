package nari.model.symbol;

import nari.model.device.SymbolProviderCluster;

public class DefaultSymbolAdapter extends AbstractSymbolAdapter {

	private SymbolProviderCluster cluster;
	
	public DefaultSymbolAdapter(SymbolProviderCluster cluster){
		this.cluster = cluster;
	}
	
	@Override
	protected SymbolProvider getProvider() {
		return cluster.getProvider();
	}

}
