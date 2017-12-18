package org.Invoker.remoting.codec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.Invoker.rpc.extension.ExtensionLoader;

public class CodecSupport {

	private final static Map<Byte,Serialization> cache = new HashMap<Byte,Serialization>();
	
	static{
		List<String> extensions = ExtensionLoader.getExtensionLoader(Serialization.class).getSupportedExtensions();
		for(String name:extensions){
			Serialization ser = ExtensionLoader.getExtensionLoader(Serialization.class).getExtension(name);
			if(cache.containsKey(ser.getId())){
				continue;
			}
			cache.put(ser.getId(), ser);
		}
	}
	
	public static Serialization getSerialization(Byte id){
		return cache.get(id);
	}
}
