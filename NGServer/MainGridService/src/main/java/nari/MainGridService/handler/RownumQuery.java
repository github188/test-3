package nari.MainGridService.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nari.Dao.interfaces.DbAdaptor;
import nari.Geometry.Coordinate;
import nari.Geometry.GeometryType;
import nari.MainGridService.MainGridServiceActivator;
import nari.model.bean.SubClassDef;
import nari.model.device.Device;
import nari.model.device.DeviceModel;
import nari.model.device.MapResultSet;
import nari.model.device.ModelService;
import nari.model.device.ResultSet;
import nari.model.device.SpatialDevice;
import nari.model.device.TopoDevice;
import nari.model.device.filter.CriteriaBuilder;
import nari.model.device.filter.Expression;
import nari.parameter.QueryService.ConditionQuery.ConditionQueryRequest;
import nari.parameter.QueryService.ConditionQuery.ConditionQueryResponse;
import nari.parameter.bean.GeometryPair;
import nari.parameter.bean.Link;
import nari.parameter.bean.Operator;
import nari.parameter.bean.PSRDef;
import nari.parameter.bean.Pair;
import nari.parameter.bean.QueryField;
import nari.parameter.bean.QueryRecord;
import nari.parameter.bean.QueryResult;
import nari.parameter.bean.SubPSRDef;
import nari.parameter.bean.TopoPair;
import nari.parameter.bean.TypeCondition;
import nari.parameter.code.ReturnCode;

public class RownumQuery {
	ModelService ms = MainGridServiceActivator.modelService;
	DbAdaptor db = MainGridServiceActivator.dbAdaptor;
	
