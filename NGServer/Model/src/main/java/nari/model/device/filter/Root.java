package nari.model.device.filter;

public interface Root {

	public Selection select(String name);

	public Field get(String name,Class<?> type);
	
	public Field get(String name,Class<?> type,FieldProcessor processor);
}
