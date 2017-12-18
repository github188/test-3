package nari.QueryService.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nari.Dao.interfaces.DbAdaptor;
import nari.Geometry.Coordinate;
import nari.Geometry.Geometry;
import nari.Geometry.GeometryCollection;
import nari.Geometry.GeometryType;
import nari.Geometry.Point;
import nari.Geometry.Polygon;
import nari.Geometry.Polyline;
import nari.Logger.Logger;
import nari.Logger.LoggerManager;
import nari.QueryService.QueryServiceActivator;
import nari.model.ModelActivator;
import nari.model.bean.FieldDef;
import nari.model.bean.FieldDetail;
import nari.model.bean.SubClassDef;
import nari.model.bean.SymbolDef;
import nari.model.device.Device;
import nari.model.device.DeviceModel;
import nari.model.device.ModelService;
import nari.model.device.ResultSet;
import nari.model.device.SpatialDevice;
import nari.model.device.filter.CriteriaBuilder;
import nari.model.device.filter.Expression;
import nari.model.geometry.GeometryService;
import nari.model.symbol.SymbolAdapter;
import nari.parameter.QueryService.SpatialQuery.SpatialQueryRequest;
import nari.parameter.QueryService.SpatialQuery.SpatialQueryResponse;
import nari.parameter.bean.GeometryPair;
import nari.parameter.bean.Link;
import nari.parameter.bean.Operator;
import nari.parameter.bean.PSRDef;
import nari.parameter.bean.Pair;
import nari.parameter.bean.QueryField;
import nari.parameter.bean.QueryRecord;
import nari.parameter.bean.QueryResult;
import nari.parameter.bean.SubPSRDef;
import nari.parameter.bean.SymbolPair;
import nari.parameter.bean.TypeCondition;
import nari.parameter.code.ReturnCode;

public class SpatialQueryHandler {
	
	private Logger logger = LoggerManager.getLogger(this.getClass());
	
	GeometryService geoService = QueryServiceActivator.geoService;
	ModelService ms = QueryServiceActivator.modelService;
	DbAdaptor db = QueryServiceActivator.dbAdaptor;
	SymbolAdapter symAdp = QueryServiceActivator.symboladapter;
	
	public SpatialQueryResponse spatialQuery(SpatialQueryRequest request) {
		
		SpatialQueryResponse resp = new SpatialQueryResponse();
		GeometryPair geomPair = request.getGeom();
		String geometryType0 = geomPair.getGeometryType();
		double[] coords0 = geomPair.getCoords();
		
		// 获取查询的多边形
		Polygon poly = getPolygon(geometryType0, coords0, geomPair, resp);
		if (null == poly) {
			return resp;
		}
		
		// 查询条件
		TypeCondition[] cond = request.getConds();
		int condLength = cond.length;
		List<QueryResult> results = new ArrayList<QueryResult>(condLength);
		
		// 根据查询条件查询数据，并将结果保存在
		for (int i = 0; i < condLength; i++) {
			
			if(cond[i].getPsrTypeSys().equals("classId")) {
				// psrType的值为classId
				int classId = Integer.parseInt(cond[i].getPsrType());
				QueryResult result = getQureyResult(classId, cond[i], poly, resp);
				if (null == result) {
					return resp;
				}
				if (result.getCount() >= 0) {
					results.add(result);
				}
				
			} else {
				// psrType的值为equId，需要转换为classId
				String equId = cond[i].getPsrType();
				List<Integer> classIds = ModelActivator.getClassIdByEquId(equId);
				for (Integer classId : classIds) {
					// 遍历其中的每一个classId，分别查找
					QueryResult result = getQureyResult(classId, cond[i], poly, resp);
					if (null == result) {
						return resp;
					}
					if (result.getCount() >= 0) {
						results.add(result);
					}
				}
			}
		} // for end
		
		QueryResult[] resultArray = new QueryResult[results.size()];
		resp.setResult(results.toArray(resultArray));
		resp.setCode(ReturnCode.SUCCESS);
		return resp;
	}
	
