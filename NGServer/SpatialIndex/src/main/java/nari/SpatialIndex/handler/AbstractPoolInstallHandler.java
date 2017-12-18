package nari.SpatialIndex.handler;

import nari.SpatialIndex.index.BlockingQueueCluster;
import nari.SpatialIndex.searcher.Record;

public abstract class AbstractPoolInstallHandler extends AbstractInstallHandler {

	private BlockingQueueCluster cluster;
	
	@Override
	protected boolean doInit() throws Exception {
		cluster = new BlockingQueueCluster(Runtime.getRuntime().availableProcessors()+1);
		return true;
	}
	
	@Override
	protected Record getRecord() throws Exception {
		return cluster.take();
	}
	
	protected void addRecods(Record[] records) throws Exception {
		if(records!=null && records.length>0){
			for(Record record:records){
				cluster.put(record);
			}
		}
	}
	
	protected void interruper() throws InterruptedException{
		cluster.interruper();
	}
}
