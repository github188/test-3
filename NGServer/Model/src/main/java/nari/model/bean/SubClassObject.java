package nari.model.bean;

public class SubClassObject {

	public static final SubClassObject NONE = new SubClassObject();
	
	private int modelId;
	
	private int classId;
	
	private String modelName;
	
	private String modelAlias;
	
	private int isConducting;
	
	private String terminalDef;
	
	private int canEditTerminal;
	
	private String symbolFields;
	
	private String psrType;
	
	private String psrName;
	
	private int cimType;
	
	private int geoType;
	
	private int isEquipment;
	
	private int electricLogic;

	public int getModelId() {
		return modelId;
	}

	public void setModelId(int modelId) {
		this.modelId = modelId;
	}

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getModelAlias() {
		return modelAlias;
	}

	public void setModelAlias(String modelAlias) {
		this.modelAlias = modelAlias;
	}

	public int getIsConducting() {
		return isConducting;
	}

	public void setIsConducting(int isConducting) {
		this.isConducting = isConducting;
	}

	public String getTerminalDef() {
		return terminalDef;
	}

	public void setTerminalDef(String terminalDef) {
		this.terminalDef = terminalDef;
	}

	public int getCanEditTerminal() {
		return canEditTerminal;
	}

	public void setCanEditTerminal(int canEditTerminal) {
		this.canEditTerminal = canEditTerminal;
	}

	public String getSymbolFields() {
		return symbolFields;
	}

	public void setSymbolFields(String symbolFields) {
		this.symbolFields = symbolFields;
	}

	public String getPsrType() {
		return psrType;
	}

	public void setPsrType(String psrType) {
		this.psrType = psrType;
	}

	public String getPsrName() {
		return psrName;
	}

	public void setPsrName(String psrName) {
		this.psrName = psrName;
	}

	public int getCimType() {
		return cimType;
	}

	public void setCimType(int cimType) {
		this.cimType = cimType;
	}

	public int getGeoType() {
		return geoType;
	}

	public void setGeoType(int geoType) {
		this.geoType = geoType;
	}

	public int getIsEquipment() {
		return isEquipment;
	}

	public void setIsEquipment(int isEquipment) {
		this.isEquipment = isEquipment;
	}

	public int getElectricLogic() {
		return electricLogic;
	}

	public void setElectricLogic(int electricLogic) {
		this.electricLogic = electricLogic;
	}

}
