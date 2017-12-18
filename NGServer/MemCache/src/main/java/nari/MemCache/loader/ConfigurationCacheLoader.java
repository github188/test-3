package nari.MemCache.loader;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.LoaderClassPath;
import nari.Dao.interfaces.DbAdaptor;
import nari.Geometry.GeometryBuilder;
import nari.Logger.LoggerManager;
import nari.MemCache.AbstractCacheLoader;
import nari.MemCache.Cache;
import nari.MemCache.CacheConfig;
import nari.MemCache.CacheFactory;
import nari.MemCache.CacheListener;
import nari.MemCache.FieldProcesser;
import nari.MemCache.IndexType;
import nari.MemCache.Key;
import nari.MemCache.RecordCacheFactory;
import nari.MemCache.Shape;
import nari.MemCache.config.CacheInitAttribute;
import nari.MemCache.config.Layer;
import nari.MemCache.config.LayerLoader;
import nari.MemCache.config.TypedField;
import nari.Xml.impl.ConfigSearchService;
import nari.Xml.interfaces.ConfigSearch;
import oracle.spatial.geometry.JGeometry;

public class ConfigurationCacheLoader extends AbstractCacheLoader {

	private nari.Logger.Logger logger = LoggerManager.getLogger(this.getClass());
	
	private DbAdaptor db = null;
	
	private CacheInitAttribute att;
	
	private Class<?> wrapperBeanClass;
	
	private boolean offsetValid = false;
	
	private double offSetX = 0;
	
	private double offSetY = 0;
	
	private int batchSize = -1;
	
	private String[] indexField;
	
	private String[] indexType;
	
	private List<TypedField> fields;
	
	private Map<String,Field> fieldMap = new HashMap<String,Field>();
	
	private CacheConfig cacheConfig = null;
	
	private static final AtomicInteger it = new AtomicInteger(0);
	
	public ConfigurationCacheLoader(String cacheConfingName,InputStream stream,DbAdaptor db,CacheConfig cacheConfig) {
		if(stream==null){
			logger.info(cacheConfingName+" config is not found,cache not load");
			return;
		}
		ConfigSearch searcher = new ConfigSearchService();
		att = searcher.loadConfigCache(cacheConfingName,stream,"xml",CacheInitAttribute.class);
		this.db = db;
		this.cacheConfig = cacheConfig;
	}
	
	@Override
	protected boolean doInit() throws Exception {
		return true;
	}

	@Override
	protected boolean doStart() throws Exception {
		return true;
	}
	
	private int submitTask(CacheParameter detail ,final Cache cache) throws Exception{
		int size = 0;
		if(batchSize>0){
			size = addCacheBatch(detail, cache);
		}else{
			size = addCache(detail, cache);
		}
		
		return size;
	}
	
