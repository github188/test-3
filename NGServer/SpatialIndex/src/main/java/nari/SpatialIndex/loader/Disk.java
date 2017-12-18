package nari.SpatialIndex.loader;

import java.io.File;

import nari.SpatialIndex.index.Grid;
import nari.SpatialIndex.searcher.IndexLayer;

public interface Disk {

	public String getRoot();
	
	public String getPath();
	
	public Disk makeDisk() throws Exception;
	
	public Disk makeFile() throws Exception;
	
	public Disk getDisk(String name) throws Exception;
	
	public boolean exist() throws Exception;
	
	public int getRecordCount();
	
	public IndexLayer getLayer(IndexLayer layer,Grid grid) throws Exception;
	
	public boolean hasLayer(IndexLayer layer) throws Exception;

	public boolean createLayer(IndexLayer layer) throws Exception;
	
	public byte[] readAll() throws Exception;
	
	public byte[] read(int offset,int length) throws Exception;
	
	public byte read() throws Exception;
	
	public int length() throws Exception;
	
	public File getFile() throws Exception;
}
