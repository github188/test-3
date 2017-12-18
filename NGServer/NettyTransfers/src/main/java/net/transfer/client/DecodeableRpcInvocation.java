package net.transfer.client;

import java.io.IOException;
import java.io.InputStream;

public class DecodeableRpcInvocation implements Decodeable {

	private Request request;
	
	private InputStream is;
	
	private Serialization s;
	
	public DecodeableRpcInvocation(Request request,InputStream is,Serialization s) {
		this.request = request;
		this.is = is;
		this.s = s;
	}

	public void decode() {
		ObjectInput input = s.deserialize(is);
		Object msg = null;
		try {
			msg = input.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		request.setData(msg);
	}

}
