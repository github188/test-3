package nari.SpatialIndex.loader;

import nari.SpatialIndex.index.Grid;
import nari.SpatialIndex.searcher.GridIndexLayer;
import nari.SpatialIndex.searcher.IndexLayer;

public abstract class AbstractDisk implements Disk {

//	private Disk parent;
	
//	public AbstractDisk(Disk parent){
//		this.parent = parent;
//	}
	
//	private static volatile ConcurrentHashMap<String,IndexLayer> cache = new ConcurrentHashMap<String,IndexLayer>();
	
	public AbstractDisk(){

	}
	
	@Override
	public Disk makeDisk() throws Exception {
		makeDir();
//		Disk disk = new PhysicsDisk(this);
		return this;
	}

	@Override
	public Disk getDisk(String name) throws Exception {
//		Disk disk = new PhysicsDisk(getPath());
//		disk.getDisk(name);
//		doGetDisk(name);
		Disk disk = new PhysicsDisk(this,name);
		return disk;
	}

	@Override
	public boolean exist() throws Exception {
		return exists();
	}

	@Override
	public int getRecordCount() {
		return 0;
	}

	@Override
	public IndexLayer getLayer(IndexLayer layer,Grid grid) throws Exception {
//		IndexLayer ly = LayerHolder.get().get(this.getFile().getName()+"-"+layer.getLayerID());
//		IndexLayer ly = new GridIndexLayer(layer.getLayerID(), layer.getLayerName(),this.getDisk(layer.getLayerID()));
//		System.out.println(this.getFile().getPath()+"-"+layer.getLayerID());
//		if(ly==null){
			
//			LayerHolder.get().put(this.getFile().getName()+"-"+layer.getLayerID(), ly);
//		}
//		String key = grid.getGridId()+"$"+layer.getLayerID();
		
		IndexLayer ly=null;
//		if(ly==null){
//			ly = cache.get(key);
//			synchronized (cache) {
				ly = new GridIndexLayer(layer.getLayerID(), layer.getLayerName(),this.getDisk(layer.getLayerID()),grid);
//				System.out.println(key);
//				cache.putIfAbsent(key, ly);
//			}
//		}
		
		return ly;
	}

	@Override
	public boolean hasLayer(IndexLayer layer) throws Exception {
		return false;
	}

	@Override
	public boolean createLayer(IndexLayer layer) throws Exception {
		return false;
	}

	@Override
	public int length() throws Exception {
		return 0;
	}
	
	@Override
	public byte[] read(int offset, int length) throws Exception {
		return null;
	}
	
	@Override
	public byte read() throws Exception {
		return 0;
	}
	
	@Override
	public byte[] readAll() throws Exception {
		return null;
	}
	
	@Override
	public Disk makeFile() throws Exception {
		doMakeFile();
		return this;
	}
	
	protected abstract void makeDir() throws Exception;
	
	protected abstract void doMakeFile() throws Exception;
	
	protected abstract void doGetDisk(String name) throws Exception;
	
	protected abstract boolean exists() throws Exception;
	
}
