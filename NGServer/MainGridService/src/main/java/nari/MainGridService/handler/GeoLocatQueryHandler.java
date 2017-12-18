package nari.MainGridService.handler;

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
import nari.Geometry.Point;
import nari.Geometry.Polygon;
import nari.Geometry.Polyline;
import nari.MainGridService.MainGridServiceActivator;
import nari.model.TableName;
import nari.model.bean.FieldDetail;
import nari.model.bean.SubClassDef;
import nari.model.device.Device;
import nari.model.device.DeviceModel;
import nari.model.device.ModelService;
import nari.model.device.ResultSet;
import nari.model.device.SpatialDevice;
import nari.model.geometry.GeometryService;
import nari.parameter.MainGridService.geoLocatQuery.GeoLocatQueryRequest;
import nari.parameter.MainGridService.geoLocatQuery.GeoLocatQueryResponse;
import nari.parameter.bean.GeometryPair;
import nari.parameter.bean.PSRDef;
import nari.parameter.bean.QueryField;
import nari.parameter.bean.QueryRecord;
import nari.parameter.bean.QueryResult;
import nari.parameter.bean.SubPSRDef;
import nari.parameter.code.ReturnCode;

public class GeoLocatQueryHandler {
	
	GeometryService geoService = MainGridServiceActivator.geoService;
	ModelService ms = MainGridServiceActivator.modelService;
	DbAdaptor db = MainGridServiceActivator.dbAdaptor;
	
