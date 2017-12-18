package nari.MapService.handler;

import nari.parameter.MapService.GetMapConfig.MapConfigRequest;
import nari.parameter.MapService.GetMapConfig.MapConfigResponse;

public class MapConfigHandler {
	public MapConfigResponse getMapConfig(MapConfigRequest request){
		MapConfigResponse resp = new MapConfigResponse();
//		String mapId = request.getMapId();
//		DbAdaptor dbh = new DbAdaptorHandler();
//		
//		//根据图类型得到地图名称
//		Map<String,Object> map0 = new HashMap<String,Object>();
//		String Sql0 = "select schemaid from CONF_DOCUMENTMETA where mapId="+mapId;
//		try {
//			map0 = dbh.findMap(Sql0);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		String mapName = (String)map0.get("mapName");
//		
//		//根据图类型得到显示方案
//		Map<String,Object> map1 = new HashMap<String,Object>();
//		String Sql1 = "select schemaid from CONF_DISPLAYSCHEMA where mapId="+mapId;
//		try {
//			map1 = dbh.findMap(Sql1);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		String schemaid = (String)map1.get("SCHEMAID");
//		
//		//根据显示方案得到切片地图配置
//		Map<String,Object> map2 = new HashMap<String,Object>();
//		String Sql2 = "select * from CONF_TILESCHEMA where titleschemaid="+schemaid;
//		try {
//			map2 = dbh.findMap(Sql2);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		double ox = (double)map2.get("OX");
//		double oy = (double)map2.get("OY");
//		String imageType = (String)map2.get("DATA_TYPE");
//		//宽高的关系
//		int imageSize = (int)map2.get("IMAGESIZE");
//		int startLevel = (int)map2.get("STARTLEVEL");
//		int endLevel = (int)map2.get("ENDLEVEL");
//		String url = (String)map2.get("URL");
//		
//		//返回RasterMap信息
//		RasterMap map = new RasterMap();
//		map.setOx(ox);
//		map.setOy(oy);
//		map.setImageType(imageType);
//		map.setStartLevel(startLevel);
//		map.setEndLevel(endLevel);
//		map.setUrl(url);
//		
//		resp.setMap(map);
//		resp.setCode(ReturnCode.SUCCESS);
		return resp;
	}
}

