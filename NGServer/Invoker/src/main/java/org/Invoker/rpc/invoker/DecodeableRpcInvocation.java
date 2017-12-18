package org.Invoker.rpc.invoker;

import java.io.IOException;
import java.io.InputStream;

import org.Invoker.remoting.codec.CodecSupport;
import org.Invoker.remoting.codec.ObjectInput;
import org.Invoker.remoting.codec.Serialization;
import org.Invoker.remoting.exchanger.Request;
import org.Invoker.rpc.result.Decodeable;

public class DecodeableRpcInvocation extends RpcInvocation implements Decodeable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2010715912155246927L;

	private Request request;
	
	private InputStream is;
	
	private byte serid;
	
	public DecodeableRpcInvocation(Request request,InputStream is,byte serid) {
		this.request = request;
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
		request.setData(msg);
	}

}
