package nari.SpatialAnalysisService.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nari.Geometry.Coordinate;
import nari.Geometry.Geometry;
import nari.Geometry.GeometryType;
import nari.Geometry.Polygon;
import nari.SpatialAnalysisService.SpatialAnalysisServiceActivator;
import nari.model.bean.SubClassDef;
import nari.model.device.Device;
import nari.model.device.DeviceModel;
import nari.model.device.ModelService;
import nari.model.device.ResultSet;
import nari.model.device.SpatialDevice;
import nari.model.device.TopoDevice;
import nari.model.device.filter.CriteriaBuilder;
import nari.model.device.filter.Expression;
import nari.model.geometry.GeometryService;
import nari.parameter.SpatialAnalysisService.SpatialAlalysis.SpatialBufferRequest;
import nari.parameter.SpatialAnalysisService.SpatialAlalysis.SpatialBufferResponse;
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

public class SpatialBufferHandler {
	public SpatialBufferResponse spatialBuffer(SpatialBufferRequest request) {
		SpatialBufferResponse resp = new SpatialBufferResponse();
		// 空間信息
		GeometryPair geomPair = request.getGeom();
		// 缓冲半径
		double radius = request.getRadius();
		// 創建一個buffer
		GeometryService geoService = SpatialAnalysisServiceActivator.geoService;
		Geometry buffer = geoService.createPolygon(geomPair.getCoords())
				.getBoundary().buffer(radius);
		// 得到缓冲的polygon
		Polygon poly = geoService.createPolygon(buffer.getCoordinates());
		// 查询条件
		TypeCondition[] cond = request.getConds();
		int condLength = cond.length;
		QueryResult[] results = new QueryResult[condLength];
		for (int i = 0; i < condLength; i++) {
			String classId = cond[i].getPsrType();
			ModelService ms = SpatialAnalysisServiceActivator.modelService;
			// 是否查询版本表
			DeviceModel model = null;
			try {
				model = ms.fromClass(classId, true);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			CriteriaBuilder builder = model.getEntryManager()
					.getCriteriaBuilder();
			// 条件组合
			Pair[] pairs = cond[i].getPairs();
			int pairsLength = pairs.length;
			Expression[] exp = new Expression[pairsLength];
			for (int j = 0; j < pairsLength; j++) {
				String key = pairs[j].getKey();
				String value = pairs[j].getValue();
				Operator op = pairs[j].getOp();
				switch (op) {
				case EQ:
					exp[j] = builder.equal(
							builder.getRoot().get(key, String.class), value);
					break;
				case LT:
					exp[j] = builder.lessThan(
							builder.getRoot().get(key, String.class), value);
					break;
				case MT:
					exp[j] = builder.greaterThan(
							builder.getRoot().get(key, String.class), value);
					break;
				case LIKE:
					exp[j] = builder.like(
							builder.getRoot().get(key, String.class), value);
					break;
				}
			}

			Link link = cond[i].getLink();
			// 得到条件
			Expression e = Expression.NONE;

			switch (link) {
			case AND:
				e = builder.and(exp);
				break;
			case OR:
				e = builder.or(exp);
				break;
			}
			String[] returnField = cond[i].getReturnField();
			ResultSet set = null;
			try {
				set = model.spatialQuery(returnField, e,null, poly);
			} catch (Exception e1) {
				e1.printStackTrace();
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
			// 返回查询记录
			QueryRecord[] records = new QueryRecord[count];
			for (int j = 0; j < count; j++) {
				// 是否查拓扑信息
				if (true) {
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
				if (true) {
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
				// 返回字段信息集合
				int returnFieldlength = returnField.length;
				QueryField[] fields = new QueryField[returnFieldlength];
				for (int k = 0; k < returnFieldlength; k++) {
					fields[k] = new QueryField();
					fields[k].setFieldName(returnField[k]);
					Device a = devList.get(j);
					if (a.getValue(returnField[k]) != null) {
						fields[k].setFieldValue(String.valueOf((a
								.getValue(returnField[k]))));
					}
					fields[k].setFieldAlias(model.getFieldDef()
							.find(returnField[k]).getFieldAlias());
				}
				records[j].setFields(fields);
				// //返回符号信息
				// SymbolAdapter symAdp = QueryServiceActivator.symboladapter;
				// SymbolDef symDef = null;
				// try {
				// symDef = symAdp.search(devList.get(j));
				// } catch (ModelException e1) {
				// e1.printStackTrace();
				// }
				// SymbolPair symbol = new SymbolPair();
				// symbol.setModelId(symDef.getModelId());
				// symbol.setSymbol(symDef.getSymbolId());
				// records[j].setSymbol(symbol);
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
}
