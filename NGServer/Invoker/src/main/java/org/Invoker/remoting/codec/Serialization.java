package org.Invoker.remoting.codec;

import java.io.InputStream;
import java.io.OutputStream;

import org.Invoker.rpc.Adaptive;
import org.Invoker.rpc.SPI;

@SPI
public interface Serialization {

	public byte getId();
	
	public String getContentType();
	
	@Adaptive
	public ObjectOutput serialize(OutputStream stream);
	
	@Adaptive
	public ObjectInput deserialize(InputStream stream);
}
