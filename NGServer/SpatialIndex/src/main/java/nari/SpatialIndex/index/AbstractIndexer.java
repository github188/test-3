package nari.SpatialIndex.index;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import nari.SpatialIndex.InitException;
import nari.SpatialIndex.Lifecycle;
import nari.SpatialIndex.StartException;
import nari.SpatialIndex.StopException;
import nari.SpatialIndex.handler.IndexerInstallHandler;
import nari.SpatialIndex.handler.Notifier;
import nari.SpatialIndex.handler.NotifyEvent;
import nari.SpatialIndex.loader.IndexReader;
import nari.SpatialIndex.loader.IndexWriter;
import nari.SpatialIndex.searcher.IndexLayer;
import nari.SpatialIndex.searcher.NotifierManager;
import nari.SpatialIndex.searcher.Record;
import nari.SpatialIndex.searcher.ResultSet;
import nari.SpatialIndex.searcher.SearchKey;

public abstract class AbstractIndexer implements Indexer {

	private IndexAttribute attribute;
	
	private boolean init = false;
	
	private boolean isInstalled = false;
	
	private ExecutorService executorService ;
	
	private IndexWriter writer;
	
	public AbstractIndexer(IndexAttribute attribute){
		this.attribute = attribute;
	}
	
	@Override
	public boolean init() throws InitException {
		try {
			doInit();
		} catch (Exception e) {
			throw new InitException(e.getMessage(),e);
		}
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2+1);
		init = true;
		
		try {
			NotifierManager.get().notify(NotifyEvent.INIT, this);
		} catch (Exception e) {
			throw new InitException(e.getMessage(),e);
		}
		return init;
	}

	@Override
	public boolean start() throws StartException {
		try {
			doStart();
		} catch (Exception e) {
			throw new StartException(e.getMessage(),e);
		}
		return true;
	}

	@Override
	public boolean stop() throws StopException {
		if(executorService!=null && !executorService.isShutdown()){
			executorService.shutdown();
		}
		return true;
	}

	@Override
	public boolean destory() throws Exception {
		return true;
	}
	
	@Override
	public boolean install() throws Exception {
		if(!init){
			init();
		}
		
		if(writer==null){
			writer = getIndexWriter();
		}
		
		if(writer==null){
			return false;
		}
		
		if(writer instanceof Lifecycle){
			((Lifecycle)writer).init();
			((Lifecycle)writer).start();
		}
		
		doInstall(getInstallHandler());
		
		long s = System.currentTimeMillis();
		
		int count = Runtime.getRuntime().availableProcessors()+1;
		final CountDownLatch latch = new CountDownLatch(count*2);
		
		for(int k=0;k<count*2;k++){
			executorService.submit(new Runnable() {
				
				@Override
				public void run() {
					Record record;
					try {
						record = AbstractIndexer.this.next();
						while(record!=null){
							IndexLayer layer = null;
							layer = record.getLayer();
							writer.write(layer,record,GridMapper.get());
							record = next();
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally{
						latch.countDown();
					}
				}
			});
		}
		latch.await();
		if(writer instanceof Lifecycle){
			((Lifecycle)writer).stop();
		}
		NotifierManager.get().notify(NotifyEvent.INSTALL, this);
		
		start();
		
		long e = System.currentTimeMillis();
		System.out.println("take complate use:"+String.valueOf(e-s)+"ms");
		executorService.shutdown();
		isInstalled = true;
		return true;
	}
	
	@Override
	public boolean isStop() {
		return false;
	}
	
	@Override
	public boolean isInited() {
		return init;
	}
	
	@Override
	public boolean isInstalled() {
		return isInstalled;
	}

	@Override
	public URI getIndexURI() {
		return null;
	}
	
	@Override
	public String getIndexerName() {
		return attribute.name();
	}
	
	@Override
	public IndexType getIndexType() {
		return attribute.getIndexType();
	}
	
	@Override
	public ResultSet search(SearchKey key) throws Exception {
		return null;
	}
	
	protected abstract Record next() throws Exception;
	
	protected abstract void doInstall(IndexerInstallHandler handler) throws Exception;
	
	protected abstract void doSearch() throws Exception;
	
	protected abstract void doInit() throws Exception;
	
	protected abstract void doStart() throws Exception;
	
	protected abstract void doStop() throws Exception;
	
	protected abstract IndexerInstallHandler getInstallHandler();
	
	protected abstract Notifier getNotifier();
	
	protected abstract IndexReader getIndexReader() throws Exception;
	
	protected abstract IndexWriter getIndexWriter() throws Exception;
}
