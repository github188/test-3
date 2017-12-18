package nari.SpatialIndex.index;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;

public class GridMapper {

	private static final AtomicReference<GridMapper> mapper = new AtomicReference<GridMapper>();
	
	private final ConcurrentMap<Long,Grid> gridCache = new ConcurrentHashMap<Long,Grid>();
	
	public static GridMapper get(){
		if(mapper.get()==null){
			GridMapper m = new GridMapper();
			mapper.compareAndSet(null, m);
		}
		return mapper.get();
	}
	
	public void map(Boundary boundary, Grid grid) throws Exception {
		
	}

	public void update(Grid grid) throws Exception {
		
	}

	public boolean hasGrid(Grid grid) throws Exception {
		return false;
	}

	public Grid[] findGrid(Boundary boundary) throws Exception {
		return null;
	}

	public boolean hasGrid(Long gridId) throws Exception {
		return gridCache.containsKey(gridId);
	}

	public Grid findGrid(Long gridId) throws Exception {
		return gridCache.get(gridId);
	}

	public void map(long gridId, Grid grid) throws Exception {
		gridCache.put(gridId, grid);
	}
}
