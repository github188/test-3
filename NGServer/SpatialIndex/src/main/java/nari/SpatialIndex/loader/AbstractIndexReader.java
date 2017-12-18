package nari.SpatialIndex.loader;

import java.io.File;

import nari.SpatialIndex.Lifecycle;
import nari.SpatialIndex.index.GridSpliter;
import nari.SpatialIndex.index.Spliter;
import nari.SpatialIndex.searcher.IndexLayer;

public abstract class AbstractIndexReader implements IndexReader {

	private Spliter spliter;
	
	private Disk disk = null;
	
	private boolean split = true;
	
	public AbstractIndexReader(){
		
	}
	
	@Override
	public boolean init() throws Exception {
		if(disk==null){
			disk = getDisk();
		}
		
		File file = new File(disk.getFile(),"index");
		if(file.exists()){
			split = false;
		}else{
			file.createNewFile();
		}
		
		return true;
	}

	@Override
	public boolean start() throws Exception {
		doStart();
		
		if(split){
			if(spliter==null){
				spliter = new GridSpliter(disk,this);
			}
			
			if(spliter instanceof Lifecycle){
				((Lifecycle)spliter).init();
				((Lifecycle)spliter).start();
			}
		}else{
			
			
			//加载缓存信息
			
		}
		
		return true;
	}

	@Override
	public boolean stop() throws Exception {
		return doStop();
	}

	@Override
	public void read(IndexLayer layer) throws Exception {
		
	}

	protected abstract Disk getDisk() throws Exception;
	
	protected abstract boolean doInit() throws Exception;
	
	protected abstract boolean doStart() throws Exception;
	
	protected abstract boolean doStop() throws Exception;
	
}
