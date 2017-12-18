package nari.model.device;

import java.util.Iterator;

import nari.model.bean.ClassDef;
import nari.model.bean.CodeDef;
import nari.model.bean.SubClassDef;
import nari.model.relation.Relation;
import nari.model.relation.RelationDef;

public interface ModelService {
	
	public static final ModelService NONE = new ModelService(){

		@Override
		public DeviceModel fromClass(String classId, boolean isVersion) throws Exception {
			return null;
		}

		@Override
		public DeviceModel fromSubClass(String modelId, boolean isVersion) throws Exception {
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
		public ClassDef getClassDef(String classId) throws Exception {
			return null;
		}

		@Override
		public SubClassDef getSubClassDef(String modelId) throws Exception {
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
		public GeneralModel fromName(String modelName) throws Exception {
			return null;
		}

		@Override
		public Iterator<Relation> relateToClass(String classId,
				RelationDef[] relationDefs, String[] relationFields) {
			return null;
		}

		@Override
		public Iterator<Relation> relateToSubClass(String modelId,
				RelationDef[] relationDefs, String[] relationFields) {
			return null;
		}

		@Override
		public RelationDef getRelationDef(int relationId) throws Exception {
			return null;
		}
		
	};

	public DeviceModel fromClass(String classId, boolean isVersion) throws Exception;
	
	/**
	 * 根据一个设备子类型获取DeviceModel
	 * @param modelId
	 * @param isVersion
	 * @return
	 * @throws Exception
	 */
	public DeviceModel fromSubClass(String modelId, boolean isVersion) throws Exception;
	
	public DeviceModel from(ClassDef classDef, boolean isVersion) throws Exception;
	
	public DeviceModel from(SubClassDef subClassDef, boolean isVersion) throws Exception;
	
	public ClassDef getClassDef(String classId) throws Exception;
	
	public SubClassDef getSubClassDef(String modelId) throws Exception;
	
	public CodeDef getCodeDef(String codeType,String code) throws Exception;
	
	public Iterator<CodeDef> getCodeDef(String codeType) throws Exception;
	
	public Iterator<CodeDef> getAllCodeDef() throws Exception;
	
	public GeneralModel fromName(String modelName) throws Exception;
	
	public RelationDef getRelationDef(int relationId) throws Exception;
	
	/**
	 * 获取设备类型下所有的关系
	 * @param classId 设备类型
	 * @param relationDefs 关系类型，若为空，则表示查询所有关系类型
	 * @param relationFields 关联字段，若不为空，则表示指定查询某一种关系
	 * @return
	 */
	public Iterator<Relation> relateToClass(String classId, RelationDef[] relationDefs, String[] relationFields) throws Exception;
	
	/**
	 * 获取设备子类型下所有的关系
	 * @param modelId 设备子类型
	 * @param relationDefs 关系类型，若为空，则表示查询所有关系类型
	 * @param relationFields 关联字段，若不为空，则表示指定查询某一种关系
	 * @return
	 */
	public Iterator<Relation> relateToSubClass(String modelId, RelationDef[] relationDefs, String[] relationFields) throws Exception;
}
