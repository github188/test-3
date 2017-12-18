package nari.MemCache;

public interface Message {

	public MessageType getMessageType() throws Exception;
	
	public Object getMessageBody() throws Exception;
}
