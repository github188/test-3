package nari.Obsevation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import nari.Dao.interfaces.DbAdaptor;
import nari.Geometry.GeometryBuilder;
import nari.Logger.Logger;
import nari.Logger.LoggerManager;
import nari.MemCache.AbstractCacheLoader;
import nari.MemCache.Cache;
import nari.MemCache.CacheFactory;
import nari.MemCache.CacheListener;
import nari.MemCache.CacheObject;
import nari.MemCache.FieldType;
import nari.MemCache.IndexType;
import nari.MemCache.Key;
import nari.MemCache.RecordCacheFactory;
import nari.MemCache.Shape;
import nari.model.TableName;
import oracle.spatial.geometry.JGeometry;

public class RecordCacheLoader extends AbstractCacheLoader {

	private Logger logger = LoggerManager.getLogger(this.getClass());
	
	private DbAdaptor db = null;
	
	public RecordCacheLoader() {
		
	}
	
	@Override
	protected boolean doInit() throws Exception {
		db = ObsevationActivator.dbAdaptor;
		return true;
	}

	@Override
	protected boolean doStart() throws Exception {
		return true;
	}
	
	private int submitTask(CacheDetail detail ,final Cache cache) throws Exception{
		
		String sbzlx = detail.modelId;
		String table = detail.modelName;
		String dydj = detail.dydj; 
		
		String sql = "select count(*) count,max(oid) as max,min(oid) as min from "+table+" where sbzlx="+sbzlx+" and dydj="+dydj;
		
		Map<String,Object> countMap = db.findMap(sql);
		if(countMap.get("max")==null || countMap.get("min")==null){
			return 0;
		}
		
		long max = Long.parseLong(String.valueOf(countMap.get("max")));
		
		long min = Long.parseLong(String.valueOf(countMap.get("min")));
		
		long loop = (max - min)/2000 + 1;
		int size = 0;
		
//		cache.addIndex("oid", IndexType.UNIQUE, FieldType.INT);
		cache.addIndex("shape", IndexType.SPATIAL, FieldType.SHAPE);
//		cache.addIndex("dydj", IndexType.NORMAL, FieldType.INT);
		
		cache.registCacheBodyClass(CacheObject.class);
		
		CacheObject obj = new CacheObject();
		List<Map<String,Object>> maps = null;
		
		sql = "select * from "+table+" t where t.sbzlx=? and dydj=? and t.oid>=? and oid<?";
		for(long i=0;i<loop;i++){
			long oid1 = min+i*2000;
			long oid2 = oid1 + 2000; 
			
			try {
				maps = db.findAllMap(sql,new Object[]{sbzlx,dydj,oid1,oid2});
				int ss = maps==null?0:maps.size();
				size += ss;
				logger.info(table+"  ----  "+dydj+"  ----  "+ss);
				if(maps==null || maps.size()==0){
					continue;
				}
				
				for(Map<String,Object> map:maps){
					obj.setOid(map.get("oid")==null?0:Long.parseLong(String.valueOf(map.get("oid"))));
					obj.setBzdx(map.get("bzdx")==null?0:Double.parseDouble(String.valueOf(map.get("bzdx"))));
					obj.setBzfw(map.get("bzfw")==null?0:Integer.parseInt(String.valueOf(map.get("bzfw"))));
					obj.setBznr(map.get("bznr")==null?"":String.valueOf(map.get("bznr")));
					obj.setBzxszd(map.get("bzxszd")==null?"":String.valueOf(map.get("bzxszd")));
					obj.setBzys(map.get("bzys")==null?0:Integer.parseInt(String.valueOf(map.get("bzys"))));
					obj.setDhzs(map.get("dhzs")==null?0:Integer.parseInt(String.valueOf(map.get("dhzs"))));
					obj.setFhdx(map.get("fhdx")==null?0:Double.parseDouble(String.valueOf(map.get("fhdx"))));
					obj.setFhjd(map.get("fhjd")==null?0:Double.parseDouble(String.valueOf(map.get("fhjd"))));
					obj.setPlfs(map.get("plfs")==null?0:Integer.parseInt(String.valueOf(map.get("plfs"))));
					obj.setSfbz(map.get("sfbz")==null?0:Integer.parseInt(String.valueOf(map.get("sfbz"))));
					obj.setDydj(map.get("dydj")==null?0:Integer.parseInt(String.valueOf(map.get("dydj"))));
					obj.setSbid(map.get("sbid")==null?"":String.valueOf(map.get("sbid")));
					obj.setSbmc(map.get("sbmc")==null?"":String.valueOf(map.get("sbmc")));
					obj.setSbzlx(map.get("sbzlx")==null?0:Integer.parseInt(String.valueOf(map.get("sbzlx"))));
					obj.setSszrq(map.get("sszrq")==null?0:Integer.parseInt(String.valueOf(map.get("sszrq"))));
					obj.setType(map.get("type")==null?0:Integer.parseInt(String.valueOf(map.get("type"))));
					JGeometry geom = GeometryBuilder.getBuilder().readGeometry(map.get("shape"));
					Shape shape = new Shape();
					shape.setType(geom.getType());
					shape.setElementInfo(geom.getElemInfo());
					if(geom.getType()==1){
						double[] mbr = geom.getMBR();
//						double[] newMbr = new double[mbr.length];
//						for(int k=0;k<mbr.length;k=k+2){
//							newMbr[k] = mbr[k] - detail.ox;
//							newMbr[k+1] = mbr[k+1] - detail.oy;
//						}
						
						shape.setCoordinates(mbr);
					}else{
						double[] coords = geom.getOrdinatesArray();
//						double[] newCoords = new double[coords.length];
//						for(int k=0;k<coords.length;k=k+2){
//							newCoords[k] = coords[k] - detail.ox;
//							newCoords[k+1] = coords[k+1] - detail.oy;
//						}
						
						shape.setCoordinates(coords);
					}
					
					obj.setShape(shape);
					cache.add(obj);
					map.clear();
					map = null;
				}
				
				maps = null;
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				
			}
		}
		logger.info("load table "+table+"-"+sbzlx+" size:"+size);
		return size;
	}

