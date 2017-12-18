package nari.SpatialIndex.searcher;

import nari.SpatialIndex.index.Boundary;
import nari.SpatialIndex.index.Grid;

public interface IndexLayer extends Comparable<IndexLayer>{

	public String getLayerName();
	
	public String getLayerID();
	
	public boolean exist() throws Exception;
	
	public IndexLayer makeLayer() throws Exception;
	
	public boolean write(Record record) throws Exception;
	
	public int getRecordCount() throws Exception;
	
	public Boundary boundary();
	
	public byte[] readAll() throws Exception;
	
	public byte[] read(int offset,int length) throws Exception;
	
	public byte read() throws Exception;
	
	public int length() throws Exception;
	
	public boolean remove() throws Exception;
	
	public IndexLayer makeFolderLayer() throws Exception;
	
	public IndexLayer getParentLayer() throws Exception;
	
	public Grid getGrid() throws Exception;
	
}
