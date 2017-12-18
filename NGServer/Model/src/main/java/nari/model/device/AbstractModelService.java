package nari.model.device;

import java.util.Iterator;

import nari.model.bean.ClassDef;
import nari.model.bean.CodeDef;
import nari.model.bean.SubClassDef;
import nari.model.relation.Relation;
import nari.model.relation.RelationDef;

public abstract class AbstractModelService implements ModelService{

	@Override
	public GeneralModel fromName(String modelName) throws Exception {
		return null;
	}
	
	@Override
	public DeviceModel fromClass(String classId, boolean isVersion) throws Exception{
		if(!validate(classId)){
			return DeviceModel.NONE;
		}
		ClassDef def = getProvider().get().getClassDef(classId);
		if(def==ClassDef.NONE){
			return DeviceModel.NONE;
		}
		return getProvider().get().from(def,isVersion);
	}

	@Override
	public DeviceModel fromSubClass(String modelId, boolean isVersion) throws Exception{
		if(!validate(modelId)){
			return DeviceModel.NONE;
		}
		SubClassDef def = getProvider().get().getSubClassDef(modelId);
		if(def==SubClassDef.NONE){
			return DeviceModel.NONE;
		}
		return getProvider().get().from(def,isVersion);
	}

	@Override
	public DeviceModel from(ClassDef classDef, boolean isVersion) throws Exception{
		if(classDef==ClassDef.NONE){
			return DeviceModel.NONE;
		}
		return getProvider().get().from(classDef,isVersion);
	}

	@Override
	public DeviceModel from(SubClassDef subClassDef, boolean isVersion) throws Exception{
		if(subClassDef==SubClassDef.NONE){
			return DeviceModel.NONE;
		}
		return getProvider().get().from(subClassDef,isVersion);
	}

	@Override
	public CodeDef getCodeDef(String codeType, String code) throws Exception{
		return getProvider().get().getCodeDef(codeType, code);
	}

	@Override
	public Iterator<CodeDef> getCodeDef(String codeType) throws Exception{
		return getProvider().get().getCodeDef(codeType);
	}

	@Override
	public Iterator<CodeDef> getAllCodeDef() throws Exception{
		return getProvider().get().getAllCodeDef();
	}

	@Override
	public ClassDef getClassDef(String classId) throws Exception{
		if(validate(classId)){
			return getProvider().get().getClassDef(classId);
		}
		return ClassDef.NONE;
	}

	@Override
	public SubClassDef getSubClassDef(String modelId) throws Exception{
		if(validate(modelId)){
			return getProvider().get().getSubClassDef(modelId);
		}
		return SubClassDef.NONE;
	}
	
	@Override
	public RelationDef getRelationDef(int relationId) throws Exception {
		return getProvider().get().getRelationDef(relationId);
	}
	
	@Override
	public Iterator<Relation> relateToClass(String classId, 
			RelationDef[] relationDefs, String[] relationFields) throws Exception {
		return getProvider().get().relateToClass(classId, relationDefs, relationFields);
	}
	
	@Override
	public Iterator<Relation> relateToSubClass(String modelId, 
			RelationDef[] relationDefs, String[] relationFields) throws Exception {
		return getProvider().get().relateToSubClass(modelId, relationDefs, relationFields);
	}

	private boolean validate(String value) throws Exception{
		if(value==null || "".equals(value)) {
			throw new Exception(value+" parameter is invalidate");
		}
		return true;
	}
	
	protected abstract SelectorProvider getProvider();
}
