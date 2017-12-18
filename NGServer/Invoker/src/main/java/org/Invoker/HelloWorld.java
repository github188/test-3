package org.Invoker;

import java.util.Map;

public interface HelloWorld {

	public String say(String name);
	
	public Map<String,Object> maps(String name,Map<String,Object> value);
	
	public Object getObject(String name,Object value);
}
