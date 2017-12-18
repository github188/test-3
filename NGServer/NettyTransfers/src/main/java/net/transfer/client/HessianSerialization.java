package net.transfer.client;

import java.io.InputStream;
import java.io.OutputStream;

public class HessianSerialization implements Serialization {
    
    public static final byte ID = 2;

	public ObjectInput deserialize(InputStream stream) {
		return new HessianObjectInput(stream);
	}

	public byte getId() {
		return ID;
	}

	public ObjectOutput serialize(OutputStream stream) {
		return new HessianObjectOutput(stream);
	}

	@Override
	public String getContentType() {
		return "x-application/hessian2";
	}
	
}