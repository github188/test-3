package nari.BaseService.Service.impl;

import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nari.BaseService.Service.interfaces.BaseService;
import nari.BaseService.bean.GetAreaGeoMessageRequest;
import nari.BaseService.bean.GetAreaGeoMessageResponse;
import nari.BaseService.bean.GetConvexHullRequest;
import nari.BaseService.bean.GetConvexHullResponse;
import nari.BaseService.bean.GetFileServiceRequest;
import nari.BaseService.bean.GetFileServiceResponse;
import nari.BaseService.bean.GetToolBoxRequest;
import nari.BaseService.handler.GetAreaGeoMessageHandler;
import nari.BaseService.handler.GetClassMetaHandler;
import nari.BaseService.handler.GetConnectionHandler;
import nari.BaseService.handler.GetConvexHullHandler;
import nari.BaseService.handler.GetDocumentModelHandler;
import nari.BaseService.handler.GetFieldHandler;
import nari.BaseService.handler.GetFileServiceHandler;
import nari.BaseService.handler.GetMapClassListHandler;
import nari.BaseService.handler.GetModelMetaHandler;
import nari.BaseService.handler.GetPSRDefHandler;
import nari.BaseService.handler.GetToolBoxConfigHandler;
import nari.BaseService.handler.GetTransCodeHandler;
import nari.BaseService.handler.GetVoltageLevelListHandler;
import nari.BaseService.handler.JudgeFeildNameHandler;
import nari.BaseService.handler.UpdXLGeoMessageHandler;
import nari.Logger.Logger;
import nari.Logger.LoggerManager;
import nari.parameter.BaseService.GetClassMeta.GetClassMetaRequest;
import nari.parameter.BaseService.GetClassMeta.GetClassMetaResponse;
import nari.parameter.BaseService.GetConnection.GetConnectionRequest;
import nari.parameter.BaseService.GetConnection.GetConnectionResponse;
import nari.parameter.BaseService.GetField.GetFieldRequest;
import nari.parameter.BaseService.GetField.GetFieldResponse;
import nari.parameter.BaseService.GetMapClassList.GetMapClassListRequest;
import nari.parameter.BaseService.GetMapClassList.GetMapClassListResponse;
import nari.parameter.BaseService.GetModelMeta.GetModelMetaRequest;
import nari.parameter.BaseService.GetModelMeta.GetModelMetaResponse;
import nari.parameter.BaseService.GetPSRDef.GetPSRDefRequest;
import nari.parameter.BaseService.GetPSRDef.GetPSRDefResponse;
import nari.parameter.BaseService.GetVoltageLevelList.GetVoltageLevelListResponse;
import nari.parameter.BaseService.JudgeFeildName.JudgeFeildNameRequest;
import nari.parameter.BaseService.JudgeFeildName.JudgeFeildNameResponse;
import nari.parameter.BaseService.UpdXLGeoMessage.UpdXLGeoMessageRequest;
import nari.parameter.BaseService.UpdXLGeoMessage.UpdXLGeoMessageResponse;
import nari.parameter.BaseService.getDocumentModel.GetDocumentModelRequest;
import nari.parameter.BaseService.getDocumentModel.GetDocumentModelResponse;
import nari.parameter.BaseService.getTransCode.GetTransCodeRequest;
import nari.parameter.BaseService.getTransCode.GetTransCodeResponse;
import nari.parameter.code.ReturnCode;
import nari.parameter.convert.Request;
import nari.parameter.convert.Response;

import org.json.JSONArray;


public class BaseServiceImpl implements BaseService {

	private Logger logger = LoggerManager.getLogger(this.getClass());
	
	/**
	 * 1.建立连接
	 */
	@Override
	public String getConnection(String input, HttpServletRequest request,
			HttpServletResponse response) {

		GetConnectionRequest req = Request.convert(input,
				GetConnectionRequest.class);
		GetConnectionResponse resp = new GetConnectionResponse();
		if (!req.validate()) {
			resp.setCode(ReturnCode.NULL);
			return Response.convert(resp);
		}

		GetConnectionHandler handler = new GetConnectionHandler();
		resp = handler.getConnection(req);
		return Response.convert(resp);
	}

