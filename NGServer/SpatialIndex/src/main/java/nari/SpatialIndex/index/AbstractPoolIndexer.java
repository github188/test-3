package nari.SpatialIndex.index;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import nari.SpatialIndex.Lifecycle;
import nari.SpatialIndex.handler.IndexerInstallHandler;
import nari.SpatialIndex.loader.IndexReader;
import nari.SpatialIndex.searcher.Record;

public abstract class AbstractPoolIndexer extends AbstractIndexer {

	private BlockingQueueCluster cluster;
	
	private ExecutorService executorService;
	
	private IndexReader reader;
	
	
	public AbstractPoolIndexer(IndexAttribute attribute) {
		super(attribute);
	}

	@Override
	protected void doInstall(final IndexerInstallHandler handler) throws Exception {
		for(int i=0;i<5;i++){
			executorService.submit(new Callable<Boolean>() {
				
				@Override
				public Boolean call() throws Exception {
					if(!handler.init()){
						return false;
					}
					
					Record record = handler.next();
					while(record!=null){
						cluster.put(record);
						record = handler.next();
					}
					cluster.interruper();
					return true;
				}
			});
		}
		
	}
	
	@Override
	protected void doInit() throws Exception {
		executorService = Executors.newFixedThreadPool(1);
		cluster = new BlockingQueueCluster(Runtime.getRuntime().availableProcessors()*2+1);
	}
	
	@Override
	protected void doStart() throws Exception {
		if(reader==null){
			reader = getIndexReader();
		}
		
		if(reader instanceof Lifecycle){
			((Lifecycle)reader).init();
			((Lifecycle)reader).start();
		}
		
	}
	
	@Override
	protected void doStop() throws Exception {
		executorService.shutdown();
		cluster.clear();
	}
	
	@Override
	protected Record next() throws Exception {
		return cluster.take();
	}

}
