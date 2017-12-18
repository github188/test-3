package nari.SpatialIndex.index;

import java.util.concurrent.atomic.AtomicReference;

import nari.SpatialIndex.handler.GridInstallHandler;
import nari.SpatialIndex.handler.IndexerInstallHandler;
import nari.SpatialIndex.handler.Notifier;
import nari.SpatialIndex.handler.NotifyEvent;
import nari.SpatialIndex.loader.GridIndexLoader;
import nari.SpatialIndex.loader.GridIndexReader;
import nari.SpatialIndex.loader.GridIndexWriter;
import nari.SpatialIndex.loader.IndexLoader;
import nari.SpatialIndex.loader.IndexReader;
import nari.SpatialIndex.loader.IndexWriter;
import nari.SpatialIndex.searcher.NotifierManager;

public class GridIndexer extends AbstractPoolIndexer {

	private IndexAttribute attribute;
	
	private AtomicReference<IndexLoader> loaderRef = new AtomicReference<IndexLoader>();
	
	public GridIndexer(IndexAttribute attribute) {
		super(attribute);
		this.attribute = attribute;
	}
	
	@Override
	protected void doInit() throws Exception {
		super.doInit();
		
		NotifierManager.get().registHook(NotifyEvent.INIT, getNotifier());
		NotifierManager.get().registHook(NotifyEvent.INSTALL, getNotifier());
	}

	@Override
	protected void doStart() throws Exception {
		super.doStart();
	}

	@Override
	protected void doStop() throws Exception {
		super.doStop();
	}

	@Override
	protected void doSearch() throws Exception {
		
	}

	@Override
	protected IndexerInstallHandler getInstallHandler() {
		return new GridInstallHandler();
	}
	
	@Override
	protected Notifier getNotifier() {
		return new Notifier() {
			
			@Override
			public void notify(NotifyEvent event, Indexer indexer) throws Exception {
				
			}
		};
	}
	
	@Override
	public IndexReader getIndexReader() throws Exception {
		IndexLoader loader = getIndexLoader();
		IndexReader reader = new GridIndexReader(loader.loadStorage(attribute));
		return reader;
	}
	
	@Override
	public IndexWriter getIndexWriter() throws Exception {
		IndexLoader loader = getIndexLoader();
		IndexWriter writer = new GridIndexWriter(loader.loadStorage(attribute));
		return writer;
	}
	
	private IndexLoader getIndexLoader(){
		if(loaderRef.get()==null){
			IndexLoader loader = new GridIndexLoader();
			loaderRef.compareAndSet(null, loader);
		}
		return loaderRef.get();
	}
}
