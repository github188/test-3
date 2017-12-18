package net.transfer.client;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ExchangerCodec extends AbstractCodec implements Codec{

	protected static final int HEADER_LENGTH = 12;
	
	protected static final short MAGIC = (short) 0xdabb;
	
	protected static final byte MAGIC_HIGH = Bytes.short2bytes(MAGIC)[0];
	
	protected static final byte MAGIC_LOW = Bytes.short2bytes(MAGIC)[1];
	
	protected static final byte FLAG_REQUEST = (byte) 1;
	
	protected static final byte FLAG_RESPONSE = (byte) 2;
	
	public Short getMagicCode() {
	    return MAGIC;
	}
	
	public ByteBuffer encode(Channel channel, Object msg) throws IOException {
	    if (msg instanceof Request) {
	        return encodeRequest(channel, (Request) msg);
	    } else if (msg instanceof Response) {
	    	return encodeResponse(channel, (Response) msg);
	    }
	    return null;
	}
	
	public Object decode(Channel channel, ByteBuffer buffer) throws IOException {
		 int readable = buffer.limit() - buffer.position();
		 if (readable < HEADER_LENGTH) {
		 	return DecodeResult.MORE_MESSAGE;
		 }
		 byte[] header = new byte[ HEADER_LENGTH];
		 buffer.get(header);
		 return decode(channel, buffer, readable, header);
	}
	
	protected Object decode(Channel channel, ByteBuffer buffer, int readable, byte[] header) throws IOException {
	    if (readable < HEADER_LENGTH) {
	        return DecodeResult.MORE_MESSAGE;
	    }
	    
	    int len = Bytes.bytes2short(header, 0);
	    
	    int tt = len + HEADER_LENGTH;
	    if( readable < tt ) {
	        return DecodeResult.MORE_MESSAGE;
	    }
	
//	    ChannelBufferInputStream is = new ChannelBufferInputStream(buffer, 0);
	    
	    return decodeBody(channel, buffer, header);
	}
	
	protected Object decodeBody(Channel channel, ByteBuffer buffer, byte[] header) throws IOException {
//		Serialization s = new HessianSerialization();
		
		int len = Bytes.bytes2short(header, 0);
		
		byte servId = header[2];
		
		byte flag = header[3];
		
		long id = Bytes.bytes2long(header, 4);
        
//		if(flag==FLAG_REQUEST){
//			Request req = new Request(id);
//			req.setServId(servId);
//            req.setVersion("2.0.0");
//            try {
//                DecodeableRpcInvocation inv = new DecodeableRpcInvocation(req, is, s);
//                inv.decode();
//            } catch (Throwable t) {
//            	t.printStackTrace();
//                req.setData(t);
//            }
//            return req;
//		}else 
		if(flag==FLAG_RESPONSE){
			Response res = new Response(id);
            res.setStatus(Response.OK);
            res.setServId(servId);
            byte[] dst = new byte[len];
            buffer.position(HEADER_LENGTH);
            buffer.get(dst, 0, len);
            res.setResult(new String(dst));
            
//            try {
//                DecodeableRpcResult result = new DecodeableRpcResult(res, is, s);
//                result.decode();
//            } catch (Throwable t) {
//                res.setStatus(Response.CLIENT_ERROR);
//                res.setErrorMessage(t.getMessage());
//            }
             
            return res;
		}
		
        return null;
	}
	
	protected Object getRequestData(long id) {
	    InvokerFuture future = InvokerFuture.getFuture(id);
	    if (future == null){
	        return null;
	    }
	    Request req = future.getRequest();
	    if (req == null){
	        return null;
	    }
	    return req.getData();
	}
	
	protected ByteBuffer encodeRequest(Channel channel, Request req) throws IOException {
//		Serialization s = new HessianSerialization();

		//header[�̶�ͷ��2byte�� + ��������ͣ����������Ӧ��1�ֽڣ� + ÿ�������ID��ʶ��8byte�� + �����Ŀ������ʶ��1byte�� + ������ĳ��ȣ�4byte��] + body[]
		//header[ÿ�������ID��ʶ��8byte)+������ĳ��ȣ�2byte��+ �����Ŀ������ʶ��1byte��] + body[]
//		Serialization s = new HessianSerialization();
	    byte[] header = new byte[HEADER_LENGTH];
//	    Bytes.short2bytes(MAGIC, header);
//	    header[2] = FLAG_REQUEST;

		//header[固定通信id(8byte)+消息长度(2byte)+ 方法标记(1byte)] + body[]
		
	    


//	    byte[] arr = new byte[256];

	    Bytes.long2bytes(req.getId(), header, 3);

//	    ChannelBufferOutputStream bos = new ChannelBufferOutputStream(arr,0);
//	    ObjectOutput out = s.serialize(bos);
//	    out.writeObject(req.getData());
//	    out.flushBuffer();
//	    bos.flush();
//	    bos.close();
	    
	    String str = (String)req.getData();
	    byte[] arr = str.getBytes();
//	    int len = bos.writtenBytes();
	    int len =  arr.length;
	    ByteBuffer buffer = ByteBuffer.allocate(len+HEADER_LENGTH);
	    
	    buffer.position(HEADER_LENGTH);
	    

	    buffer.put(arr, 0, len);

	   

	    
//	    byte[] header = new byte[HEADER_LENGTH];
	    
	    Bytes.long2bytes(req.getId(), header, 4);
	    
	    Bytes.short2bytes((short)len, header, 0);
	    
	    header[2] = req.getServId();
	    header[3] = FLAG_REQUEST;
	    
	    buffer.position(0);
	    buffer.put(header);
	    
//    	buffer.flip();
    	
    	return buffer;
	}
	
	protected ByteBuffer encodeResponse(Channel channel, Response res) throws IOException {
		ByteBuffer buffer = null;
	    try {
	    	Serialization s = new HessianSerialization();
		    
	    	byte[] arr = new byte[256];
	    	
		    ChannelBufferOutputStream bos = new ChannelBufferOutputStream(arr,HEADER_LENGTH);
		    ObjectOutput out = s.serialize(bos);
		    out.writeObject(res.getResult());
		    out.flushBuffer();
		    bos.flush();
		    bos.close();
		    
		    buffer = ByteBuffer.allocate(arr.length+HEADER_LENGTH);
		    buffer.position(HEADER_LENGTH);
		    buffer.put(arr);
		    
		    byte[] header = new byte[HEADER_LENGTH];
		    header[3] = FLAG_RESPONSE;
		    
		    Bytes.long2bytes(res.getId(), header, 3);
		    
		    int len = bos.writtenBytes();
		    Bytes.short2bytes((short)len, header, 0);
		    
		    header[2] = res.getServId() ;
		    
		    buffer.position(0);
		    buffer.put(header);
		    
//	    	buffer.flip();
	    } catch (IOException t) {
	    	t.printStackTrace();
	        try {
	            Response r = new Response(res.getId(), res.getVersion());
	            r.setStatus(Response.BAD_RESPONSE);
	            r.setErrorMessage("Failed to send response: " + res + ", cause: " + t.getMessage());
	            channel.send(r);
	            return null;
	        } catch (RemotingException e) {
	        	e.printStackTrace();
	        }
	        throw t;
	    }
	    return buffer;
	}
	
	public static void main(String[] args) {
		
	}
}
