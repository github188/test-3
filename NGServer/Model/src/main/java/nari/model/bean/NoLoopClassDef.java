package nari.model.bean;

public class NoLoopClassDef implements ClassDef {

	private ClassDef def = null;
	private SubClassDef[] defs = null;
	
	public NoLoopClassDef(ClassDef def,SubClassDef[] defs){
		this.def = def;
		this.defs = defs;
	}
	
	@Override
	public SubClassDef[] getSubClassDef() {
		return defs;
	}

	@Override
	public String getClassId() {
		return def.getClassId();
	}

	@Override
	public String getClassAlias() {
		return def.getClassAlias();
	}

	@Override
	public String getClassName() {
		return def.getClassName();
	}
	
	@Override
	public String getTableName() {
		return def.getTableName();
	}

	@Override
	public String getClassType() {
		return def.getClassType();
	}

	@Override
	public boolean isGeometry() {
		return def.isGeometry();
	}

	@Override
	public boolean isContainer() {
		return def.isContainer();
	}

	@Override
	public String getElectricLogic() {
		return def.getElectricLogic();
	}

	@Override
	public String getNetLogic() {
		return def.getNetLogic();
	}

	@Override
	public String getFeatureClass() {
		return def.getFeatureClass();
	}

	@Override
	public String getEquipmentID() {
		return def.getEquipmentID();
	}

	@Override
	public boolean isNamePlate() {
		return def.isNamePlate();
	}

	@Override
	public boolean isEquipment() {
		return def.isEquipment();
	}

	@Override
	public boolean isHangPlate() {
		return def.isHangPlate();
	}

}
