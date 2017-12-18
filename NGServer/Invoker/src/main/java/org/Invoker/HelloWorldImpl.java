package org.Invoker;

import java.util.HashMap;
import java.util.Map;

public class HelloWorldImpl implements HelloWorld {

	@Override
	public String say(String name) {
		System.out.println("hello "+name);
		return "---------------";
	}

	@Override
	public Map<String, Object> maps(String name, Map<String,Object> value) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("key1", value.values());
		map.put("key2", value.values());
		map.put("key3", value.values());
		map.put("key4", value.values());
		map.put("key5", value.values());
		map.put("key6", value.values());
		return map;
	}

	@Override
	public Object getObject(String name, Object value) {
		return value;
	}

}
