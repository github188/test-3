package nari.TopoAnalysisService.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nari.Dao.interfaces.DbAdaptor;
import nari.Geometry.Coordinate;
import nari.Geometry.Geometry;
import nari.Geometry.GeometryCollection;
import nari.Geometry.GeometryType;
import nari.Geometry.Polyline;
import nari.TopoAnalysisService.TopoServiceActivator;
import nari.model.ModelActivator;
import nari.model.TableName;
import nari.model.bean.FieldDetail;
import nari.model.bean.GeometryDef;
import nari.model.bean.SubClassDef;
import nari.model.bean.SymbolDef;
import nari.model.device.Device;
import nari.model.device.DeviceModel;
import nari.model.device.ModelService;
import nari.model.device.ResultSet;
import nari.model.device.SpatialDevice;
import nari.model.device.filter.CriteriaBuilder;
import nari.model.device.filter.Expression;
import nari.model.symbol.SymbolAdapter;
import nari.network.device.TopoDevice;
import nari.network.interfaces.NetworkAdaptor;
import nari.parameter.bean.GeometryPair;
import nari.parameter.bean.PSRDef;
import nari.parameter.bean.QueryField;
import nari.parameter.bean.QueryRecord;
import nari.parameter.bean.QueryResult;
import nari.parameter.bean.SubPSRDef;
import nari.parameter.bean.SymbolPair;

public abstract class BaseTopoAnalyzeHandler {
	
	/**
	 * 拓扑设备主键
	 * @author birderyu
	 *
	 */
	public static class TopoDeviceInfo {
		private String modelId;
		private String oid;
		
		public TopoDeviceInfo(String modelId, String oid) {
			super();
			this.modelId = modelId;
			this.oid = oid;
		}

		public String getModelId() {
			return modelId;
		}

		public void setModelId(String modelId) {
			this.modelId = modelId;
		}

		public String getOid() {
			return oid;
		}

		public void setOid(String oid) {
			this.oid = oid;
		}
		
		public int getNModelId() {
			return Integer.valueOf(modelId);
		}
		
		public int getNOid() {
			return Integer.valueOf(oid);
		}
	}
	
	protected ModelService modelService = TopoServiceActivator.modelService;
	protected DbAdaptor dbAdaptor = TopoServiceActivator.dbAdaptor;
	protected SymbolAdapter symbolAdaptor = TopoServiceActivator.symbolAdapter;
	protected NetworkAdaptor networkAdaptor = TopoServiceActivator.networkAdaptor;
	
	/**
	 * 根据EqumentID获取ClassID
	 * @param equmentId
	 * @return
	 */
	protected Iterator<Integer> getClassIdsByEqumentId(String equmentId) {
		List<Integer> classIds = ModelActivator.getClassIdByEquId(equmentId);
		if (null == classIds) {
			return null;
		}
		return classIds.iterator();
	}
	
