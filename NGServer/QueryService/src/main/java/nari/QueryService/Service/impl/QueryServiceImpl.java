package nari.QueryService.Service.impl;

import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import nari.Logger.Logger;
import nari.Logger.LoggerManager;
import nari.QueryService.Service.interfaces.QueryService;
import nari.QueryService.bean.GetAllSubSetRequest;
import nari.QueryService.bean.GetAllSubSetResponse;
import nari.QueryService.bean.QueryContainsRequest;
import nari.QueryService.bean.QueryContainsResponse;
import nari.QueryService.bean.QueryParentsRequest;
import nari.QueryService.bean.QueryParentsResponse;
import nari.QueryService.bean.QueryRelationsRequest;
import nari.QueryService.bean.QueryRelationsResponse;
import nari.QueryService.bean.SpatialqueryVirtualRequest;
import nari.QueryService.bean.SpatialqueryVirtualResponse;
import nari.QueryService.handler.ConditionQueryHandler;
import nari.QueryService.handler.GetAllSubSet;
import nari.QueryService.handler.QueryByStationIdHandler;
import nari.QueryService.handler.QueryRelationsHandler;
import nari.QueryService.handler.SpatialQueryHandler;
import nari.QueryService.handler.SpatialqueryVirtualHandler;
import nari.parameter.QueryService.ConditionQuery.ConditionQueryRequest;
import nari.parameter.QueryService.ConditionQuery.ConditionQueryResponse;
import nari.parameter.QueryService.SpatialQuery.SpatialQueryRequest;
import nari.parameter.QueryService.SpatialQuery.SpatialQueryResponse;
import nari.parameter.QueryService.StationIdQuery.QueryByStationIdRequest;
import nari.parameter.QueryService.StationIdQuery.QueryByStationIdResponse;
import nari.parameter.bean.QueryResult;
import nari.parameter.code.ReturnCode;
import nari.parameter.convert.Request;
import nari.parameter.convert.Response;

public class QueryServiceImpl implements QueryService {

	private Logger logger = LoggerManager.getLogger(this.getClass());
	
	/**
	 * 1.按条件查询设备属性
	 */
	@Override
	public String queryByCondition(String input, HttpServletRequest request,
			HttpServletResponse response) {

		logger.info("按条件查询设备属性input："+input);
		String ImplResp = "";
		ConditionQueryRequest req = new ConditionQueryRequest();
		ConditionQueryResponse resp = new ConditionQueryResponse();
		PrintStream out = null; // 流
		try {
			// out = new PrintStream("D:\\1.txt");
			out = new PrintStream(response.getOutputStream());
			
		try {
			req = Request.convert(input, ConditionQueryRequest.class);
		} catch (Exception e) {
			logger.info("传入参数(request属性类别)格式(JSON转换)出错");
			resp.setCode(ReturnCode.MISSTYPE);
			out.print(Response.convert(resp));
		}
		// condition中classid为必须传入的值
		if (req.validate()) {
			logger.info("传入参数缺少必须值");
			resp.setCode(ReturnCode.NULL);
			out.print(Response.convert(resp));
		}
		// 实现业务操作
		ConditionQueryHandler handler = new ConditionQueryHandler();
		resp = handler.queryByCondition(req);
		QueryResult[] respResult = resp.getResult();
		if(respResult == null || respResult.length == 0){
			logger.info("无数据");
			resp.setCode(ReturnCode.NODATA);
			out.print(Response.convert(resp));
		}
		// 判断返回格式
		
		if (req.getTy().equalsIgnoreCase("DZ")) {
			ImplResp = Response.forGeoJason(respResult);
		} else {
			ImplResp = Response.forDetailFeildGJson(respResult);
		}
		// 返回前台通过向网页输出返回所需数据
		
			out.print(ImplResp);
		} catch (IOException e) {
			logger.info("返回前台打印出错");
			resp.setCode(ReturnCode.PRINTERROR);
			return null;
		} finally {
			out.flush();
			out.close();
		}
		return null;
	}

	/**
	 * 2.空间查询
	 */
	@Override
	public String spatialQuery(String input, HttpServletRequest request,
			HttpServletResponse response) {

//		logger.info("空间查询"+input);
		SpatialQueryRequest req = new SpatialQueryRequest();
		SpatialQueryResponse resp = new SpatialQueryResponse();
		PrintStream out = null;		//流
		try {
			out = new PrintStream(response.getOutputStream());	//初始化out
			
		try {
			req = Request.convert(input, SpatialQueryRequest.class);
		} catch (Exception e) {
			logger.info("传入参数(request属性类别)格式(JSON转换)出错");
			resp.setCode(ReturnCode.MISSTYPE);
			out.print(Response.convert(resp));
		}

		// 判断传值是否为空
		if (req.validate()) {
			logger.info("传入参数缺少必须值");
			resp.setCode(ReturnCode.NULL);
			out.print(Response.convert(resp));
		} else {
			// 实现空间业务查询操作
			SpatialQueryHandler handler = new SpatialQueryHandler();
			resp = handler.spatialQuery(req);
			QueryResult[] respResult = resp.getResult();
			if(respResult == null || respResult.length == 0){
				logger.info("无数据");
				resp.setCode(ReturnCode.NODATA);
				out.print(Response.convert(resp));
			}
			// String ImplResp = Response.convert(respResult);
			String geoResp = Response.forDetailFeildGJson(respResult);
			// String DZGeoResp = Response.forGeoJason(respResult);
			out.print(geoResp);
		}
			
		} catch (IOException e) {
			logger.info("返回前台打印出错");
			resp.setCode(ReturnCode.PRINTERROR);
			return null;
		} finally {
			out.flush();
			out.close();
		}
		return null;
	}
	
