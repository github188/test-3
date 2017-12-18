package nari.model.bean;

public class NoLoopSubClassDef implements SubClassDef {

	private SubClassDef def = null;
	
	private ClassDef pdef = null;
	
	public NoLoopSubClassDef(SubClassDef def,ClassDef pdef){
		this.def = def;
		this.pdef = pdef;
	}
	
	@Override
	public ClassDef getClassDef() {
		return pdef;
	}

	@Override
	public String getModelId() {
		return def.getModelId();
	}

	@Override
	public String getModelName() {
		return def.getModelName();
	}

	@Override
	public String getModelAlias() {
		return def.getModelAlias();
	}

	@Override
	public boolean isConducting() {
		return def.isConducting();
	}

	@Override
	public String getTerminalDef() {
		return def.getTerminalDef();
	}

	@Override
	public boolean canEditTerminal() {
		return def.canEditTerminal();
	}

	@Override
	public String getSymbolFields() {
		return def.getSymbolFields();
	}

	@Override
	public String getPsrType() {
		return def.getPsrType();
	}

	@Override
	public String getPsrName() {
		return def.getPsrName();
	}

	@Override
	public String getCimType() {
		return def.getCimType();
	}

	@Override
	public String getGeoType() {
		return def.getGeoType();
	}

	@Override
	public boolean isEquipment() {
		return def.isEquipment();
	}

	@Override
	public String getElectricLogic() {
		return def.getElectricLogic();
	}

}
