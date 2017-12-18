package nari.model.device;

import java.util.Iterator;

import nari.MongoClient.interfaces.MongoAdaptor;
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

import org.bson.BsonDocument;
import org.bson.BsonString;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class MongoModelSelector implements ModelSelector {

	private MongoAdaptor mongoAdapter;
	
	public MongoModelSelector(MongoAdaptor mongoAdapter) {
		this.mongoAdapter = mongoAdapter;
	}
	
	@Override
	public ClassDef getClassDef(String classId) throws Exception {
		
		MongoCollection<BsonDocument> mongoColl = null;
		BsonDocument classDoc = null;
		
		try {
			mongoColl = mongoAdapter.getMongoCollection(TableName.CONF_OBJECTMETA);
			classDoc = mongoAdapter.find(mongoColl, new BsonDocument().append("classid", new BsonString(classId)),null).first();
		} catch (Exception e) {
			throw new Exception("exception when query from mongodb.", e);
		}
		
		SubClassDef[] subDef = new SubClassDef[]{};
		
		ClassObject obj = new ClassObject();
		obj.setClassAlias(classDoc.getString("CLASSALIAS").getValue());
		obj.setClassId(classDoc.getInt32("CLASSID").getValue());
		obj.setClassName(classDoc.getString("CLASSNAME").getValue());
		obj.setClassType(classDoc.getInt32("CLASSTYPE").getValue());
		obj.setElectricLogic(classDoc.getInt32("ELECTRICLOGIC").getValue());
		obj.setEnumFeatureClass(classDoc.getString("ENUMFEATURECLASS").getValue());
		obj.setEquipmentId(classDoc.getString("EQUIPMENTID").getValue());
		obj.setIsContainer(classDoc.getInt32("ISCONTAINER").getValue());
		obj.setIsEquipment(classDoc.getInt32("ISEQUIPMENT").getValue());
		obj.setIsGeometry(classDoc.getInt32("ISGEOMETRY").getValue());
		obj.setIsHangplate(classDoc.getInt32("ISHANGPLATE").getValue());
		obj.setIsNameplate(classDoc.getInt32("ISNAMEPLATE").getValue());
		obj.setNetLogic(classDoc.getInt32("NETLOGIC").getValue());
		
		ClassDef def = new DefaultClassDef(subDef,obj);
		
		MongoCursor<BsonDocument> cursor = null;
		BsonDocument doc = null;
		int count = 0;
		try {
			mongoColl = mongoAdapter.getMongoCollection(TableName.CONF_MODELMETA);
			count = mongoAdapter.count(mongoColl,new BsonDocument().append("classid", new BsonString(classId)));
			cursor = mongoAdapter.find(mongoColl, new BsonDocument().append("classid", new BsonString(classId)),null).iterator();
		} catch (Exception e) {
			throw new Exception("exception when query from mongodb.", e);
		}
		
		subDef = new SubClassDef[count];
		int i = 0;
		while(cursor.hasNext()){
			doc = cursor.next();
			
			SubClassObject sub = new SubClassObject();
			sub.setCanEditTerminal(doc.getInt32("CANEDITTERMINAL").getValue());
			sub.setCimType(doc.getInt32("CIMTYPE").getValue());
			sub.setClassId(doc.getInt32("CLASSID").getValue());
			sub.setElectricLogic(doc.getInt32("ELECTRICLOGIC").getValue());
			sub.setGeoType(doc.getInt32("GEOTYPE").getValue());
			sub.setIsConducting(doc.getInt32("ISCONDUCTING").getValue());
			sub.setIsEquipment(doc.getInt32("ISEQUIPMENT").getValue());
			sub.setModelAlias(doc.getString("MODELALIAS").getValue());
			sub.setModelId(doc.getInt32("MODELID").getValue());
			sub.setModelName(doc.getString("MODELNAME").getValue());
			sub.setPsrName(doc.getString("PSRNAME").getValue());
			sub.setPsrType(doc.getString("PSRTYPE").getValue());
			sub.setSymbolFields(doc.getString("SYMBOLFIELDS").getValue());
			sub.setTerminalDef(doc.getString("TERMINALDEF").getValue());
			subDef[i++] = new DefaultSubClassDef(def,sub);
			
		}
		cursor.close();
		ClassDef npDef = new NoLoopClassDef(def,subDef);
		return npDef;
	}

	@Override
	public SubClassDef getSubClassDef(String modelId) throws Exception {
		MongoCollection<BsonDocument> mongoColl = null;
		BsonDocument modelDoc = null;
		
		try {
			mongoColl = mongoAdapter.getMongoCollection(TableName.CONF_MODELMETA);
			modelDoc = mongoAdapter.find(mongoColl, new BsonDocument().append("modelid", new BsonString(modelId)),null).first();
		} catch (Exception e) {
			throw new Exception("exception when query from mongodb.", e);
		}
		
		SubClassObject sub = new SubClassObject();
		sub.setCanEditTerminal(modelDoc.getInt32("CANEDITTERMINAL").getValue());
		sub.setCimType(modelDoc.getInt32("CIMTYPE").getValue());
		sub.setClassId(modelDoc.getInt32("CLASSID").getValue());
		sub.setElectricLogic(modelDoc.getInt32("ELECTRICLOGIC").getValue());
		sub.setGeoType(modelDoc.getInt32("GEOTYPE").getValue());
		sub.setIsConducting(modelDoc.getInt32("ISCONDUCTING").getValue());
		sub.setIsEquipment(modelDoc.getInt32("ISEQUIPMENT").getValue());
		sub.setModelAlias(modelDoc.getString("MODELALIAS").getValue());
		sub.setModelId(modelDoc.getInt32("MODELID").getValue());
		sub.setModelName(modelDoc.getString("MODELNAME").getValue());
		sub.setPsrName(modelDoc.getString("PSRNAME").getValue());
		sub.setPsrType(modelDoc.getString("PSRTYPE").getValue());
		sub.setSymbolFields(modelDoc.getString("SYMBOLFIELDS").getValue());
		sub.setTerminalDef(modelDoc.getString("TERMINALDEF").getValue());
		
		SubClassDef def = new DefaultSubClassDef(ClassDef.NONE, sub);
		
		BsonDocument classDoc = null;
		try {
			mongoColl = mongoAdapter.getMongoCollection(TableName.CONF_OBJECTMETA);
			classDoc = mongoAdapter.find(mongoColl, new BsonDocument().append("classid", new BsonString(String.valueOf(sub.getClassId()))),null).first();
		} catch (Exception e) {
			throw new Exception("exception when query from mongodb.", e);
		}
		
		ClassObject obj = new ClassObject();
		obj.setClassAlias(classDoc.getString("CLASSALIAS").getValue());
		obj.setClassId(classDoc.getInt32("CLASSID").getValue());
		obj.setClassName(classDoc.getString("CLASSNAME").getValue());
		obj.setClassType(classDoc.getInt32("CLASSTYPE").getValue());
		obj.setElectricLogic(classDoc.getInt32("ELECTRICLOGIC").getValue());
		obj.setEnumFeatureClass(classDoc.getString("ENUMFEATURECLASS").getValue());
		obj.setEquipmentId(classDoc.getString("EQUIPMENTID").getValue());
		obj.setIsContainer(classDoc.getInt32("ISCONTAINER").getValue());
		obj.setIsEquipment(classDoc.getInt32("ISEQUIPMENT").getValue());
		obj.setIsGeometry(classDoc.getInt32("ISGEOMETRY").getValue());
		obj.setIsHangplate(classDoc.getInt32("ISHANGPLATE").getValue());
		obj.setIsNameplate(classDoc.getInt32("ISNAMEPLATE").getValue());
		obj.setNetLogic(classDoc.getInt32("NETLOGIC").getValue());
		
		ClassDef pdef = new DefaultClassDef(new SubClassDef[]{},obj);
		SubClassDef subDef = new NoLoopSubClassDef(def, pdef);
		
		return subDef;
	}

	@Override
	public DeviceModel from(ClassDef classDef, boolean isVersion) throws Exception {
		DeviceModel model = null;
		if(isVersion){
			DeviceModel base = new BaseMongoDeviceModel(classDef,classDef.getSubClassDef(),mongoAdapter);
			model = new VersionMongoDeviceModel(base,mongoAdapter);
		}else{
			model = new BaseMongoDeviceModel(classDef,classDef.getSubClassDef(),mongoAdapter);
		}
		return model;
	}

	@Override
	public DeviceModel from(SubClassDef subClassDef, boolean isVersion) throws Exception {
		DeviceModel model = null;
		if(isVersion){
			DeviceModel base = new BaseMongoDeviceModel(subClassDef.getClassDef(),new SubClassDef[]{subClassDef},mongoAdapter);
			model = new VersionMongoDeviceModel(base,mongoAdapter);
		}else{
			model = new BaseMongoDeviceModel(subClassDef.getClassDef(),new SubClassDef[]{subClassDef},mongoAdapter);
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