	protected List<String> getModelIdsByClassId(String classId) {
		
		DeviceModel model = null;
		try {
			model = modelService.fromClass(classId, false);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		
		SubClassDef[] modelDefs = model.getSubClassDef();
		if (null == modelDefs) {
			return null;
		}
		List<String> modelIds = new ArrayList<String>(modelDefs.length);
		for (SubClassDef modelDef : modelDefs) {
			modelIds.add(modelDef.getModelId());
		}
		
		return modelIds;
	}
	
	/**
	 * 根据ClassID和SBID获取拓扑设备主键
	 * @param classId
	 * @param sbId
	 * @return
	 */
	protected TopoDeviceInfo getDeviceInfoByClassId(String classId, String sbId) {
		
		// 查找模型
		DeviceModel model = null;
		try {
			model = modelService.fromClass(classId, false);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return getDeviceInfoByModel(model, sbId);
	}
	
	/**
	 * 根据ModelID和SBID获取拓扑设备主键
	 * @param modelId
	 * @param sbId
	 * @return
	 */
	protected TopoDeviceInfo getDeviceInfoByModelId(String modelId, String sbId) {
		
		// 查找模型
		DeviceModel model = null;
		try {
			model = modelService.fromSubClass(modelId, false);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return getDeviceInfoByModel(model, sbId);
	}

	/**
	 * 根据EqumentID和SBID获取拓扑设备主键
	 * @param equmentId
	 * @param sbId
	 * @return
	 */
	protected TopoDeviceInfo getDeviceInfoByEqumentId(String equmentId, String sbId) {

		Iterator<Integer> classIds = getClassIdsByEqumentId(equmentId);
		if (null == classIds) {
			return null;
		}
		
		while (classIds.hasNext()) {
			String classId = classIds.next().toString();
			TopoDeviceInfo deviceInfo = getDeviceInfoByClassId(classId, sbId);
			if (null != deviceInfo) {
				return deviceInfo;
			}
		}
		return null;
	}
	
	private TopoDeviceInfo getDeviceInfoByModel(DeviceModel model, String sbId) {
		
		if (null == model) {
			return null;
		}
		
		// 搜索设备
		String[] returnField = new String[] { "OID", "SBZLX" };
		CriteriaBuilder builder = model.getEntryManager().getCriteriaBuilder();
		Expression exp = builder.equal(builder.getRoot().get("SBID", String.class), sbId);
		ResultSet results = null;
		try {
			results = model.search(returnField, exp, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		// 结果判空
		if (null == results) {
			return null;
		}
		Iterator<Device> iter = results.resultList();
		if (null == iter) {
			return null;
		}
		
		// 获取结果
		while (iter.hasNext()) {
			Device device = iter.next();
			String modelId = String.valueOf(device.getValue("SBZLX"));
			String oid = String.valueOf(device.getValue("OID"));
			TopoDeviceInfo deviceInfo = new TopoDeviceInfo(modelId, oid);
			return deviceInfo;
		}
		return null;
	}
		
	protected Iterator<QueryResult> getQueryResultsFromDevices(Iterator<TopoDevice> topoDevices) {
		
		if (null == topoDevices) {
			return null;
		}
		
		Map<Integer, List<Integer>> deviceInfos = new HashMap<Integer, List<Integer>>();
		while (topoDevices.hasNext()) {
			TopoDevice device = topoDevices.next();
			List<Integer> oidList = deviceInfos.get(device.getModelId());
			if (null == oidList) {
				oidList = new ArrayList<Integer>();
				deviceInfos.put(device.getModelId(), oidList);
			}
			oidList.add(device.getOid());
		}
		
		List<QueryResult> results = new ArrayList<QueryResult>();
		
		// 构造查询条件进行查询
		for (Map.Entry<Integer, List<Integer>> deviceInfo : deviceInfos.entrySet()) {
			
			List<Integer> oids = deviceInfo.getValue();
			if (null == oids || oids.isEmpty()) {
				continue;
			}
			DeviceModel model = null;
			try {
				model = modelService.fromSubClass(String.valueOf(deviceInfo.getKey()), false);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (null == model) {
				continue;
			}
			
			// 构造查询条件
			Iterator<FieldDetail> fieldIter = model.getFieldDef().details();
			List<String> returnFieldList = new ArrayList<String>();
			while (fieldIter.hasNext()) {
				String fieldName = fieldIter.next().getFieldName();
				returnFieldList.add(fieldName);
			}
			String[] returnField = returnFieldList.toArray(new String[returnFieldList.size()]);
			CriteriaBuilder builder = model.getEntryManager().getCriteriaBuilder();
			Expression exp = null;
			if (oids.size() == 1) {
				exp = builder.equal(builder.getRoot().get("OID", String.class), String.valueOf(oids.get(0)));
			} else {
				Object[] oOIDs = new Object[oids.size()];
				int i = 0;
				for (Integer oid : oids) {
					oOIDs[i++] = String.valueOf(oid);
				}
				exp = builder.in(builder.getRoot().get("OID", String.class), oOIDs);
			}
			
			// 查询并将查询结果拼装在一起
			Iterator<QueryRecord> recordIter = null;
			try {
				recordIter = queryRecord(model, exp, returnField);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (null == recordIter) {
				continue;
			}
			QueryResult result = getQueryResult(recordIter, model);
			if (null == result) {
				continue;
			}
			results.add(result);
		}
		return results.iterator();
		
	}
	
	protected QueryResult getQueryResultFromDevice(TopoDevice device) {
		
		if (null == device) {
			return null;
		}
		
		DeviceModel model = null;
		try {
			model = modelService.fromSubClass(String.valueOf(device.getModelId()), false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (null == model) {
			return null;
		}
		
		// 构造查询条件
		Iterator<FieldDetail> fieldIter = model.getFieldDef().details();
		List<String> returnFieldList = new ArrayList<String>();
		while (fieldIter.hasNext()) {
			String fieldName = fieldIter.next().getFieldName();
			returnFieldList.add(fieldName);
		}
		String[] returnField = returnFieldList.toArray(new String[returnFieldList.size()]);
		CriteriaBuilder builder = model.getEntryManager().getCriteriaBuilder();
		Expression exp = builder.equal(builder.getRoot().get("OID", String.class), 
				String.valueOf(device.getOid()));
		
		// 获取查询结果
		Iterator<QueryRecord> recordIter = null;
		try {
			recordIter = queryRecord(model, exp, returnField);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null == recordIter || !recordIter.hasNext()) {
			return null;
		}
		return getQueryResult(recordIter, model);
	}
	
	/**
	 * （通用）根据查询记录构造查询结果
	 * @param recordIter
	 * @param model
	 * @return
	 */
	protected QueryResult getQueryResult(Iterator<QueryRecord> recordIter, DeviceModel model) {
		
		if (null == recordIter || null == model) {
			return null;
		}
		
		List<QueryRecord> recordList = new ArrayList<QueryRecord>();
		while (recordIter.hasNext()) {
			recordList.add(recordIter.next());
		}
		QueryRecord[] records = recordList.toArray(new QueryRecord[recordList.size()]);
		
		PSRDef psrDef = new PSRDef();
		psrDef.setName(model.getClassDef().getClassName());
		psrDef.setPsrName(model.getClassDef().getClassName());
		psrDef.setPsrType(model.getClassDef().getClassType());
		SubPSRDef[] subPsrDefs = new SubPSRDef[model.getClassDef().getSubClassDef().length];
		for (int i = 0; i < model.getClassDef().getSubClassDef().length; ++i) {
			subPsrDefs[i] = new SubPSRDef();
			subPsrDefs[i].setSubPSRName(model.getClassDef().getSubClassDef()[i].getModelName());
			subPsrDefs[i].setSubPSRType(model.getClassDef().getSubClassDef()[i].getModelId());
		}
		
		QueryResult result = new QueryResult();
		result.setRecords(records);
		result.setCount(records.length);
		result.setPsrDef(psrDef);
		return result;
	}
	
	/**
	 * （通用）查询记录
	 * @param model 设备模型
	 * @param exp	查询条件
	 * @param returnField 查询字段
	 * @return 查询结果
	 * @throws Exception 
	 */
	protected Iterator<QueryRecord> queryRecord(DeviceModel model, Expression exp, String[] returnField) 
		throws Exception {
		
		boolean hasGeometry = false;
		for (String fieldName : returnField) {
			if ("shape".equalsIgnoreCase(fieldName)) {
				hasGeometry = true;
			}
		}
		
		ResultSet resultSet = model.search(returnField, exp, null);
		if (null == resultSet) {
			return null;
		}
		Iterator<Device> iter = resultSet.resultList();
		if (null == iter) {
			return null;
		}
		List<QueryRecord> records = new ArrayList<QueryRecord>();
		while (iter.hasNext()) {
			
			Device device = iter.next();
			QueryRecord record = new QueryRecord();
			//record.
			
			// 处理几何字段
			if (hasGeometry) {
				record = setGeometry(device, record);
			}
			
			// 处理属性字段
			record = setAttribute(model, device, returnField, record);
			
			// 处理符号信息
			record = setSymbol(model, device, record);
			
			records.add(record);
		}
		
		return records.iterator();
	}

	/**
	 * （通用）设置几何字段
	 * @param device
	 * @param record
	 * @return
	 * @throws Exception
	 */
	protected QueryRecord setGeometry(Device device, QueryRecord record) throws Exception {
		
		SpatialDevice spatial = device.asSpatialDevice();
		GeometryDef geometryDef = spatial.getGeometry();
		if (GeometryDef.NONE == geometryDef) {
			// 无几何
			record.setGeom(null);
			return record;
		}
		
		Geometry geometry = geometryDef.getGeometry();
		GeometryType geoType = geometry.getGeometryType();
		
		String geoJsonType = "";
		GeometryPair geom = new GeometryPair();
		
		switch (geoType) {
		case POINT:
			geoJsonType = "POINT";
			break;
		case POLYLINE:
		{
			// 折线或者多折线
			Polyline polyline = (Polyline)geometry;
			int lineNum = polyline.getNumLineString();
			if (lineNum <= 0) {
				// 无几何
				record.setGeom(null);
				return record;
			} else if (lineNum == 1) {
				geoJsonType = "LINESTRING";
			} else {
				geoJsonType = "MULTILINESTRING";
			}
			
			int[] startDouble = new int[lineNum];
 			for (int i = 0; i < lineNum - 1; i++) {
				int doubleNum = (polyline.getSegment(i).getCoordinates().length) * 2;
				startDouble[i + 1] = startDouble[i] + doubleNum;
			}
 			geom.setOther(lineNum);
 			geom.setStartDouble(startDouble);
		}
			break;
		case POLYGON:
			break;
		case MULTIPOINT:
			break;
		case MULTIPOLYLINE:
		{
			geoJsonType = "MULTILINESTRING";
			GeometryCollection multipolyLine = (GeometryCollection)geometry;
			int lineNum = multipolyLine.getNumGeometry();
			int[] startDouble = new int[lineNum];
			startDouble[0] = 1;
			for (int i = 0; i < lineNum - 1; i++) {
				Geometry polyline = multipolyLine.getGeometry(i);
				startDouble[i + 1] = (polyline.getCoordinates().length) * 2 + startDouble[i];
			}
			geom.setOther(lineNum);
			geom.setStartDouble(startDouble);
 		}
			break;
		case MULTIPOLYGON:
			break;
		case COLLECTION:
			break;
		default:
			break;
		}
		
		Coordinate[] coordinates = geometry.getCoordinates();
		double[] coords = new double[coordinates.length * 2];
		for (int i = 0; i < coordinates.length; i++) {
			coords[2 * i] = coordinates[i].getX();
			coords[2 * i + 1] = coordinates[i].getY();
		}
		geom.setGeometryType(geoJsonType);
		geom.setCoords(coords);
		record.setGeom(geom);
		
		//String geoJson = "\"geometry\"{\"type\":" + geoJsonType + ",\"coordinates\":\"" + coordStr + "\"}";
		//record.setGeoJson(geoJson);
		
		return record;
	}

	/**
	 * （通用）设置属性字段
	 * @param device
	 * @param record
	 * @return
	 * @throws Exception
	 */
	protected QueryRecord setAttribute(DeviceModel model, Device device, 
			String[] returnField, QueryRecord record) throws Exception {
		
		List<QueryField> fieldList = new ArrayList<QueryField>(returnField.length);
		for (String fieldName : returnField) {
			
			if ("shape".equalsIgnoreCase(fieldName)) {
				continue;
			} else if ("connection".equalsIgnoreCase(fieldName)) {
				continue;
			}
			
			QueryField field = new QueryField();
			field.setFieldName(fieldName);
			
			Object fieldValue = device.getValue(fieldName);
			if (null != fieldValue) {
				String fieldValueStr = fieldValue.toString();
				fieldValueStr = repairSpacialChar(fieldValueStr);
				field.setFieldValue(fieldValueStr);
			}
			field.setFieldAlias(model.getFieldDef().find(fieldName).getFieldAlias());
			fieldList.add(field);
			
			// 对电压等级转义
			if ("DYDJ".equalsIgnoreCase(fieldName)) {
				if (null == fieldValue) {
					continue;
				}
				// TODO，改成从码值模块获取
				String dydj = field.getFieldValue();
				String dydjCode = "";
				if ("0" == dydj) {
					dydjCode = "0";
				} else {
					String sql = "select codename from "
							+ TableName.CONF_CODEDEFINITION
							+ " where codeid = 10401 and codedefid = "
							+ dydj;
					Map<String, Object> codeFieldMap = dbAdaptor.findMap(sql);
					if (null == codeFieldMap) {
						dydjCode = "0";
					} else {
						Object codeValue = codeFieldMap.get("codename");
						if (null == codeValue) {
							dydjCode = "0";
						} else {
							dydjCode = codeValue.toString();
						}
					}
				}
				
				QueryField fieldDydjCode = new QueryField();
				fieldDydjCode.setFieldName("DYZ");
				fieldDydjCode.setFieldValue(dydjCode);
				fieldDydjCode.setFieldAlias("电压值");
				fieldList.add(fieldDydjCode);
			}
		}
		
		QueryField[] fieldArr = fieldList.toArray(new QueryField[fieldList.size()]);
		record.setFields(fieldArr);
		return record;
	}
	
	/**
	 * （通用）设置符号信息
	 * @param device
	 * @param record
	 * @return
	 * @throws Exception
	 */
	protected QueryRecord setSymbol(DeviceModel model, Device device, QueryRecord record) throws Exception  {
		
		SymbolDef symDef = symbolAdaptor.search(device);
		if (symDef != null) {
			SymbolPair symbol = new SymbolPair();
			symbol.setModelId(symDef.getModelId());
			symbol.setSymbolValue(symDef.getSymbolValue());
			symbol.setSymbolId(symDef.getSymbolId());
			record.setSymbol(symbol);
		}
		return record;
		
	}
	
	/**
	 * 修复值中的特殊字符
	 * @param value
	 * @return
	 */
	private String repairSpacialChar(String value) {
		
		if (null == value) {
			return value;
		}
		
		if (value.contains(":") || value.contains("") || value.contains("'")
				|| value.contains("{") || value.contains("}")
				|| value.contains("\"") || value.contains(",")
				|| value.contains("[") || value.contains("]")
				|| value.contains("\t") || value.contains("\r")
				|| value.contains("\f") || value.contains("\b")
				|| value.contains("\n") || value.contains("\"")
				|| value.contains(" ") || value.contains("～")) {

			value = value.replaceAll(":", "；");
			value = value.replaceAll("\\\\", "");
			value = value.replaceAll("'", "");
			value = value.replaceAll("\"", "");
			value = value.replaceAll("\t", "");
			value = value.replaceAll("\r", "");
			value = value.replaceAll("\f", "");
			value = value.replaceAll("\n", "");
			value = value.replaceAll("～", "_");
			
		}
		return value;
	}
}
