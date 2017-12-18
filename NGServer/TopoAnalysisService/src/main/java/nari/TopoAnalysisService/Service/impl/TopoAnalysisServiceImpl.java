package nari.TopoAnalysisService.Service.impl;


import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nari.Logger.Logger;
import nari.Logger.LoggerManager;
import nari.TopoAnalysisService.Service.interfaces.TopoAnalysisService;
import nari.TopoAnalysisService.bean.ConnectionAnalyze.ConnectionAnalyzeRequest;
import nari.TopoAnalysisService.bean.ConnectionAnalyze.ConnectionAnalyzeResponse;
import nari.TopoAnalysisService.handler.ConnectionAnalyzeHandler;
import nari.parameter.bean.QueryResult;
import nari.parameter.code.ReturnCode;
import nari.parameter.convert.Request;
import nari.parameter.convert.Response;

public class TopoAnalysisServiceImpl implements TopoAnalysisService {
	
	private Logger logger = LoggerManager.getLogger(this.getClass());
	
	/**
	 * 连通性分析
	 */
	public String connectionAnalyze(String input, HttpServletRequest request, HttpServletResponse response) {
		
		ConnectionAnalyzeRequest req = new ConnectionAnalyzeRequest();
		ConnectionAnalyzeResponse resp = new ConnectionAnalyzeResponse();
		
		try {	
			req = Request.convert(input, ConnectionAnalyzeRequest.class);
		} catch (Exception ex) {
			logger.info("传入参数（request属性类别）格式（JSON转换）出错");
			ex.printStackTrace();
			resp.setCode(ReturnCode.MISSTYPE);
			return Response.convert(resp);
		}
		
		ConnectionAnalyzeHandler handler = new ConnectionAnalyzeHandler();
		resp = handler.connectionAnalyze(req);
		
		QueryResult[] results = resp.getResult();
		if (results == null || results.length == 0) {
			System.out.println("无数据");
			resp.setCode(ReturnCode.NODATA);
			return Response.convert(resp);
		}
		
		String geoJson = Response.forGeoJason(results);
		PrintStream out = null;
		try {
			out = new PrintStream(response.getOutputStream());
			out.print(geoJson);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
		
		return null;
	}
	
	/**
	 * 电源追溯分析
	 */
	public String powerSourceAnalyze(String input, HttpServletRequest request, HttpServletResponse response) {
		
		return null;
	}
	
	/**
	 * 停电范围分析
	 */
	public String outageRangeAnalyze(String input, HttpServletRequest request, HttpServletResponse response) {
		
		return null;
	}
	
	/**
	 * 模拟停电分析
	 */
	public String simOutageRangeAnalyze(String input, HttpServletRequest request, HttpServletResponse response) {
		
		return null;
	}
	
	/**
	 * 供电范围分析
	 */
	public String supplyRangeAnalyze(String input, HttpServletRequest request, HttpServletResponse response) {
		
		return null;
	}
	
	/**
	 * 供电半径分析
	 */
	public String supplyRadiusAnalyze(String input, HttpServletRequest request, HttpServletResponse response) {
		
		return null;
	}
	
}
