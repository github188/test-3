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
import nari.Geometry.Polyline;
import nari.MainGridService.MainGridServiceActivator;
import nari.model.bean.FieldDetail;
import nari.model.bean.SubClassDef;
import nari.model.device.Device;
import nari.model.device.DeviceModel;
import nari.model.device.ModelService;
import nari.model.device.ResultSet;
import nari.model.device.SpatialDevice;
import nari.model.device.filter.CriteriaBuilder;
import nari.model.device.filter.Expression;
import nari.parameter.MainGridService.ConditionLocatQuery.ConditionLocatQueryRequest;
import nari.parameter.MainGridService.ConditionLocatQuery.ConditionLocatQueryResponse;
import nari.parameter.bean.GeometryPair;
import nari.parameter.bean.Link;
import nari.parameter.bean.Operator;
import nari.parameter.bean.PSRDef;
import nari.parameter.bean.Pair;
import nari.parameter.bean.QueryField;
import nari.parameter.bean.QueryRecord;
import nari.parameter.bean.QueryResult;
import nari.parameter.bean.SelfDefField;
import nari.parameter.bean.SubPSRDef;
import nari.parameter.bean.TypeCondition;
import nari.parameter.code.ReturnCode;

public class ConditionLocatQueryHandler {

	ModelService ms = MainGridServiceActivator.modelService;
	DbAdaptor db = MainGridServiceActivator.dbAdaptor;
	//电压值转义缓存
	Map<String, String> DYDJCodeMap = MainGridServiceActivator.DYDJCodeMap;
	//变电站映射值缓存
	Map<String, String> BDZReflectMap = MainGridServiceActivator.BDZReflectMap;
	
