package net.transfer.client;

import java.io.InputStream;
import java.io.OutputStream;

public interface Serialization {

	public byte getId();
	
	public String getContentType();
	
	public ObjectOutput serialize(OutputStream stream);
	
	public ObjectInput deserialize(InputStream stream);
}
