package nari.Obsevation.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nari.Geometry.Coordinate;
import nari.Geometry.Envelope;
import nari.Geometry.GeometryBuilder;
import nari.Geometry.GeometryCollection;
import nari.Geometry.GeometryType;
import nari.Geometry.Polygon;
import nari.Logger.Logger;
import nari.Logger.LoggerManager;
import nari.MemCache.Cache;
import nari.MemCache.CacheEntry;
import nari.MemCache.Pointer;
import nari.MemCache.Shape;
import nari.MemCache.matcher.QueryMatcher;
import nari.MemCache.matcher.QueryPolygon;
import nari.Obsevation.ObsevationActivator;
import nari.model.bean.SymbolDef;
import nari.model.device.DefaultDevice;
import nari.model.device.Device;
import nari.model.device.DeviceModel;
import nari.model.device.ModelService;
import nari.model.device.SpatialDevice;
import nari.model.geometry.GeometryService;
import nari.model.symbol.SymbolAdapter;
import nari.parameter.MapService.GetVectorMap.GetVectorMapRequest;
import nari.parameter.MapService.GetVectorMap.GetVectorMapResponse;
import nari.parameter.bean.GeometryPair;
import nari.parameter.bean.PSRCondition;
import nari.parameter.bean.QueryField;
import nari.parameter.bean.QueryRecord;
import nari.parameter.bean.QueryResult;
import nari.parameter.bean.SymbolPair;
import nari.parameter.bean.SubPSRCondition;
import nari.parameter.code.ReturnCode;

/**
 * 实时加载多个设备信息
 */
public class GetVectorMapByCacheHandler {
	
	private Logger logger = LoggerManager.getLogger(this.getClass());
	
	private GeometryService geoService = ObsevationActivator.geoService;
	private DeviceModel model = DeviceModel.NONE;
	private SymbolAdapter symAdp = ObsevationActivator.symboladapter;
	private ModelService ms = ObsevationActivator.modelService;
//	private CacheService cacheService = MapServiceActivator.cacheService;
	
	public GetVectorMapResponse getVectorMapByCache(GetVectorMapRequest request) {
		GetVectorMapResponse resp = new GetVectorMapResponse();
		double[] coords0 = request.getBbox();
		long start = System.currentTimeMillis();
		Polygon polygon = geoService.createPolygon(coords0);
		
		if (polygon == null) {
			resp.setCode(ReturnCode.MISSTYPE);
			return resp;
		}

		PSRCondition[] classCondition = request.getClassCondition();
		String[] queryFieldName = request.getRetrunField();
		if (queryFieldName == null) {
			queryFieldName = new String[] { "OID", "SBMC", "SHAPE", "SBZLX", "DYDJ", "FHDX" };
		}
		boolean isQueryGeometry = true;
		
		Envelope enve = polygon.getEnvelope();
		QueryPolygon poly = new QueryPolygon(enve.getMinX(),enve.getMinY(),enve.getMaxX(),enve.getMaxY());
		
		QueryResult[] results = new QueryResult[classCondition.length];

		int count = 0;
		
		List<QueryRecord> records = null;
		
		for (int i = 0; i < classCondition.length; i++) {
			
			String classId = classCondition[i].getClassId();
			try {
				model = ms.fromClass(classId, false);
			} catch (Exception e) {
				logger.info("模型创建时出错");
				resp.setCode(ReturnCode.BUILDMODEL);
				return resp;
			}

			SubPSRCondition[] modelCondition = classCondition[i].getModelCondition();
			
			if(modelCondition == null){
				continue;
			}
			
			Map<String,Object> map = null;
			
			records = new ArrayList<QueryRecord>();
			QueryMatcher matcher = new QueryMatcher("shape", poly);
			
			for(SubPSRCondition condition:modelCondition){
				String sbzlx = condition.getSbzlx();
				String[] DYDJ = condition.getDYDJ();
				
//				QueryMatcher volMatcher = new QueryMatcher("dydj", DYDJ, "in");
//				matcher.and(volMatcher);
				
				for(String dydj:DYDJ){
					try {
						Cache cache = ObsevationActivator.getCache(sbzlx,dydj);
						if(cache==null){
							continue;
						}
						CacheEntry entry = cache.search(matcher);
						if(entry==null){
							continue;
						}
						count += entry.size();
						logger.info("search "+sbzlx+"-"+dydj+" count:"+entry.size());
						while(entry.hasNext()){
							Pointer ptr = entry.next();
							
							map = new HashMap<String,Object>();
							
							for(String field:queryFieldName){
								Object val = ptr.getFieldValue(field.toLowerCase(), null);
								if(val.getClass()==Shape.class){
									Shape shape = (Shape)val;
									val = GeometryBuilder.getBuilder().buildGeometry(shape.getType(), shape.getElementInfo(), shape.getCoordinates());
								}
								map.put(field, val);
							}
							Device dev = new DefaultDevice(model.getFieldDef(), model.getClassDef(), model.getSubClassDef(), map);
							
							QueryRecord record = processResult(dev,queryFieldName,isQueryGeometry);
							records.add(record);
						}
					} catch (Exception excep) {
						excep.printStackTrace();
						resp.setCode(ReturnCode.SQLERROR);
						return resp;
					}
				}
			}
			
			QueryRecord[] arr = new QueryRecord[records.size()];
			arr = records.toArray(arr);
			results[i] = new QueryResult();
			results[i].setRecords(arr);
			results[i].setCount(records.size());
		}
		
		logger.info("count:"+count);
		
		resp.setResult(results);
		resp.setCode(ReturnCode.SUCCESS);
		resp.setRequestExtend(request.getRequestExtend());
		long end = System.currentTimeMillis();
		logger.info("每次查询用时:" + (end - start) + "ms");
		return resp;
	}
	
