package nari.MongoQuery.MapService.handler;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import nari.MongoQuery.MongoQueryActivator;
import nari.MongoQuery.Adapter.SymbolAdapterImpl;
import nari.MongoQuery.Util.BsonUtil;
import nari.MongoQuery.Util.MongoDBUtil;
import nari.model.bean.SymbolDef;
import nari.model.symbol.SymbolAdapter;
import nari.parameter.MapService.GetVectorMap.GetVectorMapRequest;
import nari.parameter.MapService.GetVectorMap.GetVectorMapResponse;
import nari.parameter.bean.PSRCondition;
import nari.parameter.bean.QueryField;
import nari.parameter.bean.QueryRecord;
import nari.parameter.bean.QueryResult;
import nari.parameter.bean.SymbolPair;
import nari.parameter.bean.SubPSRCondition;
import nari.parameter.code.ReturnCode;

import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.geotools.geojson.geom.GeometryJSON;

import com.mongodb.CursorType;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

public class GetVectorMapHandler {
	private GeometryFactory geometryFactory = null;
	private SymbolAdapter symAdapter = null;
	
	public GetVectorMapHandler(){
		geometryFactory = new GeometryFactory();
		symAdapter = MongoQueryActivator.symbolAdapter;
	}
	/***
	 * get vector data in a area with conditions such as DYDJ,SBZLX
	 * */
	public GetVectorMapResponse getVectorMap(GetVectorMapRequest request) {
		
		GetVectorMapResponse resp = new GetVectorMapResponse();
		
		double[] coordsBBox = request.getBbox();
		double[] coordsPolygon = getCoordPolygon(coordsBBox);
		
		PSRCondition[] classCondition = request.getClassCondition();

		boolean isQueryGeometry = true;
		//QueryResult[] results = new QueryResult[classCondition.length];
		int conLength = classCondition.length;
		List<QueryResult> results = new ArrayList<QueryResult>(conLength);
		for(int i = 0; i < conLength; ++i) {
			
			String classId = classCondition[i].getClassId();
			SubPSRCondition[] modelCondition = classCondition[i].getModelCondition();			
			if (null == modelCondition || modelCondition.length <= 0) {
				continue;
			}
			MongoCollection<BsonDocument> model = null;

			model = MongoDBUtil.instance.getCollection(Integer.parseInt(classId));		 
			if(0 == model.count()){
				continue;
			}
			
			Boolean bHasDydj = true;
			if (classId.equals("361000") || classId.equals("150000")) {
				bHasDydj = false;
			}
			BsonDocument attributesExpression = getAttributeExp(modelCondition, bHasDydj);
			BsonDocument spatialExpExpression = getSpatialExp(coordsPolygon);
			BsonArray combine = new BsonArray();
			combine.add(0,	spatialExpExpression);
			combine.add(1,	attributesExpression);
			BsonDocument attAndspatial = new BsonDocument().append("$and", combine);
			List<QueryRecord> records = new ArrayList<QueryRecord>();
			
			String[] queryFieldName = new String[] { "OID","SBZLX","SHAPE","SBMC","FHDX","FHJD","DYDJ",
					"SFBZ","BZDX","BZYS", "BZFW", "SBID",
	                "PLFS","DHZS","BZXSZD","BZNR","X","Y","SSDKX"};
			
			queryFieldName = ModifyReturnFields(queryFieldName, classId);
			
			BsonDocument proj = getProjection(queryFieldName);
			MongoCursor<BsonDocument> cursor = 
					model.find(attAndspatial).batchSize(200).cursorType(CursorType.NonTailable).projection(proj).iterator();
			
			while (cursor.hasNext()) {
				BsonDocument dev = cursor.next();		
				QueryRecord record = new QueryRecord();					
				if (isQueryGeometry) {
					BsonValue geoValue = dev.get("Geometry");
					if (geoValue != null){
						String geoJson = geoValue.toString();
						record.setGeoJson(geoJson);
					}
				}
				record.setFields(getFields(queryFieldName, dev, classId));
				SymbolDef symdef = null;	
				String devModelId = String.valueOf(dev.getInt32("SBZLX").getValue()); 
				Iterator<SymbolDef> itertor = null;
				try {
					itertor = symAdapter.search(devModelId);
				} catch (Exception e) {
					e.printStackTrace();
				}
				symdef = SymbolAdapterImpl.searchSymbol(itertor, dev);
				if(symdef != null){
					SymbolPair symbol = new SymbolPair();
					symbol.setModelId(symdef.getModelId());
					symbol.setSymbolValue(symdef.getSymbolValue());
					symbol.setSymbolId(symdef.getSymbolId());
					record.setSymbol(symbol);
				}
				records.add(record);
			}
			cursor.close();		
			if (records.size() > 0) {
				QueryResult result = new QueryResult();
				result.setRecords(records.toArray(new QueryRecord[records.size()]));
				result.setCount(records.size());
				results.add(result);
			}
		}
		if(results.size() > 0){
			resp.setResult(results.toArray(new QueryResult[results.size()]));
		}
		resp.setCode(ReturnCode.SUCCESS);
		resp.setRequestExtend(request.getRequestExtend());
		return resp;
	}
	
