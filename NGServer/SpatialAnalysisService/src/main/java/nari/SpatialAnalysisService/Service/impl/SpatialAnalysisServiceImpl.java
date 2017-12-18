package nari.SpatialAnalysisService.Service.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nari.SpatialAnalysisService.Service.interfaces.SpatialAnalysisService;
import nari.SpatialAnalysisService.handler.DeviceBufferHandler;
import nari.SpatialAnalysisService.handler.SpatialBufferHandler;
import nari.parameter.SpatialAnalysisService.DeviceSpatialAnalysis.DeviceBufferRequest;
import nari.parameter.SpatialAnalysisService.DeviceSpatialAnalysis.DeviceBufferResponse;
import nari.parameter.SpatialAnalysisService.SpatialAlalysis.SpatialBufferRequest;
import nari.parameter.SpatialAnalysisService.SpatialAlalysis.SpatialBufferResponse;
import nari.parameter.code.ReturnCode;
import nari.parameter.convert.Request;
import nari.parameter.convert.Response;


public class SpatialAnalysisServiceImpl implements SpatialAnalysisService {

	@Override
	public String spatialBuffer(String input,HttpServletRequest request,HttpServletResponse response) {
		SpatialBufferRequest req = Request.convert(input ,SpatialBufferRequest.class);
		SpatialBufferResponse resp = new SpatialBufferResponse();
		//判断传值是否为空
		if(!req.validate()){
			resp.setCode(ReturnCode.FAILED);
			return Response.convert(resp);
		}else{
			//实现空间对象缓冲区分析操作
			SpatialBufferHandler handler = new SpatialBufferHandler();
			resp = handler.spatialBuffer(req);
			return Response.convert(resp);
		}
	}

	@Override
	public String deviceBuffer(String input,HttpServletRequest request,HttpServletResponse response) {
		DeviceBufferRequest req = Request.convert(input ,DeviceBufferRequest.class);
		DeviceBufferResponse resp = new DeviceBufferResponse();
		//判断传值是否为空
		if(!req.validate()){
			resp.setCode(ReturnCode.FAILED);
			return Response.convert(resp);
		}else{
			//实现设备缓冲区分析操作
			DeviceBufferHandler handler = new DeviceBufferHandler();
			resp = handler.deviceBuffer(req);
			return Response.convert(resp);
		}
	}

	@Override
	public String shortPath(String input,HttpServletRequest request,HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

}
