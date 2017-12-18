package nari.model.bean;

public class DefaultSubClassDef implements SubClassDef {

	private SubClassObject obj = SubClassObject.NONE;
	
	private ClassDef def = ClassDef.NONE;
	
	public DefaultSubClassDef(ClassDef def,SubClassObject obj){
		this.obj = obj;
		this.def = def;
	}
	
	@Override
	public ClassDef getClassDef() {
		return def;
	}

	@Override
	public String getModelId() {
		return String.valueOf(obj.getModelId());
	}

	@Override
	public String getModelName() {
		return obj.getModelName();
	}

	@Override
	public String getModelAlias() {
		return obj.getModelAlias();
	}

	@Override
	public boolean isConducting() {
		return obj.getIsConducting()==1;
	}

	@Override
	public String getTerminalDef() {
		return obj.getTerminalDef();
	}

	@Override
	public boolean canEditTerminal() {
		return obj.getCanEditTerminal()==1;
	}

	@Override
	public String getSymbolFields() {
		return obj.getSymbolFields();
	}

	@Override
	public String getPsrType() {
		return obj.getPsrType();
	}

	@Override
	public String getPsrName() {
		return obj.getPsrName();
	}

	@Override
	public String getCimType() {
		return String.valueOf(obj.getCimType());
	}

	@Override
	public String getGeoType() {
		return String.valueOf(obj.getGeoType());
	}

	@Override
	public boolean isEquipment() {
		return obj.getIsEquipment()==1;
	}

	@Override
	public String getElectricLogic() {
		return String.valueOf(obj.getElectricLogic());
	}

	@Override
	public int hashCode() {
		int h = 17;
		h = h*37 + obj.getClassId()==0?0:obj.getClassId();
		h = h*37 + obj.getModelId()==0?0:obj.getModelId();
		return h;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof DefaultSubClassDef)){
			return false;
		}
		
		if(this==obj){
			return true;
		}
		
		DefaultSubClassDef other = (DefaultSubClassDef)obj;
		return this.getClassDef().getClassId().equals(other.getClassDef().getClassId()) && this.getModelId().equals(other.getModelId());
	}
	
}
