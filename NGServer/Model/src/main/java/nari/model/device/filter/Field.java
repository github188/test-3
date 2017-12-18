package nari.model.device.filter;

public interface Field {

	public static final Field NONE = new Field(){

		@Override
		public String getFieldName() {
			return null;
		}

		@Override
		public Class<?> getFieldType() {
			return null;
		}

		@Override
		public Object convert(Object value) {
			return null;
		}
		
	};
	
	public String getFieldName();
	
	public Class<?> getFieldType();
	
	public Object convert(Object value);
}
