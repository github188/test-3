package org.Invoker.remoting.codec;

import java.io.IOException;
import java.io.InputStream;

import org.Invoker.remoting.buffer.ChannelBuffer;
import org.Invoker.remoting.buffer.ChannelBufferInputStream;
import org.Invoker.remoting.buffer.ChannelBufferOutputStream;
import org.Invoker.remoting.exchanger.AbstractCodec;
import org.Invoker.remoting.exchanger.Channel;
import org.Invoker.remoting.exchanger.InvokerFuture;
import org.Invoker.remoting.exchanger.RemotingException;
import org.Invoker.remoting.exchanger.Request;
import org.Invoker.remoting.exchanger.Response;
import org.Invoker.rpc.extension.ExtensionLoader;
import org.Invoker.rpc.invoker.DecodeableRpcInvocation;
import org.Invoker.rpc.invoker.Invocation;
import org.Invoker.rpc.result.DecodeableRpcResult;

public class ExchangerCodec extends AbstractCodec implements Codec{

	protected static final int HEADER_LENGTH = 16;
	
	protected static final short MAGIC = (short) 0xdabb;
	
	protected static final byte MAGIC_HIGH = Bytes.short2bytes(MAGIC)[0];
	
	protected static final byte MAGIC_LOW = Bytes.short2bytes(MAGIC)[1];
	
	protected static final byte FLAG_REQUEST = (byte) 0x80;
	
	protected static final byte FLAG_RESPONSE = (byte) 0x60;
	
	protected static final byte FLAG_SEND = (byte) 0x40;
	
	protected static final byte FLAG_HEARTBEAT = (byte) 0x20;
	
	public Short getMagicCode() {
	    return MAGIC;
	}
	
	public void encode(Channel channel, ChannelBuffer buffer, Object msg) throws IOException {
	    if (msg instanceof Request) {
	        encodeRequest(channel, buffer, (Request) msg);
	    } else if (msg instanceof Response) {
	        encodeResponse(channel, buffer, (Response) msg);
	    }
	}
	
	public Object decode(Channel channel, ChannelBuffer buffer) throws IOException {
	    int readable = buffer.readableBytes();
	    byte[] header = new byte[Math.min(readable, HEADER_LENGTH)];
	    buffer.readBytes(header);
	    return decode(channel, buffer, readable, header);
	}
	
	protected Object decode(Channel channel, ChannelBuffer buffer, int readable, byte[] header) throws IOException {
	    if (readable < HEADER_LENGTH) {
	        return DecodeResult.MORE_MESSAGE;
	    }
	    
	    int len = Bytes.bytes2int(header, 12);
	    
	    int tt = len + HEADER_LENGTH;
	    if( readable < tt ) {
	        return DecodeResult.MORE_MESSAGE;
	    }
	
	    ChannelBufferInputStream is = new ChannelBufferInputStream(buffer, 16);
	    
	    return decodeBody(channel, is, header);
	}
	
	protected Object decodeBody(Channel channel, InputStream is, byte[] header) throws IOException {
		byte flag = header[2]; 
        Serialization s = CodecSupport.getSerialization(flag);
        long id = Bytes.bytes2long(header, 4);
        
        byte type = header[3];
        if((type&FLAG_REQUEST)==FLAG_REQUEST){
        	type = FLAG_REQUEST;
        }else if((type&FLAG_RESPONSE)==FLAG_RESPONSE){
        	type = FLAG_RESPONSE;
        }else if((type&FLAG_HEARTBEAT)==FLAG_HEARTBEAT){
        	type = FLAG_HEARTBEAT;
        }else if((type&FLAG_SEND)==FLAG_SEND){
        	type = FLAG_SEND;
        }
        
        if(type == FLAG_REQUEST || type == FLAG_SEND){
        	Request req = new Request(id);
            req.setVersion("2.0.0");
            req.setRequest(type == FLAG_REQUEST?true:false);
            req.setSend(type == FLAG_SEND?true:false);
            try {
                DecodeableRpcInvocation inv = new DecodeableRpcInvocation(req, is, flag);
                inv.decode();
            } catch (Throwable t) {
            	t.printStackTrace();
                req.setData(t);
            }
            return req;
        }else if (type == FLAG_RESPONSE){
        	Response res = new Response(id);
            byte status = header[3];
            status = (byte)(status - (status&FLAG_RESPONSE));
            res.setStatus(status);
            if (status == Response.OK) {
                try {
                    DecodeableRpcResult result = new DecodeableRpcResult(res, is, (Invocation)getRequestData(id), flag);
                    result.decode();
//                    Object data = result;
//                    res.setResult(data);
                } catch (Throwable t) {
                    res.setStatus(Response.CLIENT_ERROR);
                    res.setErrorMessage(t.getMessage());
                }
            } else {
                res.setErrorMessage(s.deserialize(is).readUTF());
            }
            return res;
        }else if(type == FLAG_HEARTBEAT){
        	
        }
        return null;
	}
	
