package nari.MainGridService.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nari.Dao.interfaces.DbAdaptor;
import nari.Geometry.Coordinate;
import nari.Geometry.Geometry;
import nari.Geometry.GeometryCollection;
import nari.Geometry.GeometryType;
import nari.Geometry.Polygon;
import nari.Geometry.Polyline;
import nari.MainGridService.MainGridServiceActivator;
import nari.model.device.Device;
import nari.model.device.DeviceModel;
import nari.model.device.ModelService;
import nari.model.device.ResultSet;
import nari.model.device.SpatialDevice;
import nari.model.device.filter.CriteriaBuilder;
import nari.model.device.filter.Expression;
import nari.model.geometry.GeometryService;
import nari.model.symbol.SymbolAdapter;
import nari.parameter.MainGridService.GetViewGeometry.GetViewGeometryRequest;
import nari.parameter.MainGridService.GetViewGeometry.GetViewGeometryResponse;
import nari.parameter.bean.ClassCondition;
import nari.parameter.bean.GeometryPair;
import nari.parameter.bean.QueryField;
import nari.parameter.bean.QueryRecord;
import nari.parameter.bean.QueryResult;
import nari.parameter.bean.SelfDefField;
import nari.parameter.code.ReturnCode;

public class GetViewGeometryHandler {
	GeometryService geoService = MainGridServiceActivator.geoService;
	ModelService ms = MainGridServiceActivator.modelService;
	DbAdaptor db = MainGridServiceActivator.dbAdaptor;
	SymbolAdapter symAdp = MainGridServiceActivator.symboladapter;
	Map<String, String> DYDJCodeMap = MainGridServiceActivator.DYDJCodeMap;
	ResultSet XLset = MainGridServiceActivator.XLset;
	ResultSet set = ResultSet.NONE;
	DeviceModel model = DeviceModel.NONE;

	// List<Map<String,Object>> YXDWList = MainGridServiceActivator.YXDWList;

