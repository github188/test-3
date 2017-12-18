package nari.ThematicMapService.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nari.Dao.interfaces.DbAdaptor;
import nari.Geometry.Coordinate;
import nari.Geometry.Geometry;
import nari.Geometry.GeometryCollection;
import nari.Geometry.GeometryType;
import nari.Geometry.Polyline;
import nari.Logger.Logger;
import nari.Logger.LoggerManager;
import nari.ThematicMapService.ThematicMapServiceActivator;
import nari.ThematicMapService.bean.ThematicDevRequest;
import nari.ThematicMapService.bean.ThematicDevResponse;
import nari.model.TableName;
import nari.model.bean.DefaultGeometryDef;
import nari.model.bean.FieldDetail;
import nari.model.bean.GeometryDef;
import nari.model.bean.SymbolDef;
import nari.model.device.Device;
import nari.model.device.DeviceModel;
import nari.model.device.ModelService;
import nari.model.device.ResultSet;
import nari.model.device.filter.CriteriaBuilder;
import nari.model.device.filter.Expression;
import nari.model.symbol.SymbolAdapter;
import nari.parameter.bean.GeometryPair;
import nari.parameter.bean.QueryField;
import nari.parameter.bean.QueryRecord;
import nari.parameter.bean.QueryResult;
import nari.parameter.bean.SymbolPair;
import nari.parameter.code.ReturnCode;
import oracle.spatial.geometry.JGeometry;
import oracle.sql.STRUCT;

public class ThematicDevHandler {

	private Logger logger = LoggerManager.getLogger(this.getClass());
	private DbAdaptor db = ThematicMapServiceActivator.dbAdaptor;
	private ModelService ms = ThematicMapServiceActivator.modelService;
	private SymbolAdapter symAdp = ThematicMapServiceActivator.symboladapter;

	public ThematicDevResponse getThematicDev(ThematicDevRequest req) {
		ThematicDevResponse resp = new ThematicDevResponse();

		String mapId = req.getMapId(); // 图类型
		String documentId = req.getDocumentId(); // 图实例
		if ("".equalsIgnoreCase(mapId) || "".equalsIgnoreCase(documentId)) {
			logger.error("传入参数缺少必须值");
			resp.setCode(ReturnCode.NULL);
			return resp;
		}

		String thematicSql = "select * from " + TableName.CONF_DOCUMENTMODEL + " where mapId = "
				+ mapId;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = db.findAllMap(thematicSql);
		} catch (SQLException e) {
			logger.error("数据库查询出错");
			resp.setCode(ReturnCode.SQLERROR);
			return resp;
		}
		if(list == null || list.size() == 0){
			logger.error("无数据,传入值可能有误");
			resp.setCode(ReturnCode.NODATA);
			return resp;
		}

