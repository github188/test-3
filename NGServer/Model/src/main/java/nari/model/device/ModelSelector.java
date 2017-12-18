package nari.model.device;

import java.util.Iterator;

import nari.model.bean.ClassDef;
import nari.model.bean.CodeDef;
import nari.model.bean.SubClassDef;
import nari.model.relation.Relation;
import nari.model.relation.RelationDef;

public interface ModelSelector {
	
	public ClassDef getClassDef(String classId) throws Exception;
	
	public SubClassDef getSubClassDef(String modelId) throws Exception;
	
	public DeviceModel from(ClassDef classDef, boolean isVersion) throws Exception;
	
	public DeviceModel from(SubClassDef subClassDef, boolean isVersion) throws Exception;
	
	public CodeDef getCodeDef(String codeType, String code) throws Exception;
	
	public Iterator<CodeDef> getCodeDef(String codeType) throws Exception;

	public Iterator<CodeDef> getAllCodeDef() throws Exception;
	
	// 获取所有的RelationDef
	public Iterator<RelationDef> getAllRelationDef() throws Exception;
	
	// 获取所有的RelationDef
	public Iterator<Relation> getAllRelation() throws Exception;
	
	public Iterator<Relation> getAllRelation(Iterator<RelationDef> relationDefs) throws Exception;
	
	public RelationDef getRelationDef(int relationId) throws Exception;
	
	public Iterator<Relation> relateToClass(String classId, RelationDef[] relationDefs, String[] relationFields) throws Exception;
	
	public Iterator<Relation> relateToSubClass(String modelId, RelationDef[] relationDefs, String[] relationFields) throws Exception;
	
	public static final ModelSelector NONE = new ModelSelector(){

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
		public Iterator<Relation> relateToSubClass(String modelId,
				RelationDef[] relationDefs, String[] relationFields) throws Exception {
			return null;
		}

		@Override
		public RelationDef getRelationDef(int relationId) throws Exception {
			return null;
		}

		@Override
		public Iterator<RelationDef> getAllRelationDef() throws Exception {
			return null;
		}

		@Override
		public Iterator<Relation> getAllRelation() throws Exception {
			return null;
		}

		@Override
		public Iterator<Relation> relateToClass(String classId,
				RelationDef[] relationDefs, String[] relationFields)
				throws Exception {

			return null;
		}

		@Override
		public Iterator<Relation> getAllRelation(
				Iterator<RelationDef> relationDefs) throws Exception {
			return null;
		}
		
	};

}
