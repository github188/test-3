package nari.QueryService.handler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import nari.Dao.interfaces.DbAdaptor;
import nari.Geometry.Coordinate;
import nari.Geometry.Geometry;
import nari.Geometry.GeometryCollection;
import nari.Geometry.GeometryType;
import nari.Geometry.Polyline;
import nari.QueryService.QueryServiceActivator;
import nari.QueryService.bean.QueryContainsRequest;
import nari.QueryService.bean.QueryContainsResponse;
import nari.QueryService.bean.QueryParentsRequest;
import nari.QueryService.bean.QueryParentsResponse;
import nari.QueryService.bean.QueryRelationsRequest;
import nari.QueryService.bean.QueryRelationsResponse;
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
import nari.model.relation.Relation;
import nari.model.relation.RelationDef;
import nari.model.symbol.SymbolAdapter;
import nari.parameter.bean.GeometryPair;
import nari.parameter.bean.QueryField;
import nari.parameter.bean.QueryRecord;
import nari.parameter.bean.QueryResult;
import nari.parameter.bean.SymbolPair;
import nari.parameter.code.PsrTypeSystem;
import nari.parameter.code.ReturnCode;

public class QueryRelationsHandler {

	private ModelService ms = QueryServiceActivator.modelService;
	private DbAdaptor db = QueryServiceActivator.dbAdaptor;
	private SymbolAdapter sym = QueryServiceActivator.symboladapter;

	static class RelationDeviceInfo {
		
		public static final RelationDeviceInfo NONE = new RelationDeviceInfo();
		public String modelId;
		public String oid;
		
		public RelationDeviceInfo() {
			this.modelId = null;
			this.oid = null;
		}
		
		public RelationDeviceInfo(String modelId, String oid) {
			this.modelId = modelId;
			this.oid = oid;
		}
	}
	
	/**
	 * 通用关系查询模块
	 * @param req
	 * @return
	 */
	public QueryRelationsResponse queryRelations(QueryRelationsRequest req) {
		
		QueryRelationsResponse resp = new QueryRelationsResponse();
		RelationDeviceInfo relationDeviceInfo = getDeviceInfo(req.getPsrType(), 
				req.getPsrId(), req.getPsrTypeSys());
		if (RelationDeviceInfo.NONE == relationDeviceInfo) {
			resp.setCode(ReturnCode.NODATA);
			return resp;
		}
		
		Iterator<Relation> relations = getRelations(relationDeviceInfo.modelId, 
				req.getRelationTypes(), req.getRelationFields());
		
		boolean isFilter = false;
		Set<Integer> displayModelIds = 
				getDisplayModelIds(req.getDisplayPsrTypes(), req.getPsrTypeSys());
		if (displayModelIds != null) {
			isFilter = true;
		}
		
		List<QueryResult> queryResults = new ArrayList<QueryResult>();
		while (relations.hasNext()) {
			Relation relation = relations.next();
			
			if (isFilter && !displayModelIds.contains(relation.getRModelId())) {
				continue;
			}
			
			QueryResult queryResult = null;
			try {
				queryResult = getQueryResult(relation, relationDeviceInfo.oid);
			} catch (Exception e) {
				System.out.println("设备（modelId：" 
						+ relationDeviceInfo.modelId 
						+ "，oid：" 
						+ relationDeviceInfo.oid +"）针对关系（"
						+ relation + "）查询出错。");
				e.printStackTrace();
				continue;
			}
			if (null == queryResult) {
				continue;
			}
			queryResults.add(queryResult);
		}
		if (queryResults.isEmpty()) {
			resp.setCode(ReturnCode.NODATA);
			return resp;
		}
		
		resp.setResult(queryResults.toArray(new QueryResult[queryResults.size()]));
		resp.setCode(ReturnCode.SUCCESS);
		return resp;
	}