	@Override
	protected boolean doStop() throws Exception {
		
		return true;
	}

	@Override
	protected Cache[] loadCache(CacheFactory factory) throws Exception {
		
		logger.info("start load obsevation cache");
		
		double offSetX = 0;
		double offSetY = 0;
		
		String offSql = "select param_name,param_value from " + TableName.CONF_SERVICEPARAMS + " where param_name='OFFSETX' or param_name='OFFSETY'";
		List<Map<String,Object>> omaps = db.findAllMap(offSql);
		
		if(omaps!=null){
			for(Map<String,Object> m:omaps){
				if("OFFSETX".equalsIgnoreCase(String.valueOf(m.get("param_name")))){
					offSetX = m.get("param_value")==null?0:Double.parseDouble(String.valueOf(m.get("param_value")));
				}else if("OFFSETY".equalsIgnoreCase(String.valueOf(m.get("param_name")))){
					offSetY = m.get("param_value")==null?0:Double.parseDouble(String.valueOf(m.get("param_value")));
				}
			}
		}
		
		String sql = "select classid from " + TableName.CONF_DOCUMENTMODEL + " where mapid=1001 and featureclass in('" 
				+ TableName.T_TX_ZWYC_DXD + "','" 
				+ TableName.T_TX_ZWYC_DLD + "','" 
				+ TableName.T_TX_DYSB_DYDXD + "','" 
				+ TableName.T_TX_DYSB_DYDLD + "','" 
				+ TableName.T_TX_ZNYC_DZ + "','" 
				+ TableName.T_TX_ZNYC_ZBYQ + "','" 
				+ TableName.T_TX_ZNYC_PDBYQ + "','" 
				+ TableName.T_TX_ZWYC_ZSBYQ + "','" 
				+ TableName.T_TX_ZWYC_ZWLJX + "')";
		List<Map<String,Object>> maps = db.findAllMap(sql);
		
		final AtomicInteger size = new AtomicInteger(0);
		
		ExecutorService exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		
		List<CacheDetail> task = new ArrayList<CacheDetail>();
		
		sql = "select modelid,modelname from " + TableName.CONF_MODELMETA + " where classid=?";
		for(Map<String,Object> map:maps){
			String classId = String.valueOf(map.get("classid"));
			
			List<Map<String,Object>> list =  db.findAllMap(sql, new Object[]{classId});
			
			for(Map<String,Object> m:list){
				String groupSql = "";
				if(classId.equals("300000")){
					groupSql = "select dydj from "+String.valueOf(m.get("modelname"))+" where dydj is not null and dydj <> 0 and dydj <> -1 group by dydj";
				}else if(classId.equals("101000") || classId.equals("201000")){
					groupSql = "select dydj from "+String.valueOf(m.get("modelname"))+" where dydj is not null and dydj <> 0 and dydj <> -1 and dydj<=32 group by dydj";
				}else{
					groupSql = "select dydj from "+String.valueOf(m.get("modelname"))+" where dydj is not null and dydj <> 0 and dydj <> -1 and dydj<=22 group by dydj";
				}
				
				List<Map<String,Object>> groups =  db.findAllMap(groupSql);
				
				for(Map<String,Object> g:groups){
					CacheDetail detail = new CacheDetail();
					
					detail.modelId = String.valueOf(m.get("modelid"));
					detail.modelName = String.valueOf(m.get("modelname"));
					detail.dydj = String.valueOf(g.get("dydj"));
					detail.ox = offSetX;
					detail.oy = offSetY;
					
					task.add(detail);
				}
			}
		}
		
		final CountDownLatch latch = new CountDownLatch(task.size());
		
		Cache[] arr = new Cache[task.size()];
		int i=0;
		for(final CacheDetail detail:task){
			final Cache cache = factory.createCache(Key.val(detail.modelId+"-"+detail.dydj),5000);
			logger.info("create index:"+detail.modelId+"-"+detail.dydj);
			arr[i++] = cache;
			
			exec.submit(new Runnable() {
				
				@Override
				public void run() {
					int count = 0;
					
					try {
						count = submitTask(detail,cache);
					} catch (Exception e) {
						e.printStackTrace();
					}finally{
						latch.countDown();
						size.getAndAdd(count);
					}
				}
			});
		}
		
		latch.await();
		
		exec.shutdown();
		logger.info("load count:"+size);
		return arr;
	}
	
	@Override
	protected CacheFactory getCacheFactory(List<CacheListener> listeners) throws Exception {
		return new RecordCacheFactory();
	}
	
}
