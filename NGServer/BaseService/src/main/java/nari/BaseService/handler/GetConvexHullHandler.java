package nari.BaseService.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nari.BaseService.BaseServiceActivator;
import nari.BaseService.bean.GetConvexHullRequest;
import nari.BaseService.bean.GetConvexHullResponse;
import nari.BaseService.bean.PsrDeviceInfo;
import nari.BaseService.bean.PsrPointCoordinate;
import nari.Geometry.Coordinate;
import nari.Geometry.Geometry;
import nari.Geometry.GeometryType;
import nari.Geometry.Polyline;
import nari.model.ModelActivator;
import nari.model.device.Device;
import nari.model.device.DeviceModel;
import nari.model.device.ModelService;
import nari.model.device.ResultSet;
import nari.model.device.SpatialDevice;
import nari.model.device.filter.CriteriaBuilder;
import nari.model.device.filter.Expression;
import nari.parameter.code.ReturnCode;

public class GetConvexHullHandler {
	
	private ModelService ms = BaseServiceActivator.modelService;
	
	public GetConvexHullResponse getConvexHull(GetConvexHullRequest req) {
		
		if (req.getPsrTypeSys().equals("modelId")) {
			// 根据设备子类型查询
			return getConvexHullByModelId(req);
		} else {
			// 根据设备类型查询
			return getConvexHullByClassId(req);
		}
	}

	/**
	 * 根据设备类型生成最小凸包
	 * @param req
	 * @return
	 */
	private GetConvexHullResponse getConvexHullByClassId(GetConvexHullRequest req) {
		
		GetConvexHullResponse resp = new GetConvexHullResponse();
		PsrDeviceInfo[] devices = req.getPsrDevices();
		
		Map<String, List<String>> classId_sbIds = new HashMap<String, List<String>>();
		
		boolean isClassId = req.getPsrTypeSys().equals("classId");
		
		for (int i = 0; i < devices.length; i++) {
			
			if (!isClassId) {
				// psrType的值为equId，先查找出对应的classId
				String equId = devices[i].getPsrType();
				List<Integer> classIds = ModelActivator.getClassIdByEquId(equId);
				
				// 对于每一个与之对应的classId，都要查询一遍
				for (Integer iter : classIds) {
					String classId = iter.toString();
					List<String> sbIds = classId_sbIds.get(classId);
					if (null == sbIds) {
						sbIds = new ArrayList<String>();
						sbIds.add(devices[i].getSbId());
						classId_sbIds.put(classId, sbIds);
					} else {
						sbIds.add(devices[i].getSbId());
					}
				}
				
			} else {
				// psrType的值为classId
				String classId = devices[i].getPsrType();
				List<String> sbIds = classId_sbIds.get(classId);
				if (null == sbIds) {
					sbIds = new ArrayList<String>();
					sbIds.add(devices[i].getSbId());
					classId_sbIds.put(classId, sbIds);
				} else {
					sbIds.add(devices[i].getSbId());
				}
			}
		}
		
		List<Coordinate> allCoords = new ArrayList<Coordinate>();
		
		String[] returnField = new String[] { "shape" };
		for (Map.Entry<String, List<String>> entry : classId_sbIds.entrySet()) {
			
			DeviceModel model = null;
			try {
				model = ms.fromClass(entry.getKey(), false);
			} catch (Exception e) {
				System.out.println("找不到的设备类型：" + entry.getKey());
				continue;
			}
			
			CriteriaBuilder builder = model.getEntryManager().getCriteriaBuilder();
			Expression exp = builder.in(builder.getRoot().get("SBID", String.class), entry.getValue().toArray());
			ResultSet resultSet = ResultSet.NONE;
			try {
				resultSet = model.search(returnField, exp, null);
			} catch (Exception ex) {
				System.out.println("数据库查询出错");
				resp.setCode(ReturnCode.SQLERROR);
				return resp;
			}
			
			Iterator<Device> iter = resultSet.resultList();
			if (null == iter) {
				continue;
			}
			
			while (iter.hasNext()) {
				SpatialDevice device = iter.next().asSpatialDevice();
				
				Geometry geometry = device.getGeometry().getGeometry();
				Coordinate[] coords = geometry.getCoordinates();
				for (int i = 0; i < coords.length; i++) {
					allCoords.add(coords[i]);
				}
			}
		}
		
		// 根据坐标串生产一个多点
		return CreateConvecHullByGeoTools(allCoords, resp);
	}
	
