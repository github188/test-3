package nari.MongoDBUpdate.service.impl;

import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nari.MongoDBUpdate.bean.UdateFeederDevRequest;
import nari.MongoDBUpdate.bean.UdateFeederDevResponse;
import nari.MongoDBUpdate.bean.UpdateThematicRequest;
import nari.MongoDBUpdate.bean.UpdateThematicResponse;
import nari.MongoDBUpdate.handler.UdateFeederDevHandler;
import nari.MongoDBUpdate.handler.UpdateThematicHandler;
import nari.MongoDBUpdate.service.interfaces.MongoUpdateService;
import nari.parameter.code.ReturnCode;
import nari.parameter.convert.Request;
import nari.parameter.convert.Response;

public class MongoUpdateServiceImpl implements MongoUpdateService {

	@Override
	public String updateThematic(String input, HttpServletRequest request,
			HttpServletResponse response) {
		UpdateThematicRequest req = new UpdateThematicRequest();
		UpdateThematicResponse resp = new UpdateThematicResponse();

		try {
			req = Request.convert(input, UpdateThematicRequest.class);

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
		UpdateThematicHandler handler = new UpdateThematicHandler();
		try {
			resp = handler.updateThematic(req);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		String implResp = Response.convert(resp);

		PrintStream out = null;
		try {
			out = new PrintStream(response.getOutputStream());
			out.print(implResp);
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
	 * 大馈线设备更新
	 */
	@Override
	public String udateFeederDev(String input, HttpServletRequest request,
			HttpServletResponse response) {
		UdateFeederDevRequest req = new UdateFeederDevRequest();
		UdateFeederDevResponse resp = new UdateFeederDevResponse();

		try {
			req = Request.convert(input, UdateFeederDevRequest.class);

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
		UdateFeederDevHandler handler = new UdateFeederDevHandler();
		try {
			resp = handler.udateFeederDev(req);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String implResp = Response.convert(resp);

		PrintStream out = null;
		try {
			out = new PrintStream(response.getOutputStream());
			out.print(implResp);
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

}
