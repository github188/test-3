package net.transfer.client;

import java.io.IOException;

public interface ObjectOutput extends DataOutput{
	
	public void writeObject(Object obj) throws IOException;
	
}
