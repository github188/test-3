package org.Invoker.rpc.result;

import java.io.IOException;
import java.io.InputStream;

import org.Invoker.remoting.codec.CodecSupport;
import org.Invoker.remoting.codec.ObjectInput;
import org.Invoker.remoting.codec.Serialization;
import org.Invoker.remoting.exchanger.Response;
import org.Invoker.rpc.invoker.Invocation;

public class DecodeableRpcResult extends RpcResult implements Decodeable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 336006203333364939L;

	private Response response;
	
	private InputStream is;
	
//	private Invocation inv;
	
	private byte serid;
	
	
	public DecodeableRpcResult(Response response,InputStream is,Invocation inv,byte serid){
		this.response = response;
//		this.inv = inv;
		this.is = is;
		this.serid = serid;
	}
	
	public void decode() {
		Serialization s = CodecSupport.getSerialization(serid);
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