	public GeoLocatQueryResponse geoLocatQuery(GeoLocatQueryRequest request){
		GeoLocatQueryResponse resp = new GeoLocatQueryResponse();
		//寰楀埌鍧愭爣鐐�
		double x = request.getXcoordnates();
		double y = request.getYcoordnates();
		//鐢熸垚polygon
		
		Point point = geoService.createPoint(x, y);
		Geometry geometry = point.buffer(0.00008);
		Polygon polygon = geoService.createPolygon(geometry.getCoordinates());
		//姣忕璁惧绫诲瀷瀵瑰簲鏌ヨ
		String[] classId = request.getClassId();
		if(classId == null ||classId.length == 0 ){
			classId = new String[]{"300000","103000","102000","101000"};
		}
				int classIdLength = classId.length;
				QueryResult[] results = new QueryResult[classIdLength];
				for (int i = 0; i < classIdLength; i++) {
					if (classId[i] == null||classId[i].equals("")) {
						resp.setCode(ReturnCode.NULL);
						return resp;
					} else {
						
						DeviceModel model = null;
						try {
							model = ms.fromClass(classId[i], false);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						
						// 寰楀埌杩斿洖鐨勫瓧娈�閫氳繃浠栧垽鏂槸閮借繘琛岀┖闂存煡璇�
						boolean isQueryGeometry = false;
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
						String[] queryField = new String[fieldCount];
						for (int j = 0; j < fieldCount; j++) {
							queryField[j] = fieldList.get(j).getFieldName();
							if ("shape".equalsIgnoreCase(queryField[j])) {
								isQueryGeometry = true;
							}
						}
						
//						CriteriaBuilder builder = model.getEntryManager().getCriteriaBuilder();

						//涓洪伩鍏嶆暟鎹噺澶ぇ锛屾坊鍔犳潯浠秗ownum<60;
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
							//娣诲姞鏌ヨ鏃堕棿
							long start = System.currentTimeMillis();
							set = model.spatialQuery(queryField, null,null,polygon);
							long end = System.currentTimeMillis();
							System.out.println("姣忔鏌ヨ鐢ㄦ椂:"+(end-start)+"ms");
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						
						
						// 寰楀埌缁撴灉
						Iterator<Device> it = set.resultList();
						// 杩斿洖璁板綍鏁伴噺
						int count = 0;
						List<Device> devList = new ArrayList<Device>();
//						List<TopoDevice> topoDevList = new ArrayList<TopoDevice>();
//						List<SpatialDevice> spatialDevList = new ArrayList<SpatialDevice>();
						if (it != null) {
							while (it.hasNext()) {
								devList.add(count, it.next());
								if (devList.get(count) == null) {
									break;
								}
								count = count + 1;
							}
						}
						// 杩斿洖鏌ヨ璁板綍
						QueryRecord[] records = new QueryRecord[count];
						for (int j = 0; j < count; j++) {
							records[j] = new QueryRecord();
//							// 鏄惁鏌ユ嫇鎵戜俊鎭�
//							if (request.isQueryTopo()) {
//								topoDevList.add(j, devList.get(j).asTopoDevice());
//								// 杩斿洖鎷撴墤淇℃伅
//								TopoPair topo = new TopoPair();
//								// 杩斿洖绔瓙涓暟
//								int nodeNum = topoDevList.get(j).getTopo().nodeCount();
//								// 绔瓙鍙�
//								long[] nodes = topoDevList.get(j).getTopo().nodes();
//								topo.setNodeNum(nodeNum);
//								topo.setNodes(nodes);
//								records[j].setTopo(topo);
//							}
							// 鏄惁鏌ヨ寰楀埌绌洪棿淇℃伅
							if (isQueryGeometry) {
								SpatialDevice spaDev = devList.get(j).asSpatialDevice();
								// 杩斿洖绌洪棿瀵硅薄淇℃伅
								GeometryPair geom = new GeometryPair();
								// 绌洪棿瀵硅薄绫诲瀷 1 鐐�2 绾�3 闈�
								String geometryType = "";
								GeometryType geomType = spaDev.getGeometry().getGeometry()
										.getGeometryType();
								geometryType = geomType.toString();
								//瀵圭嚎鐨勫鐞�
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
								//瀵瑰绾跨壒娈婂鐞�
								if(("MULTIPOLYLINE").equalsIgnoreCase(geometryType)){
									geometryType = "MULTILINESTRING";
										GeometryCollection multipolyLine = (GeometryCollection)spaDev.getGeometry().getGeometry();
										int lineNum = multipolyLine.getNumGeometry();
										int[] startDouble = new int[lineNum];
										startDouble[0] = 1;
										for(int k=0;k<lineNum-1;k++){
											Geometry polyLine = multipolyLine.getGeometry(k);	//鐩寸嚎鍨�
											startDouble[k+1] = (polyLine.getCoordinates().length)*2+startDouble[k];
										}
										geom.setOther(lineNum);
										geom.setStartDouble(startDouble);
									}
								
								Coordinate[] coordnates = spaDev.getGeometry()
										.getGeometry().getCoordinates();
								int natesnum = coordnates.length;
								// 鍧愭爣鏁扮粍 [x1,y1,x2,y2,x3,y3]
								double[] coords = new double[2 * natesnum];
								for (int k = 0; k < natesnum; k++) {
									coords[2 * k] = coordnates[k].getX();
									coords[2 * k + 1] = coordnates[k].getY();
								}
								geom.setGeometryType(geometryType);
								geom.setCoords(coords);
								records[j].setGeom(geom);
							}
							
							
							// 杩斿洖瀛楁淇℃伅闆嗗悎(OID,SBMC,SHAPE,symbol,DYDJ)
							int queryFieldlength = queryField.length;
							List<QueryField> fieldsList = new ArrayList<QueryField>();
							for (int k = 0; k < queryFieldlength; k++) {
								Device a = devList.get(j);
								QueryField fields = new QueryField();
								fields.setFieldName(queryField[k]);
								
								if (a.getValue(queryField[k]) != null) {
									String fieldValue = String.valueOf(a.getValue(queryField[k]));
									//璁惧鍚嶇О(SBMC)浼樺寲(鍘绘帀涓�簺鐗规畩绗﹀彿)
									if(queryField[k].equals("SBMC")){
										fieldValue = modifySBMC(fieldValue);
									}
									
									fields.setFieldValue(fieldValue);
								}
								fields.setFieldAlias(model.getFieldDef()
										.find(queryField[k]).getFieldAlias());
								fieldsList.add(fields);
								
								//瀵圭數鍘嬬瓑绾ц浆涔�鐢靛帇绛夌骇鍙樻垚鐢靛帇鍊�(澶氬姞涓�釜杩斿洖瀛楁)
								if(queryField[k].equals("DYDJ")){
									//鏍规嵁鐢靛帇绛夌骇鏌ヨ琛╟onf_codedefinition寰楀埌鐩稿簲瀹為檯鍊�
									if(a.getValue(queryField[k]) == null){
										System.out.println(k);
										continue;
									}
									String dydj = String.valueOf((a.getValue(queryField[k])));
									String dyz = "";
									
									if(dydj.equalsIgnoreCase("0")){
										dyz = "0";
									}else{
										
										String sql = "select * from " + TableName.CONF_CODEDEFINITION + " where codeid = 10401 and codedefid = "+dydj;
										Map<String,Object> codeFieldMap = null;
										try {
											codeFieldMap = db.findMap(sql);
										} catch (SQLException e1) {
											e1.printStackTrace();
										}
										
										if(codeFieldMap.get("codename") == null){
											dyz = "0";
										}
										else{
											dyz = String.valueOf(codeFieldMap.get("codename"));
										}
											QueryField fields1 = new QueryField();
											fields1.setFieldName("DYZ");
											fields1.setFieldValue(dyz);
											fields1.setFieldAlias("鐢靛帇鍊�);
											fieldsList.add(fields1);
									}
								}
							}
							QueryField[] fields = new QueryField[fieldsList.size()];
							for(int k = 0;k<fieldsList.size();k++){
								fields[k] = fieldsList.get(k);
							}
							records[j].setFields(fields);
							
//							 //杩斿洖绗﹀彿淇℃伅
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
						}
						// 鎿嶄綔姣忔鐨勮繑鍥炴煡璇㈢粨鏋�
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
				}
				resp.setResult(results);
				resp.setCode(ReturnCode.SUCCESS);
				return resp;
			}
	
	//灏嗚澶囧悕绉板寘鍚壒娈婄鍙风殑鍘绘帀
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
						value.contains("锝�)){
					
					value = value.replaceAll(":", "锛�);
					value = value.replaceAll("\\\\", "");
					value = value.replaceAll("'", "");
					value = value.replaceAll("\"", "");
					value = value.replaceAll("\t","");
					value = value.replaceAll("\r","");
					value = value.replaceAll("\f","");
					value = value.replaceAll("\n","");
					value = value.replaceAll("锝�, "_");
				}
				return value;
			}
		}
