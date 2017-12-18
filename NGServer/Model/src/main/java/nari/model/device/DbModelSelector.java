package nari.model.device;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nari.Dao.interfaces.DbAdaptor;
import nari.model.TableName;
import nari.model.bean.ClassDef;
import nari.model.bean.ClassObject;
import nari.model.bean.CodeDef;
import nari.model.bean.DefaultClassDef;
import nari.model.bean.DefaultSubClassDef;
import nari.model.bean.NoLoopClassDef;
import nari.model.bean.NoLoopSubClassDef;
import nari.model.bean.SubClassDef;
import nari.model.bean.SubClassObject;
import nari.model.relation.Relation;
import nari.model.relation.RelationDef;

public class DbModelSelector implements ModelSelector {

	private DbAdaptor dbAdaptor;
	
	public DbModelSelector(DbAdaptor dbAdaptor) {
		this.dbAdaptor = dbAdaptor;
	}
	
	@Override
	public ClassDef getClassDef(String classId) throws Exception {
		String sql = "select * from " + TableName.CONF_OBJECTMETA + " where classid=?";
		ClassObject obj = null;
		try {
			obj = dbAdaptor.find(sql, new Object[]{classId}, ClassObject.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(obj==null){
			return ClassDef.NONE;
		}
		SubClassDef[] subDef = new SubClassDef[]{};
		ClassDef def = new DefaultClassDef(subDef,obj);
		
		sql = "select * from " + TableName.CONF_MODELMETA + " where classid=?";
		List<SubClassObject> subs = null;
		try {
			subs = dbAdaptor.findAll(sql, new Object[]{classId}, SubClassObject.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(subs==null || subs.size()==0){
			return ClassDef.NONE;
		}
		if(subs!=null && subs.size()>0){
			subDef = new SubClassDef[subs.size()];
			int i = 0;
			for(SubClassObject sub:subs){
				subDef[i++] = new DefaultSubClassDef(def,sub);
			}
		}
		
		ClassDef npDef = new NoLoopClassDef(def,subDef);
		return npDef;
	}

	@Override
	public SubClassDef getSubClassDef(String modelId) throws Exception {
		String sql = "select * from " + TableName.CONF_MODELMETA + " where modelid=?";
		SubClassObject sub = null;
		try {
			sub = dbAdaptor.find(sql, new Object[]{modelId}, SubClassObject.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(sub==null){
			return SubClassDef.NONE;
		}
		SubClassDef def = new DefaultSubClassDef(ClassDef.NONE, sub);
		
		sql = "select * from " + TableName.CONF_OBJECTMETA + " where classid=?";
		ClassObject obj = null;
		try {
			obj = dbAdaptor.find(sql, new Object[]{sub.getClassId()}, ClassObject.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(obj==null){
			return SubClassDef.NONE;
		}
		ClassDef pdef = new DefaultClassDef(new SubClassDef[]{},obj);
		SubClassDef subDef = new NoLoopSubClassDef(def, pdef);
		
		return subDef;
	}

	@Override
	public DeviceModel from(ClassDef classDef, boolean isVersion) throws Exception {
		DeviceModel model = null;
		if(isVersion){
			DeviceModel base = new BaseDBDeviceModel(classDef,classDef.getSubClassDef(),dbAdaptor);
			model = new VersionDBDeviceModel(base,dbAdaptor);
		}else{
			model = new BaseDBDeviceModel(classDef,classDef.getSubClassDef(),dbAdaptor);
		}
		return model;
	}

	@Override
	public DeviceModel from(SubClassDef subClassDef, boolean isVersion) throws Exception {
		DeviceModel model = null;
		if(isVersion){
			DeviceModel base = new BaseDBDeviceModel(subClassDef.getClassDef(),new SubClassDef[]{subClassDef},dbAdaptor);
			model = new VersionDBDeviceModel(base,dbAdaptor);
		}else{
			model = new BaseDBDeviceModel(subClassDef.getClassDef(),new SubClassDef[]{subClassDef},dbAdaptor);
		}
		return model;
	}

	@Override
	public CodeDef getCodeDef(String codeType, String code) throws Exception {
		return null;
	}

	@Override
	public Iterator<CodeDef> getCodeDef(String codeType) throws Exception {
		return null;
	}

	@Override
	public Iterator<CodeDef> getAllCodeDef() throws Exception {
		return null;
	}
	
	@Override
	public RelationDef getRelationDef(int relationId) throws Exception {
		String sql = "select relationid, relationname, describe from " 
				+ TableName.CONF_RELATIONDEFINITION 
				+ " where relationid = ?";
		List<Map<String, Object>> relationDefTable = dbAdaptor.findAllMap(sql);
		if (null == relationDefTable || relationDefTable.isEmpty()) {
			return null;
		}
		for (Map<String, Object> itemMap : relationDefTable) {
			int _relationId = Integer.valueOf(itemMap.get("relationid").toString());
			if (relationId != relationId) {
				continue;
			}
			String relationName = itemMap.get("relationname").toString();
			String describe = itemMap.get("describe").toString();
			RelationDef relationDef = new RelationDef(relationId, relationName, describe);
			return relationDef;
		}
		return null;
	}

	@Override
	public Iterator<RelationDef> getAllRelationDef() throws Exception {
		String sql = "select relationid, relationname, describe from " 
				+ TableName.CONF_RELATIONDEFINITION;
		List<Map<String, Object>> relationDefTable = dbAdaptor.findAllMap(sql);
		List<RelationDef> relationDefs = new ArrayList<RelationDef>();
		for (Map<String, Object> itemMap : relationDefTable) {
			int relationId = Integer.valueOf(itemMap.get("relationid").toString());
			String relationName = itemMap.get("relationname").toString();
			String describe = itemMap.get("describe").toString();
			RelationDef relationDef = new RelationDef(relationId, relationName, describe);
			relationDefs.add(relationDef);
		}
		return relationDefs.iterator();
	}
	
	@Override
	public Iterator<Relation> getAllRelation(
			Iterator<RelationDef> relationDefs) throws Exception {
		
		if (null == relationDefs || !relationDefs.hasNext()) {
			return null;
		}
		
		Map<Integer, RelationDef> relationDefMap = new HashMap<Integer, RelationDef>();
		while (relationDefs.hasNext()) {
			RelationDef relationDef = relationDefs.next();
			relationDefMap.put(relationDef.getRelationId(), relationDef);
		}
		
		String sql = "select pmodelid, rmodelid, relationid, relationfield, relationtypefield from " 
				+ TableName.CONF_MODELRELATION;
		
		List<Map<String, Object>> modelRelation = dbAdaptor.findAllMap(sql);
		
		List<Relation> relations = new ArrayList<Relation>();
		for (Map<String, Object> item : modelRelation) {
			
			int relationId = Integer.valueOf(item.get("relationid").toString());
			RelationDef relationDef = relationDefMap.get(relationId);
			if (null == relationDef) {
				continue;
			}
			
			int pModelId = Integer.valueOf(item.get("pmodelid").toString());
			int rModelId = Integer.valueOf(item.get("rmodelid").toString());
			
			Object oRelationField = item.get("relationfield");
			String relationField = oRelationField == null ? "" : oRelationField.toString();
			
			Object oRelationTypeField = item.get("relationtypefield");
			String relationTypeField = oRelationTypeField == null ? "" : oRelationTypeField.toString();
			Relation relation = new Relation();
			relation.setPModelId(pModelId);
			relation.setRModelId(rModelId);
			relation.setRelationDef(relationDef);
			relation.setRelationField(relationField);
			relation.setRelationTypeField(relationTypeField);
			
			relations.add(relation);
		}
		
		return relations.iterator();
	}

	@Override
	public Iterator<Relation> getAllRelation() throws Exception {
		
		String sql = "select pmodelid, rmodelid, relationid, relationfield, relationtypefield from " 
				+ TableName.CONF_MODELRELATION;
		
		List<Map<String, Object>> modelRelation = dbAdaptor.findAllMap(sql);
		
		List<Relation> relations = new ArrayList<Relation>();
		for (Map<String, Object> item : modelRelation) {
			
			int relationId = Integer.valueOf(item.get("relationid").toString());
			RelationDef relationDef = getRelationDef(relationId);
			if (null == relationDef) {
				continue;
			}
			
			int pModelId = Integer.valueOf(item.get("pmodelid").toString());
			int rModelId = Integer.valueOf(item.get("rmodelid").toString());
			
			Object oRelationField = item.get("relationfield");
			String relationField = oRelationField == null ? "" : oRelationField.toString();
			
			Object oRelationTypeField = item.get("relationtypefield");
			String relationTypeField = oRelationTypeField == null ? "" : oRelationTypeField.toString();
			Relation relation = new Relation();
			relation.setPModelId(pModelId);
			relation.setRModelId(rModelId);
			relation.setRelationDef(relationDef);
			relation.setRelationField(relationField);
			relation.setRelationTypeField(relationTypeField);
			
			relations.add(relation);
		}
		
		return relations.iterator();
	}
	
	@Override
	public Iterator<Relation> relateToSubClass(String modelId,
			RelationDef[] relationDefs, String[] relationFields) throws Exception {
		String sql = "select pmodelid, rmodelid, relationid, relationfield, relationtypefield from " 
			+ TableName.CONF_MODELRELATION 
			+ " where pmodelid = " + modelId;
		
		List<Map<String, Object>> modelRelation = dbAdaptor.findAllMap(sql);
		
		List<Relation> relations = new ArrayList<Relation>();
		for (Map<String, Object> item : modelRelation) {
			
			int relationId = Integer.valueOf(item.get("relationid").toString());
			RelationDef relationDef = getRelationDef(relationId);
			if (null == relationDef) {
				continue;
			}
			
			boolean hasDef = false;
			for (int i = 0; i < relationDefs.length; i++) {
				if (relationDef.equals(relationDefs[i])) {
					hasDef = true;
					break;
				}
			}
			if (!hasDef) {
				continue;
			}
			
			Object oRelationField = item.get("relationfield");
			String relationField = oRelationField == null ? "" : oRelationField.toString();
			
			boolean hasField = false;
			if (null == relationFields || relationFields.length <= 0) {
				hasField = true;
			} else {
				for (int i = 0; i < relationFields.length; i++) {
					if (relationField.equalsIgnoreCase(relationFields[i])) {
						hasField = true;
						break;
					}
				}
			}
			if (!hasField) {
				continue;
			}
			
			int pModelId = Integer.valueOf(item.get("pmodelid").toString());
			int rModelId = Integer.valueOf(item.get("rmodelid").toString());
			Object oRelationTypeField = item.get("relationtypefield");
			String relationTypeField = oRelationTypeField == null ? "" : oRelationTypeField.toString();
			
			Relation relation = new Relation();
			relation.setPModelId(pModelId);
			relation.setRModelId(rModelId);
			relation.setRelationDef(relationDef);
			relation.setRelationField(relationField);
			relation.setRelationTypeField(relationTypeField);
			relations.add(relation);
		}
		
		return relations.iterator();
	}

	@Override
	public Iterator<Relation> relateToClass(String classId,
			RelationDef[] relationDefs, String[] relationFields) throws Exception {
		
		ClassDef classDef = getClassDef(classId);
		SubClassDef[] subClassDefs = classDef.getSubClassDef();
		
		String modelIds = "";
		int index = 0;
		for (SubClassDef subClassDef : subClassDefs) {
			modelIds += subClassDef.getModelId();
			if (++index != subClassDefs.length) {
				modelIds += ",";
			}
		}
		modelIds.substring(0, modelIds.lastIndexOf(',') - 1);
		
		String sql = "select pmodelid, rmodelid, relationid, relationfield, relationtypefield from " 
				+ TableName.CONF_MODELRELATION 
				+ " where pmodelid in (" + modelIds + ")";
			
		List<Map<String, Object>> modelRelation = dbAdaptor.findAllMap(sql);
			
		List<Relation> relations = new ArrayList<Relation>();
		for (Map<String, Object> item : modelRelation) {
				
			int relationId = Integer.valueOf(item.get("relationid").toString());
			RelationDef relationDef = getRelationDef(relationId);
			if (null == relationDef) {
				continue;
			}
				
			boolean hasDef = false;
			for (int i = 0; i < relationDefs.length; i++) {
				if (relationDef.equals(relationDefs[i])) {
					hasDef = true;
					break;
				}
			}
			if (!hasDef) {
				continue;
			}
				
			Object oRelationField = item.get("relationfield");
			String relationField = oRelationField == null ? "" : oRelationField.toString();
				
			boolean hasField = false;
			if (null == relationFields || relationFields.length <= 0) {
				hasField = true;
			} else {
				for (int i = 0; i < relationFields.length; i++) {
					if (relationField.equalsIgnoreCase(relationFields[i])) {
						hasField = true;
						break;
					}
				}
			}
			if (!hasField) {
				continue;
			}
				
			int pModelId = Integer.valueOf(item.get("pmodelid").toString());
			int rModelId = Integer.valueOf(item.get("rmodelid").toString());
			Object oRelationTypeField = item.get("relationtypefield");
			String relationTypeField = oRelationTypeField == null ? "" : oRelationTypeField.toString();
				
			Relation relation = new Relation();
			relation.setPModelId(pModelId);
			relation.setRModelId(rModelId);
			relation.setRelationDef(relationDef);
			relation.setRelationField(relationField);
			relation.setRelationTypeField(relationTypeField);
			relations.add(relation);
		}
			
		return relations.iterator();
	}
}
