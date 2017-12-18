package nari.model.device;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nari.model.bean.ClassDef;
import nari.model.bean.CodeDef;
import nari.model.bean.SubClassDef;
import nari.model.relation.Relation;
import nari.model.relation.RelationDef;

public class PooledModelSelector extends AbstractModelSelector implements ModelSelector {

	private ModelSelector selector = ModelSelector.NONE;
	
	public PooledModelSelector(ModelSelector selector) {
		
		this.selector = selector;

		// 将所有的关系模块缓存下来
		try {
			cacheAllRelation();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ClassDef getClassDef(String classId) throws Exception {
		ClassDef def = getClassDefFromCache(classId);
		
		if(def==ClassDef.NONE){
			def = selector.getClassDef(classId);
			cacheClassDef(def);
			
			SubClassDef[] subDefs = def.getSubClassDef();
			if(subDefs!=null){
				for(SubClassDef subDef : subDefs){
					if(getSubClassDefFromCache(subDef.getModelId())==SubClassDef.NONE){
						cacheSubClassDef(subDef);
					}
				}
			}
		}
		
		return def;
	}

	@Override
	public SubClassDef getSubClassDef(String modelId) throws Exception {
		SubClassDef def = getSubClassDefFromCache(modelId);
		
		if(def==SubClassDef.NONE){
			def = selector.getSubClassDef(modelId);
			cacheSubClassDef(def);
			
			ClassDef cdef = def.getClassDef();
			if(cdef!=null){
				if(getClassDefFromCache(cdef.getClassId())==ClassDef.NONE){
					cacheClassDef(cdef);
				}
			}
		}
		
		return def;
	}

	@Override
	public DeviceModel from(ClassDef classDef, boolean isVersion) throws Exception {
		if(classDef==ClassDef.NONE){
			return DeviceModel.NONE;
		}
		
		DeviceModel dm = getDeviceModelFromCache(classDef,isVersion);
		if(dm==DeviceModel.NONE){
			dm = selector.from(classDef, isVersion);
			cacheDeviceModel(dm, isVersion);
		}
		
		return dm;
	}

	@Override
	public DeviceModel from(SubClassDef subClassDef, boolean isVersion) throws Exception {
		if(subClassDef==SubClassDef.NONE){
			return DeviceModel.NONE;
		}
		
		DeviceModel dm = getDeviceModelFromCache(subClassDef,isVersion);
		if(dm==DeviceModel.NONE){
			dm = selector.from(subClassDef, isVersion);
			cacheDeviceModel(dm, isVersion);
		}
		
		return dm;
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
	public Iterator<RelationDef> getAllRelationDef() throws Exception {
		return getRelationDefFromCache();
	}
	
	@Override
	public Iterator<Relation> getAllRelation(
			Iterator<RelationDef> relationDefs) throws Exception {
		return getRelationsFromCache();
	}

	@Override
	public Iterator<Relation> getAllRelation() throws Exception {
		return getRelationsFromCache();
	}

	@Override
	public RelationDef getRelationDef(int relationId) throws Exception {
		return getRelationDefFromCache(relationId);
	}

	@Override
	public Iterator<Relation> relateToClass(String classId,
			RelationDef[] relationDefs, String[] relationFields)
			throws Exception {
		ClassDef classDef = getClassDef(classId);
		SubClassDef[] subClassDefs = classDef.getSubClassDef();
		
		if (subClassDefs.length == 1) {
			return relateToSubClass(subClassDefs[0].getModelId(), relationDefs, relationFields);
		}
		
		List<Relation> relations = new ArrayList<Relation>();
		for (SubClassDef subClassDef : subClassDefs) {
			Iterator<Relation> relationIter = 
					relateToSubClass(subClassDef.getModelId(), relationDefs, relationFields);
			while (relationIter.hasNext()) {
				relations.add(relationIter.next());
			}	
		}
		return relations.iterator();
	}

	@Override
	public Iterator<Relation> relateToSubClass(String modelId,
			RelationDef[] relationDefs, String[] relationFields)
			throws Exception {
		
		int pModelId = Integer.valueOf(modelId);
		Iterator<Relation> relations = getRelationsFromCache(pModelId, relationDefs);
		if (null == relationFields || relationFields.length <= 0) {
			return relations;
		}
		List<Relation> relationList = new ArrayList<Relation>();
		while (relations.hasNext()) {
			Relation relation = relations.next();
			for (int i = 0; i < relationFields.length; i++) {
				if (relation.getRelationField().equalsIgnoreCase(relationFields[i])) {
					relationList.add(relation);
					break;
				}
			}
		}
		return relationList.iterator();
	}	
	
	/**
	 * 缓存下所有的关系模块
	 */
	private void cacheAllRelation() throws Exception {
		
		Iterator<RelationDef> relationDefs = selector.getAllRelationDef();
		while (relationDefs.hasNext()) {
			RelationDef relationDef = relationDefs.next();
			cacheRelationDef(relationDef);
		}
		
		relationDefs = getAllRelationDef();
		Iterator<Relation> relations = selector.getAllRelation(relationDefs);
		
		while (relations.hasNext()) {
			Relation relation = relations.next();
			cacheRelation(relation);
		}
	}
	
}
