package nari.MapService.Service.impl;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nari.Logger.LoggerManager;
import nari.MapService.Service.interfaces.MapService;
import nari.MapService.handler.AddBookMarkHandler;
import nari.MapService.handler.GetVectorMapByCacheHandler;
import nari.MapService.handler.GetVectorMapHandler;
import nari.MapService.handler.GetMapHandler;
import nari.MapService.handler.MapConfigHandler;
import nari.MapService.handler.ModifyBookMarkHandler;
import nari.MapService.handler.RemoveBookMarkHandler;
import nari.parameter.MapService.BookMark.AddBookMarkRequest;
import nari.parameter.MapService.BookMark.AddBookMarkResponse;
import nari.parameter.MapService.BookMark.ModifyBookMarkRequest;
import nari.parameter.MapService.BookMark.ModifyBookMarkResponse;
import nari.parameter.MapService.BookMark.RemoveBookMarkRequest;
import nari.parameter.MapService.BookMark.RemoveBookMarkResponse;
import nari.parameter.MapService.GetMap.GetMapRequest;
import nari.parameter.MapService.GetMap.GetMapResponse;
import nari.parameter.MapService.GetMapConfig.MapConfigRequest;
import nari.parameter.MapService.GetMapConfig.MapConfigResponse;
import nari.parameter.MapService.GetVectorMap.GetVectorMapRequest;
import nari.parameter.MapService.GetVectorMap.GetVectorMapResponse;
import nari.parameter.bean.QueryResult;
import nari.parameter.code.ReturnCode;
import nari.parameter.convert.Request;
import nari.parameter.convert.Response;

public class MapServiceImpl implements MapService {

	private nari.Logger.Logger logger = LoggerManager.getLogger(this.getClass());
	
	@Override
	public String addBookMark(String input, HttpServletRequest request,
			HttpServletResponse response) {
		AddBookMarkRequest req = Request.convert(input,
				AddBookMarkRequest.class);
		AddBookMarkResponse resp = new AddBookMarkResponse();
		// 未传数据书签名或地图MapView
		if (!req.validate()) {
			resp.setCode(ReturnCode.FAILED);
			return Response.convert(resp);
		} else {
			// 添加至数据库，
			AddBookMarkHandler handler = new AddBookMarkHandler();
			resp = handler.addBookMark(req);
			return Response.convert(resp);
		}

	}

	@Override
	public String modifyBookMark(String input, HttpServletRequest request,
			HttpServletResponse response) {
		ModifyBookMarkRequest req = Request.convert(input,
				ModifyBookMarkRequest.class);
		ModifyBookMarkResponse resp = new ModifyBookMarkResponse();
		if (!req.validate()) {
			resp.setCode(ReturnCode.FAILED);
			return Response.convert(resp);
		} else {
			ModifyBookMarkHandler handler = new ModifyBookMarkHandler();
			resp = handler.modifyBookMark(req);
			return Response.convert(resp);
		}

	}

	@Override
	public String removeBookMark(String input, HttpServletRequest request,
			HttpServletResponse response) {
		RemoveBookMarkRequest req = Request.convert(input,
				RemoveBookMarkRequest.class);
		RemoveBookMarkResponse resp = new RemoveBookMarkResponse();
		if (!req.validate()) {
			resp.setCode(ReturnCode.FAILED);
			return Response.convert(resp);
		} else {
			RemoveBookMarkHandler handler = new RemoveBookMarkHandler();
			resp = handler.removeBookMark(req);
			return Response.convert(resp);
		}
	}

	@Override
	public String getMap(HttpServletRequest request,
			HttpServletResponse response) {

		//response.setHeader("Access-Control-Allow-Origin", "*");
		//response.setHeader("Access-Control-Allow-Methods", "GET,POST");
		//response.setContentType("text/html;charset=gbk");

		GetMapRequest req = new GetMapRequest();
		req.setRow(Integer.parseInt(request.getParameter("y")));
		req.setColumn(Integer.parseInt(request.getParameter("x")));
		req.setLevel(Integer.parseInt(request.getParameter("z")));
		// GetMapRequest req = Request.convert(input ,GetMapRequest.class);
		GetMapResponse resp = new GetMapResponse();
		if (!req.validate()) {
			resp.setCode(ReturnCode.NULL);
			return Response.convert(resp);
		} else {
			GetMapHandler handler = new GetMapHandler();
			resp = handler.getMap(req);
			if (resp == null) {
				return null;
			}
			// 返回所需数据
			OutputStream output = null;
			response.reset();
			try {
				output = new BufferedOutputStream(response.getOutputStream());
				output.write(resp.getImg());
				output.flush();
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}


	@Override
	public String getMapConfig(String input, HttpServletRequest request,
			HttpServletResponse response) {
		MapConfigRequest req = Request.convert(input, MapConfigRequest.class);
		MapConfigResponse resp = new MapConfigResponse();
		if (!req.validate()) {
			resp.setCode(ReturnCode.FAILED);
			return Response.convert(resp);
		} else {
			MapConfigHandler handler = new MapConfigHandler();
			resp = handler.getMapConfig(req);
			return Response.convert(resp);
		}

	}

	/**
	 * 矢量出图
	 */

	@Override
	public String getVectorMap(String input, HttpServletRequest request,
			HttpServletResponse response) {
		
		GetVectorMapRequest req = new GetVectorMapRequest();
		GetVectorMapResponse resp = new GetVectorMapResponse();
		try {
			req = Request.convert(input, GetVectorMapRequest.class);
		} catch (Exception e) {
			logger.info("传入参数(request属性类别)格式(JSON转换)出错");
			resp.setCode(ReturnCode.MISSTYPE);
			return Response.convert(resp);
		}

		// bbox,classCondition为必须传入的值
		if (req.validate()) {
			logger.info("传入参数缺少必须值");
			resp.setCode(ReturnCode.NULL);
			return Response.convert(resp);
		} else {
			// 实现空间业务查询操作
			GetVectorMapHandler handler = new GetVectorMapHandler();
			resp = handler.getVectorMap(req);
			QueryResult[] respResult = resp.getResult();
			if(respResult == null || respResult.length == 0){
				logger.info("无数据");
				resp.setCode(ReturnCode.NODATA);
				return Response.convert(resp);
			}
			// String ImplResp = Response.convert(respResult);
			String geoResp = Response.forDetailFeildGJson(respResult);
			if (!(resp.getRequestExtend().equalsIgnoreCase(""))) {
				geoResp = resp.getRequestExtend() + "@!@" + geoResp;
			}
			// String DZGeoResp = Response.forDZGeoJason(respResult);
			// 返回前台通过向网页输出返回所需数据
			PrintStream out = null; // 流
			try {
				// out = new
				// PrintStream("C:\\Users\\huqiuxiang\\Desktop\\1.txt");
				out = new PrintStream(response.getOutputStream());
				// out.print(ImplResp);
				out.print(geoResp);
//				logger.info(geoResp);
				// out.print(DZGeoResp);
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

	/**
	 * 动态矢量出图(缓存查询)
	 */
	@Override
	public String getVectorMapByCache(String input, HttpServletRequest request,
			HttpServletResponse response) {

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
			// 实现空间业务查询操作
			GetVectorMapByCacheHandler handler = new GetVectorMapByCacheHandler();
			resp = handler.getVectorMapByCache(req);
			String geoResp = Response.forDetailFeildGJson(resp.getResult());
			if(!(resp.getRequestExtend().equalsIgnoreCase(""))){
				geoResp = resp.getRequestExtend() + "@" + geoResp;
			}
			PrintStream out = null; // 流
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