	private QueryRecord processResult(Device dev,String[] queryFieldName ,boolean isQueryGeometry) throws Exception {
		QueryRecord record = new QueryRecord();
		
		if (isQueryGeometry) {
			SpatialDevice spaDev = dev.asSpatialDevice();
			
			GeometryPair geom = new GeometryPair();
			
			String geometryType = "";
			GeometryType geomType = spaDev.getGeometry().getGeometry().getGeometryType();
			geometryType = geomType.toString();
			if(("MULTIPOLYLINE").equalsIgnoreCase(geometryType)){
				GeometryCollection multipolyLine = (GeometryCollection)spaDev.getGeometry().getGeometry();
				geom.setOther(multipolyLine.getNumGeometry());
			}
			Coordinate[] coordnates = spaDev.getGeometry().getGeometry().getCoordinates();
			int natesnum = coordnates.length;
			
			double[] coords = new double[2 * natesnum];
			for (int k = 0; k < natesnum; k++) {
				coords[2 * k] = coordnates[k].getX();
				coords[2 * k + 1] = coordnates[k].getY();
			}
			geom.setGeometryType(geometryType);
			geom.setCoords(coords);
			record.setGeom(geom);
		}
		
		Arrays.sort(queryFieldName);
		int index = Arrays.binarySearch(queryFieldName, "SHAPE");
		
		int size = index>=0?queryFieldName.length-1:queryFieldName.length;
		QueryField[] fields = new QueryField[size];
		int i = 0;
		QueryField qfield = null;
		Object val = null;
		for (String field:queryFieldName) {
			if (field.equalsIgnoreCase("SHAPE")) {
				continue;
			}
			qfield = new QueryField();
			qfield.setFieldName(field);

			val = dev.getValue(field.toUpperCase());
			qfield.setFieldValue(val==null?"":String.valueOf(val));
			qfield.setFieldAlias(model.getFieldDef().find(field.toUpperCase()).getFieldAlias());
			fields[i++] = qfield;
		}
		
		record.setFields(fields);
		
		SymbolDef symDef = symAdp.search(dev);
		if (symDef != null) {
			SymbolPair symbol = new SymbolPair();
			//symbol.setModelId(symDef.getModelId());
			symbol.setSymbolValue(symDef.getSymbolValue());
			symbol.setSymbolId(symDef.getSymbolId());
			//symbol.setDevtypeId(symDef.getDevTypeId());
			record.setSymbol(symbol);
		}
		return record;
	}
}
