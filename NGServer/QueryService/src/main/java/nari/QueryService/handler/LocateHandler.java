package nari.QueryService.handler;

import nari.parameter.QueryService.SpatialLocate.LocateRequest;
import nari.parameter.QueryService.SpatialLocate.LocateResponse;

public class LocateHandler {
	public LocateResponse locate(LocateRequest request) {
		LocateResponse resp = new LocateResponse();
//		ModelService ms = QueryServiceActivator.modelService;
//		String classId = request.getPsrType();
//		DeviceModel model = null;
//		try {
//			model = ms.fromClass(classId, true);
//		} catch (ModelException e) {
//			e.printStackTrace();
//		}
//		// 得到返回的字段(通过他判断是都进行空间查询)
//		boolean isQueryGeometry = false;
//		Iterator<FieldDetail> fieldIt = model.getFieldDef().details();
//		List<FieldDetail> fieldList = new ArrayList<FieldDetail>();
//		int fieldCount = 0;
//		while (fieldIt.hasNext()) {
//			fieldList.add(fieldCount, fieldIt.next());
//			if (fieldList.get(fieldCount) == null) {
//				break;
//			}
//			fieldCount = fieldCount + 1;
//		}
//		String[] queryReturnField = new String[fieldCount];
//		for (int i = 0; i < fieldCount; i++) {
//			queryReturnField[i] = fieldList.get(i).getFieldName();
//			if ("shape".equalsIgnoreCase(queryReturnField[i])) {
//				isQueryGeometry = true;
//			}
//		}
//		// 要定位OID集合
//		String[] oids = request.getOids();
//		CriteriaBuilder builder = model.getEntryManager().getCriteriaBuilder();
//		Expression exp = builder.in(builder.getRoot().get("OID", String.class),
//				oids);
//		ResultSet set = ResultSet.NONE;
//		try {
//			set = model.search(queryReturnField, exp);
//		} catch (ModelException e) {
//			e.printStackTrace();
//		}
//		Iterator<Device> it = set.resultList();
//		int count = 0;
//		List<Device> devList = new ArrayList<Device>();
//		while (it.hasNext()) {
//			devList.add(count, it.next());
//			if (devList.get(count) == null) {
//				break;
//			}
//			count = count + 1;
//		}
//		// 返回设备的定位信息
//		LocatePair[] location = new LocatePair[count];
//		for (int i = 0; i < count; i++) {
//			location[i] = new LocatePair();
//			location[i].setOid(String.valueOf(devList.get(i).getValue("OID")));
//
//			// 返回查询的到的空间信息
//			if (isQueryGeometry) {
//				List<SpatialDevice> spatialDevList = new ArrayList<SpatialDevice>();
//				spatialDevList.add(i, devList.get(i).asSpatialDevice());
//				// 空间对象类型 1 点 2 线 3 面
//				String geometryType = "";
//				GeometryType geomType = spatialDevList.get(i).getGeometry()
//						.getGeometry().getGeometryType();
//				geometryType = geomType.toString();
//				Coordinate[] coordnates = spatialDevList.get(i).getGeometry()
//						.getGeometry().getCoordinates();
//				int natesnum = coordnates.length;
//				// 坐标数组 [x1,y1,x2,y2,x3,y3]
//				double[] coords = new double[2 * natesnum];
//				for (int k = 0; k < natesnum; k++) {
//					coords[2 * k] = coordnates[k].getX();
//					coords[2 * k + 1] = coordnates[k].getY();
//				}
//				location[i].setGeometryType(geometryType);
//				location[i].setCoords(coords);
//			}
//
//			 //返回符号信息
//			 SymbolAdapter symAdp = QueryServiceActivator.symboladapter;
//			 SymbolDef symDef = null;
//			 try {
//			 symDef = symAdp.search(devList.get(i));
//			 } catch (ModelException e1) {
//			 // TODO Auto-generated catch block
//			 e1.printStackTrace();
//			 }
//			 if(symDef != null){
//				 SymbolPair symbol = new SymbolPair();
//				 symbol.setModelId(symDef.getModelId());
//				 symbol.setSymbolValue(symDef.getSymbolValue());
//				 symbol.setSymbolId(symDef.getSymbolId());
//				 symbol.setDevtypeId(symDef.getDevTypeId());
//				 location[i].setSymbol(symbol);
//			 }
//		}
//		resp.setPsrType(classId);
//		resp.setLocation(location);
//		resp.setCode(ReturnCode.SUCCESS);
		return resp;
	}
}
