package nari.SpatialIndex.handler;

import nari.SpatialIndex.index.Indexer;

public interface Notifier {

	public void notify(NotifyEvent event,Indexer indexer) throws Exception;
	
}
