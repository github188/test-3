package nari.QueryService.handler;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
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
import nari.QueryService.QueryServiceActivator;
import nari.model.TableName;
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
import nari.model.symbol.SymbolAdapter;
import nari.parameter.QueryService.StationIdQuery.QueryByStationIdRequest;
import nari.parameter.QueryService.StationIdQuery.QueryByStationIdResponse;
import nari.parameter.bean.GeometryPair;
import nari.parameter.bean.PSRDef;
import nari.parameter.bean.QueryField;
import nari.parameter.bean.QueryRecord;
import nari.parameter.bean.QueryResult;
import nari.parameter.bean.SubPSRDef;
import nari.parameter.bean.SymbolPair;
import nari.parameter.code.ReturnCode;

import org.json.simple.JSONArray;

public class QueryByStationIdHandler {
	ModelService ms = QueryServiceActivator.modelService;
	DbAdaptor db = QueryServiceActivator.dbAdaptor;
	SymbolAdapter symAdp = QueryServiceActivator.symboladapter;
	Map<String,String> modelRefleMap = QueryServiceActivator.modelRefleMap;
	
	public QueryByStationIdResponse queryByStationId(QueryByStationIdRequest request) {
		QueryByStationIdResponse resp = new QueryByStationIdResponse();
		//是否返回拓扑信息
		boolean isTopu = "true".equalsIgnoreCase(request.getIsTopu());
		
		String classId = "300000";
		
		DeviceModel model = null;
				
		// 得到所有站设备
		try {
			model = ms.fromClass(classId, false);
		} catch (Exception e) {
			System.out.println("模型创建时出错");
			resp.setCode(ReturnCode.BUILDMODEL);
			return resp;
		}
		CriteriaBuilder builder = model.getEntryManager().getCriteriaBuilder();
		String sbid = request.getSBID();
		Expression exp = builder.equal(builder.getRoot().get("SBID", String.class), sbid);
		String[] returnType = new String[]{"SBID","SBZLX","OID"};
		ResultSet set = ResultSet.NONE;
		// 根据OId得到相应的站点既得到站内设备子类型Id
		try {
			// 添加查询时间
			long start = System.currentTimeMillis();
			set = model.search(returnType, exp,null);
			long end = System.currentTimeMillis();
			System.out.println("每次查询用时:" + (end - start) + "ms");
		} catch (Exception e) {
			System.out.println("数据库查询出错");
			resp.setCode(ReturnCode.SQLERROR);
			return resp;
		}
		
		// 判断是否有此电站
		if (set == null || set == ResultSet.NONE) {
			System.out.println("参数值有误(可能不存在对应数据)");
			resp.setCode(ReturnCode.VALUEWRONG);
			return resp;
		}
		
		// 得到该电站的电站子类型从而此站子设备的子类型(modelid)
		Device dev = set.getSingle();
		
		if(dev == null || dev == Device.NONE){
			System.out.println("参数值有误(可能不存在对应数据)");
			resp.setCode(ReturnCode.VALUEWRONG);
			return resp;
		}
		
		String pModelId = String.valueOf(dev.getValue("SBZLX"));
		String OId = String.valueOf(dev.getValue("OID"));
		
		String sql = "select * from " + TableName.CONF_MODELRELATION + " where relationfield = 'SSDZ' and pmodelid = '" + pModelId + "'";
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = db.findAllMap(sql);
		} catch (SQLException e) {
			System.out.println("数据库查询出错");
			resp.setCode(ReturnCode.SQLERROR);
			return resp;
		}

		//要显示的model对应集合()
		boolean displayCondition = true;	//是否有显示筛选条件
		List<DeviceModel> displayModelList = new ArrayList<DeviceModel>();	//需要显示的model
		String[] disPlayRModelIds = request.getDisPlayRModelIds();
		if(disPlayRModelIds != null && disPlayRModelIds.length != 0){
			for(String rmodelId : disPlayRModelIds){
				try {
					DeviceModel displayModel = ms.fromSubClass(rmodelId, false);
					displayModelList.add(displayModel);
				} catch (Exception e) {
					System.out.println("模型创建时出错");
					resp.setCode(ReturnCode.BUILDMODEL);
					continue;
				}
			}
		}else{
			displayCondition = false;
		}
		
