package nari.SpatialIndex.searcher;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import nari.SpatialIndex.handler.Notifier;
import nari.SpatialIndex.handler.NotifyEvent;
import nari.SpatialIndex.index.Indexer;

public abstract class PooledSubIndexLayer extends AbstractSubLayer {

	private static final ArrayBlockingQueue<LayerBridge> queue = new ArrayBlockingQueue<LayerBridge>(10000);
	
	private static final int poolSize = Runtime.getRuntime().availableProcessors()*20+1;
	
	private static final ExecutorService exec = Executors.newFixedThreadPool(poolSize);
	
	private static final AtomicBoolean b = new AtomicBoolean(false);
	
	static{
		for(int i=0;i<poolSize;i++){
			exec.submit(new Runnable(){

				private final Map<String,LayerWriterCluster> map = new HashMap<String,LayerWriterCluster>();
				
				@Override
				public void run() {
					LayerBridge data = null;
					int i=0;
					do{
						data = queue.poll();
						
						if(data==null){
							for(Map.Entry<String, LayerWriterCluster> entry:map.entrySet()){
								try {
									entry.getValue().write();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							map.clear();
							i=0;
							continue;
						}
						
						try {
							if(map.containsKey(data.getLayer().getSubLayerID())){
								map.get(data.getLayer().getSubLayerID()).addRecord(data);
							}else{
								LayerWriterCluster cluster = new LayerWriterCluster(data.getLayer());
								cluster.addRecord(data);
								map.put(data.getLayer().getSubLayerID(), cluster);
							}
							
							if(i==1000){
								for(Map.Entry<String, LayerWriterCluster> entry:map.entrySet()){
									entry.getValue().write();
								}
								map.clear();
								i=0;
							}
							i++;
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					} while(data!=null || !b.get());
					
					for(Map.Entry<String, LayerWriterCluster> entry:map.entrySet()){
						try {
							entry.getValue().write();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				
			});
		}
		
		NotifierManager.get().registHook(NotifyEvent.INIT, new Notifier() {
			
			@Override
			public void notify(NotifyEvent event, Indexer indexer) throws Exception {
				b.compareAndSet(false, true);
				exec.shutdown();
			}
		});
	}
	
	@Override
	protected boolean doWrite(byte[] data) throws Exception {
		LayerBridge layerData = new LayerBridge(this,data);
		queue.put(layerData);
		return true;
	}
	
	protected abstract boolean doWrite(byte[] data,int length) throws Exception;
}