	/**
	 * 某设备的所有子设备查询
	 */
	@Override
	public String getAllSubSet(String input, HttpServletRequest request,
			HttpServletResponse response) {

		logger.info("查询某父设备下所有设备查询"+input);
		GetAllSubSetRequest req = new GetAllSubSetRequest();
		GetAllSubSetResponse resp = new GetAllSubSetResponse();
		PrintStream out = null;		//流
		try {
			out = new PrintStream(response.getOutputStream());	//初始化out
			
		try {
			req = Request.convert(input, GetAllSubSetRequest.class);
		} catch (Exception e) {
			logger.info("传入参数(request属性类别)格式(JSON转换)出错");
			resp.setCode(ReturnCode.MISSTYPE);
			out.print(Response.convert(resp));
		}

		// 判断传值是否合法
		if (!req.validate()) {
			logger.info("传入参数缺少必须值");
			resp.setCode(ReturnCode.NULL);
			out.print(Response.convert(resp));
		} else {
			// 实现业务操作
			GetAllSubSet handler = new GetAllSubSet();
			resp = handler.getAllSubSet(req);
			QueryResult[] respResult = resp.getResult();
			if(respResult == null || respResult.length == 0){
				logger.info("无数据");
				resp.setCode(ReturnCode.NODATA);
				out.print(Response.convert(resp));
			}
			// String ImplResp = Response.convert(respResult);
			// String geoResp = Response.forDetailFeildGJson(respResult);
			String DZGeoResp = Response.forGeoJason(respResult);
			out.print(DZGeoResp);
			return null;
		}
		
		} catch (IOException e) {
			System.out.println("返回前台打印出错");
			resp.setCode(ReturnCode.PRINTERROR);
			return null;
		} finally {
			out.flush();
			out.close();
		}
			
		return null;
	}