	protected Object getRequestData(long id) {
	    InvokerFuture future = InvokerFuture.getFuture(id);
	    if (future == null)
	        return null;
	    Request req = future.getRequest();
	    if (req == null)
	        return null;
	    return req.getData();
	}
	
	protected void encodeRequest(Channel channel, ChannelBuffer buffer, Request req) throws IOException {
	    Serialization serialization = getSerialization(channel);
	    //0-1  MAGIC
	    //2    serialization id
	    //3    type
	    //4-12 request id
	    byte[] header = new byte[HEADER_LENGTH];
	    Bytes.short2bytes(MAGIC, header);
	    header[2] = serialization.getId();
	
	    if(req.isHeartbeat()){
	    	header[3] = FLAG_HEARTBEAT;
	    }else if(req.isRequest()){
	    	header[3] = FLAG_REQUEST;
	    }else if(req.isSend()){
	    	header[3] = FLAG_SEND; 
	    }
	    
	    Bytes.long2bytes(req.getId(), header, 4);
	
	    int savedWriteIndex = buffer.writerIndex();
	    buffer.writerIndex(savedWriteIndex + HEADER_LENGTH);
	    
	    ChannelBufferOutputStream bos = new ChannelBufferOutputStream(buffer);
	    ObjectOutput out = serialization.serialize(bos);
	    out.writeObject(req.getData());
	    out.flushBuffer();
	    bos.flush();
	    bos.close();
	    
	    int len = bos.writtenBytes();
	    Bytes.int2bytes(len, header, 12);
	    
	    buffer.writerIndex(savedWriteIndex);
	    buffer.writeBytes(header);
	    buffer.writerIndex(savedWriteIndex + HEADER_LENGTH + len);
	}
	
	protected void encodeResponse(Channel channel, ChannelBuffer buffer, Response res) throws IOException {
	    try {
	        Serialization serialization = getSerialization(channel);
	        byte[] header = new byte[HEADER_LENGTH];
	        Bytes.short2bytes(MAGIC, header);
	        header[2] = serialization.getId();
	        
	        byte status = res.getStatus();
	        
			if(res.isHeartbeat()){
				header[3] = (byte)(FLAG_HEARTBEAT|status);
			}else {
				header[3] = (byte)(FLAG_RESPONSE|status);
			}
			
//			header[4] = status;
			Bytes.long2bytes(res.getId(), header, 4);
			
			int savedWriteIndex = buffer.writerIndex();
			buffer.writerIndex(savedWriteIndex + HEADER_LENGTH);
			ChannelBufferOutputStream bos = new ChannelBufferOutputStream(buffer);
			ObjectOutput out = serialization.serialize(bos);
			if (status == Response.OK) {
			    out.writeObject(res.getResult());
			}else{ 
				out.writeUTF(res.getErrorMessage());
			}
			out.flushBuffer();
			bos.flush();
			bos.close();
	
			int len = bos.writtenBytes();
			Bytes.int2bytes(len, header, 12);
			buffer.writerIndex(savedWriteIndex);
			buffer.writeBytes(header);
			buffer.writerIndex(savedWriteIndex + HEADER_LENGTH + len);
	    } catch (Throwable t) {
	    	t.printStackTrace();
	        try {
	            Response r = new Response(res.getId(), res.getVersion());
	            r.setStatus(Response.BAD_RESPONSE);
	            r.setErrorMessage("Failed to send response: " + res + ", cause: " + t.getMessage());
	            channel.send(r);
	            return;
	        } catch (RemotingException e) {
	        	
	        }
	        throw t;
	    }
	}
	
	public Serialization getSerialization(Channel channel){
		Serialization serialization = ExtensionLoader.getExtensionLoader(Serialization.class).getExtension("hessian");
		return serialization;
	}
}
