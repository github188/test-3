package nari.SymbolService.Service.impl;

import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nari.Logger.Logger;
import nari.Logger.LoggerManager;
import nari.SymbolService.Service.interfaces.SymbolService;
import nari.SymbolService.bean.GetSymbolIdStatusRequest;
import nari.SymbolService.bean.GetSymbolIdStatusResponse;
import nari.SymbolService.handle.GetLabelRenderHandler;
import nari.SymbolService.handle.GetRenderRuleObjHandler;
import nari.SymbolService.handle.GetRenderStyleObjHandler;
import nari.SymbolService.handle.GetSymbolIdStatusHandler;
import nari.SymbolService.handle.GetSymbolMemberHandler;
import nari.parameter.SymbolService.getLabelRender.GetLabelRenderResponse;
import nari.parameter.code.ReturnCode;
import nari.parameter.convert.Request;
import nari.parameter.convert.Response;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SymbolServiceImpl implements SymbolService {

	private Logger logger = LoggerManager.getLogger(this.getClass());

	@Override
	public String getRenderRuleObj(HttpServletRequest request,
			HttpServletResponse response) {

		GetRenderRuleObjHandler handler = new GetRenderRuleObjHandler();
		JSONArray renderRule = handler.getRenderRuleObj();

		// 返回前台通过向网页输出返回所需数据
		PrintStream out = null; // 流
		try {
			out = new PrintStream(response.getOutputStream());
			out.print(renderRule.toString());
		} catch (IOException e) {
			logger.info("返回前台打印出错");
		} finally {
			out.flush();
			out.close();
		}
		return null;
	}

	@Override
	public String getRenderStyleObj(HttpServletRequest request,
			HttpServletResponse response) {

		GetRenderStyleObjHandler handler = new GetRenderStyleObjHandler();
		JSONArray renderStyle = handler.getRenderStyleObj();
		// 返回前台通过向网页输出返回所需数据
		PrintStream out = null; // 流
		try {
			out = new PrintStream(response.getOutputStream());
			out.print(renderStyle.toString());
		} catch (IOException e) {
			logger.info("返回前台打印出错");
		} finally {
			out.flush();
			out.close();
		}
		return null;
	}

	// @Override
	// public String dataToJSON(String input, HttpServletRequest request,
	// HttpServletResponse response) {
	//
	// response.setHeader("Access-Control-Allow-Origin", "*");
	// response.setHeader("Access-Control-Allow-Methods", "GET,POST");
	// response.setContentType("text/html;charset=gbk");
	//
	// JSONObject inputJSON = JSONObject.fromObject(input);
	// int sqlNum = Integer.valueOf(inputJSON.getString("sqlNum"));
	// String[] returnField = null;
	// if (inputJSON.has("returnField")) {
	// if (!(inputJSON.getJSONArray("returnField").isEmpty())) {
	// Object[] field = (inputJSON.getJSONArray("returnField")
	// .toArray());
	// returnField = new String[field.length];
	// for (int i = 0; i < field.length; i++) {
	// returnField[i] = String.valueOf(field[i]);
	// }
	// }
	// }
	// DataToJSONHandler handler = new DataToJSONHandler();
	// JSONArray dataArray = handler.dataToJSON(sqlNum, returnField);
	//
	// // 返回前台通过向网页输出返回所需数据
	// PrintStream out = null; // 流
	// try {
	// out = new PrintStream(response.getOutputStream());
	// if(dataArray==null){
	// out.print("NODATA");
	// }else{
	// out.print(dataArray.toString());
	// }
	// } catch (IOException e) {
	// logger.info("返回前台打印出错");
	// } finally {
	// out.flush();
	// out.close();
	// }
	// return null;
	// }

	@Override
	public String getSymbolMember(HttpServletRequest request,
			HttpServletResponse response) {

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET,POST");
		response.setContentType("text/html;charset=gbk");

		System.out.println("获取符号成员信息");
		GetSymbolMemberHandler handler = new GetSymbolMemberHandler();
		JSONObject symbolMember = handler.getSymbolMember();
		// 返回前台通过向网页输出返回所需数据
		PrintStream out = null; // 流
		try {
			out = new PrintStream(response.getOutputStream());
			out.print(symbolMember.toString());
		} catch (IOException e) {
			logger.info("返回前台打印出错");
		} finally {
			out.flush();
			out.close();
		}
		return null;
	}

	/**
	 * 5.获取图层信息
	 */
	@Override
	public String getLabelRender(HttpServletRequest request,
			HttpServletResponse response) {

		GetLabelRenderResponse resp = new GetLabelRenderResponse();
		GetLabelRenderHandler handler = new GetLabelRenderHandler();
		String ImplResp = "";
		PrintStream out = null; // 流
		try {
			out = new PrintStream(response.getOutputStream());
			resp = handler.getLabelRender();
			// 返回前台通过向网页输出返回所需数据
			// 业务结束通过向网页输出返回所需数据
			if (resp.getCode().getCode() != 1000) { // 若执行不成功
				ImplResp = resp.getCode().getMessage(); // 错误信息打印
			} else {
				ImplResp = resp.getResultJSONArray().toString(); // 否则打印正确信息
			}
			out.print(ImplResp);
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

	@Override
	public String getSymbolIdStatus(String input, HttpServletRequest request,
			HttpServletResponse response) {
		GetSymbolIdStatusRequest req = new GetSymbolIdStatusRequest();
		GetSymbolIdStatusResponse resp = new GetSymbolIdStatusResponse();

		System.out.println("modelsymbol查询");
		try {
			req = Request.convert(input, GetSymbolIdStatusRequest.class);

		} catch (Exception e) {
			System.out.println("input data error");
			return Response.convert(resp);
		}

		// if(req.validate()){
		// System.out.println("input data error");
		// resp.setCode(ReturnCode.NULL);
		// return Response.convert(resp);
		// }else{
		GetSymbolIdStatusHandler handler = new GetSymbolIdStatusHandler();
		resp = handler.getSymbolIdStatus(req);
		String implResp = resp.getModelSymbol().toString();

		PrintStream out = null;
		try {
			out = new PrintStream(response.getOutputStream());
			out.print(implResp);
		} catch (IOException e) {
			System.out.println("response print error!");
			return Response.convert(resp);
		} finally {
			out.flush();
			out.close();
		}

		return null;
	}
}
