package org.Invoker.remoting.codec;

import java.io.IOException;
import java.lang.reflect.Type;

public interface ObjectInput extends DataInput{

	public Object readObject() throws IOException, ClassNotFoundException;
	
	public <T> T readObject(Class<T> cls) throws IOException, ClassNotFoundException;
	
	public <T> T readObject(Class<T> cls, Type type) throws IOException, ClassNotFoundException;
	
}