		// 判定与model是否相同(有可能不同rmodel对应的classId相同)
		QueryResult[] results = new QueryResult[list.size()];
		List<DeviceModel> modelList = new ArrayList<DeviceModel>();
		for (int i = 0; i < list.size(); i++) {
			
			boolean modelDisplayFlag = false;	//模型是否需要显示
			results[i] = new QueryResult();
			String rModelId = String.valueOf(list.get(i).get("rmodelid"));
			
			System.out.println("rmodelid:"+rModelId);
			
			String rclassId = modelRefleMap.get(rModelId);
			try {

				DeviceModel Model = ms.fromClass(rclassId, false);
//				/*******************实验*****************************/
//				DeviceModel displayModel0 = ms.fromClass("306000", false);
//				DeviceModel displayModel1 = ms.fromSubClass("30600001", false);
//				DeviceModel displayModel2 = ms.fromSubClass("30600002", false);
//				
//				/**************************************************/
				
				if(displayModelList.contains(Model) || !displayCondition){	//没有显示删选条件或者是需要显示的模型
					modelDisplayFlag = true;
				}
				if (modelList.contains(Model) || (!modelDisplayFlag)) { 	//相同或者不需要显示
					continue;
				}
				modelList.add(Model);
			} catch (Exception e) {
				System.out.println("数据库查询出错");
				resp.setCode(ReturnCode.SQLERROR);
//				return resp;
				continue;
			}
		}

