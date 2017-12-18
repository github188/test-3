package nari.TopoAnalysisService.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nari.TopoAnalysisService.bean.ConnectionAnalyze.ConnectionAnalyzeRequest;
import nari.TopoAnalysisService.bean.ConnectionAnalyze.ConnectionAnalyzeResponse;
import nari.network.device.TopoDevice;
import nari.network.interfaces.NetworkAdaptor;
import nari.network.structure.NetworkPath;
import nari.network.structure.NetworkWalker;
import nari.parameter.bean.QueryResult;
import nari.parameter.code.PsrTypeSystem;
import nari.parameter.code.ReturnCode;

public class ConnectionAnalyzeHandler 
	extends BaseTopoAnalyzeHandler {
	
	public ConnectionAnalyzeResponse connectionAnalyze(ConnectionAnalyzeRequest req) {
		
		ConnectionAnalyzeResponse resp = new ConnectionAnalyzeResponse();
		if (null == req || !req.validate()) {
			// 参数错误，抛出异常
			resp.setCode(ReturnCode.VALUEWRONG);
			return resp;
		}
		
		TopoDeviceInfo deviceInfoA = null;
		TopoDeviceInfo deviceInfoB = null;
		
		if (req.getPsrTypeSys().equals(PsrTypeSystem.CLASS_ID)) {
			deviceInfoA = getDeviceInfoByClassId(req.getPsrTypeA(), req.getPsrIdA());
			deviceInfoB = getDeviceInfoByClassId(req.getPsrTypeB(), req.getPsrIdB());
		} else if (req.getPsrTypeSys().equals(PsrTypeSystem.EQUIPMENT_ID)) {
			deviceInfoA = getDeviceInfoByEqumentId(req.getPsrTypeA(), req.getPsrIdA());
			deviceInfoB = getDeviceInfoByEqumentId(req.getPsrTypeB(), req.getPsrIdB());
		} else if (req.getPsrTypeSys().equals(PsrTypeSystem.MODEL_ID)) {
			deviceInfoA = getDeviceInfoByModelId(req.getPsrTypeA(), req.getPsrIdA());
			deviceInfoB = getDeviceInfoByModelId(req.getPsrTypeB(), req.getPsrIdB());
		} else {
			// 参数错误，抛出异常
			resp.setCode(ReturnCode.VALUEWRONG);
			return resp;
		}
		
		if (null == deviceInfoA || null == deviceInfoB) {
			// 查找不到该设备，抛出异常
			resp.setCode(ReturnCode.NODATA);
			return resp;
		}
		
		TopoDevice deviceA = networkAdaptor.getDevice(deviceInfoA.getNModelId(), deviceInfoA.getNOid());
		final TopoDevice deviceB = networkAdaptor.getDevice(deviceInfoB.getNModelId(), deviceInfoB.getNOid());
		if (null == deviceA || null == deviceB) {
			// 查找不到该设备，抛出异常
			resp.setCode(ReturnCode.NODATA);
			return resp;
		}
		Map<TopoDevice, NetworkPath> traces = new HashMap<TopoDevice, NetworkPath>();
				
		long t1 = System.currentTimeMillis(); 
		boolean result = networkAdaptor.search(
				deviceA, 
				new NetworkWalker() {

					@Override
					public int walk(TopoDevice last, TopoDevice current,
							List<TopoDevice> next) {
						if (current.equals(deviceB)) {
							return NetworkWalker.STOP_AND_COLLECT_TRACE;
						}
						return NetworkWalker.PROCESS_EACH;
					}
					
				}, 
				NetworkAdaptor.BREADTH_FIRST,
				traces);
		long t2 = System.currentTimeMillis();  
		System.out.println("连通性分析用时：" + (t2 - t1) + "毫秒。");
		
		if (result == false || traces.isEmpty()) {
			// 查询失败，抛出异常
			resp.setCode(ReturnCode.FAILED);
			resp.setConnectional(false);
			return resp;
		}
		
		NetworkPath path = traces.get(deviceB);
		if (null == path) {
			// 查询失败，抛出异常
			resp.setCode(ReturnCode.FAILED);
			resp.setConnectional(false);
			return resp;
		}
		
		if (path.isEmpty()) {
			// 查询失败，抛出异常
			resp.setCode(ReturnCode.FAILED);
			resp.setConnectional(false);
			return resp;
		}
		
		// 构造结果并返回
		Iterator<QueryResult> resultIter = getQueryResultsFromDevices(path.iterator());
		if (null == resultIter) {
			resp.setCode(ReturnCode.NODATA);
			return resp;
		}
		List<QueryResult> results = new ArrayList<QueryResult>();
		while (resultIter.hasNext()) {
			results.add(resultIter.next());
		}
		
		// 查询成功
		resp.setResult(results.toArray(new QueryResult[results.size()]));
		resp.setCode(ReturnCode.SUCCESS);
		return resp;
	}

}