	/**
	 * 查询子设备
	 */
	public QueryContainsResponse queryContains(QueryContainsRequest req) {
		
		QueryContainsResponse resp = new QueryContainsResponse();
		RelationDeviceInfo relationDeviceInfo = getDeviceInfo(req.getPsrType(), 
				req.getPsrId(), req.getPsrTypeSys());
		if (RelationDeviceInfo.NONE == relationDeviceInfo) {
			resp.setCode(ReturnCode.NODATA);
			return resp;
		}
		
		Iterator<Relation> relations = getRelations(relationDeviceInfo.modelId, 
				new String[]{ "CONTAIN" }, req.getRelationFields());
		
		boolean isFilter = false;
		Set<Integer> displayModelIds = 
				getDisplayModelIds(req.getDisplayPsrTypes(), req.getPsrTypeSys());
		if (displayModelIds != null) {
			isFilter = true;
		}
		
		List<QueryResult> queryResults = new ArrayList<QueryResult>();
		while (relations.hasNext()) {
			Relation relation = relations.next();
			
			if (isFilter && !displayModelIds.contains(relation.getRModelId())) {
				continue;
			}
			
			QueryResult queryResult = null;
			try {
				queryResult = getQueryResult(relation, relationDeviceInfo.oid);
			} catch (Exception e) {
				System.out.println("设备（modelId：" 
						+ relationDeviceInfo.modelId 
						+ "，oid：" 
						+ relationDeviceInfo.oid +"）针对关系（"
						+ relation + "）查询出错。");
				e.printStackTrace();
				continue;
			}
			queryResults.add(queryResult);
		}
		if (queryResults.isEmpty()) {
			resp.setCode(ReturnCode.NODATA);
			return resp;
		}
		
		resp.setResult(queryResults.toArray(new QueryResult[queryResults.size()]));
		resp.setCode(ReturnCode.SUCCESS);
		return resp;
	}
	
	/**
	 * 查询父设备
	 */
	public QueryParentsResponse queryParents(QueryParentsRequest req) {
		
		QueryParentsResponse resp = new QueryParentsResponse();
		RelationDeviceInfo relationDeviceInfo = getDeviceInfo(req.getPsrType(), 
				req.getPsrId(), req.getPsrTypeSys());
		if (RelationDeviceInfo.NONE == relationDeviceInfo) {
			resp.setCode(ReturnCode.NODATA);
			return resp;
		}
		
		Iterator<Relation> relations = getRelations(relationDeviceInfo.modelId, 
				new String[]{ "PARENT" }, req.getRelationFields());
		
		boolean isFilter = false;
		Set<Integer> displayModelIds = 
				getDisplayModelIds(req.getDisplayPsrTypes(), req.getPsrTypeSys());
		if (displayModelIds != null) {
			isFilter = true;
		}
		
		List<QueryResult> queryResults = new ArrayList<QueryResult>();
		while (relations.hasNext()) {
			Relation relation = relations.next();
			
			if (isFilter && !displayModelIds.contains(relation.getRModelId())) {
				continue;
			}
			
			QueryResult queryResult = null;
			try {
				queryResult = getQueryResult(relation, relationDeviceInfo.oid);
			} catch (Exception e) {
				System.out.println("设备（modelId：" 
						+ relationDeviceInfo.modelId 
						+ "，oid：" 
						+ relationDeviceInfo.oid +"）针对关系（"
						+ relation + "）查询出错。");
				e.printStackTrace();
				continue;
			}
			queryResults.add(queryResult);
		}
		if (queryResults.isEmpty()) {
			resp.setCode(ReturnCode.NODATA);
			return resp;
		}
		
		resp.setResult(queryResults.toArray(new QueryResult[queryResults.size()]));
		resp.setCode(ReturnCode.SUCCESS);
		return resp;
	}
		
	private Set<Integer> getDisplayModelIds(String[] displayPsrTypes, String psrTypeSys) {
		
		if (displayPsrTypes == null || displayPsrTypes.length <= 0) {
			return null;
		}
		
		Set<Integer> displayModelIds = new HashSet<Integer>();
		if (PsrTypeSystem.EQUIPMENT_ID.equalsIgnoreCase(psrTypeSys)) {
			
			for (int i = 0; i < displayPsrTypes.length; i++) {
				List<Integer> classIds = ModelActivator.getClassIdByEquId(displayPsrTypes[i]);
				for (Integer classId : classIds) {
					List<String> modelIds = getModelIdsByClassId(classId.toString());
					for (String modelId : modelIds) {
						displayModelIds.add(Integer.valueOf(modelId));
					}
				}
			}
			
			
		} else if (PsrTypeSystem.CLASS_ID.equalsIgnoreCase(psrTypeSys)) {
			
			for (int i = 0; i < displayPsrTypes.length; i++) {
				List<String> modelIds = getModelIdsByClassId(displayPsrTypes[i]);
				for (String modelId : modelIds) {
					displayModelIds.add(Integer.valueOf(modelId));
				}
			}
			
		} else {
			
			for (int i = 0; i < displayPsrTypes.length; i++) {
				displayModelIds.add(Integer.valueOf(displayPsrTypes[i]));
			}
			
		}
		
		return displayModelIds;
		
	}
	
