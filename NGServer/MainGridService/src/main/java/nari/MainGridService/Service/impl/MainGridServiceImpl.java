package nari.MainGridService.Service.impl;

import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nari.MainGridService.MainGridServiceActivator;
import nari.MainGridService.BaseHandler.GetFileNameHandler;
import nari.MainGridService.BaseHandler.GetIpAdressHandler;
import nari.MainGridService.BaseHandler.UpdateRealGeoHandler;
import nari.MainGridService.Service.interfaces.MainGridService;
import nari.MainGridService.handler.ConditionLocatQueryHandler;
import nari.MainGridService.handler.FuzzyQueryHandler;
import nari.MainGridService.handler.GeoLocatQueryHandler;
import nari.MainGridService.handler.GetForecastMessageHandler;
import nari.MainGridService.handler.GetHistoryMessageHandler;
import nari.MainGridService.handler.GetViewGeometryHandler;
import nari.MainGridService.handler.QueryDXDByYXGTHandler;
import nari.MainGridService.handler.VirtualDevLocatHandler;
import nari.MainGridService.handler.XLGeoQueryHandler;
import nari.parameter.MainGridService.ConditionLocatQuery.ConditionLocatQueryRequest;
import nari.parameter.MainGridService.ConditionLocatQuery.ConditionLocatQueryResponse;
import nari.parameter.MainGridService.FuzzyQuery.FuzzyQueryRequest;
import nari.parameter.MainGridService.FuzzyQuery.FuzzyQueryResponse;
import nari.parameter.MainGridService.GetFileName.GetFileNameRequest;
import nari.parameter.MainGridService.GetFileName.GetFileNameResponse;
import nari.parameter.MainGridService.GetViewGeometry.GetViewGeometryRequest;
import nari.parameter.MainGridService.GetViewGeometry.GetViewGeometryResponse;
import nari.parameter.MainGridService.QueryDXDByYXGT.QueryDXDByYXGTRequest;
import nari.parameter.MainGridService.QueryDXDByYXGT.QueryDXDByYXGTResponse;
import nari.parameter.MainGridService.XLGeoQuery.XLGeoQueryRequest;
import nari.parameter.MainGridService.XLGeoQuery.XLGeoQueryResponse;
import nari.parameter.MainGridService.geoLocatQuery.GeoLocatQueryRequest;
import nari.parameter.MainGridService.geoLocatQuery.GeoLocatQueryResponse;
import nari.parameter.MainGridService.getIpAdress.GetIpAdressRequest;
import nari.parameter.MainGridService.getIpAdress.GetIpAdressResponse;
import nari.parameter.MainGridService.virtualDevLocat.VirtualDevLocatRequest;
import nari.parameter.MainGridService.virtualDevLocat.VirtualDevLocatResponse;
import nari.parameter.bean.QueryResult;
import nari.parameter.code.ReturnCode;
import nari.parameter.convert.Request;
import nari.parameter.convert.Response;

import org.json.JSONObject;

public class MainGridServiceImpl implements MainGridService {

	public static String charSetConfig = MainGridServiceActivator.charSetConfig;	//获取字符集配置
	public String contentType = "text/html;charset="+charSetConfig;	//设置返回字符集
		
