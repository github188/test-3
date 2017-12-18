package nari.model.bean;

public interface ClassDef{

	public static final ClassDef NONE = new ClassDef(){

		@Override
		public SubClassDef[] getSubClassDef() {
			return null;
		}

		@Override
		public boolean equals(Object obj) {
			return super.equals(obj);
		}

		@Override
		public int hashCode() {
			return super.hashCode();
		}

		@Override
		public String getClassId() {
			return null;
		}

		@Override
		public String getClassAlias() {
			return null;
		}

		@Override
		public String getClassName() {
			return null;
		}
		
		@Override
		public String getTableName() {
			return null;
		}

		@Override
		public String getClassType() {
			return null;
		}

		@Override
		public boolean isGeometry() {
			return false;
		}

		@Override
		public boolean isContainer() {
			return false;
		}

		@Override
		public String getElectricLogic() {
			return null;
		}

		@Override
		public String getNetLogic() {
			return null;
		}

		@Override
		public String getFeatureClass() {
			return null;
		}

		@Override
		public String getEquipmentID() {
			return null;
		}

		@Override
		public boolean isNamePlate() {
			return false;
		}

		@Override
		public boolean isEquipment() {
			return false;
		}

		@Override
		public boolean isHangPlate() {
			return false;
		}
	};
	
	public SubClassDef[] getSubClassDef();
	
	public String getClassId();
	
	public String getClassAlias();
	
	public String getClassName();
	
	public String getTableName();
	
	public String getClassType();
	
	public boolean isGeometry();
	
	public boolean isContainer();
	
	public String getElectricLogic();
	
	public String getNetLogic();
	
	public String getFeatureClass();
	
	public String getEquipmentID();
	
	public boolean isNamePlate();
	
	public boolean isEquipment();
	
	public boolean isHangPlate();
}
