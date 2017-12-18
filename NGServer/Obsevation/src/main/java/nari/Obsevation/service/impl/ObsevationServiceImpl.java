package nari.Obsevation.service.impl;

import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nari.Logger.Logger;
import nari.Logger.LoggerManager;
import nari.Obsevation.handler.GetVectorMapByCacheHandler;
import nari.Obsevation.service.interfaces.ObsevationService;
import nari.parameter.MapService.GetVectorMap.GetVectorMapRequest;
import nari.parameter.MapService.GetVectorMap.GetVectorMapResponse;
import nari.parameter.code.ReturnCode;
import nari.parameter.convert.Request;
import nari.parameter.convert.Response;

public class ObsevationServiceImpl implements ObsevationService {

	private Logger logger = LoggerManager.getLogger(this.getClass());
	
	/**
	 * 动态矢量出图(缓存查询)
	 */
	@Override
	public String getVectorMap(String input, HttpServletRequest request,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET,POST");
		response.setContentType("text/html;charset=utf-8");

		GetVectorMapRequest req = new GetVectorMapRequest();
		GetVectorMapResponse resp = new GetVectorMapResponse();
		try {
			req = Request.convert(input, GetVectorMapRequest.class);
		} catch (Exception e) {
			logger.info("传入参数(request属性类别)格式(JSON转换)出错");
			resp.setCode(ReturnCode.MISSTYPE);
			return Response.convert(resp);
		}
		
		if (req.validate()) {
			logger.info("传入参数缺少必须值");
			resp.setCode(ReturnCode.NULL);
			return Response.convert(resp);
		} else {
			GetVectorMapByCacheHandler handler = new GetVectorMapByCacheHandler();
			resp = handler.getVectorMapByCache(req);
			String geoResp = Response.forDetailFeildGJson(resp.getResult());
			if(!(resp.getRequestExtend().equalsIgnoreCase(""))){
				geoResp = resp.getRequestExtend() + "@" + geoResp;
			}
			PrintStream out = null;
			try {
				out = new PrintStream(response.getOutputStream());
				out.print(geoResp);
			} catch (IOException e) {
				logger.info("返回前台打印出错");
				resp.setCode(ReturnCode.PRINTERROR);
				return Response.convert(resp);
			} finally {
				out.flush();
				out.close();
			}
			return null;
		}
	}
}
