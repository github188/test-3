package net.transfer.client;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface Codec {

	public ByteBuffer encode(Channel channel,Object message) throws IOException;
	
	public Object decode(Channel channel,ByteBuffer buffer) throws IOException;
	
}
