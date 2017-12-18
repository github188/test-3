package nari.Xml.bundle.module.reader;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import nari.Xml.bundle.module.ConfigModule;

public interface StrategyReader {
	
	public <T> T readStrategy(ConfigModule module) throws IOException,InstantiationException,IllegalAccessException,IntrospectionException,IllegalArgumentException,InvocationTargetException;
}