		List<QueryResult> QueryResultList = new ArrayList<QueryResult>();
		for (int i = 0; i < list.size(); i++) {		//mapid对应的每种设备类型
			
			QueryResult result = new QueryResult();
			Map<String, Object> recordMap = list.get(i);
			
			String tableName = String.valueOf(recordMap.get("geotablename"));
			if(tableName.startsWith("T_TX")){
				continue;
			}
			String modelSql = "select * from "+ tableName +" where sstsl = "+documentId;
			//根据他找到对应设备(得到sstsl)
			List<Map<String, Object>> deviceList = new ArrayList<Map<String, Object>>();
			try {
				deviceList = db.findAllMap(modelSql);
			} catch (SQLException e) {
				logger.error("数据库查询出错");
				resp.setCode(ReturnCode.SQLERROR);
				return resp;
			}

			if(deviceList == null || deviceList.size() == 0){
				continue;
			}
			
			List<QueryRecord> queryRecordList = new ArrayList<QueryRecord>();
			for(int j=0;j<deviceList.size();j++){	//每种专题图设备每一条记录
				if(i==7 && j==7){
					System.out.println("sa");
				}
				
				QueryRecord queryRecord = new QueryRecord();
				Map<String, Object> deviceMap = deviceList.get(j);	//每种专题图设备每一条记录
				String txid = String.valueOf(deviceMap.get("txid"));
				if("null".equalsIgnoreCase(txid)){	//若无txId则去掉该设备
					continue;
				}
				
				
				String classId = String.valueOf(recordMap.get("classid"));
				DeviceModel model = null;
				try {
					model = ms.fromClass(classId, false);	//每种专题图设备对应每一种地理图
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("模型创建时出错");
					resp.setCode(ReturnCode.BUILDMODEL);
					return resp;
				}
				
				//图形查询条件
				CriteriaBuilder builder = model.getEntryManager()
						.getCriteriaBuilder();
				Expression exp = builder.equal(
						builder.getRoot().get("OID", String.class), txid);
				
				//读取专题图里的shape
				Object shapeObject = deviceMap.get("shape");
				if(shapeObject == null){
					continue;
				}
				GeometryDef def = shapeOracle2Frame(shapeObject);
				// 返回空间对象信息
				GeometryPair geom = new GeometryPair();
				GeometryType geomType = def.getGeometry()
						.getGeometryType();
				String geometryType = geomType.toString();

				// 对线的处理
				if (("POLYLINE").equalsIgnoreCase(geometryType)) {
					Polyline polyline = (Polyline) def.getGeometry();
					int lineNum = polyline.getNumLineString();
					if (lineNum > 1) {
						geometryType = "MULTILINESTRING";
					} else {
						geometryType = "LINESTRING";
					}
					int[] startDouble = new int[lineNum];
					startDouble[0] = 1;
					for (int k = 0; k < lineNum - 1; k++) {
						int doubleNum = (polyline.getSegment(k)
								.getCoordinates().length) * 2;
						startDouble[k + 1] = startDouble[k] + doubleNum;
					}
					geom.setOther(lineNum);
					geom.setStartDouble(startDouble);
				}
				// 对多线特殊处理
				if (("MULTIPOLYLINE").equalsIgnoreCase(geometryType)) {
					geometryType = "MULTILINESTRING";
					GeometryCollection multipolyLine = (GeometryCollection) def.getGeometry();
					int lineNum = multipolyLine.getNumGeometry();
					int[] startDouble = new int[lineNum];
					startDouble[0] = 1;
					for (int k = 0; k < lineNum - 1; k++) {
						Geometry polyLine = multipolyLine.getGeometry(k); // 直线型
						startDouble[k + 1] = (polyLine.getCoordinates().length)
								* 2 + startDouble[k];
					}
					geom.setOther(lineNum);
					geom.setStartDouble(startDouble);
				}

				Coordinate[] coordnates = def.getGeometry().getCoordinates();
				int natesnum = coordnates.length;
				// 坐标数组 [x1,y1,x2,y2,x3,y3]
				double[] coords = new double[2 * natesnum];
				for (int k = 0; k < natesnum; k++) {
					coords[2 * k] = coordnates[k].getX();
					coords[2 * k + 1] = coordnates[k].getY();
				}
				geom.setGeometryType(geometryType);
				geom.setCoords(coords);
				queryRecord.setGeom(geom);		//地理数据返回
				
				
				//返回字段
				Iterator<FieldDetail> fieldIt = model.getFieldDef().details();
				List<String> textureFeildList = new ArrayList<String>();
				List<String> thematicProList = new ArrayList<String>();
				//专题图属性名称
				String[] thematicPros = new String[]{"FHDX","FHJD","SFBZ","BZDX","BZYS", "BZFW", "PLFS","DHZS","BZXSZD","BZNR","X","Y"};
				for(String thematicPro : thematicPros){
					thematicProList.add(thematicPro);
				}
				
				while (fieldIt.hasNext()) {
					FieldDetail field= fieldIt.next();
					String fieldName = field.getFieldName();
					if("shape".equalsIgnoreCase(fieldName)){	//去掉shape字段
						continue;
					}
					textureFeildList.add(fieldName);
				}
				
				textureFeildList.removeAll(thematicProList);		//去掉地理图中与专题图重复的属性
				String[] textureFields = new String[textureFeildList.size()];
				textureFields = textureFeildList.toArray(textureFields);
				
				ResultSet set = null;
				try {
					// 为提高效率，可以查找字段精简为(OID,SBMC,SHAPE,symbol(SBZLX))
					// queryReturnField = new String[]{"OID","SBMC","SHAPE","SBZLX"}
					set = model.search(textureFields, exp, null);
				} catch (Exception e) {
					System.out.println("数据库查询出错");
					resp.setCode(ReturnCode.SQLERROR);
					return resp;
				}
				if(set == null || set == ResultSet.NONE){
					continue;
				}
				// 得到每种该站子类型设备类型对应结果
				Device device= set.resultList().next();
				// 返回字段信息集合
				int returnFieldlength = textureFields.length;
				List<QueryField> fieldsList = new ArrayList<QueryField>();
				
				
				for(int k=0;k<thematicPros.length;k++){		//在最后的结果中添加专题图属性
					String thematicProName = thematicPros[k];
					String value = String.valueOf(deviceMap.get(thematicProName.toLowerCase()));
					value = modifySBMC(value);
					String alias = "专题图符号属性";
					QueryField thematicfields = new QueryField();
					thematicfields.setFieldName(thematicProName);
					thematicfields.setFieldValue(value);
					thematicfields.setFieldAlias(alias);
					fieldsList.add(thematicfields);
				}
				
				for (int k = 0; k < returnFieldlength; k++) {
					
					QueryField fields = new QueryField();
					fields.setFieldName(textureFields[k]);

					if (device.getValue(textureFields[k]) != null) {
						String fieldValue = String.valueOf(device
								.getValue(textureFields[k]));
						// 设备名称(SBMC)优化(去掉一些特殊符号)
						if (textureFields[k].equals("SBMC")) {
							fieldValue = modifySBMC(fieldValue);
						}

						fields.setFieldValue(fieldValue);
					}
					fields.setFieldAlias(model.getFieldDef()
							.find(textureFields[k]).getFieldAlias());
					fieldsList.add(fields);

					// 对电压等级转义(电压等级变成电压值)(多加一个返回字段)
					if (textureFields[k].equals("DYDJ")) {
						// 根据电压等级查询表conf_codedefinition得到相应实际值
						if (device.getValue(textureFields[k]) == null) {
//							System.out.println(k);
							continue;
						}
						String dydj = String.valueOf((device
								.getValue(textureFields[k])));
						String dyz = "";

						if (dydj.equalsIgnoreCase("0")) {
							dyz = "0";
						} else {

							String sql0 = "select * from " + TableName.CONF_CODEDEFINITION + " where codeid = 10401 and codedefid = "
									+ dydj;
							Map<String, Object> codeFieldMap = null;
							try {
								codeFieldMap = db.findMap(sql0);
							} catch (SQLException e1) {
								System.out.println("数据库查询出错");
								resp.setCode(ReturnCode.SQLERROR);
								return resp;
							}

							if (codeFieldMap.get("codename") == null) {
								dyz = "0";
							} else {
								dyz = String.valueOf(codeFieldMap
										.get("codename"));
							}
							QueryField fields1 = new QueryField();
							fields1.setFieldName("DYZ");
							fields1.setFieldValue(dyz);
							fields1.setFieldAlias("电压值");
							fieldsList.add(fields1);
						}
					}
				}
				QueryField[] fields = new QueryField[fieldsList.size()];
				fields = fieldsList.toArray(fields);
				queryRecord.setFields(fields);		//返回属性字段信息

				// 返回符号信息
				SymbolDef symDef = null;
				try {
					symDef = symAdp.search(device);
				} catch (Exception e1) {
					System.out.println("模型创建时出错");
					resp.setCode(ReturnCode.BUILDMODEL);
					return resp;
				}
				if (symDef != null) {
					SymbolPair symbol = new SymbolPair();
					symbol.setModelId(symDef.getModelId());
					symbol.setSymbolValue(symDef.getSymbolValue());
					symbol.setSymbolId(symDef.getSymbolId());
					// symbol.setDevtypeId(symDef.getDevTypeId());
					queryRecord.setSymbol(symbol);
				}
				
				queryRecordList.add(queryRecord);
			}	//(j层)所有queryRecord装载完成
			int count = queryRecordList.size();
			QueryRecord[] records = new QueryRecord[count];
			records = queryRecordList.toArray(records);
			result.setRecords(records);
			result.setCount(count);
			QueryResultList.add(result);
		}	//(i层)所有QueryResult装载完成
		QueryResult[] results = new QueryResult[QueryResultList.size()];
		results = QueryResultList.toArray(results);
		resp.setResult(results);
		resp.setCode(ReturnCode.SUCCESS);
		
//		/****************小工具*******************/
//		searchdocumentId();
//		/***************************************/
		return resp;
	}
	
	
	private GeometryDef shapeOracle2Frame(Object geoObject){
		STRUCT struct = null;
		Method method = null;
		JGeometry geom = null;
		if ("weblogic.jdbc.wrapper.Struct_oracle_sql_STRUCT".equals(geoObject.getClass().getName())){
			try {
				method = geoObject.getClass().getMethod("getVendorObj", new Class[0]);
				struct = (STRUCT)method.invoke(geoObject, new Object[0]);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		} else {
			struct = (STRUCT)geoObject;
		}
		try {
			geom = JGeometry.load(struct);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new DefaultGeometryDef(geom);
	}

	public String modifySBMC(String value) {
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
	
	
	
	private String searchdocumentId(){
		String documentID = "";
		String thematicSql = "select * from " + TableName.CONF_DOCUMENTMODEL + " where mapId = 3007";
		List<Map<String, Object>> thematicList = new ArrayList<Map<String, Object>>();
		try {
			thematicList = db.findAllMap(thematicSql);
		} catch (SQLException e) {
			logger.error("数据库查询出错");
		}
		if(thematicList == null || thematicList.size() == 0){
			logger.error("无数据,传入值可能有误");
		}
		for (int i = 1; i < thematicList.size(); i++) {		//每种设备类型层
		System.out.println(i);
		Map<String, Object> recordMap = thematicList.get(i);
		
		String thematictableName = String.valueOf(recordMap.get("geotablename"));
		if(thematictableName.startsWith("T_TX")){
			continue;
		}
		
		String classId = String.valueOf(recordMap.get("classid"));
		String modeltableSql = "select * from " + TableName.CONF_MODELMETA + " where classId = " + classId;
		List<Map<String, Object>> modelList = new ArrayList<Map<String, Object>>();
		try {
			modelList = db.findAllMap(modeltableSql);
		} catch (SQLException e) {
			logger.error("数据库查询出错");
		}
		if(modelList == null || modelList.size() == 0){
			logger.error("无数据,传入值可能有误");
		}
		
		String modelTableName = String.valueOf(modelList.get(0).get("modelname"));
		
		String Sql = "select a.* from "+ thematictableName +" a, " + modelTableName + " b where a.txid = b.oid and rownum<10";
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = db.findAllMap(Sql);
		} catch (SQLException e) {
			logger.error("数据库查询出错");
		}
		if(list == null || list.size() == 0){
			continue;
		}
		
		documentID = String.valueOf(list.get(0).get("sstsl"));
		System.out.println(documentID);
		return documentID;
	}
		System.out.println("查无数据");
		return "查无数据";
	}
}