	/**
	 * 1.动态获取屏幕设备
	 */
	@Override
	public String getViewGeometry(String input, HttpServletRequest request,
			HttpServletResponse response) {

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET,POST");
		response.setContentType(contentType);

		System.out.println("动态获取屏幕设备" + input);
		String ImplResp = ""; // 返回到前台的信息
		GetViewGeometryResponse resp = new GetViewGeometryResponse();
		GetViewGeometryRequest req = null;
		
		PrintStream out = null; // 流
		try {
			out = new PrintStream(response.getOutputStream());
			
		try {
			req = Request.convert(input, GetViewGeometryRequest.class);
		} catch (Exception e) {
			System.out.println("传入格式错误");
			resp.setCode(ReturnCode.MISSTYPE);
			out.print(resp.getCode().getMessage());
			return null;
		}

		// 判断传值是否为空
			if (req.validate()) {
				System.out.println("缺少必需值");
				resp.setCode(ReturnCode.NULL);
				out.print(resp.getCode().getMessage());
				return Response.convert(resp);
			}
			
			// 实现业务操作
			GetViewGeometryHandler handler = new GetViewGeometryHandler();
			resp = handler.getViewGeometry(req);
			QueryResult[] respResult = resp.getResult();
			if (respResult == null || respResult.length == 0) {
				System.out.println("无数据");
				resp.setCode(ReturnCode.NODATA);
				out.print(resp.getCode().getMessage());
				return Response.convert(resp);
			}
			
			// 业务结束通过向网页输出返回所需数据
						if (resp.getCode().getCode() != 1000) { // 若执行不成功
							ImplResp = resp.getCode().getMessage(); // 错误信息打印
						} else {
							ImplResp = Response.forDetailFeildGJson(respResult); // 否则打印正确信息
						}

						out.print(ImplResp);

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				out.flush();
				out.close();
			}
			return null;
		}

