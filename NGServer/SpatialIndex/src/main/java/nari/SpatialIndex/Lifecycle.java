package nari.SpatialIndex;

public interface Lifecycle {

	public boolean init() throws Exception;
	
	public boolean start() throws Exception;
	
	public boolean stop() throws Exception;
}
