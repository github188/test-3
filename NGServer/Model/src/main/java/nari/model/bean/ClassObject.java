package nari.model.bean;

public class ClassObject {

	public static final ClassObject NONE = new ClassObject();
	
	private int classId;
	
	private String classAlias;
	
	private String className;
	
	private int classType;
	
	private int isGeometry;
	
	private int isContainer;
	
	private int electricLogic;
	
	private int netLogic;
	
	private String enumFeatureClass;
	
	private String equipmentId;
	
	private int isNameplate;
	
	private int isEquipment;
	
	private int isHangplate;

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public String getClassAlias() {
		return classAlias;
	}

	public void setClassAlias(String classAlias) {
		this.classAlias = classAlias;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getClassType() {
		return classType;
	}

	public void setClassType(int classType) {
		this.classType = classType;
	}

	public int getIsGeometry() {
		return isGeometry;
	}

	public void setIsGeometry(int isGeometry) {
		this.isGeometry = isGeometry;
	}

	public int getIsContainer() {
		return isContainer;
	}

	public void setIsContainer(int isContainer) {
		this.isContainer = isContainer;
	}

	public int getElectricLogic() {
		return electricLogic;
	}

	public void setElectricLogic(int electricLogic) {
		this.electricLogic = electricLogic;
	}

	public int getNetLogic() {
		return netLogic;
	}

	public void setNetLogic(int netLogic) {
		this.netLogic = netLogic;
	}

	public String getEnumFeatureClass() {
		return enumFeatureClass;
	}

	public void setEnumFeatureClass(String enumFeatureClass) {
		this.enumFeatureClass = enumFeatureClass;
	}

	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}

	public int getIsNameplate() {
		return isNameplate;
	}

	public void setIsNameplate(int isNameplate) {
		this.isNameplate = isNameplate;
	}

	public int getIsEquipment() {
		return isEquipment;
	}

	public void setIsEquipment(int isEquipment) {
		this.isEquipment = isEquipment;
	}

	public int getIsHangplate() {
		return isHangplate;
	}

	public void setIsHangplate(int isHangplate) {
		this.isHangplate = isHangplate;
	}

}