	private int addCacheBatch(CacheParameter detail ,final Cache cache) throws Exception{
		String sbzlx = detail.modelId;
		String table = detail.modelName;
		String dydj = detail.dydj; 
		
		String sql = "select count(*) count,max(oid) as max,min(oid) as min from "+table+" where sbzlx="+sbzlx+" and dydj="+dydj;
		
		if(!"".equals(detail.filter)){
			sql = sql + " and "+detail.filter;
		}
		
		Map<String,Object> countMap = db.findMap(sql);
		if(countMap.get("max")==null || countMap.get("min")==null){
			return 0;
		}
		int size = 0;
		
		long max = Long.parseLong(String.valueOf(countMap.get("max")));
		
		long min = Long.parseLong(String.valueOf(countMap.get("min")));
		
		long loop = (max - min)/batchSize + 1;
		
		if(indexField!=null){
			for(int i=0;i<indexField.length;i++){
				cache.addIndex(indexField[i].toLowerCase(), IndexType.valueOf(indexType[i]), null);
			}
		}else{
			cache.addIndex("shape", IndexType.SPATIAL, null);
		}
		
		cache.registCacheBodyClass(wrapperBeanClass);
		
		Object wrapperObject = wrapperBeanClass.newInstance();
		
		List<Map<String,Object>> maps = null;
		
		sql = "select * from "+table+" t where t.sbzlx=? and dydj=? and t.oid>=? and oid<?";
		if(!"".equals(detail.filter)){
			sql = sql + " and "+detail.filter;
		}
		for(long i=0;i<loop;i++){
			long oid1 = min + i * batchSize;
			long oid2 = oid1 + batchSize; 
			
			maps = db.findAllMap(sql,new Object[]{sbzlx,dydj,oid1,oid2});
			if(maps==null || maps.size()==0){
				continue;
			}
			
			for(Map<String,Object> map:maps){
				try {
					Object obj = initCacheObject(wrapperObject,fieldMap,map,fields,cache);
					if(obj!=null){
						cache.add(obj);
						size = size+1;
					}
					
					map.clear();
					map = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			maps.clear();
			maps = null;
		}
		
		logger.info("load table:"+table+"- sbzlx:"+sbzlx+"- dydj:"+dydj+" size:"+size);
		return size;
	}
	
	private int addCache(CacheParameter detail ,final Cache cache) throws Exception{
		String sbzlx = detail.modelId;
		String table = detail.modelName;
		String dydj = detail.dydj; 
		
		int size = 0;
		
		if(indexField!=null){
			for(int i=0;i<indexField.length;i++){
				cache.addIndex(indexField[i].toLowerCase(), IndexType.valueOf(indexType[i]), null);
			}
		}else{
			cache.addIndex("shape", IndexType.SPATIAL, null);
		}
		
		cache.registCacheBodyClass(wrapperBeanClass);
		
		Object wrapperObject = wrapperBeanClass.newInstance();
		
		Field[] wrapperFields = wrapperBeanClass.getDeclaredFields();
		Map<String,Field> fieldMap = new HashMap<String,Field>();
		for(Field f:wrapperFields){
			f.setAccessible(true);
			fieldMap.put(f.getName().toUpperCase(), f);
		}
		
		List<Map<String,Object>> maps = null;
		
		String sql = "select * from "+table+" t where t.sbzlx=? and dydj=?";
		
		if(!"".equals(detail.filter)){
			sql = sql + " and "+detail.filter;
		}
		
		try {
			maps = db.findAllMap(sql,new Object[]{sbzlx,dydj});
			int ss = maps==null?0:maps.size();
			if(ss==0){
				return 0;
			}
			
			for(Map<String,Object> map:maps){
				Object obj = initCacheObject(wrapperObject,fieldMap,map,fields,cache);
				if(obj!=null){
					cache.add(obj);
					size = size+1;
				}
				
				map.clear();
				map = null;
			}
			
			maps = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("load table:"+table+"- sbzlx:"+sbzlx+"- dydj:"+dydj+" size:"+size);
		
		return size;
	}

	@Override
	protected boolean doStop() throws Exception {
		
		return true;
	}

	@Override
	protected Cache[] loadCache(CacheFactory factory) throws Exception {
		
		if(!att.isActive()){
			logger.info("cache is not active");
			return null;
		}
		logger.info("start load cache");
		
		fields = att.getFields();
		
		LayerLoader layerLoader = att.getLoader();
		
		batchSize = layerLoader.getBatchSize();
		
		String tableFilter = layerLoader.getTableFilter();
		
		String index = att.getIndex();
		
		String[] indexSp = index.split(",");
		int len = indexSp.length;
		indexField = new String[len];
		indexType = new String[len];
		
		int k=0;
		for(String str:indexSp){
			indexField[k] = str.split(":")[0];
			indexType[k] = str.split(":")[1];
			k++;
		}
		
		offsetValid = att.isOffsetValid();
		
		createBodyClass("nari.MemCache.loader.WrapperCacheObject$"+it.getAndIncrement(),fields);
		
		if(offsetValid){
			String offSql = "select param_name,param_value from CONF_SERVICEPARAMS where param_name='OFFSETX' or param_name='OFFSETY'";
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
		}
		
		
		String strategy = layerLoader.getStrategy();
		
		logger.info("use strategy:"+strategy);
		
		List<String> exclude = new ArrayList<String>();
		List<Map<String,Object>> classMaps = null;
		
		List<Layer> layers = att.getLayers();
		if("CONFIG".equalsIgnoreCase(strategy)){
			classMaps = new ArrayList<Map<String,Object>>();
			Map<String,Object> map = null;
			if(layers!=null){
				for(Layer layer:att.getLayers()){
					map = new HashMap<String,Object>();
					map.put("classid", layer.getLayerId());
					map.put("filter", layer.getFilter());
					classMaps.add(map);
					
					String str = layer.getExclude();
					if(str!=null && !"".equalsIgnoreCase(str)){
						for(String sub:str.split(",")){
							exclude.add(sub);
						}
					}
				}
			}
			
			logger.info("exclude sub layer:"+Arrays.toString(exclude.toArray()));
		}else if("TABLE".equalsIgnoreCase(strategy)){
			String tf = (tableFilter==null || "".equals(tableFilter))?"":tableFilter;
			String sql = "select classid,'"+tf+"' as filter from dwzy.CONF_OBJECTMETA where classid in(select classid from CONF_FIELDDEFINITION where classid in(select classid from CONF_FIELDDEFINITION where fieldname='DYDJ') and fieldname='SHAPE')";
			
			String excludeTableStr = att.getLoader().getExcludeTable();
			if(excludeTableStr!=null && !"".equals(excludeTableStr)){
				String[] excludeTableArr = excludeTableStr.split(",");
				String[] arr = new String[excludeTableArr.length];
				Arrays.fill(arr, "%s");
				String s = Arrays.toString(arr);
				s = s.substring(1, s.length()-1);
				String excludeTableFilter = " and classid not in ("+s+")";
				excludeTableFilter = String.format(excludeTableFilter, excludeTableArr);
				sql = sql + excludeTableFilter;
			}
			
			classMaps = db.findAllMap(sql);
			
			if(layers!=null){
				for(Layer layer:att.getLayers()){
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("classid", layer.getLayerId());
					map.put("filter", layer.getFilter());
					classMaps.add(map);
				}
			}
		}
		
		logger.info("layer count:"+classMaps.size());
		
		List<CacheParameter> task = new ArrayList<CacheParameter>();
		
		String sql = "select modelid,modelname,modelalias from CONF_MODELMETA where classid=?";
		int h = 1;
		for(Map<String,Object> map:classMaps){
			String classId = String.valueOf(map.get("classid"));
			
			List<Map<String,Object>> list =  db.findAllMap(sql, new Object[]{classId});
			
			for(Map<String,Object> m:list){
				String modelid = String.valueOf(m.get("modelid"));
				
				if(exclude.indexOf(modelid)>=0){
					continue;
				}
				
				String modelName = String.valueOf(m.get("modelname"));
				String modelAlias = String.valueOf(m.get("modelalias"));
				
				StringBuilder bd = new StringBuilder();
				bd.append("select dydj,count(dydj) as count from ").append(modelName).append(" where dydj is not null and dydj <> 0 and dydj <> -1 and sbzlx=?");
				
				String filter = map.get("filter")==null?"":String.valueOf(map.get("filter"));
				String newFilter = "";
				if(filter!=null && !"".equalsIgnoreCase(filter)){
					filter = filter.replace("AND", "and");
					bd.append(" and ").append(filter);
					if(filter.contains("and")){
						String[] subs = filter.split("and");
						StringBuilder str = new StringBuilder();
						for(String sub:subs){
							if(sub.contains("dydj") || sub.contains("DYDJ")){
								continue;
							}
							str.append(sub.trim()).append(" and ");
						}
						String s = str.substring(0, str.length()-3);
						newFilter = s;
					}else{
						if(filter.contains("dydj") || filter.contains("DYDJ")){
							newFilter = "";
						}
					}
					
				}
				
				bd.append(" group by dydj");
				
				logger.info("grouping layer("+h+"):"+modelAlias+"("+modelName+","+modelid+")");
				
				List<Map<String,Object>> groups =  db.findAllMap(bd.toString(),new Object[]{modelid});
				
				for(Map<String,Object> g:groups){
					CacheParameter detail = new CacheParameter();
					
					detail.modelId = modelid;
					detail.modelName = String.valueOf(m.get("modelname"));
					detail.modelAlias = String.valueOf(m.get("modelalias"));
					detail.dydj = String.valueOf(g.get("dydj"));
					detail.count = g.get("count")==null?0:Integer.parseInt(String.valueOf(g.get("count")));
					detail.filter = newFilter;
					
					task.add(detail);
				}
			}
			h++;
		}
		
		logger.info("count:"+task.size());
		
		final CountDownLatch latch = new CountDownLatch(task.size());
		
		final AtomicInteger size = new AtomicInteger(0);
		
		int threadPoolSize = att.getThreadPoolSize()==null?Runtime.getRuntime().availableProcessors():Integer.parseInt(att.getThreadPoolSize());
		
		if(threadPoolSize==0){
			threadPoolSize = Runtime.getRuntime().availableProcessors();
		}
		ExecutorService exec = Executors.newFixedThreadPool(threadPoolSize);
		
		Cache[] arr = new Cache[task.size()];
		int i=0;
		for(final CacheParameter detail:task){
			Key	key = Key.val(detail.modelId+"-"+detail.dydj);
			final Cache cache = factory.createCache(key,detail.count);
			
			logger.info("init task:"+detail.modelName+"-"+detail.dydj+"("+detail.modelAlias+","+detail.modelId+")");
			
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
						size.getAndAdd(count);
						latch.countDown();
					}
				}
			});
		}
		
