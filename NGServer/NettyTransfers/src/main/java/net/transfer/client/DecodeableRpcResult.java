package net.transfer.client;

import java.io.IOException;
import java.io.InputStream;

public class DecodeableRpcResult extends RpcResult implements Decodeable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 336006203333364939L;

	private Response response;
	
	private InputStream is;
	
	private Serialization s;
	
	public DecodeableRpcResult(Response response,InputStream is,Serialization s){
		this.response = response;
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
		response.setResult(msg);
	}

}
