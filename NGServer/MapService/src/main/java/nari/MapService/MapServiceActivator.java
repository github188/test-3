package nari.MapService;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import oracle.spatial.geometry.JGeometry;

import nari.Dao.interfaces.DbAdaptor;
import nari.Geometry.CoordinateSequence;
import nari.Geometry.DefaultCoordinateSequence;
import nari.Geometry.GeometryBuilder;
import nari.Geometry.GeometryType;
import nari.Logger.LoggerManager;
import nari.MapService.Service.impl.MapServiceImpl;
import nari.MapService.Service.interfaces.MapService;
import nari.MapService.bean.LineObject;
import nari.MemCache.Cache;
import nari.MemCache.CacheEngine;
import nari.MemCache.CacheService;
import nari.MemCache.CacheTracker;
import nari.MemCache.Key;
import nari.MemCache.config.CacheInitAttribute;
import nari.MemCache.loader.ConfigurationCacheEngine;
import nari.Xml.impl.ConfigSearchService;
import nari.Xml.interfaces.ConfigSearch;
import nari.model.TableName;
import nari.model.device.ModelService;
import nari.model.geometry.GeometryService;
import nari.model.symbol.SymbolAdapter;

import com.application.plugin.Activator;
import com.application.plugin.ActivatorReg;
import com.application.plugin.AttributeKey;
import com.application.plugin.BundleContext;
import com.application.plugin.ExportConfig;
import com.application.plugin.Provider;
import com.application.plugin.ServiceType;
import com.application.plugin.Version;
import com.application.plugin.bundle.BundleConfig;
import com.application.plugin.bundle.BundleException;
import com.application.plugin.service.ServiceFilter;
import com.application.plugin.service.ServiceReference;
import com.application.plugin.tracker.TrackerListener;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.operation.linemerge.LineMerger;

@ActivatorReg(name="MapServiceActivator")
public class MapServiceActivator implements Activator,TrackerListener{
	
	private nari.Logger.Logger logger = LoggerManager.getLogger(this.getClass());
	
	public static ModelService modelService = ModelService.NONE;
	
	public static DbAdaptor dbAdaptor = DbAdaptor.NONE;
	
	public static SymbolAdapter symboladapter = SymbolAdapter.NONE;
	
	public static GeometryService geoService = GeometryService.NONE;
	
	private GeometryFactory factory = new GeometryFactory();
	
	public static final Map<String,LineObject> lineCache = new HashMap<String,LineObject>();
	
	private static CacheEngine engine = null;
	
	@Override
	public boolean init(BundleConfig context) throws BundleException {
		context.getServiceTracker().track(ModelService.class, this);
		context.getServiceTracker().track(DbAdaptor.class, this);
		context.getServiceTracker().track(SymbolAdapter.class, this);
		context.getServiceTracker().track(GeometryService.class, this);
		context.getServiceTracker().track(CacheService.class, this);
		return true;
	}

	@Override
	public boolean start(BundleContext context) throws BundleException {
		ExportConfig config = new ExportConfig(){

			@Override
			public ServiceType getServiceType() {
				return ServiceType.HTTP;
			}
			
		};
		context.export(MapService.class, new Provider<MapService>() {

			@Override
			public MapServiceImpl get() throws BundleException {
				return new MapServiceImpl();
			}

			@Override
			public AttributeKey getKey() throws BundleException {
				return AttributeKey.key(MapService.class, Version.defaultVersion());
			}

			@Override
			public Version version() throws BundleException {
				return  Version.defaultVersion();
			}


			@Override
			public ServiceFilter[] getFilter() throws BundleException {
				return null;
			}
			
			
		}, config);
		
		return true;
	}

	@Override
	public boolean stop(BundleContext context) throws BundleException {
		return true;
	}

