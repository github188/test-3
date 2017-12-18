package nari.session.md;

import java.util.Iterator;

import nari.session.codec.CodecRegistry;

public abstract class AbstractMappedModel implements MappedModel {

	@Override
	public ModelObject createObject() throws Exception {
		return new StdModelObject(this);
	}

	@Override
	public Iterator<ModelField> fieldIterator() throws Exception {
		return null;
	}

	@Override
	public ModelField field(String name) throws Exception {
		return null;
	}

	@Override
	public String getModelName() throws Exception {
		return null;
	}

	@Override
	public CodecRegistry getCodecRegistry() throws Exception {
		return null;
	}

	@Override
	public int fieldCount() throws Exception {
		return 0;
	}

	@Override
	public ModelField getPrimaryKey() throws Exception {
		return null;
	}
	
	@Override
	public void destroy() throws Exception {
		
	}
}
