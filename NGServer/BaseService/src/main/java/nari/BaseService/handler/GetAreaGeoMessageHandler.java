package nari.BaseService.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nari.BaseService.BaseServiceActivator;
import nari.BaseService.bean.AreaGeoMessage;
import nari.BaseService.bean.GetAreaGeoMessageRequest;
import nari.BaseService.bean.GetAreaGeoMessageResponse;
import nari.parameter.code.ReturnCode;
import oracle.spatial.geometry.JGeometry;

import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.GeometryUtils;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

public class GetAreaGeoMessageHandler {

	private Map<String,String> areaCacheMap = BaseServiceActivator.areaCacheMap;
	
	public GetAreaGeoMessageResponse getAreaGeoMessage(GetAreaGeoMessageRequest req){
		GetAreaGeoMessageResponse resp = new GetAreaGeoMessageResponse();
		
		String[] areaIds = req.getAreaIds();
		List<AreaGeoMessage> areaGeoMessageList = new ArrayList<AreaGeoMessage>();
		for(String areaId : areaIds){	//根据地域标识对每个地域进行操作
			AreaGeoMessage areaGeoMessage = new AreaGeoMessage();
			areaGeoMessage.setAreaId(areaId);
			
			String geometryString = areaCacheMap.get(areaId);
			if(geometryString == null || "".equals(geometryString)){
				continue;
			}
			areaGeoMessage.setGeometryString(geometryString);
			
			Geometry areaExtend = null;
			try {
				areaExtend = calculateExtend(geometryString);
			} catch (IOException e) {
				e.printStackTrace();
				resp.setCode(ReturnCode.FAILED);
				return resp;
			}
			String extendString = Geo2JSONString(areaExtend);
			areaGeoMessage.setExtendString(extendString);
			
			areaGeoMessageList.add(areaGeoMessage);
		}
		//若无数据
		if(areaGeoMessageList.size()==0){
			resp.setCode(ReturnCode.NODATA);
		}else{
			AreaGeoMessage[] areaGeoMessages = new AreaGeoMessage[areaGeoMessageList.size()];
			areaGeoMessages = areaGeoMessageList.toArray(areaGeoMessages);
			resp.setAreaGeoMessages(areaGeoMessages);
			resp.setCode(ReturnCode.SUCCESS);
		}
		
		return resp;
	}
	
	
	private Geometry calculateExtend(String geometryString) throws IOException{
		GeometryJSON geometryJSON = new GeometryJSON();
		Geometry geometry = null;
		geometry = geometryJSON.read(geometryString);
//		Geometry boundary = geometry.getBoundary();	//外围边界
		Geometry extend = geometry.getEnvelope();	//范围边界
		
		return extend;
	}
	
	
	private String Geo2JSONString(Geometry areaExtend){
		String extendString = new String();
		GeometryJSON geometryJSON = new GeometryJSON();
		extendString = geometryJSON.toString(areaExtend);
		return extendString;
	}
	
//	public static void main(String[] args) {
//		GeometryJSON geotools = new GeometryJSON();
//		Geometry geometry = null;
//		try {
//			geometry = geotools.read(null);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		System.out.println("wancheng");
//	}
}
