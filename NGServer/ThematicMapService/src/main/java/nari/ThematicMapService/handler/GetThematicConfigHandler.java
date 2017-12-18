package nari.ThematicMapService.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nari.Dao.interfaces.DbAdaptor;
import nari.Logger.Logger;
import nari.Logger.LoggerManager;
import nari.ThematicMapService.ThematicMapServiceActivator;
import nari.ThematicMapService.bean.GetThematicConfigRequest;
import nari.ThematicMapService.bean.GetThematicConfigResponse;
import nari.ThematicMapService.bean.ThematicDocument;
import nari.model.TableName;
import nari.model.device.ModelService;
import nari.model.symbol.SymbolAdapter;
import nari.parameter.code.ReturnCode;

public class GetThematicConfigHandler {

	private Logger logger = LoggerManager.getLogger(this.getClass());
	private DbAdaptor db = ThematicMapServiceActivator.dbAdaptor;
	private ModelService ms = ThematicMapServiceActivator.modelService;
	private SymbolAdapter symAdp = ThematicMapServiceActivator.symboladapter;
	
	public GetThematicConfigResponse getThematicConfig(GetThematicConfigRequest req){
		GetThematicConfigResponse resp = new GetThematicConfigResponse();
		
		String mapId = req.getMapId(); // 图类型
		if ("".equalsIgnoreCase(mapId)) {
			logger.error("传入参数缺少必须值");
			resp.setCode(ReturnCode.NULL);
			return resp;
		}
		
		String documentId = req.getDocumentId(); // 图实例
		String documentSql = "";
		if(!"".equalsIgnoreCase(documentId)){
			documentSql = " and documentId = " +documentId;
		}
		
		String parentId = req.getParentId(); //图所属设备id
		String parentSql = "";
		if(!"".equalsIgnoreCase(documentId)){
			parentSql = " and parentId = " +parentId;
		}
		
		String parentModelId = req.getParentModelId(); //图所属设备modelid
		String parentModelSql = "";
		if(!"".equalsIgnoreCase(documentId)){
			parentModelSql = " and parentModelId = " +parentModelId;
		}
		
		String sql = "select * from " + TableName.CONF_DOCUMENTINSTANCE + " where mapid = " + mapId + documentSql + parentSql + parentModelSql;
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = db.findAllMap(sql);
		} catch (SQLException e) {
			logger.error("数据库查询出错");
			resp.setCode(ReturnCode.SQLERROR);
			return resp;
		}
		
		resp = formatResponse(list);
		return resp;
	}
	
	private GetThematicConfigResponse formatResponse(List<Map<String, Object>> list){
		GetThematicConfigResponse resp = new GetThematicConfigResponse();
		List<ThematicDocument> thematicDocumentlist = new ArrayList<ThematicDocument>();
		
		for(int i=0;i<list.size();i++){
			Map<String, Object> record = list.get(i);
			ThematicDocument thematicDocument = new ThematicDocument();
			
			thematicDocument.setDocumentId(String.valueOf(record.get("documentId".toLowerCase())));
			
			thematicDocument.setMapID(String.valueOf(record.get("mapid".toLowerCase())));
			
			String parentModelId = String.valueOf(record.get("parentModelId".toLowerCase()));
			thematicDocument.setParentModelId(parentModelId);
			
			String parentId = String.valueOf(record.get("parentId".toLowerCase()));
			thematicDocument.setParentId(parentId);
			
			String documentName = String.valueOf(record.get("documentName".toLowerCase()));
			thematicDocument.setDocumentName(documentName);
			
			String originX = String.valueOf(record.get("originX".toLowerCase()));
			thematicDocument.setOriginX(originX);
			
			String originY = String.valueOf(record.get("originY".toLowerCase()));
			thematicDocument.setOriginY(originY);
			
			String Xmax = String.valueOf(record.get("Xmax".toLowerCase()));
			thematicDocument.setXmax(Xmax);
			
			String Xmin = String.valueOf(record.get("Xmin".toLowerCase()));
			thematicDocument.setXmin(Xmin);
			
			String Ymax = String.valueOf(record.get("Ymax".toLowerCase()));
			thematicDocument.setYmax(Ymax);
			
			String Ymin = String.valueOf(record.get("Ymin".toLowerCase()));
			thematicDocument.setYmin(Ymin);
			
			String state_id = String.valueOf(record.get("state_id".toLowerCase()));
			thematicDocument.setState_id(state_id);
			
			String isc_id = String.valueOf(record.get("isc_id".toLowerCase()));
			thematicDocument.setIsc_id(isc_id);
			
			String gridSize = String.valueOf(record.get("gridSize".toLowerCase()));
			thematicDocument.setGridSize(gridSize);
			
			thematicDocumentlist.add(thematicDocument);
		}
		
		ThematicDocument[] thematicDocuments = new ThematicDocument[thematicDocumentlist.size()];
		thematicDocuments = thematicDocumentlist.toArray(thematicDocuments);
		resp.setThematicDocuments(thematicDocuments);
		resp.setCode(ReturnCode.SUCCESS);
		return resp;
	}
}