	public ConditionQueryResponse rownumQuery(ConditionQueryRequest request,int rownumBegin,int rownumEnd) {
		ConditionQueryResponse resp = new ConditionQueryResponse();
		// 查询条件
		TypeCondition[] cond = request.getCondition();
		int condLength = cond.length;
		QueryResult[] results = new QueryResult[condLength];
		for (int i = 0; i < condLength; i++) {
			String classId = cond[i].getPsrType();
			// //前面判断了,这里可不判断
			// if (classId == null) {
			// resp.setCode(ReturnCode.NULL);
			// return resp;
			// }
			
			// 是否查询版本表
			DeviceModel model = null;
			try {
				model = ms.fromClass(classId, request.isVersion());
			} catch (Exception e1) {
				System.out.println("模型创建时出错");
				resp.setCode(ReturnCode.BUILDMODEL);
				return resp;
			}
			CriteriaBuilder builder = model.getEntryManager()
					.getCriteriaBuilder();
			// 条件组合
			Pair[] pairs = cond[i].getPairs();
			Expression e = Expression.NONE;

			// 若条件组合不为空
			if (pairs != null && pairs.length != 0) {
				int pairsLength = pairs.length;
				Expression[] exp = new Expression[pairsLength];
				for (int j = 0; j < pairsLength; j++) {
					// 每一个条件拼成一个完整条件
					String key = pairs[j].getKey();
					String value = pairs[j].getValue();
					Operator op = pairs[j].getOp();
					// 接口初始化
					exp[j] = Expression.NONE;
					switch (op) {
					case EQ:
						exp[j] = builder
								.equal(builder.getRoot().get(key, String.class),
										value);
						break;
					case LT:
						exp[j] = builder
								.lessThan(
										builder.getRoot()
												.get(key, String.class), value);
						break;
					case MT:
						exp[j] = builder
								.greaterThan(
										builder.getRoot()
												.get(key, String.class), value);
						break;
					case LIKE:
						exp[j] = builder
								.like(builder.getRoot().get(key, String.class),
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

			// 为避免数据量太大，添加条件rownum<60;
//			if(e != null){
//			e = builder.and(
//					e,
//					builder.lessThan(
//							builder.getRoot().get("rownum", String.class), 60000));
//			}else{
//				e = builder.lessThan(
//						builder.getRoot().get("rownum", String.class), 60000);
//			}

			// 查询字段的设置
			String[] queryField = cond[i].getReturnField();
			if (queryField == null || queryField.length == 0) {
				// 默认查询字段(OID,SBZLX,SBMC)
				queryField = new String[] { "OID", "SBZLX", "SBMC" };
			}
			ResultSet set = null;
			try {
				// 添加查询时间
				String sql = "select * from (select b.*,rownum r from (select * from T_TX_ZNYC_DZ a where (a.shape.SDO_GTYPE = 3 and a.dydj in (85,84,83,82,37,36,35) and a.sbzlx = 30000000) order by dydj) b) where r>=? and r<=?";
//				String sql = "select * from (select b.*,rownum r from (select * from T_TX_ZWYC_DXD a where (a.shape.SDO_GTYPE = 2 and a.dydj in (85,84,83,82,37,36,35)) order by dydj) b) where r>=? and r<=?";
//				String sql = "select * from (select b.*,rownum r from (select * from T_TX_ZWYC_WLG a where (a.shape.SDO_GTYPE = 1) order by dydj) b) where r>=? and r<=?";
//				String sql = "select * from (select b.*,rownum r from (select * from T_TX_ZWYC_YXGT a where (a.shape.SDO_GTYPE = 1 and a.dydj in (85,84,83,82,37,36,35)) order by dydj) b) where r>=? and r<=?";

				Object[] params = new Object[]{rownumBegin,rownumEnd};
				long start = System.currentTimeMillis();
				List<Map<String,Object>> maps = db.findAllMap(sql, params);
				long end = System.currentTimeMillis();
				System.out.println("每次查询用时:" + (end - start) + "ms");
				set = new MapResultSet(maps,model.getFieldDef(),model.getClassDef(),model.getSubClassDef());
				
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
			List<TopoDevice> topoDevList = new ArrayList<TopoDevice>();
			List<SpatialDevice> spatialDevList = new ArrayList<SpatialDevice>();
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
			// 对每条数据做处理
			for (int j = 0; j < count; j++) {
				records[j] = new QueryRecord();
				// 是否查拓扑信息
				if (request.isQueryTopo()) {
					topoDevList.add(j, devList.get(j).asTopoDevice());
					// 返回拓扑信息
					TopoPair topo = new TopoPair();
					// 返回端子个数
					int nodeNum = topoDevList.get(j).getTopo().nodeCount();
					// 端子号
					long[] nodes = topoDevList.get(j).getTopo().nodes();
					topo.setNodeNum(nodeNum);
					topo.setNodes(nodes);
					records[j].setTopo(topo);
				}
				// 是否查询得到空间信息
				if (request.isQueryGeometry()) {
					spatialDevList.add(j, devList.get(j).asSpatialDevice());
					// 返回空间对象信息
					GeometryPair geom = new GeometryPair();
					// 空间对象类型 1 点 2 线 3 面
					String geometryType = "";
					GeometryType geomType = spatialDevList.get(j).getGeometry()
							.getGeometry().getGeometryType();
					geometryType = geomType.toString();
					Coordinate[] coordnates = spatialDevList.get(j)
							.getGeometry().getGeometry().getCoordinates();
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

				// 返回字段信息集合(OID,SBMC,SHAPE,symbol(SBZLX),DYDJ)
				String[] returnField = queryField;
				int returnFieldlength = returnField.length;
				// 一条记录中字段的集合List
				List<QueryField> fieldsList = new ArrayList<QueryField>();
				// 每条记录中对每个字段进行操作
				for (int k = 0; k < returnFieldlength; k++) {
					Device a = devList.get(j);
					QueryField fields = new QueryField();
					fields.setFieldName(returnField[k]);

					if (a.getValue(returnField[k]) != null) {
						fields.setFieldValue(String.valueOf((a
								.getValue(returnField[k]))));
					}
					fields.setFieldAlias(model.getFieldDef()
							.find(returnField[k]).getFieldAlias());
					fieldsList.add(fields);

//					// 对电压等级转义(电压等级变成电压值)(多加一个返回字段)
//					if (returnField[k].equals("DYDJ")) {
//						// 根据电压等级查询表conf_codedefinition得到相应实际值
//						if (a.getValue(returnField[k]) == null) {
//							System.out.println(k);
//							continue;
//						}
//						String dydj = String.valueOf((a
//								.getValue(returnField[k])));
//						String dyz = "";
//
//						if (dydj.equalsIgnoreCase("0")) {
//							dyz = "0";
//						} else {
//							DbAdaptor db = QueryServiceActivator.dbAdaptor;
//							String sql = "select * from conf_codedefinition where codeid = 10401 and codedefid = "
//									+ dydj;
//							Map<String, Object> codeFieldMap = null;
//							try {
//								codeFieldMap = db.findMap(sql);
//							} catch (SQLException e1) {
//								System.out.println("数据库查询出错");
//								resp.setCode(ReturnCode.SQLERROR);
//								return resp;
//							}
//
//							if (codeFieldMap.get("codename") == null) {
//								dyz = "0";
//							} else {
//								dyz = String.valueOf(codeFieldMap
//										.get("codename"));
//							}
//							QueryField fields1 = new QueryField();
//							fields1.setFieldName("DYZ");
//							fields1.setFieldValue(dyz);
//							fields1.setFieldAlias("电压值");
//							fieldsList.add(fields1);
//						}
//					}
				}
				QueryField[] fields = new QueryField[fieldsList.size()];
				for (int k = 0; k < fieldsList.size(); k++) {
					fields[k] = fieldsList.get(k);
				}
				records[j].setFields(fields);

//				// 返回符号信息
//				SymbolAdapter symAdp = QueryServiceActivator.symboladapter;
//				SymbolDef symDef = null;
//				try {
//					symDef = symAdp.search(devList.get(j));
//				} catch (ModelException e1) {
//					System.out.println("模型创建时出错");
//					resp.setCode(ReturnCode.BUILDMODEL);
//					return resp;
//				}
//				if (symDef != null) {
//					SymbolPair symbol = new SymbolPair();
//					symbol.setModelId(symDef.getModelId());
//					symbol.setSymbolValue(symDef.getSymbolValue());
//					symbol.setSymbolId(symDef.getSymbolId());
//					symbol.setDevtypeId(symDef.getDevTypeId());
//					records[j].setSymbol(symbol);
//				}
			}
			// 返回查询结果
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
}