	public GetViewGeometryResponse getViewGeometry(GetViewGeometryRequest request) {
		GetViewGeometryResponse resp = new GetViewGeometryResponse();
		double[] coords0 = request.getBbox();
		// 创建矩形polygon

		Polygon polygon = geoService.createPolygon(coords0);
		// 判断给的是否能创建成polygon
		if (polygon == null) {
			System.out.println("模型创建时出错");
			resp.setCode(ReturnCode.BUILDMODEL);
			return resp;
		}

		// //运行单位缓存
		// String sql =
		// "select * from conf_codedefinition where codeid = 10401 ";
		// List<Map<String,Object>> codeFieldList = null;
		// try {
		// codeFieldList = db.findAllMap(sql);
		// } catch (SQLException e1) {
		// e1.printStackTrace();
		// }

		String[] classId = null;
		String[] queryedFields = null; 
		// 得到所需查询设备类型(电站,站外物理杆，运行杆，导线段)
		ClassCondition[] classConditions = request.getClassConditions();
		if (classConditions == null || classConditions.length == 0) {
			classId = new String[] { "300000", "103000", "102000", "101000" };
			queryedFields = new String[]{"OID","SBMC"};
			
		}
		
		// 分别查询对应的
		QueryResult[] results = new QueryResult[classConditions.length];
		classId = new String[classConditions.length];
		for (int i = 0; i < classConditions.length; i++) {
			classId[i] = classConditions[i].getClassId();
			// 查询字段queryReturnField(在后面取出OID,SBMC,SHAPE,symbol)
			queryedFields = classConditions[i].getReturnField();
			//加载电路缓存
			if(classId[i].equalsIgnoreCase("101000")){
				set = XLset;
				
				try {
					model = ms.fromClass(classId[i], false);
				} catch (Exception e) {
					System.out.println("模型创建时出错");
					resp.setCode(ReturnCode.BUILDMODEL);
					return resp;
				}
			}else{
			try {
				model = ms.fromClass(classId[i], false);
			} catch (Exception e) {
				System.out.println("模型创建时出错");
				resp.setCode(ReturnCode.BUILDMODEL);
				return resp;
			}

			
			// 查询条件(根据电压等级)
			Expression EXP = null;	//总条件
			CriteriaBuilder builder = model.getEntryManager()
					.getCriteriaBuilder();
			String[] DYDJ = classConditions[i].getDYDJ();
			Expression dydjExpression = Expression.NONE;	//电压等级条件
			if (DYDJ != null && DYDJ.length !=0) {
				dydjExpression = builder.in(
						builder.getRoot().get("dydj", String.class), DYDJ);
					EXP = dydjExpression;
			}
			String[] SSXL = classConditions[i].getSSXL();
			Expression ssxlExpression = Expression.NONE;	//所属线路条件
			if(SSXL != null && SSXL.length !=0){
				ssxlExpression = builder.in(
						builder.getRoot().get("ssxl", String.class), SSXL);
				if(EXP != null && EXP != Expression.NONE){
					EXP = builder.and(EXP, ssxlExpression);
				}else{
					EXP = ssxlExpression;
				}
			}

			try {
				// 添加查询时间
				long start = System.currentTimeMillis();
				set = model.spatialQuery(queryedFields, EXP, null,
						polygon);
				long end = System.currentTimeMillis();
				System.out.println("每次查询用时:" + (end - start) + "ms");
			} catch (Exception e1) {
				System.out.println("数据库查询出错");
				resp.setCode(ReturnCode.SQLERROR);
				return resp;
			}
			}
			Iterator<Device> it = set.resultList();
			// 返回记录数量
			int count = 0;
			List<Device> devList = new ArrayList<Device>();
			if (it != null) {
				while (it.hasNext()) {
					devList.add(count, it.next());
					if (devList.get(count) == null) {
						break;
					}
					count = count + 1;
				}
			}

			//判断有无shape字段
			boolean isGeometry = false;
			for(int j=0;j<queryedFields.length;j++){
				if("SHAPE".equalsIgnoreCase(queryedFields[j])){
					isGeometry = true;
					break;
				}
			}
			
			// 返回查询记录
			QueryRecord[] records = new QueryRecord[count];
			for (int j = 0; j < count; j++) {
				records[j] = new QueryRecord();
				// 是否查拓扑信息

				// topoDevList.add(j, devList.get(j).asTopoDevice());
				// // 返回拓扑信息
				// TopoPair topo = new TopoPair();
				// // 返回端子个数
				// int nodeNum = topoDevList.get(j).getTopo().nodeCount();
				// // 端子号
				// long[] nodes = topoDevList.get(j).getTopo().nodes();
				// topo.setNodeNum(nodeNum);
				// topo.setNodes(nodes);
				// records[j].setTopo(topo);

				
				
				// 是否查询得到空间信息
				if (isGeometry) {
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
				}

				boolean oIdFlag = false;
				// 返回字段信息集合(加一个dyz字段)
				int returnFieldlength = queryedFields.length;
				List<QueryField> fieldsList = new ArrayList<QueryField>();
				for (int k = 0; k < returnFieldlength; k++) {
					Device device = devList.get(j);
					// 删除指定导线段
					if (queryedFields[k].equalsIgnoreCase("OID")) {
						if (String.valueOf((device.getValue(queryedFields[k])))
								.equalsIgnoreCase("110230367")
								|| String.valueOf(
										(device.getValue(queryedFields[k])))
										.equalsIgnoreCase("110230369")
								|| String.valueOf(
										(device.getValue(queryedFields[k])))
										.equalsIgnoreCase("110230371")
								|| String.valueOf(
										(device.getValue(queryedFields[k])))
										.equalsIgnoreCase("110230375")
								|| String.valueOf(
										(device.getValue(queryedFields[k])))
										.equalsIgnoreCase("110230400")
								|| String.valueOf(
										(device.getValue(queryedFields[k])))
										.equalsIgnoreCase("110230407")
								|| String.valueOf(
										(device.getValue(queryedFields[k])))
										.equalsIgnoreCase("110230408")
								|| String.valueOf(
										(device.getValue(queryedFields[k])))
										.equalsIgnoreCase("110230424")
								|| String.valueOf(
										(device.getValue(queryedFields[k])))
										.equalsIgnoreCase("110230439")
								|| String.valueOf(
										(device.getValue(queryedFields[k])))
										.equalsIgnoreCase("110230451")
								|| String.valueOf(
										(device.getValue(queryedFields[k])))
										.equalsIgnoreCase("110232677")) {
							oIdFlag = true;
						}
					}
					QueryField fields = new QueryField();
					fields.setFieldName(queryedFields[k]);

					// //对运行单位转义
					// Map<String,Object> YXDWmap = new
					// HashMap<String,Object>();
					// YXDWmap.put("isc_id",String.valueOf((a
					// .getValue(queryField[k]))));
					// for(int m=1;m<YXDWList.size();)

					if (device.getValue(queryedFields[k]) != null) {
						String fieldValue = String.valueOf(device.getValue(queryedFields[k]));
						//设备名称(SBMC)优化(去掉一些特殊符号)
						if(queryedFields[k].equals("SBMC")){
							fieldValue = modifySBMC(fieldValue);
						}
						
						fields.setFieldValue(fieldValue);
					}
					fields.setFieldAlias(model.getFieldDef()
							.find(queryedFields[k]).getFieldAlias());
					fieldsList.add(fields);

					// 对电压等级转义(电压等级变成电压值)(多加一个返回字段)
					if (queryedFields[k].equals("DYDJ")) {
						// 根据电压等级codeFieldMap得到相应实际值
						if (device.getValue(queryedFields[k]) == null) {
							// System.out.println(k);
							continue;
						}
						String dydj = String.valueOf((device
								.getValue(queryedFields[k])));
						String dyz = "";
						try {
							dyz = DYDJCodeMap.get(dydj);
						} catch (Exception e) {
							System.out.println("无对应电压");
							resp.setCode(ReturnCode.VALUEWRONG);
							return resp;
						}
						QueryField fields1 = new QueryField();
						fields1.setFieldName("DYZ");
						fields1.setFieldValue(dyz);
						fields1.setFieldAlias("电压值");
						fieldsList.add(fields1);
						
					}
				}	//原feild结束
				
				//添加自定义feild
				SelfDefField[] selfDefFields = request.getSelfDefFields();
				if(selfDefFields != null && selfDefFields.length != 0){
					for(SelfDefField selfDefField:selfDefFields){
						QueryField fields2 = new QueryField();
						fields2.setFieldName(selfDefField.getSelfFieldName());
						fields2.setFieldValue(selfDefField.getSelfFieldValue());
						fields2.setFieldAlias(selfDefField.getSelfFieldAlias());
						fieldsList.add(fields2);
					}
				}	//自定义添加feild结束
				
				// 根据flag,删除指定oid
				if (oIdFlag) {
					records[j] = null;
					continue;
				}
				QueryField[] fields = new QueryField[fieldsList.size()];
				fields = fieldsList.toArray(fields);
				records[j].setFields(fields);
				// // 返回符号信息
				// SymbolDef symDef = null;
				// try {
				// symDef = symAdp.search(devList.get(j));
				// } catch (ModelException e1) {
				// System.out.println("模型创建时出错");
				// resp.setCode(ReturnCode.BUILDMODEL);
				// return resp;
				// }
				// if (symDef != null) {
				// SymbolPair symbol = new SymbolPair();
				// symbol.setModelId(symDef.getModelId());
				// symbol.setSymbolValue(symDef.getSymbolValue());
				// symbol.setSymbolId(symDef.getSymbolId());
				// symbol.setDevtypeId(symDef.getDevTypeId());
				// records[j].setSymbol(symbol);
				// }
			}
			// 操作每次的返回查询结果
			results[i] = new QueryResult();
			results[i].setRecords(records);
			results[i].setCount(count);
//			PSRDef psrDef = new PSRDef();
//			psrDef.setPsrName(model.getClassDef().getClassAlias());
//			psrDef.setName(model.getClassDef().getClassId());
//			psrDef.setPsrType(model.getClassDef().getClassType());
//			SubClassDef[] subClassDef = model.getSubClassDef();
//			int subLength = subClassDef.length;
//			SubPSRDef[] subPSRDef = new SubPSRDef[subLength];
//			for (int j = 0; j < subLength; j++) {
//				subPSRDef[j] = new SubPSRDef();
//				subPSRDef[j].setSubPSRType(subClassDef[j].getPsrType());
//				subPSRDef[j].setSubPSRName(subClassDef[j].getPsrName());
//			}
//			psrDef.setSubPSRDef(subPSRDef);
//			results[i].setPsrDef(psrDef);
		}	//i循环结束

		//返回结果
		resp.setResult(results);
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
}
