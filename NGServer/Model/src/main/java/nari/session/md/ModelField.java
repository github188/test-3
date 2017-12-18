package nari.session.md;

public interface ModelField {

	public String getName();
	
	public Class<?> getType();
	
	public Object get(ModelObject obj) throws Exception;
	
	public void set(ModelObject obj,Object val) throws Exception;
	
	public MappedModel getMappedModel() throws Exception;
	
	public boolean isPrimaryKey();
}
