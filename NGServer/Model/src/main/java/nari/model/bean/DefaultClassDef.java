package nari.model.bean;

import nari.model.TableName;

public class DefaultClassDef implements ClassDef {

	private SubClassDef[] defs = new SubClassDef[]{};
	
	private ClassObject obj = ClassObject.NONE;
	
	private String tableName = null;
	
	public DefaultClassDef(SubClassDef[] defs,ClassObject obj){
		this.defs = defs;
		this.obj = obj;
		tableName = (TableName.DB_USER_NAME + this.obj.getClassName()).toUpperCase(); // by pang
	}
	
	@Override
	public SubClassDef[] getSubClassDef() {
		return defs;
	}

	@Override
	public String getClassId() {
		return String.valueOf(obj.getClassId());
	}

	@Override
	public String getClassAlias() {
		return obj.getClassAlias();
	}

	@Override
	public String getClassName() {
		return obj.getClassName();
	}
	
	@Override
	public String getTableName() {
		return tableName;
	}

	@Override
	public String getClassType() {
		return String.valueOf(obj.getClassType());
	}

	@Override
	public boolean isGeometry() {
		return obj.getIsGeometry()==1;
	}

	@Override
	public boolean isContainer() {
		return obj.getIsContainer()==1;
	}

	@Override
	public String getElectricLogic() {
		return String.valueOf(obj.getElectricLogic());
	}

	@Override
	public String getNetLogic() {
		return String.valueOf(obj.getNetLogic());
	}

	@Override
	public String getFeatureClass() {
		return obj.getEnumFeatureClass();
	}

	@Override
	public String getEquipmentID() {
		return obj.getEquipmentId();
	}

	@Override
	public boolean isNamePlate() {
		return obj.getIsNameplate()==1;
	}

	@Override
	public boolean isHangPlate() {
		return obj.getIsHangplate()==1;
	}

	@Override
	public boolean isEquipment() {
		return obj.getIsEquipment()==1;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof DefaultClassDef)){
			return false;
		}
		
		if(this==obj){
			return true;
		}
		
		DefaultClassDef other = (DefaultClassDef)obj;
		return this.getClassId().equals(other.getClassId());
	}
	
	@Override
	public int hashCode() {
		int h = 17;
		h = h*37 + obj.getClassId()==0?0:obj.getClassId();
		return h;
	}
	
}
