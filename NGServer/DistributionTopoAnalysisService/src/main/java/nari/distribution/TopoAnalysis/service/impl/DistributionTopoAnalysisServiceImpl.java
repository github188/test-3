package nari.distribution.TopoAnalysis.service.impl;

import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nari.Logger.Logger;
import nari.Logger.LoggerManager;
import nari.distribution.TopoAnalysis.bean.MockOutageRangeAnalyze.MockOutageRangeAnalyzeRequest;
import nari.distribution.TopoAnalysis.bean.MockOutageRangeAnalyze.MockOutageRangeAnalyzeResponse;
import nari.distribution.TopoAnalysis.handler.MockOutageRangeAnalyzeHandler;
import nari.distribution.TopoAnalysis.service.DistributionTopoAnalysisService;
import nari.parameter.code.ReturnCode;
import nari.parameter.convert.Request;
import nari.parameter.convert.Response;

public class DistributionTopoAnalysisServiceImpl 
	implements DistributionTopoAnalysisService {
	
	private Logger logger = LoggerManager.getLogger(this.getClass());
	
	public String testAnalyze(String input, 
			HttpServletRequest request, HttpServletResponse response) {
		
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET,POST");
		response.setContentType("text/html;charset=utf-8");
		
		MockOutageRangeAnalyzeRequest req = new MockOutageRangeAnalyzeRequest();
		MockOutageRangeAnalyzeResponse resp = new MockOutageRangeAnalyzeResponse();
		
		try {	
			req = Request.convert(input, MockOutageRangeAnalyzeRequest.class);
		} catch (Exception ex) {
			logger.info("传入参数（request属性类别）格式（JSON转换）出错");
			ex.printStackTrace();
			resp.setCode(ReturnCode.MISSTYPE);
			return Response.convert(resp);
		}
		
		MockOutageRangeAnalyzeHandler handler = new MockOutageRangeAnalyzeHandler();
		resp = handler.test(req);
		
		PrintStream out = null; // 流
		try {
			out = new PrintStream(response.getOutputStream());
			String implResp = Response.convert(resp);
			out.print(implResp);	
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
		return null;
	}

	public String mockOutageRangeAnalyze(String input,
			HttpServletRequest request, HttpServletResponse response) {
		
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET,POST");
		response.setContentType("text/html;charset=utf-8");
		
		MockOutageRangeAnalyzeRequest req = new MockOutageRangeAnalyzeRequest();
		MockOutageRangeAnalyzeResponse resp = new MockOutageRangeAnalyzeResponse();
		
		System.out.println("配网模拟停电分析，输入参数：" + input);
		
		try {	
			req = Request.convert(input, MockOutageRangeAnalyzeRequest.class);
		} catch (Exception ex) {
			logger.info("传入参数（request属性类别）格式（JSON转换）出错");
			ex.printStackTrace();
			resp.setCode(ReturnCode.MISSTYPE);
			return Response.convert(resp);
		}
		
		long t1 = System.currentTimeMillis();
		MockOutageRangeAnalyzeHandler handler = new MockOutageRangeAnalyzeHandler();
		resp = handler.mockOutageRangeAnalyze(req);
		long t2 = System.currentTimeMillis();
		System.out.println("配网模拟停电分析结束，共用时：" + (t2 - t1) + "毫秒。");
		
		PrintStream out = null; // 流
		try {
			out = new PrintStream(response.getOutputStream());
			String implResp = Response.convert(resp);
			out.print(implResp);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
		return null;
	}

}
