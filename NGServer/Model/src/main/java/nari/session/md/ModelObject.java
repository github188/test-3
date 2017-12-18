package nari.session.md;

public interface ModelObject {

	public void setInt(String fieldName,int val) throws Exception;
	
	public void setLong(String fieldName,long val) throws Exception;
	
	public void setDouble(String fieldName,double val) throws Exception;
	
	public void setFloat(String fieldName,float val) throws Exception;
	
	public void setShort(String fieldName,short val) throws Exception;
	
	public void setByte(String fieldName,byte val) throws Exception;
	
	public void setString(String fieldName,String val) throws Exception;
	
	public void setBoolean(String fieldName,boolean val) throws Exception;
	
	public void setObject(String fieldName,Object val,Class<?> typeClass) throws Exception;
	
	public int getInt(String fieldName,int def) throws Exception;
	
	public long getLong(String fieldName,long def) throws Exception;
	
	public double getDouble(String fieldName,double def) throws Exception;
	
	public float getFloat(String fieldName,float def) throws Exception;
	
	public byte getByte(String fieldName,byte def) throws Exception;
	
	public boolean getBoolean(String fieldName,boolean def) throws Exception;
	
	public String getString(String fieldName,String def) throws Exception;
	
	public <T> T getObject(String fieldName,Class<T> typeClass,int def) throws Exception;
	
	public MappedModel getMappedModel() throws Exception;
	
	public void clear() throws Exception;
}
