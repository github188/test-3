package nari.SpatialIndex.searcher;

import nari.SpatialIndex.index.Boundary;
import nari.SpatialIndex.index.Grid;
import nari.SpatialIndex.loader.Disk;

public class GridIndexLayer implements IndexLayer {

	private String layerId;
	
	private String layerName;
	
	private Boundary boundary;
	
	private Grid grid;
	
	private Disk disk;
	
//	private FileChannel channel;
	
	private IndexLayer parentLayer;
	
//	private RandomAccessFile file;
	
	private LayerHeader header = null;
	
//	private final AtomicReference<IndexLayer> cluster = new AtomicReference<IndexLayer>();
	
	public GridIndexLayer(String layerId,String layerName,Disk disk,Grid grid) {
		this.layerId = layerId;
		this.layerName = layerName;
		this.disk = disk;
		this.grid = grid;
		try {
			makeLayer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public GridIndexLayer(String layerId,String layerName) {
		this(layerId,layerName,null,null,null);
	}
	
	public GridIndexLayer(String layerId,String layerName,Grid grid,Boundary boundary,IndexLayer parentLayer) {
		this.layerId = layerId;
		this.layerName = layerName;
//		this.grid = grid;
		this.boundary = boundary;
		this.parentLayer = parentLayer;
	}

	@Override
	public String getLayerName() {
		return layerName;
	}

	@Override
	public String getLayerID() {
		return layerId;
	}

	@Override
	public int compareTo(IndexLayer o) {
		if(o==null){
			return -1;
		}
		if("".equals(layerId)){
			return -1;
		}
		
		if(layerId.equals(o.getLayerID())){
			return 0;
		}
		return -1;
	}

	@Override
	public boolean exist() throws Exception{
		return disk.getFile().exists();
	}

	@Override
	public IndexLayer makeLayer() throws Exception{
//		String indexId = disk.getFile().getName()+"$index";
//		String indexKey = grid.getGridId()+"$"+indexId;
//		synchronized (HeaderHolder.class) {
//			if((header=HeaderHolder.get().getHeader(indexKey))==null){
//				File pfile = disk.getFile().getParentFile();
//				File f = new File(pfile,indexId);
//				if(!f.exists()){
//					f.createNewFile();
//				}
				
//				file = new RandomAccessFile(f,"rw");
				header = new GridIndexLayerHeader(grid,disk/**,file.getChannel()**/);
//				HeaderHolder.get().put(indexKey, header);
//			}
//		}
		
		
		return this;
	}

	@Override
	public boolean write(Record record) throws Exception {
//		System.out.println(disk.getFile().getParentFile().getPath()+","+record.getIndexValue()+","+record.getLayer().getLayerID()+","+Arrays.toString(record.getGeometry().getCoordinates()));
//		System.out.println(grid.getGridId()+","+this.getLayerID()+","+record.getIndexValue());
		return header.write(record.getGeometry(), record.toByte());
	}

	@Override
	public int getRecordCount() throws Exception {
		return 0;
	}

	@Override
	public Boundary boundary() {
		return boundary;
	}
	
	@Override
	public byte[] readAll() throws Exception {
		return null;
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
	public int length() throws Exception {
		return 0;
	}

	@Override
	public boolean remove() throws Exception {
		return false;
	}

	@Override
	public IndexLayer makeFolderLayer() throws Exception {
		return null;
	}

	@Override
	public IndexLayer getParentLayer() throws Exception {
		return parentLayer;
	}

	@Override
	public Grid getGrid() throws Exception {
		return grid;
	}
	
}
