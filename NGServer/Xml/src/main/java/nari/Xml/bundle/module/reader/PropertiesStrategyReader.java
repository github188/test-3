package nari.Xml.bundle.module.reader;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

import nari.Xml.bundle.annotation.XmlAttribute;
import nari.Xml.bundle.module.ConfigModule;

public class PropertiesStrategyReader extends DefaultStrategyReader implements StrategyReader {

	@SuppressWarnings("unchecked")
	public <T> T readStrategy(ConfigModule module) throws IOException,InstantiationException,IllegalAccessException,IntrospectionException,IllegalArgumentException,InvocationTargetException{
		Class<?> clazz = module.getModuleClass();
		Properties properties = new Properties();
		properties.load(module.getStream());
		
		T instance = (T)clazz.newInstance();
		Field[] fields = clazz.getDeclaredFields();
		XmlAttribute attribute = null;
		String value = "";
		PropertyDescriptor pd = null;
		Method method = null;
		
		for(Field field:fields){
			attribute = field.getAnnotation(XmlAttribute.class);
			if(attribute!=null){
				value = properties.getProperty(attribute.name());
				pd = new PropertyDescriptor(field.getName(),clazz);
				method = pd.getWriteMethod();
				method.invoke(instance, value);
			}
		}
		
		return instance;
	}
}
