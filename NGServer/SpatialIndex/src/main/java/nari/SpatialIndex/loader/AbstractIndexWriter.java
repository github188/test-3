package nari.SpatialIndex.loader;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import nari.SpatialIndex.geom.Geometry;
import nari.SpatialIndex.index.BlockingQueueCluster;
import nari.SpatialIndex.index.Grid;
import nari.SpatialIndex.index.GridMapper;
import nari.SpatialIndex.searcher.IndexLayer;
import nari.SpatialIndex.searcher.Record;

public abstract class AbstractIndexWriter implements IndexWriter {

	private ExecutorService exec;
	
	private int count = Runtime.getRuntime().availableProcessors()*10+1;
	
	private static final BlockingQueueCluster cluster = new BlockingQueueCluster(10,1000);
	
//	private ArrayBlockingQueue<Record> cluster = new ArrayBlockingQueue<Record>(1000);
	
	@Override
	public boolean write(final IndexLayer layer,final Record record,final GridMapper mapper) throws Exception {
		final Geometry geom = record.getGeometry();
		final Grid[] grids = getGrid(layer,geom,mapper);
		
		if(grids==null || grids.length==0){
			return true;
		}
		for(Grid grid:grids){
			final Map<String,Object> map = new HashMap<String,Object>();
			map.put("grid", grid);
			map.put("record", record);
			map.put("mapper", mapper);

			cluster.put(new Record() {
				
				@Override
				public byte[] toByte() throws Exception {
					return null;
				}
				
				@Override
				public Object getValue(String label) {
					return map.get(label);
				}
				
				@Override
				public IndexLayer getLayer() {
					return layer;
				}
				
				@Override
				public String getIndexValue() {
					return null;
				}
				
				@Override
				public Geometry getGeometry() {
					return geom;
				}
			});
		}
		
		return true;
	}

	@Override
	public boolean init() throws Exception {
		exec = Executors.newFixedThreadPool(count);
		return true;
	}
	
	@Override
	public boolean start() throws Exception {
		for(int i=0;i<count;i++){
			exec.submit(new Runnable() {
				
				@Override
				public void run() {
					try {
						Record record = cluster.take();
						while(record!=null){
							Grid grid = (Grid)record.getValue("grid");
							Record r = (Record)record.getValue("record");
							GridMapper mapper = (GridMapper)record.getValue("mapper");
							
							grid.write(r.getLayer(),r);
							mapper.map(grid.boundary(), grid);
							
							record = cluster.take();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		
		return true;
	}
	
	@Override
	public boolean stop() throws Exception {
		cluster.interruper();
		
		if(exec!=null && !exec.isShutdown()){
			exec.shutdown();
		}
		return true;
	}
	
	protected abstract Grid[] getGrid(IndexLayer layer,Geometry geom,GridMapper mapper) throws Exception;
	
}
