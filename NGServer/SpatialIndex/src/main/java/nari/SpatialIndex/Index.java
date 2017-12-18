package nari.SpatialIndex;

import nari.SpatialIndex.factory.IndexerFactory;
import nari.SpatialIndex.handler.IndexerInstallHandler;
import nari.SpatialIndex.index.IndexAttribute;
import nari.SpatialIndex.index.Indexer;
import nari.SpatialIndex.index.IndexerHolder;

public class Index {
	
	public static Indexer install(IndexAttribute attribute,IndexerInstallHandler handler) throws Exception {
		Indexer indexer = IndexerHolder.get().getIndexer(attribute.name());
		
		if(indexer==null){
			indexer = IndexerFactory.getFactory().createIndexer(attribute);
		}
		
		boolean suc = false;
		
		if(!indexer.isInstalled()){
			suc = indexer.install();
		}
		
		if(!suc){
			return null;
		}
		
		return indexer;
	}
	
	public static Indexer init(IndexAttribute attribute) throws Exception {
		Indexer indexer = IndexerHolder.get().getIndexer(attribute.name());
		if(indexer == null){
			return null;
		}
		
		if(indexer.isInited()){
			return indexer;
		}
		
		if(!indexer.init()){
			return null;
		}
		
		if(!indexer.start()){
			return null;
		}
		
		return indexer;
	}
	
}
