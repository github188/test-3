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
import nari.model.bean.SubClassDef;
import nari.model.device.Device;
import nari.model.device.DeviceModel;
import nari.model.device.ModelService;
import nari.model.device.ResultSet;
import nari.model.device.SpatialDevice;
import nari.model.device.filter.CriteriaBuilder;
import nari.model.device.filter.Expression;
import nari.parameter.MainGridService.QueryDXDByYXGT.QueryDXDByYXGTRequest;
import nari.parameter.MainGridService.QueryDXDByYXGT.QueryDXDByYXGTResponse;
import nari.parameter.bean.GTpairCondition;
import nari.parameter.bean.GeometryPair;
import nari.parameter.bean.PSRDef;
import nari.parameter.bean.QueryField;
import nari.parameter.bean.QueryRecord;
import nari.parameter.bean.QueryResult;
import nari.parameter.bean.SubPSRDef;
import nari.parameter.code.ReturnCode;

/**
 * 12.通过杆塔查导线段
 */
public class QueryDXDByYXGTHandler {
	ModelService ms = MainGridServiceActivator.modelService;
	DbAdaptor db = MainGridServiceActivator.dbAdaptor;
	//电压值转义缓存
	Map<String, String> DYDJCodeMap = MainGridServiceActivator.DYDJCodeMap;
	//变电站映射值缓存
	Map<String, String> BDZReflectMap = MainGridServiceActivator.BDZReflectMap;
	//新老杆塔oid缓存
	Map<String,String>	SBIDtoZZGTOidMap = MainGridServiceActivator.SBIDtoZZGTOidMap;	//有sbid到ZZGT对应oid
	
	/*
	 * 主方法
	 */
	public QueryDXDByYXGTResponse queryDXDByYXGT(QueryDXDByYXGTRequest request) {
		QueryDXDByYXGTResponse resp = new QueryDXDByYXGTResponse();
		
			//查询导线段故为一个
				QueryResult[] results = new QueryResult[1];
					String classId = "101000";
						DeviceModel model = null;
						try {
							model = ms.fromClass(classId, false);
						} catch (Exception e1) {
							System.out.println("模型创建时出错");
							resp.setCode(ReturnCode.BUILDMODEL);
							return resp;
						}
						
						//得到返回字段
						String[] queryField = request.getReturnFields();
						if(queryField == null || queryField.length == 0){
							queryField = new String[]{"OID","SHAPE","SBID","YXDW"};
						}
						//判断是否有关于shape的操作
						boolean isQueryGeometry = false;
						for(String a:queryField){
							if(a.equalsIgnoreCase("shape")){
								isQueryGeometry = true;
							}
						}
						
						//对条件进行处理
						CriteriaBuilder builder = model.getEntryManager().getCriteriaBuilder();
						//得到对应条件(由于前面判断过，不为空)
						GTpairCondition[] GTpairConditions = request.getGTpairConditions();
						int GTpairNum = GTpairConditions.length;
						
						
						//对每个进行处理放入list
						List<QueryRecord> recordList = new ArrayList<QueryRecord>();
						int count = 0;	//记录总数
						for(int i=0;i<GTpairNum;i++){
							Expression e = Expression.NONE;	//总条件表达式
							GTpairCondition eachCondition = GTpairConditions[i];
							if(eachCondition == null){
								continue;
							}
							String QSGTSBID = eachCondition.getQSGTSBID();
							String QSGTOid = SBIDtoZZGTOidMap.get(QSGTSBID);
							//若给的sbid错误(即oid为null)
							if(QSGTOid == null){
								continue;
							}
							String ZZGTSBID = eachCondition.getZZGTSBID();
							String ZZGTOid = SBIDtoZZGTOidMap.get(ZZGTSBID);
							if(ZZGTOid == null){
								continue;
							}
							Expression exp1 = Expression.NONE;
							Expression exp2 = Expression.NONE;
							exp1 = builder.equal(builder.getRoot().get("QSGT", String.class), QSGTOid);
							exp2 = builder.equal(builder.getRoot().get("ZZGT", String.class), ZZGTOid);
							e = builder.and(exp1, exp2);	//每一组对应的条件
						
						//得到返回结果
						ResultSet set = null;
						try {
							//添加查询时间
							long start = System.currentTimeMillis();
							set = model.search(queryField, e,null);
							long end = System.currentTimeMillis();
							System.out.println("通过杆塔查导线段用时:"+(end-start)+"ms");
						} catch (Exception e1) {
							System.out.println("数据库查询出错");
							resp.setCode(ReturnCode.SQLERROR);
							return resp;
						}
						
						
						// 得到结果
						Iterator<Device> it = set.resultList();
						// 返回记录数量
						List<Device> devList = new ArrayList<Device>();
						if (it != null) {
							while (it.hasNext()) {
								devList.add(it.next());
								count = count + 1;
							}
						}
						// 返回查询记录
						for (int j = 0; j < devList.size(); j++) {
							QueryRecord record = new QueryRecord();
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
								record.setGeom(geom);
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
							}	//对字段的处理循环结束
							//在这里加上需判断的sbmc
							if(hasSBMC){
							QueryField fields1 = new QueryField();
							fields1.setFieldName("SBMC");
							fields1.setFieldAlias("设备名称");
							if(BDZReflectFlag){
								fields1.setFieldValue(BDZReflectMap.get(BDZReflectId));
							}else{
								fields1.setFieldValue(String.valueOf(a.getValue("SBMC".toUpperCase())));
							}
							fieldsList.add(fields1);
							}
							
							//添加color字段
							QueryField field = new QueryField();
							field.setFieldName("COLOR");
							field.setFieldValue(eachCondition.getColor());
							field.setFieldAlias("导线段颜色");
							fieldsList.add(field);
							
							QueryField[] fields = new QueryField[fieldsList.size()];
							for(int k = 0;k<fieldsList.size();k++){
								fields[k] = fieldsList.get(k);
							}
							
							
							record.setFields(fields);
							
//							 //返回符号信息
//							 SymbolAdapter symAdp = QueryServiceActivator.symboladapter;
//							 SymbolDef symDef = null;
//							 try {
//							 symDef = symAdp.search(devList.get(j));
//							 } catch (ModelException e1) {
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
							recordList.add(record);
						}//每条记录查询结果操作完毕
						}//每次查询结束
						QueryRecord[] records = new QueryRecord[recordList.size()];
						records = recordList.toArray(records);
						// 操作每次的返回查询结果
						results[0] = new QueryResult();
						results[0].setRecords(records);
						results[0].setCount(count);
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
						results[0].setPsrDef(psrDef);
				resp.setResult(results);
				resp.setCode(ReturnCode.SUCCESS);
				return resp;
			}
		}


	