	/**
	 * 2.具体设备按条件查询
	 */
	@Override
	public String conditionLocatQuery(String input, HttpServletRequest request,
			HttpServletResponse response) {

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET,POST");
		response.setContentType(contentType);
		
		System.out.println("具体设备按条件查询" + input);
		String ImplResp = ""; // 返回到前台的信息
		ConditionLocatQueryResponse resp = new ConditionLocatQueryResponse();
		ConditionLocatQueryRequest req = null;
		PrintStream out = null; // 流
		try {
			out = new PrintStream(response.getOutputStream());
			
		try {
			req = Request.convert(input, ConditionLocatQueryRequest.class);
		} catch (Exception e) {
			System.out.println("传入格式错误");
			resp.setCode(ReturnCode.MISSTYPE);
			out.print(resp.getCode().getMessage());
			return null;
		}

		// 判断传值是否为空
		// 实现业务操作
		ConditionLocatQueryHandler handler = new ConditionLocatQueryHandler();
		resp = handler.conditionLocatQuery(req);
		QueryResult[] respResult = resp.getResult();
		if (respResult == null || respResult.length == 0) {
			System.out.println("无数据");
			resp.setCode(ReturnCode.NODATA);
			ImplResp = "NoData";
		} else {
			ImplResp = Response.forDetailFeildGJson(respResult);
		}
		// 返回前台通过向网页输出返回所需数据
		
			out.print(ImplResp);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
		return null;
	}

	/**
	 * 3.具体设备按坐标查询
	 */
	@Override
	public String geoLocatQuery(String input, HttpServletRequest request,
			HttpServletResponse response) {

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET,POST");
		response.setContentType(contentType);
		
		// System.out.println(input);
		GeoLocatQueryRequest req = null;
		try {
			req = Request.convert(input, GeoLocatQueryRequest.class);
		} catch (Exception e) {
			System.out.println("传入格式错误");
			return null;
		}

		GeoLocatQueryResponse resp = new GeoLocatQueryResponse();
		// 判断传值是否为空
		// 实现业务操作
		GeoLocatQueryHandler handler = new GeoLocatQueryHandler();
		resp = handler.geoLocatQuery(req);
		QueryResult[] respResult = resp.getResult();
		String ImplResp = null;
		if (respResult == null || respResult.length == 0) {
			System.out.println("无数据");
			resp.setCode(ReturnCode.NODATA);
			ImplResp = "NoData";
		} else {
			ImplResp = Response.forDetailFeildGJson(respResult);
		}
		// 返回前台通过向网页输出返回所需数据
		PrintStream out = null; // 流
		try {
			out = new PrintStream(response.getOutputStream());
			out.print(ImplResp);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
		return null;
	}

	/**
	 * 4.获取文件夹下特殊指定文件
	 */
	@Override
	public String getFileName(String input, HttpServletRequest request,
			HttpServletResponse response) {

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET,POST");
		response.setContentType(contentType);
		
		// System.out.println(input);
		GetFileNameRequest req = null;
		try {
			req = Request.convert(input, GetFileNameRequest.class);
		} catch (Exception e) {
			System.out.println("传入格式错误");
			return null;
		}

		GetFileNameResponse resp = new GetFileNameResponse();
		// 判断传值是否为空
		// 实现业务操作
		GetFileNameHandler handler = new GetFileNameHandler();
		resp = handler.getFileName(req);
		String geoResp = Response.convert(resp);
		// 返回前台通过向网页输出返回所需数据
		PrintStream out = null; // 流
		try {
			out = new PrintStream(response.getOutputStream());
			out.print(geoResp);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
		return null;
	}

	/**
	 * 5.由虚拟设备线路得到导线OID
	 */
	@Override
	public String getXLOID(String input, HttpServletRequest request,
			HttpServletResponse response) {

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET,POST");
		response.setContentType(contentType);
		
		// System.out.println(input);
		return null;
	}

	/**
	 * 6.合并完后(框架外)线路查询
	 */
	@Override
	public String XLGeoQuery(String input, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET,POST");
		response.setContentType(contentType);
		
		// 返回前台通过向网页输出返回所需数据
		PrintStream out = null; // 流
		try {
			// out = new PrintStream("D:\\1.txt");
			out = new PrintStream(response.getOutputStream());

			System.out.println("框架外线路查询input：" + input);
			XLGeoQueryRequest req = new XLGeoQueryRequest();
			XLGeoQueryResponse resp = new XLGeoQueryResponse();
			try {
				req = Request.convert(input, XLGeoQueryRequest.class);
			} catch (Exception e) {
				System.out.println("传入参数(request属性类别)格式(JSON转换)出错");
				resp.setCode(ReturnCode.MISSTYPE);
				return Response.convert(resp);
			}
			// 必须传入的值
			if (req.validate()) {
				System.out.println("传入参数缺少必须值");
				resp.setCode(ReturnCode.NULL);
				out.print(Response.convert(resp));
				return Response.convert(resp);
			}
			// 实现业务操作
			XLGeoQueryHandler handler = new XLGeoQueryHandler();
			resp = handler.xLGeoQuery(req);
			QueryResult[] respResult = resp.getResult();
			String ImplResp = null;
			if (respResult == null || respResult.length == 0) {
				System.out.println("无数据");
				resp.setCode(ReturnCode.NODATA);
				ImplResp = "NoData";
			} else {
				ImplResp = Response.forDetailFeildGJson(respResult);
			}

			out.print(ImplResp);
		} catch (IOException e) {
			System.out.println("返回前台打印出错");
		} finally {
			out.flush();
			out.close();
		}
		return null;
	}
	
//	/**
//	 * 7.分页转shp
//	 */
//	@Override
//	public String rownumQuery(String input, HttpServletRequest request,
//			HttpServletResponse response) {
//
//		response.setHeader("Access-Control-Allow-Origin", "*");
//		response.setHeader("Access-Control-Allow-Methods", "GET,POST");
//		response.setContentType(contentType);
//		
//		// System.out.println("属性查询input："+input);
//		ConditionQueryRequest req = new ConditionQueryRequest();
//		ConditionQueryResponse resp = new ConditionQueryResponse();
//		try {
//			req = Request.convert(input, ConditionQueryRequest.class);
//		} catch (Exception e) {
//			System.out.println("传入参数(request属性类别)格式(JSON转换)出错");
//			resp.setCode(ReturnCode.MISSTYPE);
//			return Response.convert(resp);
//		}
//		// condition中classid为必须传入的值
//		if (req.validate()) {
//			System.out.println("传入参数缺少必须值");
//			resp.setCode(ReturnCode.NULL);
//			return Response.convert(resp);
//		}
//		// 实现业务操作
//		RownumQuery handler = new RownumQuery();
//		int rownumBegin = Integer.valueOf(request.getParameter("rownumBegin"));
//		int rownumEnd = Integer.valueOf(request.getParameter("rownumEnd"));
//		resp = handler.rownumQuery(req, rownumBegin, rownumEnd);
//		QueryResult[] respResult = resp.getResult();
//		// 判断返回格式
//		String ImplResp = "";
//		if (req.getTy().equalsIgnoreCase("DZ")) {
//			ImplResp = Response.forDZGeoJason(respResult);
//		} else {
//			ImplResp = Response.forDetailFeildGJson(respResult);
//		}
//		GeoJSONtoSHP JSONtoSHP = new GeoJSONtoSHP();
//		long c = System.currentTimeMillis();
//		ImplResp = ImplResp.replaceAll("\r|\n", "");
//		JSONObject geoJSON = new JSONObject(ImplResp);
//		// JSONObject geoJSON = JSONObject.fromObject(ImplResp);
//		long a = System.currentTimeMillis();
//		System.out.println("转成JSON时间" + (a - c) + "ms");
//
//		String URL = "D:\\1\\变电站(" + rownumBegin + "~" + rownumEnd + "个).shp";
//		JSONtoSHP.geoJSONtoSHP(geoJSON, URL);
//		long b = System.currentTimeMillis();
//		System.out.println("shp转换时间：" + (b - a) + "ms");
//		// 返回前台通过向网页输出返回所需数据
//		// PrintStream out = null; // 流
//		// try {
//		// // out = new PrintStream("D:\\1.txt");
//		// out = new PrintStream(response.getOutputStream());
//		// out.print(ImplResp);
//		// } catch (IOException e) {
//		// System.out.println("返回前台打印出错");
//		// resp.setCode(ReturnCode.PRINTERROR);
//		// return Response.convert(resp);
//		// } finally {
//		// out.flush();
//		// out.close();
//		// }
//		return null;
//	}

	/**
	 * 8.模糊查询(得到设备名字)
	 */
	@Override
	public String fuzzyQuery(String input, HttpServletRequest request,
			HttpServletResponse response) {

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET,POST");
		response.setContentType(contentType);

		// System.out.println("属性查询input："+input);
		FuzzyQueryRequest req = new FuzzyQueryRequest();
		FuzzyQueryResponse resp = new FuzzyQueryResponse();
		try {
			req = Request.convert(input, FuzzyQueryRequest.class);
		} catch (Exception e) {
			System.out.println("传入参数(request属性类别)格式(JSON转换)出错");
			resp.setCode(ReturnCode.MISSTYPE);
			return Response.convert(resp);
		}
		// 必须传入的值
		// if (req.validate()) {
		// System.out.println("传入参数缺少必须值");
		// resp.setCode(ReturnCode.NULL);
		// return Response.convert(resp);
		// }
		// 实现业务操作
		FuzzyQueryHandler handler = new FuzzyQueryHandler();
		resp = handler.fuzzyQuery(req);
		String ImplResp = Response.convert(resp);
		// 返回前台通过向网页输出返回所需数据
		PrintStream out = null; // 流
		try {
			// out = new PrintStream("D:\\1.txt");
			out = new PrintStream(response.getOutputStream());
			out.print(ImplResp);
		} catch (IOException e) {
			System.out.println("返回前台打印出错");
			resp.setCode(ReturnCode.PRINTERROR);
			return Response.convert(resp);
		} finally {
			out.flush();
			out.close();
		}
		return null;
	}

	/**
	 * 9.虚拟设备查询(根据sbId)
	 */
	@Override
	public String virtualDevLocat(String input, HttpServletRequest request,
			HttpServletResponse response) {

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET,POST");
		response.setContentType(contentType);

		System.out.println("虚拟设备input：" + input);
		VirtualDevLocatRequest req = new VirtualDevLocatRequest();
		VirtualDevLocatResponse resp = new VirtualDevLocatResponse();
		try {
			req = Request.convert(input, VirtualDevLocatRequest.class);
		} catch (Exception e) {
			System.out.println("传入参数(request属性类别)格式(JSON转换)出错");
			resp.setCode(ReturnCode.MISSTYPE);
			return Response.convert(resp);
		}
		// 必须传入的值
		if (req.validate()) {
			System.out.println("传入参数缺少必须值");
			resp.setCode(ReturnCode.NULL);
			return Response.convert(resp);
		}
		// 实现业务操作
		VirtualDevLocatHandler handler = new VirtualDevLocatHandler();
		resp = handler.virtualDevLocat(req);
		QueryResult[] respResult = resp.getResult();
		String ImplResp = null;
		if (respResult == null || respResult.length == 0) {
			System.out.println("无数据");
			resp.setCode(ReturnCode.NODATA);
			ImplResp = "NoData";
		} else {
			ImplResp = Response.forDetailFeildGJson(respResult);
		}
		// 返回前台通过向网页输出返回所需数据
		PrintStream out = null; // 流
		try {
			// out = new PrintStream("D:\\1.txt");
			out = new PrintStream(response.getOutputStream());
			out.print(ImplResp);
		} catch (IOException e) {
			System.out.println("返回前台打印出错");
			resp.setCode(ReturnCode.PRINTERROR);
			return Response.convert(resp);
		} finally {
			out.flush();
			out.close();
		}
		return null;
	}

	/**
	 * 10.获取气象台风预测信息
	 */
	@Override
	public String getForecastMessage(String input, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET,POST");
		response.setContentType(contentType);

		System.out.println("台风预测查询input：" + input);
		JSONObject requestJSON = new JSONObject(input);
		if (!requestJSON.has("TFBH")) {
			System.out.println("缺少必需值");
		}
		String TFBH = requestJSON.getString("TFBH");
		// 获取系统当前时间
		// Date date = new Date();
		// SimpleDateFormat format = new
		// SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		// String forecastTime = format.format(date);
		// 从前台获取时间
		if (!requestJSON.has("forecastTime")) {
			System.out.println("缺少必需值");
		}
		String forecastTime = requestJSON.getString("forecastTime"); // 测试数据
		if (forecastTime == null) {
			System.out.println("传入参数缺少必须值");
		} else {
			GetForecastMessageHandler handler = new GetForecastMessageHandler();
			JSONObject responeJSON = handler.getForecastMessage(TFBH,
					forecastTime);
			// 返回前台通过向网页输出返回所需数据
			PrintStream out = null; // 流
			try {
				// out = new PrintStream("D:\\1.txt");
				out = new PrintStream(response.getOutputStream());
				if (responeJSON.has("result")) {
					out.print(responeJSON);
				} else {
					out.print("NoData");
				}

			} catch (IOException e) {
				System.out.println("返回前台打印出错");
			} finally {
				out.flush();
				out.close();
			}
		}
		return null;
	}

	/**
	 * 11.获取气象台风历史信息
	 */
	@Override
	public String getHistoryMessage(String input, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET,POST");
		response.setContentType(contentType);

		System.out.println("台风历史查询input：" + input);
		JSONObject requestJSON = new JSONObject(input);
		if (!requestJSON.has("TFBH")) {
			System.out.println("缺少必需值");
		}
		String TFBH = requestJSON.getString("TFBH");
		String startTime = null;
		String endTime = null;
		if (requestJSON.has("startTime") && requestJSON.has("endTime")) {
			startTime = requestJSON.getString("startTime");
			endTime = requestJSON.getString("endTime");
		}
		GetHistoryMessageHandler handler = new GetHistoryMessageHandler();
		JSONObject responeJSON = handler.getHistoryMessage(TFBH, startTime,
				endTime);
		// 返回前台通过向网页输出返回所需数据
		PrintStream out = null; // 流
		try {
			// out = new PrintStream("D:\\1.txt");
			out = new PrintStream(response.getOutputStream());
			if (responeJSON.has("result")) {
				out.print(responeJSON);
			} else {
				out.print("NoData");
			}
		} catch (IOException e) {
			System.out.println("返回前台打印出错");
		} finally {
			out.flush();
			out.close();
		}
		return null;
	}

	/**
	 * 12.通过杆塔查导线段
	 */
	@Override
	public String queryDXDByYXGT(String input, HttpServletRequest request,
			HttpServletResponse response) {

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET,POST");
		response.setContentType(contentType);

		// 返回前台通过向网页输出返回所需数据
		PrintStream out = null; // 流
		try {
			// out = new PrintStream("D:\\1.txt");
			out = new PrintStream(response.getOutputStream());
			System.out.println("通过杆塔的导线段查询input：" + input);
			QueryDXDByYXGTRequest req = new QueryDXDByYXGTRequest();
			QueryDXDByYXGTResponse resp = new QueryDXDByYXGTResponse();
			try {
				req = Request.convert(input, QueryDXDByYXGTRequest.class);
			} catch (Exception e) {
				System.out.println("传入参数(request属性类别)格式(JSON转换)出错");
				resp.setCode(ReturnCode.MISSTYPE);
				return Response.convert(resp);
			}
			// 必须传入的值（GT条件）
			if (req.validate()) {
				System.out.println("传入参数缺少必须值");
				resp.setCode(ReturnCode.NULL);
				out.print(Response.convert(resp));
			}
			// 实现业务操作
			QueryDXDByYXGTHandler handler = new QueryDXDByYXGTHandler();
			resp = handler.queryDXDByYXGT(req);
			QueryResult[] respResult = resp.getResult();
			String ImplResp = null;
			if (respResult == null || respResult.length == 0) {
				System.out.println("无数据");
				resp.setCode(ReturnCode.NODATA);
				ImplResp = "NoData";
			} else {
				ImplResp = Response.forDetailFeildGJson(respResult);
			}

			out.print(ImplResp);
		} catch (IOException e) {
			System.out.println("返回前台打印出错");
		} finally {
			out.flush();
			out.close();
		}
		return null;
	}
	
	/**
	 * 13.地理信息纠偏
	 */
	@Override
	public String updateRealGeoHandler(String input,
			HttpServletRequest request, HttpServletResponse response) {

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET,POST");
		response.setContentType(contentType);

		System.out.println("地理信息纠偏" + input);
		String ImplResp = ""; // 返回到前台的信息
		String tableName = request.getParameter("input");

		// if (!req.validate()) {
		// resp.setCode(ReturnCode.NULL);
		// return Response.convert(resp);
		// }
		UpdateRealGeoHandler handler = new UpdateRealGeoHandler();

		if (handler.updateRealGeoHandler(tableName)) {
			ImplResp = "更新成功";
		} else {
			ImplResp = "更新失败";
		}
		// String geoResp = Response.forDZGeoJason(resp.getResult());
		// 通过向网页输出返回所需数据
		PrintStream out = null; // 流
		try {
			out = new PrintStream(response.getOutputStream());
			out.print(ImplResp);
		} catch (IOException e) {
			System.out.println("返回前台打印出错");
		} finally {
			out.flush();
			out.close();
		}
		return null;
	}

	/**
	 * 14.获取ip信息
	 */
	@Override
	public String getIpAdress(String input, HttpServletRequest request,
			HttpServletResponse response) {
		
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET,POST");
		response.setContentType(contentType);

		System.out.println("获取ip信息" + input);
		String ImplResp = ""; // 返回到前台的信息
		GetIpAdressResponse resp = new GetIpAdressResponse();
		GetIpAdressRequest req = null;
		
		PrintStream out = null; // 流
		try {
			out = new PrintStream(response.getOutputStream());
			
		try {
			req = Request.convert(input, GetIpAdressRequest.class);
		} catch (Exception e) {
			e.printStackTrace();
//			System.out.println("传入格式错误");
//			resp.setCode(ReturnCode.MISSTYPE);
//			out.print(resp.getCode().getMessage());
			return null;
		}

//		// 判断传值是否为空
//			if (req.validate()) {
//				System.out.println("缺少必需值");
//				resp.setCode(ReturnCode.NULL);
//				out.print(resp.getCode().getMessage());
//				return Response.convert(resp);
//			}
			
			// 实现业务操作
			GetIpAdressHandler handler = new GetIpAdressHandler();
			resp = handler.getIpAdress(req);
			// 业务结束通过向网页输出返回所需数据
			if (resp.getCode().getCode() != 1000) { // 若执行不成功
				ImplResp = resp.getCode().getMessage(); // 错误信息打印
			} else {
				ImplResp = resp.getResultJSONArray().toString(); // 否则打印正确信息
			}

			out.print(ImplResp);
		} catch (IOException e) {
			System.out.println("返回前台打印出错");
			resp.setCode(ReturnCode.PRINTERROR);
			return Response.convert(resp);
		} finally {
			out.flush();
			out.close();
		}
		return null;
	}
		
	
}
