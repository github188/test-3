package net.transfer.client;

import java.util.concurrent.atomic.AtomicLong;

public class Request {
    
    private static final AtomicLong INVOKE_ID = new AtomicLong(1);
    
    protected static final byte SERVERTYPE_TOPO = (byte)0x11;
	
	protected static final byte SERVERTYPE_QUERY = (byte)0x15;
	
    private final long id;

    private String  version;

    private Object  mData;
    
    private byte target;
    
    private byte servId;

    public Request() {
    	id = newId();
    }

    public Request(long id){
    	this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
    	this.version = version;
    }

    public Object getData() {
        return mData;
    }

    public void setData(Object msg) {
        mData = msg;
    }
    
    private static long newId() {
        return INVOKE_ID.getAndIncrement();
    }

    public byte getTarget() {
		return target;
	}

	public void setTarget(byte target) {
		this.target = target;
	}
	
	public void setServId(byte servId){
		this.servId = servId;
	}
	
	public byte getServId(){
		return this.servId;
	}

	@Override
    public String toString() {
        return "Request [id=" + id + ", version=" + version + ", isRequest=true, data=" + (mData == this ? "this" : mData==null?"":mData.toString()) + "]";
    }
}