	public ConditionLocatQueryResponse conditionLocatQuery(ConditionLocatQueryRequest request) {
		ConditionLocatQueryResponse resp = new ConditionLocatQueryResponse();
		
		//每种设备类型对应查询条件
				TypeCondition[] cond = request.getCondition();
				int condLength = cond.length;
				QueryResult[] results = new QueryResult[condLength];
				for (int i = 0; i < condLength; i++) {
					String classId = cond[i].getPsrType();
					if (classId == null) {
						resp.setCode(ReturnCode.NULL);
						return resp;
					} 
						DeviceModel model = null;
						try {
							model = ms.fromClass(classId, false);
						} catch (Exception e1) {
							System.out.println("模型创建时出错");
							resp.setCode(ReturnCode.BUILDMODEL);
							return resp;
						}
						
						String[] queryField = cond[i].getReturnField();
						boolean isQueryGeometry = false;
						if (queryField == null || queryField.length == 0) {
						// 得到返回的字段(通过他判断是都进行空间查询)
						Iterator<FieldDetail> fieldIt = model.getFieldDef().details();
						List<FieldDetail> fieldList = new ArrayList<FieldDetail>();
						int fieldCount = 0;
						while (fieldIt.hasNext()) {
							fieldList.add(fieldCount, fieldIt .next());
							if (fieldList.get(fieldCount) == null) {
								break;
							}
							fieldCount = fieldCount + 1;
						}
						queryField = new String[fieldCount];
						for (int j = 0; j < fieldCount; j++) {
							queryField[j] = fieldList.get(j).getFieldName();
							if ("shape".equalsIgnoreCase(queryField[j])) {
								isQueryGeometry = true;
							}
						}
						}else{
							for (int j = 0; j < queryField.length; j++) {
								if ("shape".equalsIgnoreCase(queryField[j])) {
									isQueryGeometry = true;
								}
							}
						}
						
						CriteriaBuilder builder = model.getEntryManager().getCriteriaBuilder();
						// 条件组合
						Pair[] pairs = cond[i].getPairs();
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
							Link link = cond[i].getLink();
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
						//为避免数据量太大，添加条件rownum<60;
//						if(e != null){
//							e = builder.and(
//									e,
//									builder.lessThan(
//											builder.getRoot().get("rownum", String.class), 60));
//
//						}else{
//							e = builder.lessThan(
//									builder.getRoot().get("rownum", String.class), 60);
//						}

						ResultSet set = null;
						try {
							//添加查询时间
							long start = System.currentTimeMillis();
							set = model.search(queryField, e,null);
							long end = System.currentTimeMillis();
							System.out.println("每次查询用时:"+(end-start)+"ms");
						} catch (Exception e1) {
							System.out.println("数据库查询出错");
							resp.setCode(ReturnCode.SQLERROR);
							return resp;
						}
						
						
						// 得到结果
						Iterator<Device> it = set.resultList();
						// 返回记录数量
						int count = 0;
						List<Device> devList = new ArrayList<Device>();
//						List<TopoDevice> topoDevList = new ArrayList<TopoDevice>();
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
							records[j] = new QueryRecord();
//							// 是否查拓扑信息
//							if (request.isQueryTopo()) {
//								topoDevList.add(j, devList.get(j).asTopoDevice());
//								// 返回拓扑信息
//								TopoPair topo = new TopoPair();
//								// 返回端子个数
//								int nodeNum = topoDevList.get(j).getTopo().nodeCount();
//								// 端子号
//								long[] nodes = topoDevList.get(j).getTopo().nodes();
//								topo.setNodeNum(nodeNum);
//								topo.setNodes(nodes);
//								records[j].setTopo(topo);
//							}
							// 是否查询得到空间信息
							if (isQueryGeometry) {
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
							
							
							// 返回字段信息集合(OID,SBMC,SHAPE,symbol,DYDJ)
							int queryFieldlength = queryField.length;
							List<QueryField> fieldsList = new ArrayList<QueryField>();
							//判断是否转换名字的标志(变电站须转)
							boolean BDZReflectFlag = false;
							boolean hasSBMC = false;
							String BDZReflectId = "";
							Device a = devList.get(j);
							for (int k = 0; k < queryFieldlength; k++) {
								if(queryField[k] == null || queryField[k].equalsIgnoreCase("null")){
									continue;
								}
								
								QueryField fields = new QueryField();
								fields.setFieldName(queryField[k]);
								//给值时徐判断是否要反射
								if (a.getValue(queryField[k]) != null) {
									//对变电站用反射值当名字(根据sbid)
									if(queryField[k].equalsIgnoreCase("sbid") 
										&& BDZReflectMap.containsKey(String.valueOf((a.getValue(queryField[k])))) ){
										BDZReflectFlag = true;
										BDZReflectId = String.valueOf((a.getValue(queryField[k])));
									}
									//若是sbmc则先跳过最后单独加上
									if(queryField[k].equalsIgnoreCase("sbmc")){
										hasSBMC = true;
										continue;
									}
									fields.setFieldValue(String.valueOf((a.getValue(queryField[k]))));
								}
								
								fields.setFieldAlias(model.getFieldDef()
										.find(queryField[k]).getFieldAlias());
								fieldsList.add(fields);
								
								// 对电压等级转义(电压等级变成电压值)(多加一个返回字段)
								if (queryField[k].equals("DYDJ")) {
									// 根据电压等级codeFieldMap得到相应实际值
									if (a.getValue(queryField[k]) == null) {
										// System.out.println(k);
										continue;
									}
									String dydj = String.valueOf((a
											.getValue(queryField[k])));
									String dyz = "";
									try {
										dyz = DYDJCodeMap.get(dydj);
									} catch (Exception e1) {
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
							SelfDefField[] selfDefFields = cond[i].getSelfDefFields();
							if(selfDefFields != null && selfDefFields.length != 0){
								for(SelfDefField selfDefField:selfDefFields){
									QueryField fields2 = new QueryField();
									fields2.setFieldName(selfDefField.getSelfFieldName());
									fields2.setFieldValue(selfDefField.getSelfFieldValue());
									fields2.setFieldAlias(selfDefField.getSelfFieldAlias());
									fieldsList.add(fields2);
								}
							}	//自定义添加feild结束
							
							//在这里加上需判断的sbmc
							if(hasSBMC){
							QueryField fields1 = new QueryField();
							fields1.setFieldName("SBMC");
							fields1.setFieldAlias("设备名称");
							if(BDZReflectFlag){
								fields1.setFieldValue(BDZReflectMap.get(BDZReflectId));
							}else{	//对其他的sbmc做优化
								String sbmcValue = String.valueOf(a.getValue("SBMC".toUpperCase()));
								fields1.setFieldValue(modifySBMC(sbmcValue));
							}
							fieldsList.add(fields1);
							}
							QueryField[] fields = new QueryField[fieldsList.size()];
							for(int k = 0;k<fieldsList.size();k++){
								fields[k] = fieldsList.get(k);
							}
							records[j].setFields(fields);
							
//							 //返回符号信息
//							 SymbolAdapter symAdp = QueryServiceActivator.symboladapter;
//							 SymbolDef symDef = null;
//							 try {
//							 symDef = symAdp.search(devList.get(j));
//							 } catch (Exception e1) {
//							 e1.printStackTrace();
//							 }
//							 if(symDef != null){
//								 SymbolPair symbol = new SymbolPair();
//								 symbol.setModelId(symDef.getModelId());
//								 symbol.setSymbolValue(symDef.getSymbolValue());
//								 symbol.setSymbolId(symDef.getSymbolId());
//								 symbol.setDevtypeId(symDef.getDevTypeId());
//								 records[j].setSymbol(symbol);
//							 }
						}
						// 操作每次的返回查询结果
						results[i] = new QueryResult();
						results[i].setRecords(records);
						results[i].setCount(count);
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
						results[i].setPsrDef(psrDef);
				}
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
