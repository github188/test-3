package nari.parameter.convert;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class Request {

	@SuppressWarnings("unchecked")
	public static <T> T convert(String input ,Class<T> clazz){
		JSONObject obj = JSONObject.fromObject(input);
		Object bean = JSONObject.toBean(obj, clazz);
		if(bean==null){
			return null;
		}
		return (T)bean;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T convert(String input, Class<T> clazz, JsonConfig config){
		JSONObject obj = JSONObject.fromObject(input, config);
		Object bean = JSONObject.toBean(obj, clazz);
		if(bean==null){
			return null;
		}
		return (T)bean;
	}
}
