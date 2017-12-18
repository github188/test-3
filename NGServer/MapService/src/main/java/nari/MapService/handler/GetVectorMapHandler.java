package nari.MapService.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.bson.BsonValue;

import nari.Geometry.Coordinate;
import nari.Geometry.Geometry;
import nari.Geometry.GeometryCollection;
import nari.Geometry.GeometryType;
import nari.Geometry.Polygon;
import nari.Geometry.Polyline;
import nari.Logger.LoggerManager;
import nari.MapService.MapServiceActivator;
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
import nari.parameter.MapService.GetVectorMap.GetVectorMapRequest;
import nari.parameter.MapService.GetVectorMap.GetVectorMapResponse;
import nari.parameter.bean.GeometryPair;
import nari.parameter.bean.PSRCondition;
import nari.parameter.bean.PSRDef;
import nari.parameter.bean.QueryField;
import nari.parameter.bean.QueryRecord;
import nari.parameter.bean.QueryResult;
import nari.parameter.bean.SubPSRCondition;
import nari.parameter.bean.SubPSRDef;
import nari.parameter.bean.SymbolPair;
import nari.parameter.code.ReturnCode;

/**
 * 实时加载多个设备信息
 */
public class GetVectorMapHandler {
	
	private nari.Logger.Logger logger = LoggerManager.getLogger(this.getClass());
	
	GeometryService geoService = MapServiceActivator.geoService;
	DeviceModel model = DeviceModel.NONE;
	SymbolAdapter symAdp = MapServiceActivator.symboladapter;
	ModelService ms = MapServiceActivator.modelService;
	