	/**
	 * modify some character in String value of SBMC field
	 * @value value of field SBMC
	 * */
	public String modifySBMC(String value){
		if (null == value) {
			return value;
		}
		if(value.contains(":") || 
				value.contains("") || 
				value.contains("'") || 
				value.contains("{") || 
				value.contains("}") || 
				value.contains("\"") || 
				value.contains(",") || 
				value.contains("[") || 
				value.contains("]") || 
				value.contains("\t") || 
				value.contains("\r") || 
				value.contains("\f") || 
				value.contains("\b") || 
				value.contains("\n") || 
				value.contains("\"") || 
				value.contains(" ")||
				value.contains("～")){
			
			value = value.replaceAll(":", "；");
			value = value.replaceAll("\\\\", "");
			value = value.replaceAll("'", "");
			value = value.replaceAll("\"", "");
			value = value.replaceAll("\t","");
			value = value.replaceAll("\r","");
			value = value.replaceAll("\f","");
			value = value.replaceAll("\n","");
			value = value.replaceAll("～", "_");
		}
		return value;
	}
	
	/**
	 * get feild value array from BsonDocument
	 * @param dev ,one record in MongoCollection
	 * @param queryFieldName
	 * */
	public QueryField[] getFields(String[] queryFieldName, BsonDocument dev, String classId){
		QueryField []fields = null;
		String BZFW = "BZFW", SHAPE = "SHAPE", SBZLX = BsonUtil.BsonToString(dev.get("SBZLX"));
		boolean isDxType = (
				SBZLX.equals("10000000") || 
				SBZLX.equals("10000001") || 
				SBZLX.equals("10100000") || 
				SBZLX.equals("20100000") || 
				SBZLX.equals("14000000") || 
				SBZLX.equals("13000000") );
		if( !isDxType && dev.containsKey(BZFW) && dev.get("BZFW").isNull()){
			String queryFields[] = new String[]{"OID", "SBZLX","SBID","SBMC","FHDX","FHJD","DYDJ","SFBZ","SSDKX"};
			queryFields = ModifyReturnFields(queryFields, classId);
			
			fields = new QueryField[queryFields.length];
			for(int i = 0; i < queryFields.length; ++ i){
				BsonValue bValue = dev.get(queryFields[i]);
				
//				if(bValue == null){
//					int pp=0;
//					pp=pp;
//				}
//				
				String value = BsonUtil.BsonToString(bValue);
				if(queryFields[i].equalsIgnoreCase("SBMC")){
					value = modifySBMC(value);
				}
				fields[i] = new QueryField();
				fields[i].setFieldName(queryFields[i]);
				fields[i].setFieldValue(value);
			}
			
		}
		else if( isDxType && dev.containsKey(BZFW) && dev.get("BZFW").isNull() ){
			String queryFields[] = new String[]{"OID", "SBZLX", "SBID", "SBMC", "DYDJ", "SFBZ","SSDKX"};
			queryFields = ModifyReturnFields(queryFields, classId);
			
			fields = new QueryField[queryFields.length];
			for(int i = 0; i < queryFields.length; ++ i){
				BsonValue bValue = dev.get(queryFields[i]);
				
				String value = BsonUtil.BsonToString(bValue);
				if(queryFields[i].equalsIgnoreCase("SBMC")){
					value = modifySBMC(value);
				}
				fields[i] = new QueryField();
				fields[i].setFieldName(queryFields[i]);
				fields[i].setFieldValue(value);
			}
		}
		else{
			queryFieldName = ModifyReturnFields(queryFieldName, classId);
			int rFieldLength = queryFieldName.length;
			
			fields = new QueryField[rFieldLength - 1];
			for(int r = 0, f = 0; r < rFieldLength; ++r){
				String queryField = queryFieldName[r].toUpperCase();
				
				if(queryField.equalsIgnoreCase(SHAPE)){
					continue;
				}
			
				fields[f] = new QueryField();
				fields[f].setFieldName(queryField);
				
				if(dev.containsKey(queryField)){
					String value = null;
					BsonValue bValue = dev.get(queryField);
					value = BsonUtil.BsonToString(bValue);
										
					if(value != null){
						if(queryField.equals("SBMC")){
							value = modifySBMC(value);
						}
						fields[f].setFieldValue(value);
					}
				}
				++f;
			}
		}
		return fields;
	}
	
	
	/**
	 * 调整返回字段
	 * */
	private String[] ModifyReturnFields(String[] queryFieldName, String classId){
		Boolean bHasDydj = true;
		if(classId.equals("361000") || classId.equals("150000")){
			bHasDydj = false;
		}
		
		Boolean bZnyc = false;
		int classIdInt = Integer.parseInt(classId);
		if(classIdInt > 300000 && classIdInt <= 399999){  //不包含电站
			bZnyc = true;
		}
		
		List<String> arrFields = new LinkedList<String>();
		for(int k = 0; k < queryFieldName.length; k++){
			arrFields.add(queryFieldName[k]);
		}
		
		if(bHasDydj == false){
			arrFields.remove("DYDJ");
		}
		if(bZnyc == true){  //如果是站内设备
			arrFields.add("SSDZ");
		}
		
		if(classId.equals("100000")){ //如果是线路
			arrFields.add("FLAG");
		}
		
		queryFieldName = new String[arrFields.size()];
		queryFieldName = arrFields.toArray(queryFieldName);
		
		return queryFieldName;
	}
	
	
	/**
	 * getgeoJson of polygon with polygon's coordinate
	 * @param coordsPolygon coordinates of polygon, format [x1,y1,x2,y2....]
	 * */
	public String getGeoJSON(double[] coordsPolygon){
		String queryGeoJson = null;
		
		int plength = coordsPolygon.length / 2;
		Coordinate[] pcoord = new Coordinate[plength];
		for(int p = 0; p < plength; ++p){
			pcoord[p] = new Coordinate(coordsPolygon[2 * p], coordsPolygon[2 * p + 1]);
		}
		
		LinearRing outLine = geometryFactory.createLinearRing(pcoord);
		Polygon polygon = geometryFactory.createPolygon(outLine, null);
		GeometryJSON gjson = new GeometryJSON(12);
		
		if(null == polygon){
			return null; 
		}else{	
			OutputStream os= new ByteArrayOutputStream();
			try {
				gjson.write(polygon, os);
				queryGeoJson = os.toString();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return queryGeoJson;
	}
	/**
	 * get coordinates array for bbox for a polygon,
	 * @param coordsBBox, coordinates array,  
	 * */
	public double[] getCoordPolygon(double[] coordsBBox){
		
		int clength = coordsBBox.length;
		double []coordsPolygon = null;
		if(coordsBBox[0] != coordsBBox[clength -2] || coordsBBox[1] != coordsBBox[clength - 1]){
			coordsPolygon = new double[clength + 2];
			for(int i = 0; i< clength; ++i){
				coordsPolygon[i] = coordsBBox[i];
			}
			coordsPolygon[clength] = coordsBBox[0];
			coordsPolygon[clength + 1] = coordsBBox[1];
			
		}else{
			coordsPolygon = new double[clength];
			for(int i = 0; i< clength; ++i){
				coordsPolygon[i] = coordsBBox[i];
			}
		}
		return coordsPolygon;
	}
	/**
	 * compose query exp
	 * @param model
	 * @param classcondition
	 * @param coordsPolygon
	 * */
	public BsonDocument getAttributeExp(SubPSRCondition[] modelCondition, Boolean bHasDydj){	
		BsonArray eachSBZLXConExp = new BsonArray();
		
		for(int j = 0; j < modelCondition.length; ++j){
			BsonDocument eachSBZLXConExpj = new BsonDocument();
			
			BsonDocument dydjConExp = new BsonDocument();
			BsonDocument sbzlxConExp = new BsonDocument();
			
			String sbzlx = modelCondition[j].getSbzlx();
			if(sbzlx != null && sbzlx != ""){
				sbzlxConExp.put("SBZLX", new BsonInt32(Integer.parseInt(sbzlx)));
			}			
			
			String[] DYDJ = modelCondition[j].getDYDJ();
			if(DYDJ != null && bHasDydj == true){
				BsonArray djarr = new BsonArray();
				for(int d = 0; d < DYDJ.length; ++d){
					djarr.add(new BsonInt32(Integer.parseInt(DYDJ[d])));
				}	
				dydjConExp.append("DYDJ", new BsonDocument().append("$in", djarr));
			}
			if(dydjConExp.isEmpty()){
				eachSBZLXConExpj = sbzlxConExp;
			}else if(sbzlxConExp.isEmpty()){
				eachSBZLXConExpj = dydjConExp;
			}else{
				BsonArray tmp = new BsonArray();
				tmp.add(0, dydjConExp);
				tmp.add(1, sbzlxConExp);
				eachSBZLXConExpj.append("$and", tmp);
			}
			eachSBZLXConExp.add(eachSBZLXConExpj);
		}//for
		
		// “$or”不支持数组长度为1的查询
		if(eachSBZLXConExp.size() == 1){
			BsonDocument falseExp = new BsonDocument();
			falseExp.put("SBZLX", new BsonInt32(0));
			eachSBZLXConExp.add(falseExp);
		}
			
		BsonDocument attributesExpression = new BsonDocument();
		attributesExpression.append("$or", eachSBZLXConExp);
		return attributesExpression;
	}
	/**
	 * create BsonDocument of the "Geometry" in query BsonDocument
	 * @param coordsPolygon the coordinates of the polygon
	 * 
	 * */
	public BsonDocument getSpatialExp(double[] coordsPolygon){
		BsonDocument geometry = new BsonDocument();
		BsonDocument type = new BsonDocument();
		BsonDocument coordinates = new BsonDocument();
		
		type.append("type", new BsonString("Polygon"));
		
		BsonArray Line = new BsonArray();
		BsonArray Polygon = new BsonArray();
		int plength = coordsPolygon.length / 2;
		for(int i = 0; i < plength; ++ i){
			BsonArray point = new BsonArray(Arrays.asList(new BsonDouble(coordsPolygon[2*i]), 
					new BsonDouble(coordsPolygon[2*i+1])));
			Line.add(point);
		}
		Polygon.add(Line);
		
		coordinates.append("coordinates", Polygon);
		geometry.put("type", new BsonString("Polygon"));
		geometry.put("coordinates", Polygon);
		BsonDocument spatialExp = new BsonDocument();
		spatialExp.append("Geometry", new BsonDocument().append("$geoIntersects", new BsonDocument().append("$geometry", geometry)));
		
		return spatialExp;
	}
	
	
	/**
	 * get projection bsondocument used to find
	 * */
	public BsonDocument getProjection(String[] queryFieldName){
		BsonDocument proj = new BsonDocument();
		proj.append("_id", new BsonInt32(0));
		proj.append("Geometry", new BsonInt32(1));
		for(int i = 0; i < queryFieldName.length; ++ i){
			String name = queryFieldName[i];
			proj.append(name, new BsonInt32(1));
		}
		return proj;
	}
}
