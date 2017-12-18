package nari.model.bean;

import nari.model.TableName;

public class DefaultVerClassDef implements ClassDef {

	private ClassDef classDef = ClassDef.NONE;
	
	private SubClassDef[] subClassDef = null;
	
	private String tableName = null;
	
	public DefaultVerClassDef(ClassDef classDef,SubClassDef[] subClassDef){
		this.classDef = classDef;
		this.subClassDef =subClassDef;
		tableName = this.classDef.getTableName() +"_VER"; // by pang
	}
	
	@Override
	public SubClassDef[] getSubClassDef() {
		return subClassDef;
	}

	@Override
	public String getClassId() {
		return classDef.getClassId();
	}

	@Override
	public String getClassAlias() {
		return classDef.getClassAlias();
	}

	@Override
	public String getClassName() {
		return classDef.getClassName();
	}
	
	@Override
	public String getTableName() {
		return tableName;
	}

	@Override
	public String getClassType() {
		return classDef.getClassType();
	}

	@Override
	public boolean isGeometry() {
		return classDef.isGeometry();
	}

	@Override
	public boolean isContainer() {
		return classDef.isContainer();
	}

	@Override
	public String getElectricLogic() {
		return classDef.getElectricLogic();
	}

	@Override
	public String getNetLogic() {
		return classDef.getElectricLogic();
	}

	@Override
	public String getFeatureClass() {
		return classDef.getFeatureClass();
	}

	@Override
	public String getEquipmentID() {
		return classDef.getEquipmentID();
	}

	@Override
	public boolean isNamePlate() {
		return classDef.isNamePlate();
	}

	@Override
	public boolean isEquipment() {
		return classDef.isEquipment();
	}

	@Override
	public boolean isHangPlate() {
		return classDef.isHangPlate();
	}

}
