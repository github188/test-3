package nari.ThematicMapService.Service.impl;

import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nari.Logger.Logger;
import nari.Logger.LoggerManager;
import nari.ThematicMapService.Service.interfaces.ThematicService;
import nari.ThematicMapService.bean.GetThematicConfigRequest;
import nari.ThematicMapService.bean.GetThematicConfigResponse;
import nari.ThematicMapService.bean.ThematicDevRequest;
import nari.ThematicMapService.bean.ThematicDevResponse;
import nari.ThematicMapService.handler.GetThematicConfigHandler;
import nari.ThematicMapService.handler.ThematicDevHandler;
import nari.parameter.bean.QueryResult;
import nari.parameter.code.ReturnCode;
import nari.parameter.convert.Request;
import nari.parameter.convert.Response;

public class ThematicServiceImpl implements ThematicService {
	
	private Logger logger = LoggerManager.getLogger(this.getClass());
	
	@Override
	public String getCableSectionMap(String input, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCableWellSectionMap(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSingleLineMap(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStationMap(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSupplyRangeMap(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSystemMap(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String spatialQuery(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	
	/**
	 * 专题图图实例下设备查询
	 */
	@SuppressWarnings("null")
	@Override
	public String thematicDev(String input, HttpServletRequest request,
			HttpServletResponse response) {
//		response.setHeader("Access-Control-Allow-Origin", "*");
//		response.setHeader("Access-Control-Allow-Methods", "GET,POST");
//		response.setContentType("text/html;charset=utf-8");

		logger.info("专题图图实例下设备查询"+input);
		PrintStream out = null;
		ThematicDevHandler handler= new ThematicDevHandler();
		
		ThematicDevRequest req = Request.convert(input, ThematicDevRequest.class);
		ThematicDevResponse resp = handler.getThematicDev(req);
		try {
			out = new PrintStream(response.getOutputStream());
			QueryResult[] respResult = resp.getResult();
			if(respResult == null || respResult.length == 0){
				logger.info("无数据");
				resp.setCode(ReturnCode.NODATA);
				out.print(Response.convert(resp));
				return null;
			}
			String implResp = Response.forGeoJason(respResult);
			out.print(implResp);
		} catch (IOException e) {
			logger.info("返回前台打印出错");
			resp.setCode(ReturnCode.PRINTERROR);
			out.print(Response.convert(resp));
			return null;
		} finally {
			out.flush();
			out.close();
		}
		return null;
	}

	/**
	 * 获取专题图配置信息
	 */
	@Override
	public String getThematicConfig(String input, HttpServletRequest request,
			HttpServletResponse response) {
//		response.setHeader("Access-Control-Allow-Origin", "*");
//		response.setHeader("Access-Control-Allow-Methods", "GET,POST");
//		response.setContentType("text/html;charset=utf-8");

		logger.info("获取专题图配置信息"+input);
		PrintStream out = null;
		GetThematicConfigHandler handler= new GetThematicConfigHandler();
		
		GetThematicConfigRequest req = Request.convert(input, GetThematicConfigRequest.class);
		GetThematicConfigResponse resp = handler.getThematicConfig(req);
		try {
			
			String implResp = Response.convert(resp);
			out = new PrintStream(response.getOutputStream());
			out.print(implResp);
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
