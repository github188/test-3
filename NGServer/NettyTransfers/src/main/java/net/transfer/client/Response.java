package net.transfer.client;

public class Response {
    
    public static final byte OK = 20;

    public static final byte CLIENT_TIMEOUT = 30;

    public static final byte SERVER_TIMEOUT = 31;

    public static final byte BAD_REQUEST = 40;

    public static final byte BAD_RESPONSE = 50;

    public static final byte SERVICE_NOT_FOUND = 60;

    public static final byte SERVICE_ERROR = 70;

    public static final byte SERVER_ERROR = 80;

    public static final byte CLIENT_ERROR = 90;

    private boolean heartbeat = false;
    
    private long mId = 0;

    private String mVersion;

    private byte mStatus = OK;

    private String errorMsg;

    private Object mResult;

    private byte servId;
    
    public Response(){
    	
    }

    public Response(long id){
        mId = id;
    }

    public Response(long id, String version){
        mId = id;
        mVersion = version;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getVersion() {
        return mVersion;
    }

    public void setVersion(String version) {
        mVersion = version;
    }

    public byte getStatus() {
        return mStatus;
    }

    public void setStatus(byte status) {
        mStatus = status;
    }
    
    public boolean isHeartbeat() {
    	return heartbeat;
    }

    public void setHeartbeat(boolean isHeartbeat) {
       this.heartbeat = isHeartbeat;
    }

    public Object getResult() {
        return mResult;
    }

    public void setResult(Object msg) {
        mResult = msg;
    }

    public String getErrorMessage() {
        return errorMsg;
    }

    public void setErrorMessage(String msg) {
    	errorMsg = msg;
    }

    public void setServId(byte servId){
		this.servId = servId;
	}
	
	public byte getServId(){
		return this.servId;
	}
	
    @Override
    public String toString() {
        return "Response [id=" + mId + ", version=" + mVersion + ", status=" + mStatus + " , error=" + errorMsg + ", result=" + (mResult == this ? "this" : mResult) + "]";
    }
}