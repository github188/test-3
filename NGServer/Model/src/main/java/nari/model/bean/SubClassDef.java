package nari.model.bean;

public interface SubClassDef{

	public static final SubClassDef NONE = new SubClassDef(){

		@Override
		public ClassDef getClassDef() {
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
		public String getModelId() {
			return null;
		}

		@Override
		public String getModelName() {
			return null;
		}

		@Override
		public String getModelAlias() {
			return null;
		}

		@Override
		public boolean isConducting() {
			return false;
		}

		@Override
		public String getTerminalDef() {
			return null;
		}

		@Override
		public boolean canEditTerminal() {
			return false;
		}

		@Override
		public String getSymbolFields() {
			return null;
		}

		@Override
		public String getPsrType() {
			return null;
		}

		@Override
		public String getPsrName() {
			return null;
		}

		@Override
		public String getCimType() {
			return null;
		}

		@Override
		public String getGeoType() {
			return null;
		}

		@Override
		public boolean isEquipment() {
			return false;
		}

		@Override
		public String getElectricLogic() {
			return null;
		}		
	};
	
	public ClassDef getClassDef();
	
	public String getModelId();
	
	public String getModelName();
	
	public String getModelAlias();
	
	public boolean isConducting();
	
	public String getTerminalDef();
	
	public boolean canEditTerminal();
	
	public String getSymbolFields();
	
	public String getPsrType();
	
	public String getPsrName();
	
	public String getCimType();
	
	public String getGeoType();
	
	public boolean isEquipment();
	
	public String getElectricLogic();
	
}
