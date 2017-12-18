package nari.MapService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import nari.Dao.interfaces.DbAdaptor;
import nari.Geometry.Coordinate;
import nari.Geometry.GeometryBuilder;
import nari.Geometry.GeometryCollection;
import nari.Geometry.GeometryType;
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

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.operation.linemerge.LineMerger;

public class RecordCacheLoader extends AbstractCacheLoader {

	private nari.Logger.Logger logger = LoggerManager.getLogger(this.getClass());
	
	private DbAdaptor db = null;
	
	public RecordCacheLoader() {
		
	}
	
	@Override
	protected boolean doInit() throws Exception {
		db = MapServiceActivator.dbAdaptor;
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
		
		int size = 0;
		
		if(sbzlx.equals("10100000") && (dydj.equals("35") || dydj.equals("36") || dydj.equals("37"))){
			int c = merger(detail,cache);
			logger.info("load table "+table+"-"+sbzlx+" merger size:"+c);
			return c;
		}
		
		long max = Long.parseLong(String.valueOf(countMap.get("max")));
		
		long min = Long.parseLong(String.valueOf(countMap.get("min")));
		
		long loop = (max - min)/2000 + 1;
		
		cache.addIndex("shape", IndexType.SPATIAL, FieldType.SHAPE);
		
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
						double[] newMbr = new double[mbr.length];
						for(int k=0;k<mbr.length;k=k+2){
							newMbr[k] = mbr[k] - detail.ox;
							newMbr[k+1] = mbr[k+1] - detail.oy;
						}
						
						shape.setCoordinates(newMbr);
						
//						shape.setCoordinates(geom.getMBR());
					}else{
						double[] coords = geom.getOrdinatesArray();
						double[] newCoords = new double[coords.length];
						for(int k=0;k<coords.length;k=k+2){
							newCoords[k] = coords[k] - detail.ox;
							newCoords[k+1] = coords[k+1] - detail.oy;
						}
						
						shape.setCoordinates(newCoords);
						
//						shape.setCoordinates(geom.getOrdinatesArray());
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
	
	@SuppressWarnings("unchecked")
	private int merger(CacheDetail detail,Cache cache) throws Exception{
		logger.info("merger "+detail.modelName+"--"+detail.dydj);
		int size = 0;
		
		cache.addIndex("shape", IndexType.SPATIAL, FieldType.SHAPE);
		
		cache.registCacheBodyClass(CacheObject.class);
		
		CacheObject obj = new CacheObject();
		List<Map<String,Object>> maps = null;
		
		String sql = "select ssxl from "+detail.modelName+" t where t.sbzlx=? and dydj=? and ssxl is not null group by ssxl";
		
		maps = db.findAllMap(sql,new Object[]{detail.modelId,detail.dydj});
		int ss = maps==null?0:maps.size();
		logger.info(detail.modelName+"  ----  "+detail.dydj+"  ----  "+ss);
		if(maps==null || maps.size()==0){
			return 0;
		}
		
		sql = "select * from " + TableName.T_TX_ZWYC_XL + " where oid=?";
		
		String mergerSql = "select * from " + detail.modelName + " where sbzlx=? and dydj=? and ssxl=?";
		
		GeometryFactory factory = new GeometryFactory();
		
		for(Map<String,Object> map:maps){
			LineMerger merger = new LineMerger();
			String ssxl = String.valueOf(map.get("ssxl"));
			
			Map<String,Object> xl = db.findMap(sql, new Object[]{ssxl});
			
			List<Map<String,Object>> dxdMaps = db.findAllMap(mergerSql, new Object[]{detail.modelId,detail.dydj,ssxl});
			
			for(Map<String,Object> dxdMap:dxdMaps){
				
				JGeometry jgeom = GeometryBuilder.getBuilder().readGeometry(dxdMap.get("shape"));
				
				nari.Geometry.Geometry geom = GeometryBuilder.getBuilder().buildGeometry(jgeom);
				
				Geometry geometry = null;
				
				if(geom.getGeometryType()==GeometryType.POLYLINE){
					Coordinate[] coords = geom.getCoordinates();
					
					com.vividsolutions.jts.geom.Coordinate[] jcoords = new com.vividsolutions.jts.geom.Coordinate[coords.length];
					int i=0;
					for(Coordinate c:coords){
						jcoords[i++] = new com.vividsolutions.jts.geom.Coordinate(c.getX(),c.getY());
					}
					geometry = factory.createLineString(jcoords);
				}else if(geom.getGeometryType()==GeometryType.MULTIPOLYLINE){
					
					if(geom instanceof GeometryCollection){
						GeometryCollection coll = (GeometryCollection)geom;
						
						LineString[] lineStrings = new LineString[coll.getNumGeometry()];
						for(int i=0;i<coll.getNumGeometry();i++){
							nari.Geometry.Geometry line = coll.getGeometry(i);
							
							Coordinate[] coords = line.getCoordinates();
							com.vividsolutions.jts.geom.Coordinate[] jcoords = new com.vividsolutions.jts.geom.Coordinate[coords.length];
							int k=0;
							for(Coordinate c:coords){
								jcoords[k++] = new com.vividsolutions.jts.geom.Coordinate(c.getX(),c.getY());
							}
							
							lineStrings[i] = factory.createLineString(jcoords);
						}
						
						geometry = factory.createMultiLineString(lineStrings);
					}
				}
				
				if(geometry!=null){
					merger.add(geometry);
				}
			}
			
			Collection<com.vividsolutions.jts.geom.Geometry> colls = merger.getMergedLineStrings();
			
			Shape shape = new Shape();
			
			if (colls.size() > 1) {
				int[] eles = new int[colls.size()*3];
				
				List<com.vividsolutions.jts.geom.Coordinate> list = new ArrayList<com.vividsolutions.jts.geom.Coordinate>();
				int index = 1;
				int i = 0;
				for (Geometry geo : colls) {
					LineString line = factory.createLineString(geo.getCoordinates());
					com.vividsolutions.jts.geom.Coordinate[] coords = line.getCoordinates();
					for(com.vividsolutions.jts.geom.Coordinate c:coords){
						list.add(c);
					}
					
					eles[i++] = index;
					eles[i++] = 2;
					eles[i++] = 1;
					
					index = coords.length*2 + index;
				}
				double[] arr = new double[list.size()*2];
				int k=0;
				for(com.vividsolutions.jts.geom.Coordinate c:list){
					arr[k++] = c.x;
					arr[k++] = c.y;
				}
				
				shape.setType(6);
				shape.setElementInfo(eles);
				if(detail.ox==0 && detail.oy==0){
					shape.setCoordinates(arr);
				}else{
					double[] coords = arr;
					double[] newCoords = new double[coords.length];
					for(int j=0;j<coords.length;j=j+2){
						newCoords[j] = coords[j] - detail.ox;
						newCoords[j+1] = coords[j+1] - detail.oy;
					}
					
					shape.setCoordinates(newCoords);
				}
			} else if(colls.size() == 1){
				Geometry geo = colls.iterator().next();
				
				double[] arr = new double[geo.getCoordinates().length*2];
				
				int i=0;
				for(com.vividsolutions.jts.geom.Coordinate c:geo.getCoordinates()){
					arr[2*i] = c.x;
					arr[2*i+1] = c.y;
					i++;
				}
				
				shape.setType(2);
				shape.setElementInfo(new int[]{1,2,1});
				
				if(detail.ox==0 && detail.oy==0){
					shape.setCoordinates(arr);
				}else{
					double[] coords = arr;
					double[] newCoords = new double[coords.length];
					for(int j=0;j<coords.length;j=j+2){
						newCoords[j] = coords[j] - detail.ox;
						newCoords[j+1] = coords[j+1] - detail.oy;
					}
					
					shape.setCoordinates(newCoords);
				}
			}
			
			obj.setShape(shape);
			obj.setOid(xl.get("oid")==null?0:Long.parseLong(String.valueOf(xl.get("oid"))));
			obj.setBzdx(xl.get("bzdx")==null?0:Double.parseDouble(String.valueOf(xl.get("bzdx"))));
			obj.setBzfw(xl.get("bzfw")==null?0:Integer.parseInt(String.valueOf(xl.get("bzfw"))));
			obj.setBznr(xl.get("bznr")==null?"":String.valueOf(xl.get("bznr")));
			obj.setBzxszd(xl.get("bzxszd")==null?"":String.valueOf(xl.get("bzxszd")));
			obj.setBzys(xl.get("bzys")==null?0:Integer.parseInt(String.valueOf(xl.get("bzys"))));
			obj.setDhzs(xl.get("dhzs")==null?0:Integer.parseInt(String.valueOf(xl.get("dhzs"))));
			obj.setFhdx(xl.get("fhdx")==null?0:Double.parseDouble(String.valueOf(xl.get("fhdx"))));
			obj.setFhjd(xl.get("fhjd")==null?0:Double.parseDouble(String.valueOf(xl.get("fhjd"))));
			obj.setPlfs(xl.get("plfs")==null?0:Integer.parseInt(String.valueOf(xl.get("plfs"))));
			obj.setSfbz(xl.get("sfbz")==null?0:Integer.parseInt(String.valueOf(xl.get("sfbz"))));
//			obj.setDydj(xl.get("dydj")==null?0:Integer.parseInt(String.valueOf(xl.get("dydj"))));
			obj.setDydj(Integer.parseInt(detail.dydj));
			obj.setSbid(xl.get("sbid")==null?"":String.valueOf(xl.get("sbid")));
			obj.setSbmc(xl.get("sbmc")==null?"":String.valueOf(xl.get("sbmc")));
//			obj.setSbzlx(xl.get("sbzlx")==null?0:Integer.parseInt(String.valueOf(xl.get("sbzlx"))));
			obj.setSbzlx(Integer.parseInt(detail.modelId));
			obj.setSszrq(xl.get("sszrq")==null?0:Integer.parseInt(String.valueOf(xl.get("sszrq"))));
			obj.setType(xl.get("type")==null?0:Integer.parseInt(String.valueOf(xl.get("type"))));
			cache.add(obj);
			size = size+1;
		}
		
		
		sql = "select * from "+detail.modelName+" t where t.sbzlx=? and dydj=? and ssxl is null";
		
		List<Map<String,Object>> nullMaps = db.findAllMap(sql, new Object[]{detail.modelId,detail.dydj});
		if(nullMaps!=null){
			for(Map<String,Object> nmap:nullMaps){
				obj.setOid(nmap.get("oid")==null?0:Long.parseLong(String.valueOf(nmap.get("oid"))));
				obj.setBzdx(nmap.get("bzdx")==null?0:Double.parseDouble(String.valueOf(nmap.get("bzdx"))));
				obj.setBzfw(nmap.get("bzfw")==null?0:Integer.parseInt(String.valueOf(nmap.get("bzfw"))));
				obj.setBznr(nmap.get("bznr")==null?"":String.valueOf(nmap.get("bznr")));
				obj.setBzxszd(nmap.get("bzxszd")==null?"":String.valueOf(nmap.get("bzxszd")));
				obj.setBzys(nmap.get("bzys")==null?0:Integer.parseInt(String.valueOf(nmap.get("bzys"))));
				obj.setDhzs(nmap.get("dhzs")==null?0:Integer.parseInt(String.valueOf(nmap.get("dhzs"))));
				obj.setFhdx(nmap.get("fhdx")==null?0:Double.parseDouble(String.valueOf(nmap.get("fhdx"))));
				obj.setFhjd(nmap.get("fhjd")==null?0:Double.parseDouble(String.valueOf(nmap.get("fhjd"))));
				obj.setPlfs(nmap.get("plfs")==null?0:Integer.parseInt(String.valueOf(nmap.get("plfs"))));
				obj.setSfbz(nmap.get("sfbz")==null?0:Integer.parseInt(String.valueOf(nmap.get("sfbz"))));
				obj.setDydj(nmap.get("dydj")==null?0:Integer.parseInt(String.valueOf(nmap.get("dydj"))));
				obj.setSbid(nmap.get("sbid")==null?"":String.valueOf(nmap.get("sbid")));
				obj.setSbmc(nmap.get("sbmc")==null?"":String.valueOf(nmap.get("sbmc")));
				obj.setSbzlx(nmap.get("sbzlx")==null?0:Integer.parseInt(String.valueOf(nmap.get("sbzlx"))));
				obj.setSszrq(nmap.get("sszrq")==null?0:Integer.parseInt(String.valueOf(nmap.get("sszrq"))));
				obj.setType(nmap.get("type")==null?0:Integer.parseInt(String.valueOf(nmap.get("type"))));
				JGeometry geom = GeometryBuilder.getBuilder().readGeometry(nmap.get("shape"));
				Shape shape = new Shape();
				shape.setType(geom.getType());
				shape.setElementInfo(geom.getElemInfo());
				
				if(detail.ox==0 && detail.oy==0){
					if(geom.getType()==1){
						shape.setCoordinates(geom.getMBR());
					}else{
						shape.setCoordinates(geom.getOrdinatesArray());
					}
				}else{
					if(geom.getType()==1){
						double[] mbr = geom.getMBR();
						double[] newMbr = new double[mbr.length];
						for(int k=0;k<mbr.length;k=k+2){
							newMbr[k] = mbr[k] - detail.ox;
							newMbr[k+1] = mbr[k+1] - detail.oy;
						}
						
						shape.setCoordinates(newMbr);
					}else{
						double[] coords = geom.getOrdinatesArray();
						double[] newCoords = new double[coords.length];
						for(int k=0;k<coords.length;k=k+2){
							newCoords[k] = coords[k] - detail.ox;
							newCoords[k+1] = coords[k+1] - detail.oy;
						}
						
						shape.setCoordinates(newCoords);
					}
				}
				
				obj.setShape(shape);
				cache.add(obj);
				nmap.clear();
				nmap = null;
				
				size = size+1;
			}
		}
		
		return size;
	}

	@Override
	protected boolean doStop() throws Exception {
		
		return true;
	}

	@Override
	protected Cache[] loadCache(CacheFactory factory) throws Exception {
		
		logger.info("start load mapservice cache");
		
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
		
		String sql = "select classid from " + TableName.CONF_DOCUMENTMODEL 
				+ " where mapid = 1001 and featureclass in('" + TableName.T_TX_ZWYC_DXD 
				+ "','" + TableName.T_TX_ZWYC_DLD 
				+ "','" + TableName.T_TX_DYSB_DYDXD 
				+ "','" + TableName.T_TX_DYSB_DYDLD 
				+ "','" + TableName.T_TX_ZNYC_DZ + "')";
		List<Map<String,Object>> maps = db.findAllMap(sql);
		
//		List<Cache> caches = new ArrayList<Cache>();
		
//		Map<CacheDetail,Cache> caches = new HashMap<CacheDetail,Cache>();
		
		
//		int size = 0;
		final AtomicInteger size = new AtomicInteger(0);
		
		ExecutorService exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		
		List<CacheDetail> task = new ArrayList<CacheDetail>();
		
		sql = "select modelid,modelname from " + TableName.CONF_MODELMETA + " where classid=?";
		for(Map<String,Object> map:maps){
			String classId = String.valueOf(map.get("classid"));
			
			List<Map<String,Object>> list =  db.findAllMap(sql, new Object[]{classId});
			
			for(Map<String,Object> m:list){
				String groupSql = "select dydj from "+String.valueOf(m.get("modelname"))+" where dydj is not null and dydj <> 0 and dydj <> -1 group by dydj";
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
//			caches.put(detail, cache);
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
		
//		arr = caches.values().toArray(arr);
		
		exec.shutdown();
		logger.info("load count:"+size);
		return arr;
	}
	
	@Override
	protected CacheFactory getCacheFactory(List<CacheListener> listeners) throws Exception {
		return new RecordCacheFactory();
	}
	
}
