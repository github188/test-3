package nari.SpatialIndex.index;

import java.util.ArrayList;
import java.util.List;

import nari.SpatialIndex.geom.CoordinateSequence;
import nari.SpatialIndex.geom.DefaultCoordinateSequence;
import nari.SpatialIndex.geom.DefaultPolygon;
import nari.SpatialIndex.geom.Geometry;
import nari.SpatialIndex.searcher.GridIndexLayer;
import nari.SpatialIndex.searcher.IndexLayer;
import nari.SpatialIndex.searcher.Record;

@Deprecated
public class ClusterIndexLayer implements IndexLayer {

	private Grid grid;
	
	private IndexLayer baseLayer;
	
	private double ox;
	
	private double oy;
	
	private double width;
	
	private double height;
	
	private IndexLayer[] layers;
	
	public ClusterIndexLayer(Grid grid,IndexLayer baseLayer){
		this.grid = grid;
		this.baseLayer = baseLayer;
		ox = grid.boundary().getMinX();
		oy = grid.boundary().getMinY();
		width = grid.boundary().getMaxX() - grid.boundary().getMinX();
		height = grid.boundary().getMaxY() - grid.boundary().getMinY();
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void init() throws Exception {
		
//		byte[] data = baseLayer.readAll();
		
		baseLayer.remove();
		
		IndexLayer parent = baseLayer.makeFolderLayer();
		
		layers = new IndexLayer[4];
		layers[0] = new GridIndexLayer("0","0",grid,new DefaultBoundary(ox,oy,ox+width/2,oy+height/2),parent);
		layers[1] = new GridIndexLayer("1","1",grid,new DefaultBoundary(ox+width/2,oy,ox+width/2,oy+height/2),parent);
		layers[2] = new GridIndexLayer("2","2",grid,new DefaultBoundary(ox+width/2,oy+height/2,ox+width/2,oy+height/2),parent);
		layers[3] = new GridIndexLayer("3","3",grid,new DefaultBoundary(ox,oy+height/2,ox+width/2,oy+height/2),parent);
		
		List<Record> records = new ArrayList<Record>();
		for(Record record:records){
			for(IndexLayer layer:layers){
				Boundary bd = layer.boundary();
				CoordinateSequence pyseq = new DefaultCoordinateSequence(new double[]{bd.getMinX(),bd.getMinY(),bd.getMinX(),bd.getMaxY(),bd.getMaxX(),bd.getMaxY(),bd.getMaxX(),bd.getMinY(),bd.getMinX(),bd.getMinY()});
				Geometry g = new DefaultPolygon(pyseq);
				
				if(record.getGeometry().intersects(g)){
					layer.write(record);
				}
			}
		}
		
	}
	
	@Override
	public int compareTo(IndexLayer o) {
		return 0;
	}

	@Override
	public String getLayerName() {
		return baseLayer.getLayerName();
	}

	@Override
	public String getLayerID() {
		return baseLayer.getLayerID();
	}

	@Override
	public boolean exist() {
		return false;
	}

	@Override
	public IndexLayer makeLayer() {
		return null;
	}

	@Override
	public boolean write(Record record) throws Exception {
		IndexLayer[] layers = marchLayer(record.getGeometry());
		
		for(IndexLayer layer:layers){
			layer.write(record);
		}
		
		return true;
	}

	@Override
	public int getRecordCount() throws Exception {
		return grid.getDisk().getRecordCount();
	}

	private IndexLayer[] marchLayer(Geometry geom) throws Exception {
		List<IndexLayer> list = new ArrayList<IndexLayer>();
		
		for(IndexLayer layer:layers){
			Boundary bd = layer.boundary();
			CoordinateSequence pyseq = new DefaultCoordinateSequence(new double[]{bd.getMinX(),bd.getMinY(),bd.getMinX(),bd.getMaxY(),bd.getMaxX(),bd.getMaxY(),bd.getMaxX(),bd.getMinY(),bd.getMinX(),bd.getMinY()});
			Geometry g = new DefaultPolygon(pyseq);
			
			if(geom.intersects(g)){
				list.add(layer);
			}
		}
		
		IndexLayer[] gs = new IndexLayer[list.size()];
		gs = list.toArray(gs);
		return gs;
	}

	@Override
	public Boundary boundary() {
		return grid.boundary();
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
		return null;
	}

	@Override
	public Grid getGrid() throws Exception {
		return grid;
	}
}
