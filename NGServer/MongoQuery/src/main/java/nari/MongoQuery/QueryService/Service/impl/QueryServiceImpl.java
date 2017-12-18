package nari.MongoQuery.QueryService.Service.impl;

import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nari.MongoQuery.QueryService.Service.interfaces.QueryService;
import nari.MongoQuery.QueryService.bean.ThematicDevRequest;
import nari.MongoQuery.QueryService.bean.ThematicDevResponse;
import nari.MongoQuery.QueryService.handler.ConditionQueryHandler;
import nari.MongoQuery.QueryService.handler.QueryByStationIdHandler;
import nari.MongoQuery.QueryService.handler.SpatialQueryHandler;
import nari.MongoQuery.QueryService.handler.ThematicDevHandler;
import nari.MongoQuery.QueryService.bean.QueryRelationsRequest;
import nari.MongoQuery.QueryService.bean.QueryRelationsResponse;
import nari.MongoQuery.QueryService.handler.QueryRelationsHandler;
import nari.MongoQuery.QueryService.bean.QueryContainsRequest;
import nari.MongoQuery.QueryService.bean.QueryContainsResponse;
import nari.MongoQuery.QueryService.bean.QueryParentsRequest;
import nari.MongoQuery.QueryService.bean.QueryParentsResponse;
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

	@Override
	public String queryByCondition(String input, HttpServletRequest request,
			HttpServletResponse response) {

		ConditionQueryRequest req = new ConditionQueryRequest();
		ConditionQueryResponse resp = new ConditionQueryResponse();
		try {
			req = Request.convert(input, ConditionQueryRequest.class);

		} catch (Exception e) {
			System.out.println("input parameter formating error ");
			resp.setCode(ReturnCode.MISSTYPE);
			return Response.convert(resp);
		}
		//
		if (req.validate()) {
			System.out.print("no essential parameter in input");
			resp.setCode(ReturnCode.NULL);
			return Response.convert(resp);
		}
		//
		ConditionQueryHandler handler = new ConditionQueryHandler();
		try {
			resp = handler.queryByCondition(req);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		QueryResult[] respResult = resp.getResult();
		// check format of response
		String geoResp = "";
		if (req.getTy().equalsIgnoreCase("DZ")) {
			geoResp = Response.forDZGeoJson(respResult);
		} else {
			geoResp = Response.forGeoJsonForConditionQuery2(respResult);
		}
		// return data required
		PrintStream out = null;
		try {
			out = new PrintStream(response.getOutputStream());
			out.print(geoResp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}

		return null;
	}

	@Override
	public String queryByStationId(String input, HttpServletRequest request,
			HttpServletResponse response) {

		QueryByStationIdRequest req = new QueryByStationIdRequest();
		QueryByStationIdResponse resp = new QueryByStationIdResponse();

		req = Request.convert(input, QueryByStationIdRequest.class);
		if (req.validate()) {
			resp.setCode(ReturnCode.NULL);
			return Response.convert(resp);
		} else {
			QueryByStationIdHandler handler = new QueryByStationIdHandler();
			resp = handler.queryByStationId(req);
			QueryResult[] respResult = resp.getResult();
			String DZGeoResp = Response.forDZGeoJson(respResult);
			// 返回前台通过向网页输出返回所需数据
			PrintStream out = null; // 流
			try {
				out = new PrintStream(response.getOutputStream());
				out.print(DZGeoResp);
			} catch (IOException e) {
				System.out.println("response print error!");
				resp.setCode(ReturnCode.PRINTERROR);
				return Response.convert(resp);
			} finally {
				out.flush();
				out.close();
			}
		}
		return null;
	}

	@Override
	public String queryLandMark(String input, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String spatialQuery(String input, HttpServletRequest request,
			HttpServletResponse response) {

		SpatialQueryRequest req = new SpatialQueryRequest();
		SpatialQueryResponse resp = new SpatialQueryResponse();

		try {
			req = Request.convert(input, SpatialQueryRequest.class);

		} catch (Exception e) {
			System.out.println("input data error");
			resp.setCode(ReturnCode.MISSTYPE);
			return Response.convert(resp);
		}

		if (req.validate()) {
			System.out.println("input data error");
			resp.setCode(ReturnCode.NULL);
			return Response.convert(resp);
		} else {
			SpatialQueryHandler handler = new SpatialQueryHandler();
			resp = handler.spatialQuery(req);
			QueryResult[] respResult = resp.getResult();
			String geoResp = Response.forGeoJsonForSpatialQuery(respResult);

			PrintStream out = null;
			try {
				out = new PrintStream(response.getOutputStream());
				out.print(geoResp);
			} catch (IOException e) {
				System.out.println("response print error!");
				resp.setCode(ReturnCode.PRINTERROR);
				return Response.convert(resp);
			} finally {
				out.flush();
				out.close();
			}
		}

		return null;
	}

	/**
	 * 专题图图实例下设备查询
	 */
	@Override
	public String thematicDev(String input, HttpServletRequest request,
			HttpServletResponse response) {
		ThematicDevRequest req = new ThematicDevRequest();
		ThematicDevResponse resp = new ThematicDevResponse();

		try {
			req = Request.convert(input, ThematicDevRequest.class);

		} catch (Exception e) {
			System.out.println("input data error");
			resp.setCode(ReturnCode.MISSTYPE);
			return Response.convert(resp);
		}

		// if(req.validate()){
		// System.out.println("input data error");
		// resp.setCode(ReturnCode.NULL);
		// return Response.convert(resp);
		// }else{
		ThematicDevHandler handler = new ThematicDevHandler();
		resp = handler.getThematicDev(req);
		QueryResult[] respResult = resp.getResult();
		String geoResp = Response.forMongoGeoJson(respResult);

		PrintStream out = null;
		try {
			out = new PrintStream(response.getOutputStream());
			out.print(geoResp);
		} catch (IOException e) {
			System.out.println("response print error!");
			resp.setCode(ReturnCode.PRINTERROR);
			return Response.convert(resp);
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
	

	public String queryFeadrLine(String input, HttpServletRequest request,
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
			resp = handler.queryFeederLine(req);
			QueryResult[] respResult = resp.getResult();
			if(respResult != null){
				String geoJson = Response.forMongoGeoJson(respResult);
				out.print(geoJson);
			}
			else{
				resp.setCode(ReturnCode.NODATA);
				out.print(Response.convert(resp));
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
	
}
