package nari.model.device;

import java.util.Iterator;

import nari.model.bean.ClassDef;
import nari.model.bean.CodeDef;
import nari.model.bean.SubClassDef;
import nari.model.relation.Relation;
import nari.model.relation.RelationDef;

public class ServerModelSelector implements ModelSelector {

	@Override
	public ClassDef getClassDef(String classId) throws Exception {
		return null;
	}

	@Override
	public SubClassDef getSubClassDef(String modelId) throws Exception {
		return null;
	}

	@Override
	public DeviceModel from(ClassDef classDef, boolean isVersion) throws Exception {
		return null;
	}

	@Override
	public DeviceModel from(SubClassDef subClassDef, boolean isVersion) throws Exception {
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Relation> getAllRelation() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RelationDef getRelationDef(int relationId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Relation> relateToClass(String classId,
			RelationDef[] relationDefs, String[] relationFields)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Relation> relateToSubClass(String modelId,
			RelationDef[] relationDefs, String[] relationFields)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Relation> getAllRelation(Iterator<RelationDef> relationDefs)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
