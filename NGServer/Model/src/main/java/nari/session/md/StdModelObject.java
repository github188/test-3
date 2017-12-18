package nari.session.md;

public class StdModelObject implements ModelObject {

	private MappedModel model = null;
	
	public StdModelObject(MappedModel model) {
		this.model = model;
	}
	
	@Override
	public void setInt(String fieldName, int val) throws Exception {
		
	}

	@Override
	public void setLong(String fieldName, long val) throws Exception {
		
	}

	@Override
	public void setDouble(String fieldName, double val) throws Exception {
		
	}

	@Override
	public void setFloat(String fieldName, float val) throws Exception {
		
	}

	@Override
	public void setShort(String fieldName, short val) throws Exception {
		
	}

	@Override
	public void setByte(String fieldName, byte val) throws Exception {
		
	}

	@Override
	public void setString(String fieldName, String val) throws Exception {
		
	}

	@Override
	public void setBoolean(String fieldName, boolean val) throws Exception {
		
	}
	
	@Override
	public void setObject(String fieldName, Object val, Class<?> typeClass) throws Exception {
		
	}

	@Override
	public int getInt(String fieldName, int def) throws Exception {
		return 0;
	}

	@Override
	public long getLong(String fieldName, long def) throws Exception {
		return 0;
	}

	@Override
	public double getDouble(String fieldName, double def) throws Exception {
		return 0;
	}

	@Override
	public float getFloat(String fieldName, float def) throws Exception {
		return 0;
	}

	@Override
	public byte getByte(String fieldName, byte def) throws Exception {
		return 0;
	}

	@Override
	public boolean getBoolean(String fieldName, boolean def) throws Exception {
		return false;
	}
	
	@Override
	public String getString(String fieldName, String def) throws Exception {
		return null;
	}

	@Override
	public <T> T getObject(String fieldName, Class<T> typeClass, int def) throws Exception {
		return null;
	}

	@Override
	public MappedModel getMappedModel() throws Exception {
		return model;
	}

	@Override
	public void clear() throws Exception {
		
	}

}
