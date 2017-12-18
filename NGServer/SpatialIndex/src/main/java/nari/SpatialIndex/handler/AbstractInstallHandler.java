package nari.SpatialIndex.handler;

import nari.SpatialIndex.searcher.Record;

public abstract class AbstractInstallHandler implements IndexerInstallHandler {
	
	private boolean isInit = false;
	
	private boolean isStart = false;
	
	private boolean isStop = false;
	
	public AbstractInstallHandler(){
		
	}
	
	@Override
	public boolean init() throws Exception {
		if(!isInit){
			boolean suc = doInit();
			isInit = true;
			return suc;
		}
		return true;
	}
	
	@Override
	public boolean start() throws Exception {
		if(!isStart){
			if(doStart()){
				isStart = true;
				return true;
			}
			return false;
		}
		return true;
	}
	
	@Override
	public boolean stop() throws Exception {
		if(!isStop){
			return doStop();
		}
		return true;
	}
	
	@Override
	public Record next() throws Exception {
		if(!isInit){
			init();
		}
		if(!isStart){
			start();
		}
		if(isStop){
			return null;
		}
		return getRecord();
	}
	
	@Override
	public boolean isInit() {
		return isInit;
	}
	
	protected abstract Record getRecord() throws Exception;
	
	protected abstract boolean doInit() throws Exception;
	
	protected abstract boolean doStart() throws Exception;
	
	protected abstract boolean doStop() throws Exception;
}
