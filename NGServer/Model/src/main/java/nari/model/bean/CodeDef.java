package nari.model.bean;

public interface CodeDef{

	public static final CodeDef NONE = new CodeDef(){

		@Override
		public boolean equals(Object obj) {
			return super.equals(obj);
		}
		
		@Override
		public int hashCode() {
			return super.hashCode();
		}
	};
}
