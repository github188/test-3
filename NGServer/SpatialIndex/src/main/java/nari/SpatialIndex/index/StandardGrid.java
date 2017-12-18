package nari.SpatialIndex.index;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

import nari.SpatialIndex.loader.Disk;
import nari.SpatialIndex.searcher.IndexLayer;
import nari.SpatialIndex.searcher.Record;

public class StandardGrid implements Grid {

	private double ox;
	
	private double oy;
	
	private double width;
	
	private double height;
	
	private long gridId;
	
	private int column;
	
	private int row;
	
	private Disk root;
	
	private final AtomicBoolean b = new AtomicBoolean(false);
	
	private Disk disk ;
	
	public StandardGrid(long gridId,double ox,double oy,double width,double height,Disk root){
		this.gridId = gridId;
		this.ox = ox;
		this.oy = oy;
		this.width = width;
		this.height = height;
		this.root = root;
		transfGridId();
	}
	
	@Override
	public boolean read() throws Exception {
		return false;
	}
	
	@Override
	public boolean write(IndexLayer layer,Record record) throws Exception {
//		root.reset();
//		System.out.println(this.gridId+","+record.getValue("sbzlx")+","+record.getIndexValue());
//		Disk disk = root.getDisk(String.valueOf(gridId));
//		if(!disk.exist()){
//			disk = disk.makeDisk();
//		}
		IndexLayer ly = null;
//		synchronized (cache) {
//			ly = cache.get(gridId+"-"+layer.getLayerID());
//			if(ly==null){
//				System.out.println(gridId+"-"+layer.getLayerID());
		ly = disk.getLayer(layer,this);
//				cache.put(gridId+"-"+layer.getLayerID(), ly);
//			}
//		}
		
//		if(ly!=null && !ly.exist()){
//			ly = ly.makeLayer();
//		}
		return ly.write(record);
	}

	@Override
	public Boundary boundary() {
		return new Boundary() {
			
			@Override
			public double getMinY() {
				return oy;
			}
			
			@Override
			public double getMinX() {
				return ox;
			}
			
			@Override
			public double getMaxY() {
				return oy+height;
			}
			
			@Override
			public double getMaxX() {
				return ox+width;
			}
		};
	}

	@Override
	public long getGridId() {
		return gridId;
	}

	@Override
	public int getColumn() {
		return column;
	}

	@Override
	public int getRow() {
		return row;
	}

	private void transfGridId(){
		ByteBuffer buf = ByteBuffer.allocate(8);
		buf.putLong(gridId);
		buf.flip();
		byte[] bytes = buf.array();
		
		byte[] colbytes = new byte[4];
		byte[] rowbytes = new byte[4];
		
		colbytes[0] = bytes[0];
		colbytes[1] = bytes[1];
		colbytes[2] = bytes[2];
		colbytes[3] = bytes[3];
		
		rowbytes[0] = bytes[4];
		rowbytes[1] = bytes[5];
		rowbytes[2] = bytes[6];
		rowbytes[3] = bytes[7];
		column = 0xFF & colbytes[0] | 0xFF00 & colbytes[1] << 8 | 0xFF0000 & colbytes[2] << 16 | 0xFF000000 & colbytes[3] << 24;
		row = 0xFF & rowbytes[0] | 0xFF00 & rowbytes[1] << 8 | 0xFF0000 & rowbytes[2] << 16 | 0xFF000000 & rowbytes[3] << 24;
	}

	@Override
	public boolean hasLayer(IndexLayer layer) throws Exception {
		return root.hasLayer(layer);
	}

	@Override
	public boolean createLayer(IndexLayer layer) throws Exception {
		return root.createLayer(layer);
	}

	@Override
	public Disk getDisk() {
		return root;
	}

	@Override
	public boolean init() throws Exception {
		disk = root.getDisk(String.valueOf(gridId));
		if(!disk.exist()){
			disk = disk.makeDisk();
		}
		b.compareAndSet(false, true);
		return false;
	}

	@Override
	public boolean isInit() throws Exception {
		return b.get();
	}
}