	public GetVectorMapResponse getVectorMap(GetVectorMapRequest request) {
		GetVectorMapResponse resp = new GetVectorMapResponse();
		double[] coords0 = request.getBbox();
		// 创建矩形polygon
		Polygon polygon = geoService.createPolygon(coords0);
		// 判断给的是否能创建成polygon
		if (polygon == null) {
			resp.setCode(ReturnCode.MISSTYPE);
			return resp;
		}

		//得到设备查询条件(肯定不是null)
		PSRCondition[] classCondition = request.getClassCondition();
		
		boolean isQueryGeometry = true;
		// 分别查询对应的
		QueryResult[] results = new QueryResult[classCondition.length];

		for (int i = 0; i < classCondition.length; i++) {
			// 得到所需查询设备类型(电站,站外物理杆，运行杆，导线段)
			String classId = classCondition[i].getClassId();
			
			try {
				model = ms.fromClass(classId, false);
			} catch (Exception e) {
				logger.info("模型创建时出错");
				resp.setCode(ReturnCode.BUILDMODEL);
				return resp;
			}
			
			Boolean bHasDydj = true;
			if(classId.equals("361000") || classId.equals("150000")){
				bHasDydj = false;
			}

			// 查询条件(根据设备子类型和电压等级)
			Expression exp = Expression.NONE;
			Expression dydjConExp = Expression.NONE;	//每个sbzlx下的dydj条件
			Expression sbzlxConExp = Expression.NONE;	//每个sbzlx下值的条件
			
			SubPSRCondition[] modelCondition = classCondition[i].getModelCondition();
			if(modelCondition != null && modelCondition.length != 0){
				Expression[] eachSbzlxConExp = new Expression[modelCondition.length];	//每个sbzlx下所有的条件
				
				CriteriaBuilder builder = model.getEntryManager().getCriteriaBuilder();
				//每个子类型对应一种DYDJ条件
				for(int j = 0; j < modelCondition.length; j++){
					eachSbzlxConExp[j] = Expression.NONE;
					String sbzlx = modelCondition[j].getSbzlx();
					if(sbzlx != null && sbzlx != ""){
						sbzlxConExp = builder.equal(builder.getRoot().get("sbzlx", String.class), sbzlx);
					}
					String[] DYDJ = modelCondition[j].getDYDJ();
					if (DYDJ != null && DYDJ.length !=0) {
						dydjConExp = builder.in(builder.getRoot().get("dydj", String.class),
								DYDJ);
					}
					
					if(bHasDydj == false){
						dydjConExp = Expression.NONE;
					}
					
					if(dydjConExp == Expression.NONE){
						eachSbzlxConExp[j] = sbzlxConExp;
					}else if(sbzlxConExp == Expression.NONE){
						eachSbzlxConExp[j] = dydjConExp;
					}
					else{
						eachSbzlxConExp[j] = builder.and(sbzlxConExp, dydjConExp);
					}
				}
				//总条件
				exp = builder.or(eachSbzlxConExp);
			}
			ResultSet set = ResultSet.NONE;
			
			String[] queryFieldName = new String[] { "OID","SBZLX","SHAPE","SBMC","FHDX","FHJD","DYDJ",
					"SFBZ","BZDX","BZYS", "BZFW", "SBID",
                    "PLFS","DHZS","BZXSZD","BZNR","X","Y"};
			
			queryFieldName = ModifyReturnFields(queryFieldName, classId);
			
			try {
				// 添加查询时间
				long start = System.currentTimeMillis();
				set = model.spatialQuery(queryFieldName, exp,null, polygon);
				long end = System.currentTimeMillis();
				logger.info("每次查询用时:" + (end - start) + "ms");
			} catch (Exception excep) {
				logger.info("数据库查询出错");
				resp.setCode(ReturnCode.SQLERROR);
				return resp;
			}
			Iterator<Device> it = set.resultList();
			// 返回记录数量
			List<Device> devList = new ArrayList<Device>();
			// List<TopoDevice> topoDevList = new ArrayList<TopoDevice>();
			Device dev = null;
			if (it != null) {
				while (it.hasNext()) {
					dev = it.next();
					if (dev == null || dev == Device.NONE) {
						continue;
					}
					devList.add(dev);
				}
			}

			int count = devList.size();

			// 返回查询记录
			QueryRecord[] records = new QueryRecord[count];
			for (int j = 0; j < count; j++) {
				records[j] = new QueryRecord();
				Device eachdev = devList.get(j);
				
				// 是否查拓扑信息
				// topoDevList.add(j, eachdev.asTopoDevice());
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
					SpatialDevice spaDev = eachdev.asSpatialDevice();
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

				// 返回字段信息集合(OID,SBMC,symbol(SBZLX),DYDJ)
				List<QueryField> fieldsList = new ArrayList<QueryField>();
				
				//细节处理
				String sbzlx = String.valueOf(eachdev.getValue("SBZLX"));

				boolean isDxType = (
						sbzlx.equals("10100000") || 
						sbzlx.equals("20100000") || 
						sbzlx.equals("14000000") || 
						sbzlx.equals("13000000") );
				
				boolean hasBZFW = false;
				Iterator<FieldDetail> fdIt = eachdev.keyIterator();
				while (fdIt.hasNext()){
					if(fdIt.next().getFieldName().equalsIgnoreCase("BZFW")){
						hasBZFW = true;
						break;
					}
				}
				
				if( !isDxType && hasBZFW && String.valueOf(eachdev.getValue("BZFW")).equalsIgnoreCase("null") ){
					String queryFields[] = new String[]{"OID", "SBZLX","SBID","SBMC","FHDX","FHJD","DYDJ","SFBZ"};
					
					queryFields = ModifyReturnFields(queryFields, classId);
					
					String value = "";
					for(int k = 0; k < queryFields.length; ++ k){
						String fieldName = queryFields[k];
						value = String.valueOf(eachdev.getValue(fieldName));
						QueryField fields = new QueryField();
						fields.setFieldName(fieldName);
						fields.setFieldValue(value);
						fieldsList.add(fields);
					}
				}
				else if( isDxType && hasBZFW && String.valueOf(eachdev.getValue("BZFW")).equalsIgnoreCase("null") ){
					String queryFields[] = new String[]{"OID", "SBZLX","SBID","SBMC","DYDJ","SFBZ"};
					String value = "";
					for(int k = 0; k < queryFields.length; ++ k){
						String fieldName = queryFields[k];
						value = String.valueOf(eachdev.getValue(fieldName));
						QueryField fields = new QueryField();
						fields.setFieldName(fieldName);
						fields.setFieldValue(value);
						fieldsList.add(fields);
					}
				}
				else{
					int returnFieldlength = queryFieldName.length;
					
					for (int k = 0; k < returnFieldlength; k++) {
						QueryField fields = new QueryField();
						fields.setFieldName(queryFieldName[k]);

						if (eachdev.getValue(queryFieldName[k].toUpperCase()) != null) {
							String value = String.valueOf((eachdev.getValue(queryFieldName[k].toUpperCase())));
							fields.setFieldValue(value);
						}
						fieldsList.add(fields);
					}
				}
				QueryField[] fields = new QueryField[fieldsList.size()];
				fields = fieldsList.toArray(fields);
				records[j].setFields(fields);

				// 返回符号信息
				SymbolDef symDef = null;
				try {
					symDef = symAdp.search(eachdev);
				} catch (Exception excep) {
					logger.info("模型创建时出错");
					resp.setCode(ReturnCode.BUILDMODEL);
					return resp;
				}
				if (symDef != null) {
					SymbolPair symbol = new SymbolPair();
//					symbol.setModelId(symDef.getModelId());
					symbol.setSymbolValue(symDef.getSymbolValue());
					symbol.setSymbolId(symDef.getSymbolId());
//					symbol.setDevtypeId(symDef.getDevTypeId());
					records[j].setSymbol(symbol);
				}
			}
			// 操作每次的返回查询结果
			results[i] = new QueryResult();
			results[i].setRecords(records);
			results[i].setCount(count);
			PSRDef psrDef = new PSRDef();
			psrDef.setPsrName(model.getClassDef().getClassAlias());
			psrDef.setName(model.getClassDef().getClassId());
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
		}	//i循环结束

		resp.setResult(results);
		resp.setCode(ReturnCode.SUCCESS);
		resp.setRequestExtend(request.getRequestExtend());
		return resp;
	}

	/**
	 * 调整返回字段
	 * */
	private String[] ModifyReturnFields(String[] queryFieldName, String classId){
		Boolean bHasDydj = true;
		if(classId.equals("361000") || classId.equals("150000")){
			bHasDydj = false;
		}
		
		Boolean bZnyc = false;
		int classIdInt = Integer.parseInt(classId);
		if(classIdInt > 300000 && classIdInt <= 399999){    //不包含电站
			bZnyc = true;
		}
		
		List<String> arrFields = new LinkedList<String>();
		for(int k = 0; k < queryFieldName.length; k++){
			arrFields.add(queryFieldName[k]);
		}
		
		if(bHasDydj == false){
			arrFields.remove("DYDJ");
		}
		if(bZnyc == true){  //如果是站内设备
			arrFields.add("SSDZ");
		}
		
		queryFieldName = new String[arrFields.size()];
		queryFieldName = arrFields.toArray(queryFieldName);
		
		return queryFieldName;
	}
   
	
}
