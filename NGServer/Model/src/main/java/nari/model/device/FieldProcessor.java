package nari.model.device;

public interface FieldProcessor {

	public static final FieldProcessor NONE = new FieldProcessor(){

		@Override
		public Object process(Object value) {
			return null;
		}
		
	};
	
	public Object process(Object value);
}
