package nari.Xml.bundle.strategy;

import nari.Xml.bundle.module.ConfigModule;
import nari.Xml.bundle.module.reader.StrategyReader;

public abstract class ConfigLoadAbstractHandler implements ConfLoadStrategy{
	
	public ConfigLoadAbstractHandler(){
		
	}
	
	@SuppressWarnings("unchecked")
	public <T> T loadConfig(ConfigModule module,StrategyReader reader) {
		String confName = module.getConfigName();
		T model = (T)getConfModel(module,reader);
		put(confName,model);
		return model;
	}
	
	public abstract Object getConfModel(ConfigModule module,StrategyReader reader);
	
	public abstract void put(String confName,Object model);
}
