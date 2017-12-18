package org.Invoker.remoting.exchanger;

import java.util.concurrent.atomic.AtomicLong;

public class Request {
    
    private static final AtomicLong INVOKE_ID = new AtomicLong(1);

    private final long id;

    private String  version;

    private boolean request = true;
    
    private boolean send = false;

    private boolean heartbeat = false;
    
    private boolean subscribe = false;

    private Object  mData;

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
    
    public boolean isRequest(){
    	return request;
    }
    
    public void setRequest(boolean isRequest){
    	this.request = isRequest;
    }

    public boolean isSend(){
    	return send;
    }
    
    public void setSend(boolean isSend){
    	this.send = isSend;
    }
    
    public boolean isHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(boolean isHeartbeat) {
        this.heartbeat = isHeartbeat;
    }

    private static long newId() {
        return INVOKE_ID.getAndIncrement();
    }

    @Override
    public String toString() {
        return "Request [id=" + id + ", version=" + version + ", isRequest=" + request + ", isSend=" + send + ", isHeartbeat=" + heartbeat + ", isSubscribe="+subscribe+", data=" + (mData == this ? "this" : mData==null?"":mData.toString()) + "]";
    }
}