	@Override
	public String disConnection(String input, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 3.查询前获取设备类型(由6种大类获取其对应classID数组)
	 */
	@Override
	public String getPSRDef(String input, HttpServletRequest request,
			HttpServletResponse response) {

		logger.info("查询前获取设备类型请求" + input);
		String ImplResp = "";
		GetPSRDefRequest req = Request.convert(input, GetPSRDefRequest.class);
		GetPSRDefResponse resp = new GetPSRDefResponse();
		

		PrintStream out = null; // 流
		try {
			out = new PrintStream(response.getOutputStream());
			if (req.validate()) {
				resp.setCode(ReturnCode.NULL);
				out.print(resp.getCode().getMessage());
				return Response.convert(resp);
			}

			GetPSRDefHandler handler = new GetPSRDefHandler();
			resp = handler.getPSRDef(req);

			// 业务结束通过向网页输出返回所需数据
			if (resp.getCode().getCode() != 1000) { // 若执行不成功
				ImplResp = resp.getCode().getMessage(); // 错误信息打印
			} else {
				ImplResp = Response.convert(resp); // 否则打印正确信息
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
	 * 4.根据类型得到所属字段
	 */
	@Override
	public String getField(String input, HttpServletRequest request,
			HttpServletResponse response) {

		logger.info("查询前根据类型得到所属字段" + input);
		GetFieldRequest req = Request.convert(input, GetFieldRequest.class);
		GetFieldResponse resp = new GetFieldResponse();
		String ImplResp = ""; // 返回到前台的信息

		PrintStream out = null; // 流
		try {
			out = new PrintStream(response.getOutputStream());
			if (req.validate()) {
				resp.setCode(ReturnCode.NULL);
				out.print(resp.getCode().getMessage());
				return Response.convert(resp);
			}
			GetFieldHandler handler = new GetFieldHandler();
			resp = handler.getField(req);
			// 业务结束通过向网页输出返回所需数据
			if (resp.getCode().getCode() != 1000) { // 若执行不成功
				ImplResp = resp.getCode().getMessage(); // 错误信息打印
			} else {
				ImplResp = Response.convert(resp); // 否则打印正确信息
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
	 * 5.得到含有需要字段的设备类型数组
	 */
	@Override
	public String judgeFeildName(String input, HttpServletRequest request,
			HttpServletResponse response) {

		logger.info("得到含有需要字段的设备类型数组" + input);
		String ImplResp = ""; // 返回到前台的信息
		JudgeFeildNameRequest req = Request.convert(input,
				JudgeFeildNameRequest.class);
		JudgeFeildNameResponse resp = new JudgeFeildNameResponse();
		

		PrintStream out = null; // 流
		try {
			out = new PrintStream(response.getOutputStream());
			// logger.info(input);
			// if (!req.validate()) {
			// resp.setCode(ReturnCode.NULL);
			// return Response.convert(resp);
			// }
			JudgeFeildNameHandler handler = new JudgeFeildNameHandler();
			resp = handler.judgeFeildName(req);

			// 业务结束通过向网页输出返回所需数据
			if (resp.getCode().getCode() != 1000) { // 若执行不成功
				ImplResp = resp.getCode().getMessage(); // 错误信息打印
			} else {
				ImplResp = Response.convert(resp); // 否则打印正确信息
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
	 * 6.更新线路地理信息表
	 */
	@Override
	public String updXLGeoMessage(String input, HttpServletRequest request,
			HttpServletResponse response) {

		logger.info("更新线路地理信息表" + input);
		String ImplResp = ""; // 返回到前台的信息
		UpdXLGeoMessageRequest req = Request.convert(input,
				UpdXLGeoMessageRequest.class);
		UpdXLGeoMessageResponse resp = new UpdXLGeoMessageResponse();

		PrintStream out = null; // 流
		try {
			out = new PrintStream(response.getOutputStream());
			// if (!req.validate()) {
			// resp.setCode(ReturnCode.NULL);
			// return Response.convert(resp);
			// }
			UpdXLGeoMessageHandler handler = new UpdXLGeoMessageHandler();
			if (handler.updXLGeoMessage(req)) {
				resp.setCode(ReturnCode.SUCCESS);
			} else {
				resp.setCode(ReturnCode.FAILED);
			}
			// String geoResp = Response.forDZGeoJason(resp.getResult());
			// 业务结束通过向网页输出返回所需数据
			if (resp.getCode().getCode() != 1000) { // 若执行不成功
				ImplResp = resp.getCode().getMessage(); // 错误信息打印
			} else {
				ImplResp = Response.convert(resp); // 否则打印正确信息
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

	/**
	 * 7.得到modelid相关信息
	 */
	@Override
	public String getModelMeta(String input, HttpServletRequest request,
			HttpServletResponse response) {

		logger.info("得到modelid相关信息" + input);
		String ImplResp = ""; // 返回到前台的信息
		GetModelMetaRequest req = Request.convert(input,
				GetModelMetaRequest.class);
		GetModelMetaResponse resp = new GetModelMetaResponse();

		PrintStream out = null; // 流
		try {
			out = new PrintStream(response.getOutputStream());
			// if (req.validate()) {
			// resp.setCode(ReturnCode.NULL);
			// return Response.convert(resp);
			// }
			GetModelMetaHandler handler = new GetModelMetaHandler();
			resp = handler.getModelMeta(req);
			// String geoResp = Response.forDZGeoJason(resp.getResult());
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
	
	/**
	 * 8.得到classid相关信息
	 */
	@Override
	public String getClassMeta(String input, HttpServletRequest request,
			HttpServletResponse response) {

		logger.info("得到classid相关信息" + input);
		String ImplResp = ""; // 返回到前台的信息
		GetClassMetaRequest req = Request.convert(input,
				GetClassMetaRequest.class);
		GetClassMetaResponse resp = new GetClassMetaResponse();

		PrintStream out = null; // 流
		try {
			out = new PrintStream(response.getOutputStream());
			// if (req.validate()) {
			// resp.setCode(ReturnCode.NULL);
			// return Response.convert(resp);
			// }
			GetClassMetaHandler handler = new GetClassMetaHandler();
			resp = handler.getClassMeta(req);
			// String geoResp = Response.forDZGeoJason(resp.getResult());
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
	
	/**
	 * 9.得到模型种类相关信息(mapId)
	 */
	@Override
	public String getDocumentModel(String input, HttpServletRequest request,
			HttpServletResponse response) {

		logger.info("得到模型种类相关信息(mapId)" + input);
		String ImplResp = ""; // 返回到前台的信息
		GetDocumentModelRequest req = Request.convert(input,
				GetDocumentModelRequest.class);
		GetDocumentModelResponse resp = new GetDocumentModelResponse();

		PrintStream out = null; // 流
		try {
			out = new PrintStream(response.getOutputStream());
			// if (req.validate()) {
			// resp.setCode(ReturnCode.NULL);
			// return Response.convert(resp);
			// }
			GetDocumentModelHandler handler = new GetDocumentModelHandler();
			resp = handler.getDocumentModel(req);
			// String geoResp = Response.forDZGeoJason(resp.getResult());
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
	
	/**
	 * 10.得到转义表码值及相关信息
	 */
	@Override
	public String getTransCode(String input, HttpServletRequest request,
			HttpServletResponse response) {

		logger.info("得到转义表码值");
		String ImplResp = ""; // 返回到前台的信息
		GetTransCodeRequest req = Request.convert(input,
				GetTransCodeRequest.class);
		GetTransCodeResponse resp = new GetTransCodeResponse();
		PrintStream out = null; // 流
		try {
			out = new PrintStream(response.getOutputStream());
			// if (req.validate()) {
			// resp.setCode(ReturnCode.NULL);
			// return Response.convert(resp);
			// }
			GetTransCodeHandler handler = new GetTransCodeHandler();
			resp = handler.getTransCode(req);
			// String geoResp = Response.forDZGeoJason(resp.getResult());
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
	
	/**
	 * 11.获取电压等级列表
	 */
	@Override
	public String getVoltageLevelList(HttpServletRequest request,
			HttpServletResponse response) {

		System.out.println("获取电压等级列表");
		String implResp = ""; // 返回到前台的信息
		
		GetVoltageLevelListResponse resp = new GetVoltageLevelListResponse();
		PrintStream out = null; // 流
		
		try {
			out = new PrintStream(response.getOutputStream());
			GetVoltageLevelListHandler handler = new GetVoltageLevelListHandler();
			resp = handler.getVoltageLevelList();
			if (resp.getCode().getCode() != ReturnCode.SUCCESS_CODE) { // 若执行不成功
				implResp = resp.getCode().getMessage(); // 错误信息打印
			} else {
				implResp = Response.convert(resp); // 否则打印正确信息
			}

			//implResp = new String(implResp.getBytes("gbk"), "iso-8859-1");
			out.print(implResp);
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
	 * 12.获取图类型对应的设备类型
	 */
	@Override
	public String getMapClassList(String input,HttpServletRequest request,
			HttpServletResponse response) {
		
		System.out.println("获取图类型对应的设备类型");
		String implResp = ""; // 返回到前台的信息
		GetMapClassListRequest req = Request.convert(input, GetMapClassListRequest.class);
		GetMapClassListResponse resp = new GetMapClassListResponse();
		
		PrintStream out = null; // 流
		try {
			out = new PrintStream(response.getOutputStream());
			GetMapClassListHandler handler = new GetMapClassListHandler();
			resp = handler.getMapClassList(req);

			if (resp.getCode().getCode() != ReturnCode.SUCCESS_CODE) { // 若执行不成功
				implResp = resp.getCode().getMessage(); // 错误信息打印
			} else {
				implResp = Response.convert(resp); // 否则打印正确信息
			}

			out.print(implResp);
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
	 * 13.获取geo的相关地理信息(范围，图形等)
	 */
	@Override
	public String getAreaGeoMessage(String input, HttpServletRequest request,
			HttpServletResponse response) {

		System.out.println("获取geo的相关地理信息");
		String implResp = ""; // 返回到前台的信息
		GetAreaGeoMessageRequest req = Request.convert(input, GetAreaGeoMessageRequest.class);
		GetAreaGeoMessageResponse resp = new GetAreaGeoMessageResponse();
		
		PrintStream out = null; // 流
		try {
			out = new PrintStream(response.getOutputStream());
			GetAreaGeoMessageHandler handler = new GetAreaGeoMessageHandler();
			resp = handler.getAreaGeoMessage(req);

			if (resp.getCode().getCode() != ReturnCode.SUCCESS_CODE) { // 若执行不成功
				implResp = resp.getCode().getMessage(); // 错误信息打印
			} else {
				implResp = Response.convert(resp); // 否则打印正确信息
			}

			out.print(implResp);
		} catch (IOException e) {
			System.out.println("返回前台打印出错");
		} finally {
			out.flush();
			out.close();
		}
		return null;
	}

	/**
	 * 14.获取设备的最小凸包多边形
	 */
	public String getConvexHull(String input, HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("获取设备的最小凸包多边形");
		String implResp = ""; // 返回到前台的信息
		GetConvexHullRequest req = Request.convert(input, GetConvexHullRequest.class); 
		GetConvexHullResponse resp = new GetConvexHullResponse();
		
		PrintStream out = null; // 流
		try {
			out = new PrintStream(response.getOutputStream());
			GetConvexHullHandler handler = new GetConvexHullHandler();
			resp = handler.getConvexHull(req);

			if (resp.getCode().getCode() != ReturnCode.SUCCESS_CODE) { // 若执行不成功
				implResp = resp.getCode().getMessage(); // 错误信息打印
			} else {
				implResp = Response.convert(resp); // 否则打印正确信息
			}

			out.print(implResp);
		} catch (IOException e) {
			System.out.println("返回前台打印出错");
		} finally {
			out.flush();
			out.close();
		}
		return null;
	}

	/**
	 * 15.下载文档服务
	 */
	@Override
	public String getFileService(String input, HttpServletRequest request,
			HttpServletResponse response) {
		
		System.out.println("下载文档服务");
		
		String implResp = ""; // 返回到前台的信息
		GetFileServiceRequest req = Request.convert(input, GetFileServiceRequest.class); 
		response.setHeader("Content-Disposition", "attachment;filename="+req.getPathName());
		GetFileServiceResponse resp = new GetFileServiceResponse();
		
		PrintStream out = null; // 流
		try {
			out = new PrintStream(response.getOutputStream());
			GetFileServiceHandler handler = new GetFileServiceHandler();
			resp = handler.GetFile(req);

			if (resp.getCode().getCode() != ReturnCode.SUCCESS_CODE) { // 若执行不成功
				implResp = resp.getCode().getMessage(); // 错误信息打印
				out.print(implResp);
			} else {
				implResp = Response.convert(resp); // 否则打印正确信息
				out.write(resp.getBytes());
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
	 * 16.获取工具箱配置
	 */
	@Override
	public String getToolBoxconfig(String input, HttpServletRequest request,
			HttpServletResponse response) {
		
		System.out.println("获取工具箱配置");
		String implResp = ""; // 返回到前台的信息  
		PrintStream out = null; // 流
		GetToolBoxRequest req = Request.convert(input, GetToolBoxRequest.class);
		try {
			out = new PrintStream(response.getOutputStream());
			GetToolBoxConfigHandler handler = new GetToolBoxConfigHandler();
			JSONArray resp = handler.gettoolBoxConfig(req);

//			if (resp.getCode().getCode() != ReturnCode.SUCCESS_CODE) { // 若执行不成功
//				implResp = resp. // 错误信息打印
//				out.print(implResp);
//			} else {
				implResp = resp.toString(); // 否则打印正确信息
				out.write(implResp.getBytes());
//			}

		} catch (IOException e) {
			System.out.println("返回前台打印出错");
		} finally {
			out.flush();
			out.close();
		}
		return null;
	}
}
 