	//将设备名称包含特殊符号的去掉
	public String modifySBMC(String value){
		if(value.contains(":") || 
				value.contains("") || 
				value.contains("'") || 
				value.contains("{") || 
				value.contains("}") || 
				value.contains("\"") || 
				value.contains(",") || 
				value.contains("[") || 
				value.contains("]") || 
				value.contains("\t") || 
				value.contains("\r") || 
				value.contains("\f") || 
				value.contains("\b") || 
				value.contains("\n") || 
				value.contains("\"") || 
				value.contains(" ")||
				value.contains("～")){
			
			value = value.replaceAll(":", "；");
			value = value.replaceAll("\\\\", "");
			value = value.replaceAll("'", "");
			value = value.replaceAll("\"", "");
			value = value.replaceAll("\t","");
			value = value.replaceAll("\r","");
			value = value.replaceAll("\f","");
			value = value.replaceAll("\n","");
			value = value.replaceAll("～", "_");
		}
		return value;
	}

	/**
	 * 根据一系列条件，返回一个查询结果
	 * @param classId
	 * @param cond
	 * @param poly
	 * @param resp
	 * @return 返回null表示查询出错，应该直接返回
	 */
	private QueryResult getQureyResult(int classId, TypeCondition cond, Polygon poly, SpatialQueryResponse resp) {
		
		DeviceModel model = null;
		try {
			model = ms.fromClass(String.valueOf(classId), false);
		} catch (Exception e1) {
			logger.info("模型创建时出错");
			resp.setCode(ReturnCode.BUILDMODEL);
			return null;
		}

		// 查询字段的设置
		String[] queryField = cond.getReturnField();
		if (queryField == null || queryField.length == 0) {
			ModelService ms = QueryServiceActivator.modelService;
			DeviceModel modelDev = DeviceModel.NONE;
			try {
				modelDev = ms.fromClass(String.valueOf(classId), false);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			FieldDef fieldDef = modelDev.getFieldDef();
			Iterator<FieldDetail> it = fieldDef.details();
			List<FieldDetail> fieldList = new ArrayList<FieldDetail>();
			while(it.hasNext()){
				FieldDetail fd = it.next();
				if( fd.getFieldName().equals("KZ") ||
				    fd.getFieldName().equals("RLBZ") ||
					fd.getFieldName().equals("SSKX") ){
					continue;
				}
				fieldList.add(fd);
			}
			
			int count = fieldList.size();
			queryField = new String[count];
			for(int j=0;j<count;j++){
				queryField[j] = fieldList.get(j).getFieldName();
			}
		}
		
		ResultSet set = null;

		CriteriaBuilder builder = model.getEntryManager().getCriteriaBuilder();
		// 条件组合
		Pair[] pairs = cond.getPairs();
		// 得到条件
		Expression e = Expression.NONE;
		// 若条件组合不为空
		if (pairs != null && pairs.length != 0 ){
			int pairsLength = pairs.length;
			Expression[] exp = new Expression[pairsLength];
			for (int j = 0; j < pairsLength; j++) {
				String key = pairs[j].getKey();
				String value = pairs[j].getValue();
				Operator op = pairs[j].getOp();
				// 接口初始化
				exp[j] = Expression.NONE;
				switch (op) {
				case EQ:
					exp[j] = builder.equal(
							builder.getRoot().get(key, String.class),
							value);
					break;
				case LT:
					exp[j] = builder.lessThan(
							builder.getRoot().get(key, String.class),
							value);
					break;
				case MT:
					exp[j] = builder.greaterThan(
							builder.getRoot().get(key, String.class),
							value);
					break;
				case LIKE:
					exp[j] = builder.like(
							builder.getRoot().get(key, String.class),
							value);
					break;
				}
			}
			Link link = cond.getLink();
			// 若组合条件不为空
			if (link != null) {
				switch (link) {
				case AND:
					e = builder.and(exp);
					break;
				case OR:
					e = builder.or(exp);
					break;
				}
			} else {
				e = exp[0];
			}
		}
		
		//为避免数据量太大，添加条件rownum<500;
		if(e != Expression.NONE){
			e = builder.and(
					e,
					builder.lessThan(
							builder.getRoot().get("rownum", String.class), 500));

		}else{
			e = builder.lessThan(
					builder.getRoot().get("rownum", String.class), 500);
		}

		try {
			// 添加查询时间
			long start = System.currentTimeMillis();
			set = model.spatialQuery(queryField, e,null, poly);
			long end = System.currentTimeMillis();
			logger.info("每次查询用时:" + (end - start) + "ms");
		} catch (Exception e1) {
			logger.info("数据库查询出错");
			resp.setCode(ReturnCode.SQLERROR);
			return null;
		}
		// 得到结果
		Iterator<Device> it = set.resultList();
		// 返回记录数量
		int count = 0;
		List<Device> devList = new ArrayList<Device>();
//		List<TopoDevice> topoDevList = new ArrayList<TopoDevice>();
		if (it != null) {
			while (it.hasNext()) {
				devList.add(count, it.next());
				if (devList.get(count) == null) {
					break;
				}
				count = count + 1;
			}
		}
		// 处理查询得到的数据
		QueryRecord[] records = new QueryRecord[count];
		for (int j = 0; j < count; j++) {
			records[j] = new QueryRecord();
			// 返回查询得到空间信息
			SpatialDevice spaDev = devList.get(j).asSpatialDevice();
			// 返回空间对象信息
			GeometryPair geom = new GeometryPair();
			// 空间对象类型 1 点 2 线 3 面
			String geometryType = "";
			GeometryType geomType = spaDev.getGeometry().getGeometry()
					.getGeometryType();
			geometryType = geomType.toString();
			//对线的处理
			if(("POLYLINE").equalsIgnoreCase(geometryType)){
				Polyline polyline = (Polyline)spaDev.getGeometry().getGeometry();
				int lineNum = polyline.getNumLineString();
				if(lineNum>1){
					geometryType = "MULTILINESTRING";
				}else{
					geometryType = "LINESTRING";
				}
				int[] startDouble = new int[lineNum];
				startDouble[0] = 1;
				for(int k=0;k<lineNum-1;k++){
					int doubleNum = (polyline.getSegment(k).getCoordinates().length)*2;
					startDouble[k+1] = startDouble[k]+doubleNum;
				}
				geom.setOther(lineNum);
				geom.setStartDouble(startDouble);
			}
			//对多线特殊处理
			if(("MULTIPOLYLINE").equalsIgnoreCase(geometryType)){
				geometryType = "MULTILINESTRING";
					GeometryCollection multipolyLine = (GeometryCollection)spaDev.getGeometry().getGeometry();
					int lineNum = multipolyLine.getNumGeometry();
					int[] startDouble = new int[lineNum];
					startDouble[0] = 1;
					for(int k=0;k<lineNum-1;k++){
						Geometry polyLine = multipolyLine.getGeometry(k);	//直线型
						startDouble[k+1] = (polyLine.getCoordinates().length)*2+startDouble[k];
					}
					geom.setOther(lineNum);
					geom.setStartDouble(startDouble);
				}
			
			Coordinate[] coordnates = spaDev.getGeometry()
					.getGeometry().getCoordinates();
			int natesnum = coordnates.length;
			// 坐标数组 [x1,y1,x2,y2,x3,y3]
			double[] coords = new double[2 * natesnum];
			for (int k = 0; k < natesnum; k++) {
				coords[2 * k] = coordnates[k].getX();
				coords[2 * k + 1] = coordnates[k].getY();
			}
			geom.setGeometryType(geometryType);
			geom.setCoords(coords);
			records[j].setGeom(geom);

			// 返回字段信息集合(OID,SBMC,SHAPE,symbol,DYDJ)
			String[] returnField = queryField;
			int returnFieldlength = returnField.length;
			List<QueryField> fieldsList = new ArrayList<QueryField>();
			for (int k = 0; k < returnFieldlength; k++) {
				Device a = devList.get(j);
				QueryField fields = new QueryField();
				fields.setFieldName(returnField[k]);

				if (a.getValue(returnField[k]) != null) {
					String fieldValue = String.valueOf(a.getValue(returnField[k]));
					//设备名称(SBMC)优化(去掉一些特殊符号)
					if(returnField[k].equals("SBMC")){
						fieldValue = modifySBMC(fieldValue);
					}
					
					fields.setFieldValue(fieldValue);
				}
				fields.setFieldAlias(model.getFieldDef()
						.find(returnField[k]).getFieldAlias());
				fieldsList.add(fields);
			}
			QueryField[] fields = new QueryField[fieldsList.size()];
			for (int k = 0; k < fieldsList.size(); k++) {
				fields[k] = fieldsList.get(k);
			}
			records[j].setFields(fields);

			// 返回符号信息
			
			SymbolDef symDef = null;
			try {
				symDef = symAdp.search(devList.get(j));
			} catch (Exception e1) {
				logger.info("模型创建时出错");
				resp.setCode(ReturnCode.BUILDMODEL);
				return null;
			}
			if (symDef != null) {
				SymbolPair symbol = new SymbolPair();
				symbol.setModelId(symDef.getModelId());
				symbol.setSymbolValue(symDef.getSymbolValue());
				symbol.setSymbolId(symDef.getSymbolId());
				records[j].setSymbol(symbol);
			}

		}
		// 操作每次的返回查询结果
		QueryResult result = new QueryResult();
		result.setRecords(records);
		result.setCount(count);
		PSRDef psrDef = new PSRDef();
		psrDef.setPsrName(model.getClassDef().getClassAlias());
		psrDef.setPsrType(model.getClassDef().getClassType());
		SubClassDef[] subClassDef = model.getSubClassDef();
		int subLength = subClassDef.length;
		SubPSRDef[] subPSRDef = new SubPSRDef[subLength];
		for (int j = 0; j < subLength; j++) {
			subPSRDef[j] = new SubPSRDef();
			subPSRDef[j].setSubPSRType(subClassDef[j].getPsrType());
			subPSRDef[j].setSubPSRName(subClassDef[j].getPsrName());
		}
		psrDef.setSubPSRDef(subPSRDef);
		result.setPsrDef(psrDef);
		
		return result;
	}

	/**
	 * 获取一个用于查询的多边形
	 * @param geometryType0
	 * @param coords0
	 * @param geomPair
	 * @param resp
	 * @return 返回null表示查询出错，应该直接返回
	 */
	private Polygon getPolygon(String geometryType0, double[] coords0, GeometryPair geomPair, SpatialQueryResponse resp) {
		// 根据获得的空间对象创建一个Polygon
		// 空间对象类型 1 点 2 线 3 面
		Polygon poly = null;
		if ("1".equals(geometryType0)) {
			Point point1 = geoService.createPoint(coords0[0], coords0[1]);
			double bufferLen = geomPair.getOther();
			Geometry geo = point1.buffer(bufferLen);
			poly = geoService.createPolygon(geo.getCoordinates());
		} else if ("2".equals(geometryType0)) {
			Polyline line1 = geoService.createPolyline(coords0);
			double bufferLen = geomPair.getOther();
			Geometry geo = line1.buffer(bufferLen);
			poly = geoService.createPolygon(geo.getCoordinates());
		} else if ("3".equals(geometryType0)) {
			poly = geoService.createPolygon(geomPair.getCoords());
		} else {
			// 设备类型出错，直接返回null
			logger.info("参数值有误(可能不存在对应数据)");
			resp.setCode(ReturnCode.VALUEWRONG);
			return null;
		}
		
		// 未成功创建poly
		if (poly == null) {
			logger.info("模型创建时出错");
			resp.setCode(ReturnCode.BUILDMODEL);
		}
		return poly;
	}
}