	/**
	 * 根据设备子类型生产最小凸包
	 * @param req
	 * @return
	 */
	private GetConvexHullResponse getConvexHullByModelId(GetConvexHullRequest req) {
		
		GetConvexHullResponse resp = new GetConvexHullResponse();
		PsrDeviceInfo[] devices = req.getPsrDevices();
		
		Map<String, List<String>> modelId_sbIds = new HashMap<String, List<String>>();
		for (int i = 0; i < devices.length; i++) {
			
			String modelId = devices[i].getPsrType();			
			List<String> sbIds = modelId_sbIds.get(modelId);
			if (null == sbIds) {
				sbIds = new ArrayList<String>();
				sbIds.add(devices[i].getSbId());
				modelId_sbIds.put(modelId, sbIds);
			} else {
				sbIds.add(devices[i].getSbId());
			}
			
		}
		
		List<Coordinate> allCoords = new ArrayList<Coordinate>();
		
		String[] returnField = new String[] { "shape" };
		for (Map.Entry<String, List<String>> entry : modelId_sbIds.entrySet()) {
			
			DeviceModel model = null;
			try {
				model = ms.fromSubClass(entry.getKey(), false);
			} catch (Exception e) {
				System.out.println("找不到的设备类型：" + entry.getKey());
				continue;
			}
			
			CriteriaBuilder builder = model.getEntryManager().getCriteriaBuilder();
			Expression exp = builder.in(builder.getRoot().get("SBID", String.class), entry.getValue().toArray());
			ResultSet resultSet = ResultSet.NONE;
			try {
				resultSet = model.search(returnField, exp, null);
			} catch (Exception ex) {
				System.out.println("数据库查询出错");
				resp.setCode(ReturnCode.SQLERROR);
				return resp;
			}
			
			Iterator<Device> iter = resultSet.resultList();
			if (null == iter) {
				continue;
			}
			
			while (iter.hasNext()) {
				SpatialDevice device = iter.next().asSpatialDevice();
				Geometry geometry = device.getGeometry().getGeometry();
				Coordinate[] coords = geometry.getCoordinates();
				for (int i = 0; i < coords.length; i++) {
					allCoords.add(coords[i]);
				}
			}
		}
		
		// 根据坐标串生产一个多点
		return CreateConvecHullByGeoTools(allCoords, resp);
		
	}
	
	/**
	 * 由一系列点集生成凸包
	 */
	private GetConvexHullResponse CreateConvecHullByGeoTools(List<Coordinate> allCoords, GetConvexHullResponse resp) {
		
		if (allCoords.size() < 3) {  
			// 小于三个坐标点，无法组成多边形
			resp.setCode(ReturnCode.NODATA);
			return resp;
		}
		
		com.vividsolutions.jts.geom.Coordinate[] coords = new com.vividsolutions.jts.geom.Coordinate[allCoords.size()];
		
		int index = 0;
		for (Coordinate coord : allCoords) {
			
			coords[index] = new com.vividsolutions.jts.geom.Coordinate();
			coords[index].x = coord.getX();
			coords[index].y = coord.getY();
			
			++index;
		}
		
		com.vividsolutions.jts.geom.GeometryFactory factory = new com.vividsolutions.jts.geom.GeometryFactory();
		com.vividsolutions.jts.geom.MultiPoint multiPoint = factory.createMultiPoint(coords);
		com.vividsolutions.jts.geom.Geometry convecHull = multiPoint.convexHull();
		
		if (org.geotools.geometry.jts.Geometries.get(convecHull) 
				!= org.geotools.geometry.jts.Geometries.POLYGON) {
			
			// 返回的凸包不是多边形
			resp.setCode(ReturnCode.FAILED);
			return resp;
			
		}
		
		com.vividsolutions.jts.geom.Polygon convecHullPolygon = (com.vividsolutions.jts.geom.Polygon)convecHull;
		if (convecHullPolygon.getCoordinates().length <= 0) {
			// 返回的凸包中没有点
			resp.setCode(ReturnCode.FAILED);
			return resp;
		}
		
		PsrPointCoordinate[] polygon = new PsrPointCoordinate[convecHullPolygon.getCoordinates().length];
		for (int i = 0; i < polygon.length; i++) {
			polygon[i] = new PsrPointCoordinate();
			polygon[i].setX(convecHullPolygon.getCoordinates()[i].x);
			polygon[i].setY(convecHullPolygon.getCoordinates()[i].y);
		}
		
		resp.setCode(ReturnCode.SUCCESS);
		resp.setPolygon(polygon);
		
		return resp;
	}
}
