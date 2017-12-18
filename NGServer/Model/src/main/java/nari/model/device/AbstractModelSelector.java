package nari.model.device;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import nari.model.bean.ClassDef;
import nari.model.bean.SubClassDef;
import nari.model.relation.Relation;
import nari.model.relation.RelationDef;

public abstract class AbstractModelSelector {

	private final ConcurrentMap<String, ClassDef> classDefMap = new ConcurrentHashMap<String, ClassDef>();
	
	private final ConcurrentMap<String, SubClassDef> subClassDefMap = new ConcurrentHashMap<String, SubClassDef>();
	
	private final ConcurrentMap<ClassDef, DeviceModel> deviceModelMap = new ConcurrentHashMap<ClassDef, DeviceModel>();
	
	private final ConcurrentMap<ClassDef, DeviceModel> deviceModelVersionMap = new ConcurrentHashMap<ClassDef, DeviceModel>();
	
	// <relationId, relationDef>
	private final ConcurrentMap<Integer, RelationDef> relationDefs = new ConcurrentHashMap<Integer, RelationDef>();
	
	// <pmodelid, <relationdef, relation>>
	private final ConcurrentMap<Integer, ConcurrentMap<RelationDef, List<Relation>>> relations 
	= new ConcurrentHashMap<Integer, ConcurrentMap<RelationDef, List<Relation>>>();

	
	public AbstractModelSelector(){
		
	}
	
	protected ClassDef getClassDefFromCache(String classId){
		ClassDef def = classDefMap.get(classId);
		if(def==null){
			return ClassDef.NONE;
		}
		return def;
	}
	
	protected void cacheClassDef(ClassDef def){
		if(def==null || def==ClassDef.NONE){
			return;
		}
		classDefMap.putIfAbsent(def.getClassId(), def);
	}
	
	protected SubClassDef getSubClassDefFromCache(String modelId){
		SubClassDef def = subClassDefMap.get(modelId);
		if(def == null){
			return SubClassDef.NONE;
		}
		return def;
	}
	
	protected void cacheSubClassDef(SubClassDef def){
		if(def==null || def==SubClassDef.NONE){
			return;
		}
		subClassDefMap.putIfAbsent(def.getModelId(), def);
	}
	
	protected DeviceModel getDeviceModelFromCache(ClassDef def, boolean isVersion){
		DeviceModel dm = null;
		if(isVersion){
			dm = deviceModelVersionMap.get(def);
			if(dm==null){
				return DeviceModel.NONE;
			}
		}else{
			dm = deviceModelMap.get(def);
			if(dm==null){
				return DeviceModel.NONE;
			}
		}
		return dm;
	}
	
	protected DeviceModel getDeviceModelFromCache(SubClassDef def, boolean isVersion){
		ClassDef classDef = def.getClassDef();
		DeviceModel dm = null;
		if(!isVersion){
			if(classDef==null || classDef==ClassDef.NONE){
				return DeviceModel.NONE;
			}
			dm = deviceModelMap.get(classDef);
			if(dm==null){
				return DeviceModel.NONE;
			}
		}else{
			if(classDef==null || classDef==ClassDef.NONE){
				return DeviceModel.NONE;
			}
			dm = deviceModelVersionMap.get(classDef);
			if(dm==null){
				return DeviceModel.NONE;
			}
		}
		return dm;
	}
	
	protected void cacheDeviceModel(DeviceModel deviceModel, boolean isVersion){
		if (deviceModel == DeviceModel.NONE) {
			return;
		}
		ClassDef def = deviceModel.getClassDef();
		if (def==null || def == ClassDef.NONE) {
			return;
		}
		if (!isVersion) {
			deviceModelMap.putIfAbsent(def, deviceModel);
		} else {
			deviceModelVersionMap.putIfAbsent(def, deviceModel);
		}
	}
	
	protected Iterator<RelationDef> getRelationDefFromCache() {
		return relationDefs.values().iterator();
	}
	
	protected RelationDef getRelationDefFromCache(int relationId) {
		return relationDefs.get(relationId);
	}
	
	protected Iterator<Relation> getRelationsFromCache() {
		
		List<Relation> _relations = new ArrayList<Relation>();
		
		for (Map.Entry<Integer, ConcurrentMap<RelationDef, List<Relation>>> entry : relations.entrySet()) {
			ConcurrentMap<RelationDef, List<Relation>> relationMap = entry.getValue();
			for (Map.Entry<RelationDef, List<Relation>> _entry : relationMap.entrySet()) {
				_relations.addAll(_entry.getValue());
			}
		}
		
		return _relations.iterator();
	}
	
	protected Iterator<Relation> getRelationsFromCache(int pModelId) {
		Map<RelationDef, List<Relation>> def_relations = relations.get(pModelId);
		List<Relation> _relations = new ArrayList<Relation>();
		for (Map.Entry<RelationDef, List<Relation>> entry : def_relations.entrySet()) {
			_relations.addAll(entry.getValue());
		}
		return _relations.iterator();
	}

	protected Iterator<Relation> getRelationsFromCache(int pModelId, RelationDef relationDef) {
		Map<RelationDef, List<Relation>> def_relations = relations.get(pModelId);
		if (null == def_relations) {
			return null;
		}
		return def_relations.get(relationDef).iterator();
	}
	
	protected Iterator<Relation> getRelationsFromCache(int pModelId, RelationDef[] relationDefs) {
		
		if (null == relationDefs || relationDefs.length <= 0) {
			return getRelationsFromCache(pModelId);
		} else if (relationDefs.length == 1) {
			return getRelationsFromCache(pModelId, relationDefs[0]);
		} else {
			Map<RelationDef, List<Relation>> def_relations = relations.get(pModelId);
			List<Relation> _relations = new ArrayList<Relation>();
			for (RelationDef relationDef : relationDefs) {
				_relations.addAll(def_relations.get(relationDef));
			}
			return _relations.iterator();
		}
		
	}

	protected void cacheRelationDef(RelationDef relationDef) {
		relationDefs.putIfAbsent(relationDef.getRelationId(), relationDef);
	}
	
	protected void cacheRelation(Relation relation) {
		int pModelId = relation.getPModelId();
		RelationDef relationDef = relation.getRelationDef();
		ConcurrentMap<RelationDef, List<Relation>> def_relations = relations.get(Integer.valueOf(pModelId));
		
		if (null == def_relations) {
			def_relations = new ConcurrentHashMap<RelationDef, List<Relation>>();
			List<Relation> _relations = new ArrayList<Relation>();
			_relations.add(relation);
			def_relations.putIfAbsent(relationDef, _relations);
			relations.putIfAbsent(pModelId, def_relations);
		} else {
			List<Relation> _relations = def_relations.get(relationDef);
			if (null == _relations) {
				_relations = new ArrayList<Relation>();
				_relations.add(relation);
				def_relations.putIfAbsent(relationDef, _relations);
			} else {
				_relations.add(relation);
				def_relations.putIfAbsent(relationDef, _relations);
			}
			relations.putIfAbsent(pModelId, def_relations);
		}
	}
}
