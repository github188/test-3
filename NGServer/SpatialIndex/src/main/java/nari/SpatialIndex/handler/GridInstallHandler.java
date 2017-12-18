package nari.SpatialIndex.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import nari.Dao.interfaces.DbAdaptor;
import nari.SpatialIndex.SpatialIndexActivator;
import nari.SpatialIndex.searcher.Record;

public class GridInstallHandler extends AbstractPoolInstallHandler {

	private ExecutorService exec;
	
	private final int threadCount = Runtime.getRuntime().availableProcessors()*2+1;
	
	private List<DataObject> datas = new ArrayList<DataObject>();
	
	private DbAdaptor dbAdaptor;
	
	public GridInstallHandler(){
		
	}
	
	@Override
	protected boolean doInit() throws Exception {
		super.doInit();
		dbAdaptor = SpatialIndexActivator.dbAdaptor;
		exec = Executors.newFixedThreadPool(threadCount, Executors.defaultThreadFactory());
		
		datas.add(new DataObject("T_TX_ZWYC_DXD","DYDJ IN(35,37,36,83,84,85,86)",20000));
		datas.add(new DataObject("T_TX_ZWYC_YXGT","DYDJ IN(35,37,36,83,84,85,86)",20000));
		datas.add(new DataObject("T_TX_ZWYC_WLG","DYDJ IN(35,37,36,83,84,85,86)",20000));
		datas.add(new DataObject("T_TX_ZNYC_DZ","DYDJ IN(35,37,36,83,84,85,86)",20000));
		
		return true;
	}
	
	@Override
	protected boolean doStart() throws Exception {
		for(final DataObject data:datas){
			String sql = "select min(oid) minoid,max(oid) maxoid from "+data.getTable()+" where "+data.getWhereCaluse();
			Map<String,Object> map = dbAdaptor.findMap(sql);
			if(map==null){
				data.setMinOid(0);
				continue;
			}
			
			long minOid = Long.valueOf(String.valueOf(map.get("minoid")));
			long maxOid = Long.valueOf(String.valueOf(map.get("maxoid")));
			data.setMinOid(minOid);
			data.setMaxOid(maxOid);
		}
		
		List<String> sqls = new ArrayList<String>();
		
		while(!isEnd()){
			for(final DataObject data:datas){
				final long start = data.getMinOid();
				final long end = data.nextOid();
				data.increateMinOid();
				String sql = "select shape,oid,sbzlx from "+data.getTable()+" where "+data.getWhereCaluse()+" and oid>="+start+" and oid <"+end;
				sqls.add(sql);
			}
		}
		
		final CountDownLatch latch = new CountDownLatch(sqls.size());
		
		for(final String sql:sqls){
			exec.submit(new Runnable() {
			
				@Override
				public void run() {
					
					List<Map<String,Object>> maps=null;
					try {
						maps = dbAdaptor.findAllMap(sql);
						if(maps==null){
							return;
						}
						
						Record[] records = new Record[maps.size()];
						int i=0;
						for(Map<String,Object> map:maps){
							records[i++] = new GridDataRecord(map);
						}
						
						GridInstallHandler.this.addRecods(records);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						latch.countDown();
					}
				}
			});
		}
		
		exec.submit(new Runnable() {
			
			@Override
			public void run() {
				try {
					latch.await();
					interruper();
					exec.shutdown();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		return true;
	}
	
	private boolean isEnd(){
		boolean end = true;
		for(final DataObject data:datas){
			if(data.getMinOid()<data.getMaxOid()){
				end = end && false;
				if(!end){
					break;
				}
			}
		}
		return end;
	}
	
	@Override
	protected boolean doStop() throws Exception {
		if(exec!=null && !exec.isShutdown()){
			exec.shutdown();
		}
		return true;
	}
	
}