	private List<String> getModelIdsByClassId(String classId) {
			
			DeviceModel model = null;
			try {
				model = ms.fromClass(classId, false);
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
	
	private RelationDeviceInfo getDeviceInfo(String psrType, String psrId, String psrTypeSys) {
		
		RelationDeviceInfo relationDeviceInfo = RelationDeviceInfo.NONE;
		if (PsrTypeSystem.EQUIPMENT_ID.equalsIgnoreCase(psrTypeSys)) {
			// equipmentId
			List<Integer> classIds = ModelActivator.getClassIdByEquId(psrType);
			boolean hasModelIdAndOid = false;
			for (Integer classId : classIds) {
				relationDeviceInfo = getDeviceInfoByClass(classId.toString(), psrId);
				if (RelationDeviceInfo.NONE != relationDeviceInfo) {
					hasModelIdAndOid = true;
					break;
				}
			}
			if (!hasModelIdAndOid) {
				System.out.println("未找到的设备类型：(equipmentId)" 
						+ psrType 
						+ ", 设备ID：" 
						+ psrId);
			}
		} else if (PsrTypeSystem.CLASS_ID.equalsIgnoreCase(psrTypeSys)) {
			// classId
			relationDeviceInfo = getDeviceInfoByClass(psrType, psrId);
			if (RelationDeviceInfo.NONE == relationDeviceInfo) {
				System.out.println("未找到的设备类型：(classId)" 
						+ psrType
						+ ", 设备ID：" 
						+ psrId);
			}
		} else {
			// modelId
			relationDeviceInfo = getDeviceInfoBySubClass(psrType, psrId);
			if (RelationDeviceInfo.NONE == relationDeviceInfo) {
				System.out.println("未找到的设备子类型：(modelId)" 
						+ psrType 
						+ ", 设备ID：" 
						+ psrId);
			}
		}
		
		return relationDeviceInfo;
	}
		
	/**
	 * 根据ClassID和SBID获取ModelID和OID
	 * @param [in] classId
	 * @param [in] sbId
	 * @return 
	 */
	private RelationDeviceInfo getDeviceInfoByClass(String classId, String sbId) {
		
		DeviceModel model = null;
		try {
			model = ms.fromClass(classId, false);
		} catch (Exception e) {
			e.printStackTrace();
			return RelationDeviceInfo.NONE;
		}
		
		CriteriaBuilder builder = model.getEntryManager().getCriteriaBuilder();
		Expression exp = builder.equal(builder.getRoot().get("SBID", String.class), sbId);
		ResultSet resultSet = null;
		try {
			resultSet = model.search(new String[] {"OID", "SBZLX"}, exp, null);		
		} catch (Exception e) {
			e.printStackTrace();
			return RelationDeviceInfo.NONE;
		}
		
		if (null == resultSet || null == resultSet.resultList()) {
			return RelationDeviceInfo.NONE;
		}
		
		Iterator<Device> deviceIter = resultSet.resultList();
		while (deviceIter.hasNext()) {
			Device device = deviceIter.next();
			if (null == device) {
				continue;
			}
			Object oidObj = device.getValue("oid");
			Object modelIdObj = device.getValue("sbzlx");
			if (null == oidObj || null == modelIdObj) {
				continue;
			}
			return new RelationDeviceInfo(modelIdObj.toString(), oidObj.toString());
		}
		
		return RelationDeviceInfo.NONE;
	}
	
	/**
	 * 根据ClassID和SBID获取ModelID和OID
	 * @param [in] modelId
	 * @param [in] sbId
	 * @return
	 */
	private RelationDeviceInfo getDeviceInfoBySubClass(String modelId, String sbId) {
		
		DeviceModel model = null;
		try {
			model = ms.fromSubClass(modelId, false);
		} catch (Exception e) {
			e.printStackTrace();
			return RelationDeviceInfo.NONE;
		}
		
		CriteriaBuilder builder = model.getEntryManager().getCriteriaBuilder();
		Expression exp = builder.equal(builder.getRoot().get("SBID", String.class), sbId);
		ResultSet resultSet = null;
		try {
			resultSet = model.search(new String[] {"OID"}, exp, null);		
		} catch (Exception e) {
			e.printStackTrace();
			return RelationDeviceInfo.NONE;
		}
		
		if (null == resultSet || null != resultSet.resultList()) {
			return RelationDeviceInfo.NONE;
		}
		
		while (resultSet.resultList().hasNext()) {
			Device device = resultSet.resultList().next();
			if (null == device) {
				continue;
			}
			Object oidObj = device.getValue("oid");
			if (null == oidObj) {
				continue;
			}
			return new RelationDeviceInfo(modelId, oidObj.toString());
		}
		
		return RelationDeviceInfo.NONE;
	}
		
	/**
	 * 获取所有的关系
	 * @param modelId
	 * @param relationTypes
	 * @param relationFields
	 * @return
	 */
	private Iterator<Relation> getRelations(String modelId, String[] relationTypes, String[] relationFields) {
		
		List<RelationDef> relationDefs = new ArrayList<RelationDef>();
		if (null != relationTypes && relationTypes.length > 0) {
			for (String relationType : relationTypes) {
				int relationId = getRelationId(relationType);
				RelationDef relationDef = null;
				try {
					relationDef = ms.getRelationDef(relationId);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (null == relationDef) {
					continue;
				}
				relationDefs.add(relationDef);
			}
		}
		RelationDef[] relationDefArr = null;
		if (!relationDefs.isEmpty()) {
			relationDefArr = relationDefs.toArray(new RelationDef[relationDefs.size()]);
		}
		
		Iterator<Relation> relations = null;
		try {
			relations = ms.relateToSubClass(modelId, relationDefArr, relationFields);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return relations;
	}

	/**
	 * 根据一个关系和OID获取查询结果
	 * @param relation
	 * @param oid
	 * @return
	 */
	private QueryResult getQueryResult(Relation relation, String oid) throws Exception {
		
		DeviceModel model = ms.fromSubClass(String.valueOf(relation.getRModelId()), false);
		
		Iterator<FieldDetail> fieldIter = model.getFieldDef().details();
		List<String> returnFieldList = new ArrayList<String>();
		while (fieldIter.hasNext()) {
			String fieldName = fieldIter.next().getFieldName();
			returnFieldList.add(fieldName);
		}
		String[] returnField = returnFieldList.toArray(new String[returnFieldList.size()]);
		
		CriteriaBuilder builder = model.getEntryManager().getCriteriaBuilder();
		Expression exp = builder.equal(builder.getRoot().get(relation.getRelationField(), String.class)
				, oid);
		
		Iterator<QueryRecord> recordIter = queryRecord(model, exp, returnField);
		if (null == recordIter) {
			return null;
		}
		
		List<QueryRecord> records = new ArrayList<QueryRecord>();
		if (null != recordIter) {
			while (recordIter.hasNext()) {
				records.add(recordIter.next());
			}
		}
		
		QueryRecord[] recordArr = records.toArray(new QueryRecord[records.size()]);
		QueryResult result = new QueryResult();
		result.setRecords(recordArr);
		result.setCount(recordArr.length);

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
	private Iterator<QueryRecord> queryRecord(DeviceModel model, Expression exp, String[] returnField) 
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
	private QueryRecord setGeometry(Device device, QueryRecord record) throws Exception {
		
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
		StringBuilder coordStr = new StringBuilder("");
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
			geoJsonType = "POLYGON";
			break;
		case MULTIPOINT:
			geoJsonType = "MULTIPOINT";
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
			geoJsonType = "MULTIPOLYGON";
			break;
		case COLLECTION:
			geoJsonType = "GEOMETRYCOLLECTION";
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
	private QueryRecord setAttribute(DeviceModel model, Device device, 
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
					Map<String, Object> codeFieldMap = db.findMap(sql);
					Object codeValue = codeFieldMap.get("codename");
					if (null == codeValue) {
						dydjCode = "0";
					} else {
						dydjCode = codeValue.toString();
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
	private QueryRecord setSymbol(DeviceModel model, Device device, QueryRecord record) throws Exception  {
		
		SymbolDef symDef = sym.search(device);
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

	private int getRelationId(String relationType) {
		if (relationType.equalsIgnoreCase("CONTAIN")) {
			return 3002;
		} else if (relationType.equalsIgnoreCase("PARENT")) {
			return 3001;
		}
		return 0;
	}
}