	/**
	 * 某站内所有设备查询
	 */
	@Override
	public String queryByStationId(String input, HttpServletRequest request,
			HttpServletResponse response) {

		System.out.println("站内查询input:" + input);
		QueryByStationIdRequest req = new QueryByStationIdRequest();
		QueryByStationIdResponse resp = new QueryByStationIdResponse();
		PrintStream out = null;		//流
		try {
			out = new PrintStream(response.getOutputStream());	//初始化out
			
			try {
				req = Request.convert(input, QueryByStationIdRequest.class);
			} catch (Exception e) {
				System.out.println("传入参数(request属性类别)格式(JSON转换)出错");
				resp.setCode(ReturnCode.MISSTYPE);
				out.print(Response.convert(resp));
			}
	
			// 判断传值是否为空
			if (req.validate()) {
				System.out.println("传入参数缺少必须值");
				resp.setCode(ReturnCode.NULL);
				out.print(Response.convert(resp));
			} else {
				// 实现业务操作
				QueryByStationIdHandler handler = new QueryByStationIdHandler();
				resp = handler.queryByStationId(req);
				QueryResult[] respResult = resp.getResult();
				if(respResult == null || respResult.length == 0){
					System.out.println("无数据");
					resp.setCode(ReturnCode.NODATA);
					out.print(Response.convert(resp));
				}
				// String ImplResp = Response.convert(respResult);
				// String geoResp = Response.forGeoJason(respResult);
				String DZGeoResp = Response.forGeoJason(respResult);
				// 返回前台通过向网页输出返回所需数据
					// out.print(ImplResp);
					// out.print(geoResp);
					out.print(DZGeoResp);
			}
		
		} catch (IOException e) {
			System.out.println("返回前台打印出错");
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
	 * 5.空间虚拟设备包含设备查询
	 */
	@Override
	public String spatialqueryVirtual(String input, HttpServletRequest request,
			HttpServletResponse response) {

		System.out.println("站内查询input:"+input);
		SpatialqueryVirtualRequest req = new SpatialqueryVirtualRequest();
		SpatialqueryVirtualResponse resp = new SpatialqueryVirtualResponse();
		PrintStream out = null;		//流
		try {
			out = new PrintStream(response.getOutputStream());	//初始化out
			
		try {
			req = Request.convert(input, SpatialqueryVirtualRequest.class);
		} catch (Exception e) {
			System.out.println("传入参数(request属性类别)格式(JSON转换)出错");
			resp.setCode(ReturnCode.MISSTYPE);
			out.print(Response.convert(resp));
		}

		// 判断传值是否为空
		if (req.validate()) {
			System.out.println("传入参数缺少必须值");
			resp.setCode(ReturnCode.NULL);
			out.print(Response.convert(resp));
		} else {
			// 实现业务操作
			SpatialqueryVirtualHandler handler = new SpatialqueryVirtualHandler();
			resp = handler.spatialqueryVirtual(req);
			QueryResult[] respResult = resp.getResult();
			if(respResult == null || respResult.length == 0){
				System.out.println("无数据");
				resp.setCode(ReturnCode.NODATA);
				out.print(Response.convert(resp));
			}
			// String ImplResp = Response.convert(respResult);
			// String geoResp = Response.forGeoJason(respResult);
			String DZGeoResp = Response.forGeoJason(respResult);
			// 返回前台通过向网页输出返回所需数据
				// out.print(ImplResp);
				// out.print(geoResp);
				out.print(DZGeoResp);
		}
		
		} catch (IOException e) {
			System.out.println("返回前台打印出错");
			resp.setCode(ReturnCode.PRINTERROR);
			return null;
		} finally {
			out.flush();
			out.close();
		}
		return null;
	}
	
	public String queryRelations(String input, HttpServletRequest request,
			HttpServletResponse response) {

		System.out.println("关系查询输入参数：" + input);
		QueryRelationsRequest req = new QueryRelationsRequest();
		QueryRelationsResponse resp = new QueryRelationsResponse();
		PrintStream out = null;		//流
		try {
			out = new PrintStream(response.getOutputStream());	//初始化out
			try {
				req = Request.convert(input, QueryRelationsRequest.class);
			} catch (Exception e) {
				System.out.println("传入参数(request属性类别)格式(JSON转换)出错");
				resp.setCode(ReturnCode.MISSTYPE);
				out.print(Response.convert(resp));
			}
			// 实现业务操作
			QueryRelationsHandler handler = new QueryRelationsHandler();
			resp = handler.queryRelations(req);
			QueryResult[] respResult = resp.getResult();
			String geoJson = Response.forGeoJason(respResult);
			out.print(geoJson);
		} catch (IOException e) {
			System.out.println("返回前台打印出错");
			resp.setCode(ReturnCode.PRINTERROR);
			return null;
		} finally {
			out.flush();
			out.close();
		}
		return null; 
	}
	
	/**
	 * 5. 查询子设备
	 */
	public String queryContains(String input, HttpServletRequest request,
			HttpServletResponse response) {

		System.out.println("查询子设备接口："+input);
		
		QueryContainsRequest req = new QueryContainsRequest();
		QueryContainsResponse resp = new QueryContainsResponse();
		
		PrintStream out = null;		//流
		try {
			out = new PrintStream(response.getOutputStream());	//初始化out
			try {
				req = Request.convert(input, QueryContainsRequest.class);
			} catch (Exception e) {
				System.out.println("传入参数(request属性类别)格式(JSON转换)出错");
				resp.setCode(ReturnCode.MISSTYPE);
				out.print(Response.convert(resp));
			}
			// 实现业务操作
			QueryRelationsHandler handler = new QueryRelationsHandler();
			resp = handler.queryContains(req);
			QueryResult[] respResult = resp.getResult();
			String geoJson = Response.forGeoJason(respResult);
			out.print(geoJson);
		} catch (IOException e) {
			System.out.println("返回前台打印出错");
			resp.setCode(ReturnCode.PRINTERROR);
			return null;
		} finally {
			out.flush();
			out.close();
		}
		return null; 
	}
	
	/**
	 * 6. 查询父设备
	 */
	public String queryParents(String input, HttpServletRequest request,HttpServletResponse response) {
		
		System.out.println("查询父设备接口："+input);
		
		QueryParentsRequest req = new QueryParentsRequest();
		QueryParentsResponse resp = new QueryParentsResponse();
		
		PrintStream out = null;		//流
		try {
			out = new PrintStream(response.getOutputStream());	//初始化out
			try {
				req = Request.convert(input, QueryParentsRequest.class);
			} catch (Exception e) {
				System.out.println("传入参数(request属性类别)格式(JSON转换)出错");
				resp.setCode(ReturnCode.MISSTYPE);
				out.print(Response.convert(resp));
			}
			// 实现业务操作
			QueryRelationsHandler handler = new QueryRelationsHandler();
			resp = handler.queryParents(req);
			QueryResult[] respResult = resp.getResult();
			String geoJson = Response.forGeoJason(respResult);
			out.print(geoJson);
		} catch (IOException e) {
			System.out.println("返回前台打印出错");
			resp.setCode(ReturnCode.PRINTERROR);
			return null;
		} finally {
			out.flush();
			out.close();
		}
		return null; 
	}
	
}
