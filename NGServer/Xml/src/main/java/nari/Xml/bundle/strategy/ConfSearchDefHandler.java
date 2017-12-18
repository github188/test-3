package nari.Xml.bundle.strategy;

import java.util.Map;

import nari.Xml.bundle.ConfigHelper;

public class ConfSearchDefHandler extends ConfigSearchAbstractHandler {

	@Override
	public Object get(String confName) {
		Map<String,Object> conf = ConfigHelper.getConfigCache();
		Object obj = conf.get(confName);
		return obj;
	}
}
