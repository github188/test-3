package nari.Xml.bundle.module.reader;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import nari.Xml.bundle.annotation.NodeType;
import nari.Xml.bundle.annotation.XmlAttribute;
import nari.Xml.bundle.annotation.XmlNode;
import nari.Xml.bundle.module.ConfigModule;

public class XMLStrategyReader extends DefaultStrategyReader implements StrategyReader {

	@SuppressWarnings("unchecked")
	public <T> T readStrategy(ConfigModule module) throws IOException,InstantiationException,IllegalAccessException,IntrospectionException,IllegalArgumentException,InvocationTargetException{
		Class<?> clazz = module.getModuleClass();
		String xml = read(module.getStream());
		
		T instance = (T)clazz.newInstance();
		
		Field[] fields = clazz.getDeclaredFields();
		XmlAttribute attribute = null;
		XmlNode node = null;
		Object value = "";
		PropertyDescriptor pd = null;
		Method method = null;
		
		Map<String,Object> map = getDom(xml);
		
		for(Field field:fields){
			attribute = field.getAnnotation(XmlAttribute.class);
			if(attribute!=null){
				value = getTextValue(map.get(attribute.name()));
				pd = new PropertyDescriptor(field.getName(),clazz);
				method = pd.getWriteMethod();
				if(field.getType()==boolean.class){
					value = Boolean.valueOf(String.valueOf(value));
				}
				method.invoke(instance, value);
				continue;
			}
			
			node = field.getAnnotation(XmlNode.class);
			if(node!=null){
				value = getValue(map.get(node.name()),node.clazz(),node.type(),field.getName());
				pd = new PropertyDescriptor(field.getName(),clazz);
				
				method = pd.getWriteMethod();
				method.invoke(instance, value);
			}
		}
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	private Object getValue(Object obj,Class<?> clazz,NodeType type,String filedName) throws IOException,InstantiationException,IllegalAccessException,IntrospectionException,IllegalArgumentException,InvocationTargetException{
		if(obj==null){
			return null;
		}
		
		Object instance = clazz.newInstance();
		
		if(type==NodeType.List){
			List<Object> list = new ArrayList<Object>();
			
			if(obj instanceof List){
				List<Object> objlist = (List<Object>)obj;
				for(Object o:objlist){
					list.add(getValue(o,clazz,NodeType.Normal,filedName));
				}
				return list;
			}else{
				list.add(getValue(obj,clazz,NodeType.Normal,filedName));
				return list;
			}
		}else if(type==NodeType.Array){
			Object[] array = null;
			if(obj instanceof List){
				List<Object> objlist = (List<Object>)obj;
				array = new Object[objlist.size()];
				int i = 0;
				for(Object o:objlist){
					array[i++] = getValue(o,clazz,NodeType.Normal,filedName);
				}
				return array;
			}else{
				array = new Object[1];
				array[0] = getValue(obj,clazz,NodeType.Normal,filedName);
				return array;
			}
		}else if(type==NodeType.Normal){
			Element ele = (Element)obj;
			Map<String,Object> map = getSubDom(ele);
			if(instance instanceof String){
				return getTextValue(ele);
			}
			Field[] fields = clazz.getDeclaredFields();
			
			XmlAttribute attribute = null;
			XmlNode node = null;
			Object value = "";
			PropertyDescriptor pd = null;
			Method method = null;
			
			for(Field field:fields){
				attribute = field.getAnnotation(XmlAttribute.class);
				if(attribute!=null){
					value = getTextValue(map.get(attribute.name()));
					pd = new PropertyDescriptor(field.getName(),clazz);
					method = pd.getWriteMethod();
					if(field.getType()==boolean.class){
						value = Boolean.valueOf(String.valueOf(value));
					}else if(field.getType()==int.class){
						value = Integer.parseInt(String.valueOf(value));
					}else if(field.getType()==float.class){
						value = Float.parseFloat(String.valueOf(value));
					}else if(field.getType()==long.class){
						value = Long.parseLong(String.valueOf(value));
					}else if(field.getType()==double.class){
						value = Double.parseDouble(String.valueOf(value));
					}else if(field.getType()==short.class){
						value = Short.parseShort(String.valueOf(value));
					}
					method.invoke(instance, value);
					continue;
				}
				
				node = field.getAnnotation(XmlNode.class);
				if(node!=null){
					getValue(map.get(node.name()),node.clazz(),node.type(),field.getName());
				}
			}
		}
		
		return instance;
	}

	@SuppressWarnings("unchecked")
	public Map<String,Object> getSubDom(Element root){
		List<Element> eles = root.elements();
		List<Element> list = null;
		Map<String,Object> cache = new HashMap<String,Object>();
		for(Element ele:eles){
			if(!cache.containsKey(ele.getName())){
				cache.put(ele.getName(), ele);
			}else{
				list = new ArrayList<Element>();
				if(cache.get(ele.getName()) instanceof Element){
					list.add((Element)cache.get(ele.getName()));
					list.add(ele);
					cache.put(ele.getName(), list);
				}else{
					list = (List<Element>)cache.get(ele.getName());
					list.add(ele);
					cache.put(ele.getName(), list);
				}
			}
		}
		return cache;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String,Object> getDom(String xml){
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(xml);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		Map<String,Object> cache = new HashMap<String,Object>();
		Element root = doc.getRootElement();
		List<Element> eles = root.elements();
		List<Element> list = null;
		
		for(Element ele:eles){
			if(!cache.containsKey(ele.getName())){
				cache.put(ele.getName(), ele);
			}else{
				list = new ArrayList<Element>();
				if(cache.get(ele.getName()) instanceof Element){
					list.add((Element)cache.get(ele.getName()));
					list.add(ele);
					cache.put(ele.getName(), list);
				}else{
					list = (List<Element>)cache.get(ele.getName());
					list.add(ele);
					cache.put(ele.getName(), list);
				}
			}
		}
		return cache;
	}
	
	private String getTextValue(Object obj){
		if(obj==null){
			return "";
		}
		Element ele = (Element)obj;
		return ele.getTextTrim();
	}
	
	private String read(InputStream stream){
		BufferedReader buf = new BufferedReader(new InputStreamReader(stream));
		
		String line = "";
		StringBuffer buffer = new StringBuffer();
		try {
			while((line=buf.readLine())!=null){
				buffer.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}
}
