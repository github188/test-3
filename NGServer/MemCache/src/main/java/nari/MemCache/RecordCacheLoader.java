//package nari.MemCache;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.atomic.AtomicInteger;
//
//import oracle.spatial.geometry.JGeometry;
//
//import nari.Dao.interfaces.DbAdaptor;
//import nari.Geometry.helper.GeometryHelper;
//
//public class RecordCacheLoader extends AbstractCacheLoader {
//
//	private DbAdaptor db = null;
//	
//	public RecordCacheLoader() {
//		
//	}
//	
//	@Override
//	protected boolean doInit() throws Exception {
//		db = MemCacheActivator.dbAdaptor;
//		return true;
//	}
//
//	@Override
//	protected boolean doStart() throws Exception {
//		return true;
//	}
//	
//	private int submitTask(CacheDetail detail ,final Cache cache) throws Exception{
//		
////		int size = 0;
//////		cache.addIndex("oid", IndexType.UNIQUE, FieldType.INT);
////		cache.addIndex("shape", IndexType.SPATIAL, FieldType.SHAPE);
//////		cache.addIndex("dydj", IndexType.NORMAL, FieldType.INT);
////		
////		cache.registCacheBodyClass(CacheObject.class);
////		
////		String sbzlx = String.valueOf(m.get("modelid"));
////		String table = String.valueOf(m.get("modelname"));
////		
////		CacheObject obj = new CacheObject();
////		List<Map<String,Object>> maps = null;
////		
////		String sql = "select * from "+table+" t where t.sbzlx="+sbzlx;
////		maps = db.findAllMap(sql);
////		if(maps==null){
////			return 0;
////		}
////		
////		size = maps.size();
////		for(Map<String,Object> map:maps){
////			obj.setOid(map.get("oid")==null?0:Long.parseLong(String.valueOf(map.get("oid"))));
////			obj.setBzdx(map.get("bzdx")==null?0:Double.parseDouble(String.valueOf(map.get("bzdx"))));
////			obj.setBzfw(map.get("bzfw")==null?0:Integer.parseInt(String.valueOf(map.get("bzfw"))));
////			obj.setBznr(map.get("bznr")==null?"":String.valueOf(map.get("bznr")));
////			obj.setBzxszd(map.get("bzxszd")==null?"":String.valueOf(map.get("bzxszd")));
////			obj.setBzys(map.get("bzys")==null?0:Integer.parseInt(String.valueOf(map.get("bzys"))));
////			obj.setDhzs(map.get("dhzs")==null?0:Integer.parseInt(String.valueOf(map.get("dhzs"))));
////			obj.setFhdx(map.get("fhdx")==null?0:Double.parseDouble(String.valueOf(map.get("fhdx"))));
////			obj.setFhjd(map.get("fhjd")==null?0:Double.parseDouble(String.valueOf(map.get("fhjd"))));
////			obj.setPlfs(map.get("plfs")==null?0:Integer.parseInt(String.valueOf(map.get("plfs"))));
////			obj.setSfbz(map.get("sfbz")==null?1:Integer.parseInt(String.valueOf(map.get("sfbz"))));
////			obj.setDydj(map.get("dydj")==null?0:Integer.parseInt(String.valueOf(map.get("dydj"))));
////			obj.setSbid(map.get("sbid")==null?"":String.valueOf(map.get("sbid")));
////			obj.setSbmc(map.get("sbmc")==null?"":String.valueOf(map.get("sbmc")));
////			obj.setSbzlx(map.get("sbzlx")==null?0:Integer.parseInt(String.valueOf(map.get("sbzlx"))));
////			obj.setSszrq(map.get("sszrq")==null?0:Integer.parseInt(String.valueOf(map.get("sszrq"))));
////			obj.setType(map.get("type")==null?0:Integer.parseInt(String.valueOf(map.get("type"))));
////			JGeometry geom = GeometryHelper.readGeometry(map.get("shape"));
////			Shape shape = new Shape();
////			shape.setType(geom.getType());
////			shape.setElementInfo(geom.getElemInfo());
////			if(geom.getType()==1){
////				shape.setCoordinates(geom.getMBR());
////			}else{
////				shape.setCoordinates(geom.getOrdinatesArray());
////			}
////			
////			obj.setShape(shape);
////			cache.add(obj);
////			map.clear();
////			map = null;
////		}
////		
////		maps = null;
////		System.out.println("load table "+table+"-"+sbzlx+" size:"+size);
//		
//		
//		String sbzlx = detail.modelId;
//		String table = detail.modelName;
//		
//		String sql = "select count(*) count,max(oid) as max,min(oid) as min from "+table+" where sbzlx="+sbzlx;
//		if(detail.op!=null){
//			sql = sql + "and "+detail.op.key+;
//		}
//		
//		Map<String,Object> countMap = db.findMap(sql);
//		if(countMap.get("max")==null || countMap.get("min")==null){
//			return 0;
//		}
//		
//		long max = Long.parseLong(String.valueOf(countMap.get("max")));
//		
//		long min = Long.parseLong(String.valueOf(countMap.get("min")));
//		
//		long loop = (max - min)/2000 + 1;
//		
//		int size = 0;
//		
////		cache.addIndex("oid", IndexType.UNIQUE, FieldType.INT);
//		cache.addIndex("shape", IndexType.SPATIAL, FieldType.SHAPE);
////		cache.addIndex("dydj", IndexType.NORMAL, FieldType.INT);
//		
//		cache.registCacheBodyClass(CacheObject.class);
//		
//		CacheObject obj = new CacheObject();
//		List<Map<String,Object>> maps = null;
//		
//		sql = "select * from "+table+" t where t.sbzlx="+sbzlx+" and t.oid>=? and oid<?";
//		for(long i=0;i<loop;i++){
//			long oid1 = min+i*2000;
//			long oid2 = oid1 + 2000; 
//			
//			try {
//				maps = db.findAllMap(sql,new Object[]{oid1,oid2});
//				int ss = maps==null?0:maps.size();
////				size = maps==null?(size + 0):(size + maps.size());
//				size += ss;
//				String s = "select * from "+table+" t where t.sbzlx="+sbzlx+" and t.oid>="+oid1+" and oid<"+oid2;
//				System.out.println(s+"  ----  "+ss);
//				if(maps==null || maps.size()==0){
//					continue;
//				}
//				
//				for(Map<String,Object> map:maps){
//					obj.setOid(map.get("oid")==null?0:Long.parseLong(String.valueOf(map.get("oid"))));
//					obj.setBzdx(map.get("bzdx")==null?0:Double.parseDouble(String.valueOf(map.get("bzdx"))));
//					obj.setBzfw(map.get("bzfw")==null?0:Integer.parseInt(String.valueOf(map.get("bzfw"))));
//					obj.setBznr(map.get("bznr")==null?"":String.valueOf(map.get("bznr")));
//					obj.setBzxszd(map.get("bzxszd")==null?"":String.valueOf(map.get("bzxszd")));
//					obj.setBzys(map.get("bzys")==null?0:Integer.parseInt(String.valueOf(map.get("bzys"))));
//					obj.setDhzs(map.get("dhzs")==null?0:Integer.parseInt(String.valueOf(map.get("dhzs"))));
//					obj.setFhdx(map.get("fhdx")==null?0:Double.parseDouble(String.valueOf(map.get("fhdx"))));
//					obj.setFhjd(map.get("fhjd")==null?0:Double.parseDouble(String.valueOf(map.get("fhjd"))));
//					obj.setPlfs(map.get("plfs")==null?0:Integer.parseInt(String.valueOf(map.get("plfs"))));
//					obj.setSfbz(map.get("sfbz")==null?1:Integer.parseInt(String.valueOf(map.get("sfbz"))));
//					obj.setDydj(map.get("dydj")==null?0:Integer.parseInt(String.valueOf(map.get("dydj"))));
//					obj.setSbid(map.get("sbid")==null?"":String.valueOf(map.get("sbid")));
//					obj.setSbmc(map.get("sbmc")==null?"":String.valueOf(map.get("sbmc")));
//					obj.setSbzlx(map.get("sbzlx")==null?0:Integer.parseInt(String.valueOf(map.get("sbzlx"))));
//					obj.setSszrq(map.get("sszrq")==null?0:Integer.parseInt(String.valueOf(map.get("sszrq"))));
//					obj.setType(map.get("type")==null?0:Integer.parseInt(String.valueOf(map.get("type"))));
//					JGeometry geom = GeometryHelper.readGeometry(map.get("shape"));
//					Shape shape = new Shape();
//					shape.setType(geom.getType());
//					shape.setElementInfo(geom.getElemInfo());
//					if(geom.getType()==1){
//						shape.setCoordinates(geom.getMBR());
//					}else{
//						shape.setCoordinates(geom.getOrdinatesArray());
//					}
//					
//					obj.setShape(shape);
//					cache.add(obj);
//					map.clear();
//					map = null;
//				}
//				
//				maps = null;
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally{
//				
//			}
//		}
//		System.out.println("load table "+table+"-"+sbzlx+" size:"+size);
//		return size;
//	}
//
//	@Override
//	protected boolean doStop() throws Exception {
//		
//		return true;
//	}
//
//	@Override
//	protected Cache[] loadCache(CacheFactory factory) throws Exception {
//		String sql = "select classid from conf_documentmodel where mapid=1001 and featureclass in('T_TX_ZWYC_DXD','T_TX_ZWYC_DLD','T_TX_DYSB_DYDXD','T_TX_DYSB_DYDLD','T_TX_ZNYC_DZ')";
////		String sql = "select classid from conf_documentmodel where mapid=1001 and featureclass not like 'T_TX_GGSS_%'";
////		String sql = "select classid from conf_documentmodel where mapid=1001 and (featureclass like 'T_TX_ZWYC_%' or featureclass like 'T_TX_ZNYC_%')";
//		List<Map<String,Object>> maps = db.findAllMap(sql);
//		
////		List<Cache> caches = new ArrayList<Cache>();
//		
//		Map<CacheDetail,Cache> caches = new HashMap<CacheDetail,Cache>();
//		
//		
////		int size = 0;
//		final AtomicInteger size = new AtomicInteger(0);
//		
//		ExecutorService exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//		
//		List<CacheDetail> task = new ArrayList<CacheDetail>();
//		
//		for(Map<String,Object> map:maps){
//			String classId = String.valueOf(map.get("classid"));
//			
//			sql = "select modelid,modelname from conf_modelmeta where classid=?";
//			
//			List<Map<String,Object>> list =  db.findAllMap(sql, new Object[]{classId});
//			
//			for(Map<String,Object> m:list){
//				if(String.valueOf(m.get("modelid")).equals("10100000")){
//					CacheDetail d1 = new CacheDetail();
//					
//					d1.modelId = String.valueOf(m.get("modelid"));
//					d1.modelName = String.valueOf(m.get("modelname"));
//					
//					CacheEqOp op = new CacheEqOp();
//					op.key = "dydj";
//					op.val = "22";
//					
//					d1.op = op;
//					
//					task.add(d1);
//					
//					
//					CacheDetail d2 = new CacheDetail();
//					
//					d2.modelId = String.valueOf(m.get("modelid"));
//					d2.modelName = String.valueOf(m.get("modelname"));
//					
//					CacheNeqOp nop = new CacheNeqOp();
//					nop.key = "dydj";
//					nop.val = "22";
//					
//					d2.op = nop;
//					
//					task.add(d2);
//				}else{
//					CacheDetail detail = new CacheDetail();
//					
//					detail.modelId = String.valueOf(m.get("modelid"));
//					detail.modelName = String.valueOf(m.get("modelname"));
//					detail.op = null;
//					
//					task.add(detail);
//				}
//			}
//			
//		}
//		
//		final CountDownLatch latch = new CountDownLatch(task.size());
//		
//		for(final CacheDetail detail:task){
//			final Cache cache = factory.createCache(Key.val(detail.modelId));
//			caches.add(cache);
//			
//			exec.submit(new Runnable() {
//				
//				@Override
//				public void run() {
//					int count = 0;
//					try {
//						count = submitTask(detail,cache);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}finally{
//						latch.countDown();
//						size.getAndAdd(count);
//					}
//				}
//			});
//		}
//		
//		
//		latch.await();
//		Cache[] arr = new Cache[caches.size()];
//		arr = caches.toArray(arr);
//		
//		exec.shutdown();
//		System.out.println("load count:"+size);
//		return arr;
//	}
//	
//	@Override
//	protected CacheFactory getCacheFactory() throws Exception {
//		return new RecordCacheFactory();
//	}
//	
//}
