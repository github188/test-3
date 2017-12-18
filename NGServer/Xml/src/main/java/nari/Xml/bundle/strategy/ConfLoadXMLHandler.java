package nari.Xml.bundle.strategy;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import nari.Xml.bundle.ConfigHelper;
import nari.Xml.bundle.module.ConfigModule;
import nari.Xml.bundle.module.reader.StrategyReader;

public class ConfLoadXMLHandler extends ConfigLoadAbstractHandler{

	@Override
	public Object getConfModel(ConfigModule module,StrategyReader reader) {
		try {
			return reader.readStrategy(module);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void put(String confName, Object model) {
		ConfigHelper.getConfigCache().put(confName, model);
	}
}