		latch.await();
		
		exec.shutdown();
		logger.info("load feature count:"+size);
		return arr;
	}
	
	@Override
	protected CacheFactory getCacheFactory(List<CacheListener> listeners) throws Exception {
		return new RecordCacheFactory(cacheConfig==null?null:cacheConfig.getCacheListeners());
	}

	private void createBodyClass(String classFullName,List<TypedField> fields) throws Exception {
		ClassPool pool = new ClassPool(true);
		pool.appendClassPath(new LoaderClassPath(super.getClass().getClassLoader()));

		CtClass cls = pool.makeClass(classFullName);
		
		pool.importPackage(Shape.class.getName());
		
//		List<TypedField> fields = fieldSet.getFields();
		for (TypedField field : fields) {
			if (field.getFieldType().equalsIgnoreCase("int")){
				cls.addField(CtField.make("private int " + field.getFieldName()+ ";", cls));
			} else if (field.getFieldType().equalsIgnoreCase("long")){
				cls.addField(CtField.make("private long " + field.getFieldName() + ";", cls));
			} else if (field.getFieldType().equalsIgnoreCase("double")){
				cls.addField(CtField.make("private double " + field.getFieldName() + ";", cls));
			} else if (field.getFieldType().equalsIgnoreCase("short")){
				cls.addField(CtField.make("private short " + field.getFieldName() + ";", cls));
			} else if (field.getFieldType().equalsIgnoreCase("float")) {
				cls.addField(CtField.make("private float " + field.getFieldName() + ";", cls));
			} else if(field.getFieldType().equalsIgnoreCase("String")){
				cls.addField(CtField.make("private String " + field.getFieldName() + ";", cls));
			} else if(field.getFieldType().equalsIgnoreCase("Spatial")){
				cls.addField(CtField.make("private Shape " + field.getFieldName() + ";", cls));
			}
		}
		
		wrapperBeanClass = cls.toClass(super.getClass().getClassLoader(),super.getClass().getProtectionDomain());
		Field[] wrapperFields = wrapperBeanClass.getDeclaredFields();
		
		for(Field f:wrapperFields){
			f.setAccessible(true);
			fieldMap.put(f.getName().toUpperCase(), f);
		}
	}
	
	private Object initCacheObject(Object obj,Map<String,Field> fieldMap,Map<String,Object> map,List<TypedField> fields,Cache cache) throws Exception{
		Field field = null;
		FieldProcesser processer = null;
		boolean flag = false;
		
		for(TypedField f:fields){
			field = fieldMap.get(f.getFieldName().toUpperCase());
			
			processer = cache.getFieldProcesser(f.getFieldName());
			
			if("int".equalsIgnoreCase(f.getFieldType())){
				if(processer!=null){
					field.set(obj, processer.process(map.get(f.getFieldName().toLowerCase()), null));
				}else{
					field.set(obj, map.get(f.getFieldName().toLowerCase())==null?0:Integer.parseInt(String.valueOf(map.get(f.getFieldName().toLowerCase()))));
				}
			}else if("long".equalsIgnoreCase(f.getFieldType())){
				if(processer!=null){
					field.set(obj, processer.process(map.get(f.getFieldName().toLowerCase()), null));
				}else{
					field.set(obj, map.get(f.getFieldName().toLowerCase())==null?0:Long.parseLong(String.valueOf(map.get(f.getFieldName().toLowerCase()))));
				}
			}else if("double".equalsIgnoreCase(f.getFieldType())){
				if(processer!=null){
					field.set(obj, processer.process(map.get(f.getFieldName().toLowerCase()), null));
				}else{
					field.set(obj, map.get(f.getFieldName().toLowerCase())==null?0:Double.parseDouble(String.valueOf(map.get(f.getFieldName().toLowerCase()))));
				}
			}else if("short".equalsIgnoreCase(f.getFieldType())){
				if(processer!=null){
					field.set(obj, processer.process(map.get(f.getFieldName().toLowerCase()), null));
				}else{
					field.set(obj, map.get(f.getFieldName().toLowerCase())==null?0:Short.parseShort(String.valueOf(map.get(f.getFieldName().toLowerCase()))));
				}
			}else if("float".equalsIgnoreCase(f.getFieldType())){
				if(processer!=null){
					field.set(obj, processer.process(map.get(f.getFieldName().toLowerCase()), null));
				}else{
					field.set(obj, map.get(f.getFieldName().toLowerCase())==null?0:Float.parseFloat(String.valueOf(map.get(f.getFieldName().toLowerCase()))));
				}
			}else if("String".equalsIgnoreCase(f.getFieldType())){
				if(processer!=null){
					field.set(obj, processer.process(map.get(f.getFieldName().toLowerCase()), null));
				}else{
					field.set(obj, map.get(f.getFieldName().toLowerCase())==null?"":String.valueOf(map.get(f.getFieldName().toLowerCase())));
				}
			}else if("Spatial".equalsIgnoreCase(f.getFieldType())){
				Object shapeObj = map.get("shape");
				if(processer!=null){
					Object shp = processer.process(shapeObj, map.get("oid"));
					if(shp==null){
						flag = true;
						break;
					}
					field.set(obj, processer.process(shapeObj, map.get("oid")));
				}else{
					if(shapeObj==null){
						field.set(obj, null);
					}else{
						Shape shape = createShape(shapeObj);
						field.set(obj, shape);
					}
				}
			}
		}
		
		if(flag){
			return null;
		}
		return obj;
	}
	
	private Shape createShape(Object shapeObj){
		JGeometry geom = GeometryBuilder.getBuilder().readGeometry(shapeObj);
		Shape shape = new Shape();
		shape.setType(geom.getType());
		shape.setElementInfo(geom.getElemInfo());
		if(geom.getType()==1){
			if(offsetValid){
				double[] mbr = geom.getMBR();
				double[] newMbr = new double[mbr.length];
				for(int k=0;k<mbr.length;k=k+2){
					newMbr[k] = mbr[k] - offSetX;
					newMbr[k+1] = mbr[k+1] - offSetY;
				}
				
				shape.setCoordinates(newMbr);
			}else{
				shape.setCoordinates(geom.getMBR());
			}
		}else{
			if(offsetValid){
				double[] coords = geom.getOrdinatesArray();
				double[] newCoords = new double[coords.length];
				for(int k=0;k<coords.length;k=k+2){
					newCoords[k] = coords[k] - offSetX;
					newCoords[k+1] = coords[k+1] - offSetY;
				}
				
				shape.setCoordinates(newCoords);
			}else{
				shape.setCoordinates(geom.getOrdinatesArray());
			}
		}
		return shape;
	}
	
}