	@Override
	public <T> void serviceAdd(ServiceReference<T> ref,BundleContext context) throws BundleException {
		T ins = (T)ref.get();
		if(ins instanceof ModelService){
			modelService = (ModelService)ins;
		}
		
		T symIns = (T)ref.get();
		if(symIns instanceof SymbolAdapter){
			symboladapter = (SymbolAdapter)symIns;
		}
		
		T dbins = (T)ref.get();
		if(dbins instanceof DbAdaptor){
			dbAdaptor = (DbAdaptor)dbins;
			
			InputStream stream = context.getResourceAsStream("config/cache_mapservice.xml");
			
			ConfigSearch searcher = new ConfigSearchService();
			CacheInitAttribute att = searcher.loadConfigCache("cache_mapservice",stream,"xml",CacheInitAttribute.class);
			if(att!=null){
				String mergerFilter = att.getMergerFilter();
	//			engine = new StdCacheEngine();
	//			
	//			try {
	//				if(engine.init()){
	//					engine.start();
	//				}
	//			} catch (Exception e) {
	//				e.printStackTrace();
	//			}
				
				if(att.isMerger()){
					merger(mergerFilter);
				}
				
	//			InputStream stream = context.getResourceAsStream("config/cache_mapservice.xml");
				
				stream = context.getResourceAsStream("config/cache_mapservice.xml");
				engine = new ConfigurationCacheEngine("mapservice", stream, dbAdaptor);
				
				try {
					engine.getCacheConfig().addCacheListener(new LineGeometryCacheListener(context,dbAdaptor));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				try {
					if(engine.init()){
						engine.start();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		
		T geoIns = (T)ref.get();
		if(geoIns instanceof GeometryService){
			geoService = (GeometryService)geoIns;
		}
		
//		if(modelService!=null && modelService!=ModelService.NONE && dbAdaptor!=null && dbAdaptor!=DbAdaptor.NONE){
//			InputStream stream = context.getResourceAsStream("config/cache_mapservice.xml");
//			
//			engine = new ConfigurationCacheEngine("cache_mapservice", stream, dbAdaptor);
//			
//			try {
//				engine.getCacheConfig().addCacheListener(new LineGeometryCacheListener(context,dbAdaptor));
//			} catch (Exception e1) {
//				e1.printStackTrace();
//			}
//			
//			try {
//				if(engine.init()){
//					engine.start();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
	}

	@Override
	public <T> void serviceModify(ServiceReference<T> ref,BundleContext context) throws BundleException {
//		T ins = (T)ref.get();
//		if(ins instanceof UserService){
//			userService = (UserService)ins;
//		}
//		
//		T userIns = (T)ref.get();
//		if(userIns instanceof UserService){
//			userService = (UserService)userIns;
//		}
	}

	@Override
	public <T> void serviceRemove(ServiceReference<T> ref,BundleContext context) throws BundleException {
//		T ins = (T)ref.get();
//		if(ins instanceof UserService){
//			userService = null;
//		}
//		
//		T userIns = (T)ref.get();
//		if(userIns instanceof UserService){
//			userService = null;
//		}
	}

	public static Cache getCache(String cacheId,String dydj) throws Exception{
		CacheTracker tracker = MapServiceActivator.engine.getTracker();
		Cache cache = tracker.select(Key.val(cacheId+"-"+dydj));
		
		return cache;
	}
	
	@SuppressWarnings("unchecked")
	private void merger(String mergerFilter){
		String sql = "select * from " + TableName.T_TX_ZWYC_XL;
		
		if(!StringUtils.isEmpty(mergerFilter)){
			sql = sql+" where "+mergerFilter;
		}
		
		List<Map<String,Object>> list = null;
		try {
			list = dbAdaptor.findAllMap(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(list==null){
			return;
		}
		
		sql = "select * from " + TableName.T_TX_ZWYC_DXD + " where ssxl = ?";
		List<Map<String,Object>> dxdList = null;
		for(Map<String,Object> map:list){
			logger.info(map.get("sbmc")+"("+map.get("dydj")+")");
			
			try {
				dxdList = dbAdaptor.findAllMap(sql,new Object[]{map.get("oid")});
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			if(dxdList==null || dxdList.size()==0){
				continue;
			}
			
			LineMerger lineMerger = new LineMerger();
			
			List<Geometry> geoList = new ArrayList<Geometry>();
			
			for(Map<String,Object> m:dxdList){
				
				JGeometry jGeometry = GeometryBuilder.getBuilder().readGeometry(m.get("shape"));
				Object[] ordinatesArray = jGeometry.getOrdinatesOfElements();
				
				for(Object obj: ordinatesArray){
					double[] lineStringDoubles = (double[])obj;
					int coordsNum = lineStringDoubles.length/2;
					Coordinate[] coords = new Coordinate[coordsNum];
					for(int k=0;k<coordsNum;k++){
						coords[k] = new Coordinate();
						coords[k].x = lineStringDoubles[2*k];
						coords[k].y = lineStringDoubles[2*k+1];
					}
					geoList.add(factory.createLineString(coords));
				}
			}
			
			lineMerger.add(geoList);
			Collection<Geometry> geometryollection = lineMerger.getMergedLineStrings();
			
			int lineStringNum = geometryollection.size();
			
			List<Double> doubleList = new ArrayList<Double>();
			
			int[] startDouble = new int[lineStringNum];
			int beforeSize = 0;
			
			int startDoubleIndex = 0;
			for(Geometry geometry:geometryollection){
				Coordinate[] coords = geometry.getCoordinates();
				int coordsNum = coords.length;
				for(int k=0;k<coordsNum;k++){
					doubleList.add(coords[k].x);
					doubleList.add(coords[k].y);
				}
				startDouble[startDoubleIndex] = beforeSize+1;
				beforeSize = doubleList.size();
				startDoubleIndex++;
			}
			
			double[] doubleArray = new double[doubleList.size()];
			for(int j=0;j<doubleList.size();j++){
				doubleArray[j] = doubleList.get(j);
			}
			
			CoordinateSequence[] seqs = new CoordinateSequence[lineStringNum];
			
			for(int j=0;j<lineStringNum;j++){
				double[] coords = null;
				if(j==lineStringNum-1){
					coords = new double[doubleList.size()-startDouble[j]+1];
				}else{
					coords = new double[startDouble[j+1]-startDouble[j]];
				}
				for(int k=0;k<coords.length;k++){
					coords[k] = doubleArray[startDouble[j]+k-1];
				}
				seqs[j] = new DefaultCoordinateSequence(coords);
			}
			GeometryType gtype = null;
			if(lineStringNum>1){
				gtype = GeometryType.MULTIPOLYLINE;
			}else{
				gtype = GeometryType.POLYLINE;
			}
			
			JGeometry jgeom= GeometryBuilder.getBuilder().createGeometry(gtype, seqs);
			
			LineObject line = new LineObject();
			
			line.setGeometry(jgeom);
			line.setOid(Integer.parseInt(String.valueOf(map.get("oid"))));
			line.setSbmc(String.valueOf(map.get("sbmc")));
			line.setSbzlx(Integer.parseInt(String.valueOf(map.get("sbzlx"))));
			MapServiceActivator.lineCache.put(String.valueOf(map.get("oid")), line);
		}
		logger.info("OK");
	}
	
	public static JGeometry getGeometry(String lineId){
		LineObject obj = MapServiceActivator.lineCache.get(lineId);
		if(obj==null){
			return null;
		}
		return obj.getGeometry();
	}
	
	public static LineObject getLine(String lineId){
		return MapServiceActivator.lineCache.get(lineId);
	}
}