		// 得到该站内设备的类型model集合，根据对应的站(根据SSDZ)得到具体的设备
		for (int i = 0; i < modelList.size(); i++) {
			// 得到该子设备的类型
			try {
				PSRDef psrDef = new PSRDef();
				String psrType = modelList.get(i).getClassDef().getClassId();
				String psrName = modelList.get(i).getClassDef().getClassAlias();
				
				System.out.println("model:"+psrType+","+psrName);
				
				
				SubClassDef[] SubClassDef = modelList.get(i).getSubClassDef();
				SubPSRDef[] subPSRDef = new SubPSRDef[SubClassDef.length];
				for (int j = 0; j < SubClassDef.length; j++) {
					subPSRDef[j] = new SubPSRDef();
					subPSRDef[j].setSubPSRName(SubClassDef[j].getPsrName());
					subPSRDef[j].setSubPSRType(SubClassDef[j].getPsrType());
				}
				psrDef.setSubPSRDef(subPSRDef);
				psrDef.setPsrName(psrName);
				psrDef.setPsrType(psrType);
				results[i].setPsrDef(psrDef);
				exp = builder.equal(builder.getRoot().get("SSDZ", String.class), OId);
				// //为避免数据量太大，添加条件rownum<100;
				// exp =
				// builder.and(exp,builder.lessThan(builder.getRoot().get("rownum",
				// String.class),
				// 2));

				// 得到返回的字段(通过他判断是都进行空间查询)
				boolean isQueryGeometry = false;
				boolean HasConnectionField = false;
				Iterator<FieldDetail> fieldIt = modelList.get(i).getFieldDef().details();
				List<FieldDetail> fieldList = new ArrayList<FieldDetail>();
				int fieldCount = 0;
				while (fieldIt.hasNext()) {
					fieldList.add(fieldCount, fieldIt.next());
					if (fieldList.get(fieldCount) == null) {
						break;
					}
					fieldCount = fieldCount + 1;
				}
				String[] queryReturnField = new String[fieldCount];
				for (int j = 0; j < fieldCount; j++) {
					queryReturnField[j] = fieldList.get(j).getFieldName();
					if ("shape".equalsIgnoreCase(queryReturnField[j])) {
						isQueryGeometry = true;
					}
					if ("connection".equalsIgnoreCase(queryReturnField[j])) {
						HasConnectionField = true;
					}
				}
				try {
					// 为提高效率，可以查找字段精简为(OID,SBMC,SHAPE,symbol(SBZLX))
					// queryReturnField = new String[]{"OID","SBMC","SHAPE","SBZLX"}
					set = modelList.get(i).search(queryReturnField, exp,null);
				} catch (Exception e) {
					System.out.println("数据库查询出错");
					resp.setCode(ReturnCode.SQLERROR);
					continue;
//					return resp;
				}
				// 得到每种该站子类型设备类型对应结果
				Iterator<Device> it = set.resultList();
				// 返回记录数量
				int count = 0;
				List<Device> devList = new ArrayList<Device>();
//				List<TopoDevice> topoDevList = new ArrayList<TopoDevice>();
				if (it != null) {
					while (it.hasNext()) {
						devList.add(count, it.next());
						if (devList.get(count) == null) {
							break;
						}
						count = count + 1;
					}
				}
				// 返回查询记录
				QueryRecord[] records = new QueryRecord[count];
							
				for (int j = 0; j < count; j++) {
					try {
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
						if (isQueryGeometry) {
							SpatialDevice spaDev = devList.get(j).asSpatialDevice();
							
							System.out.println(devList.get(j).getValue("SBMC")+"---"+devList.get(j).getValue("SBZLX"));
							// 返回空间对象信息
							GeometryPair geom = new GeometryPair();
							// 空间对象类型 1 点 2 线 3 面
							String geometryType = "";
							Geometry nariGeometry = spaDev.getGeometry().getGeometry();
							if(nariGeometry != null){

								GeometryType geomType = nariGeometry.getGeometryType();
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
									if(lineNum>0){
										int[] startDouble = new int[lineNum];
										startDouble[0] = 1;
										for(int k=0;k<lineNum-1;k++){
											int doubleNum = (polyline.getSegment(k).getCoordinates().length)*2;
											startDouble[k+1] = startDouble[k]+doubleNum;
										}
										geom.setOther(lineNum);
										geom.setStartDouble(startDouble);
									}
									
								}
								//对多线特殊处理
								if(("MULTIPOLYLINE").equalsIgnoreCase(geometryType)){
									geometryType = "MULTILINESTRING";
									GeometryCollection multipolyLine = (GeometryCollection)spaDev.getGeometry().getGeometry();
									int lineNum = multipolyLine.getNumGeometry();
									if (lineNum > 0) {
										int[] startDouble = new int[lineNum];
										startDouble[0] = 1;
										for(int k=0;k<lineNum-1;k++){
											Geometry polyLine = multipolyLine.getGeometry(k);	//直线型
											startDouble[k+1] = (polyLine.getCoordinates().length)*2+startDouble[k];
										}
										geom.setOther(lineNum);
										geom.setStartDouble(startDouble);
									}
								}
								
								Coordinate[] coordnates = spaDev.getGeometry().getGeometry().getCoordinates();
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
						}
						
						//是否返回拓扑信息
						long[] connectionNodes = null;
						JSONArray connectionNodesJSONArray = new JSONArray();
						if(isTopu && HasConnectionField){
							Blob connection = (Blob)devList.get(j).getValue("connection");
							try {
								connectionNodes = parseConnection(connection);
								for(int k=0;k<connectionNodes.length;k++){
									connectionNodesJSONArray.add(connectionNodes[k]);
								}
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}

						// 返回字段信息集合(OID,SBMC,SHAPE,symbol,DYDJ)
						int returnFieldlength = queryReturnField.length;
						List<QueryField> fieldsList = new ArrayList<QueryField>();
						for (int k = 0; k < returnFieldlength; k++) {
							Device a = devList.get(j);
							QueryField fields = new QueryField();
							fields.setFieldName(queryReturnField[k]);

							if (a.getValue(queryReturnField[k]) != null) {
								String fieldValue = String.valueOf(a.getValue(queryReturnField[k]));
//								//设备名称(SBMC)优化(去掉一些特殊符号)
//								if(queryReturnField[k].equals("SBMC")){
//									fieldValue = modifySBMC(fieldValue);
//								}
								//优化字段(去掉特殊符号)
								fieldValue = modifySBMC(fieldValue);
								fields.setFieldValue(fieldValue);
							}
							
							//若为拓扑信息
							if(queryReturnField[k].equalsIgnoreCase("connection")){
								String fieldValue = connectionNodesJSONArray.toString();
								fields.setFieldValue(fieldValue);
							}
							
							fields.setFieldAlias(model.getFieldDef().find(queryReturnField[k]).getFieldAlias());
							fieldsList.add(fields);
							
							/*************************************************/
//							//调试
//							if (queryReturnField[k].equals("OID")) {
//								// 根据电压等级查询表conf_codedefinition得到相应实际值
//								String fieldValue = String.valueOf(a.getValue(queryReturnField[k]));
//								//设备名称(SBMC)优化(去掉一些特殊符号)
//								if("232997".equalsIgnoreCase(fieldValue)){
//									System.out.println("i=" + i);
//									System.out.println("j=" + j);
//								}
//							}
							/**************************************************/

							// 对电压等级转义(电压等级变成电压值)(多加一个返回字段)
							if (queryReturnField[k].equals("DYDJ")) {
								// 根据电压等级查询表conf_codedefinition得到相应实际值
								if (a.getValue(queryReturnField[k]) == null) {
									System.out.println(k);
									continue;
								}
								String dydj = String.valueOf((a.getValue(queryReturnField[k])));
								String dyz = "";

								if (dydj.equalsIgnoreCase("0")) {
									dyz = "0";
								} else {
									
									String sql0 = "select * from " + TableName.CONF_CODEDEFINITION + " where codeid = 10401 and codedefid = " + dydj;
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
										dyz = String.valueOf(codeFieldMap.get("codename"));
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
						for (int k = 0; k < fieldsList.size(); k++) {
							fields[k] = fieldsList.get(k);
						}
						records[j].setFields(fields);

						// 返回符号信息
						
						SymbolDef symDef = null;
						try {
							symDef = symAdp.search(devList.get(j));
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
//							symbol.setDevtypeId(symDef.getDevTypeId());
							records[j].setSymbol(symbol);
						}
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
				// 操作每次的返回查询结果
				results[i].setRecords(records);
				results[i].setCount(count);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			
		}
		resp.setResult(results);
		resp.setCode(ReturnCode.SUCCESS);
		return resp;
	}
	
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
//			value = value.replaceAll(" ", "");
		}
		return value;
	}
	
		private long[] parseConnection(Blob blob) throws SQLException {
		if (null == blob || blob.length() <= 0) {
			return null;
		}
		InputStream is = blob.getBinaryStream();
		int terminalCount = 0;
		try {
			terminalCount = is.read();	//第一个byte代表拓扑点个数
		} catch (IOException e) {
			return null;
		}
		long blength = blob.length();
		if (terminalCount == 0 || blength != (8 * terminalCount + 1) ) {	//拓扑点个数*8 + 1即为blob的长度（8byte表示一个拓扑点id）
			return null;
		}
		
		byte[] bytes = new byte[8 * terminalCount];
		try {
			if (8 * terminalCount != is.read(bytes, 0, 8 * terminalCount)) {
				return null;
			}
		} catch (IOException ex) {
			return null;
		}
		
		long[] connectionNodes = new long[terminalCount];
		int offset = 0;
		for (int i = 0; i < terminalCount; i++) {
			connectionNodes[i] = bytesToLong(bytes, offset);
			offset += 8;
		}
		return connectionNodes;
	}
	
	private long bytesToLong(byte[] bytes, int off) {
		int b0 = bytes[off + 0] & 0xFF;  
	    int b1 = bytes[off + 1] & 0xFF;  
	    int b2 = bytes[off + 2] & 0xFF;  
	    int b3 = bytes[off + 3] & 0xFF; 
	    int b4 = bytes[off + 4] & 0xFF;  
	    int b5 = bytes[off + 5] & 0xFF; 
	    int b6 = bytes[off + 6] & 0xFF; 
	    int b7 = bytes[off + 7] & 0xFF;
	    return (b7 << 56) | (b6 << 48) | (b5 << 40) | (b4 << 32) |
	    		(b3 << 24) | (b2 << 16) | (b1 << 8) | b0;  
	}
}
