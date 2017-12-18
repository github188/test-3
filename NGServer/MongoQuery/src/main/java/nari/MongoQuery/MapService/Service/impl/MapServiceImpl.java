package nari.MongoQuery.MapService.Service.impl;

import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nari.MongoQuery.MapService.Service.interfaces.MapService;
import nari.MongoQuery.MapService.handler.GetVectorMapHandler;
import nari.parameter.MapService.GetVectorMap.GetVectorMapRequest;
import nari.parameter.MapService.GetVectorMap.GetVectorMapResponse;
import nari.parameter.bean.QueryResult;
import nari.parameter.code.ReturnCode;
import nari.parameter.convert.Request;
import nari.parameter.convert.Response;

public class MapServiceImpl implements MapService {

	@Override
	public String getVectorMap(String input, HttpServletRequest request,
			HttpServletResponse response) {

		//response.setHeader("Access-Control-Allow-Origin", "*");
		//response.setHeader("Access-Control-Allow-Methods", "GET,POST");
		//response.setContentType("text/html;charset=gbk");

		GetVectorMapRequest req = new GetVectorMapRequest();
		GetVectorMapResponse resp = new GetVectorMapResponse();

		req = Request.convert(input, GetVectorMapRequest.class);
		if (req.validate()) {
			System.out.println("parameter enssiential error");
			resp.setCode(ReturnCode.NULL);
			return Response.convert(resp);
		} else {
			GetVectorMapHandler handler = new GetVectorMapHandler();
			long t1 = System.currentTimeMillis();
			resp = handler.getVectorMap(req);
			QueryResult[] respResult = resp.getResult();
			String geoResp = Response.forMongoGeoJson(respResult);
			if (!(resp.getRequestExtend().equalsIgnoreCase(""))) {
				geoResp = resp.getRequestExtend() + "@!@" + geoResp;
			}
			PrintStream out = null;
			try {
				out = new PrintStream(response.getOutputStream());
				out.print(geoResp);
			} catch (IOException e) {
				e.printStackTrace();
				resp.setCode(ReturnCode.PRINTERROR);
				return Response.convert(resp);
			} finally {
				out.flush();
				out.close();
			}			
			long t = System.currentTimeMillis() - t1;
			System.out.println("getVectorMap: " + t + "ms");
		}
		return null;
	}

}
