package nari.session.md;

import java.util.Iterator;

import nari.session.codec.CodecRegistry;

public interface MappedModel {

	public ModelObject createObject() throws Exception;
	
	public Iterator<ModelField> fieldIterator() throws Exception;
	
	public ModelField field(String name) throws Exception;
	
	public String getModelName() throws Exception;
	
	public CodecRegistry getCodecRegistry() throws Exception;
	
	public int fieldCount() throws Exception;
	
	public void destroy() throws Exception;
	
	public ModelField getPrimaryKey() throws Exception;
}
