package nari.model.cluster;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReference;

import nari.model.symbol.SymbolProvider;

public class ModelSymbolProviderCluster extends AbstractSymbolProviderCluster{

	private SymbolProvider[] providerClusters;
	
	private final AtomicReference<SymbolProvider> currentProvider = new AtomicReference<SymbolProvider>(null);
	
	public ModelSymbolProviderCluster(SymbolProvider[] providerClusters) {
		this.providerClusters = providerClusters;
		
		Arrays.sort(providerClusters, new Comparator<SymbolProvider>() {
			
			@Override
			public int compare(SymbolProvider sp1, SymbolProvider sp2) {
				if(sp1.getPriority()>sp2.getPriority()){
					return 1;
				}
				if(sp1.getPriority()<sp2.getPriority()){
					return -1;
				}
				return 0;
			}
			
		});
		
		this.providerClusters = providerClusters;
		
		currentProvider.getAndSet(providerClusters[providerClusters.length-1]);
	}
	
	@Override
	protected SymbolProvider select() {
		return currentProvider.get();
	}

	@Override
	protected void update() {
		SymbolProvider provider = currentProvider.get();
		
		if(!provider.selfCheck()){
			for(int i=providerClusters.length-1;i>=0;i--){
				if(currentProvider.get()!=providerClusters[i]){
					currentProvider.getAndSet(providerClusters[i]);
					break;
				}
			}
		}else{
			for(SymbolProvider p:providerClusters){
				if(p!=currentProvider.get() && p.selfCheck() && p.getPriority()>provider.getPriority()){
					currentProvider.getAndSet(p);
					break;
				}
			}
		}
	}
}
