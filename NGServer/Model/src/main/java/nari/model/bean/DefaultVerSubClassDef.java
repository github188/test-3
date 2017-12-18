package nari.model.bean;

public class DefaultVerSubClassDef implements SubClassDef {

	private ClassDef classDef = ClassDef.NONE;
	
	private SubClassDef subClassDef = null;
	
	public DefaultVerSubClassDef(ClassDef classDef,SubClassDef subClassDef){
		this.classDef = classDef;
		this.subClassDef = subClassDef;
	}
	
	@Override
	public ClassDef getClassDef() {
		return classDef;
	}

	@Override
	public String getModelId() {
		return subClassDef.getModelId();
	}

	@Override
	public String getModelName() {
		return subClassDef.getModelName();
	}

	@Override
	public String getModelAlias() {
		return subClassDef.getModelAlias();
	}

	@Override
	public boolean isConducting() {
		return subClassDef.isConducting();
	}

	@Override
	public String getTerminalDef() {
		return subClassDef.getTerminalDef();
	}

	@Override
	public boolean canEditTerminal() {
		return subClassDef.canEditTerminal();
	}

	@Override
	public String getSymbolFields() {
		return subClassDef.getSymbolFields();
	}

	@Override
	public String getPsrType() {
		return subClassDef.getPsrType();
	}

	@Override
	public String getPsrName() {
		return subClassDef.getPsrName();
	}

	@Override
	public String getCimType() {
		return subClassDef.getCimType();
	}

	@Override
	public String getGeoType() {
		return subClassDef.getGeoType();
	}

	@Override
	public boolean isEquipment() {
		return subClassDef.isEquipment();
	}

	@Override
	public String getElectricLogic() {
		return subClassDef.getElectricLogic();
	}

}
