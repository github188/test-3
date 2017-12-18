package nari.Xml.bundle.module;

import java.io.InputStream;

public class ConfigStrategyModule implements ConfigModule {

	private Strategy strategy;
	
	private Class<?> clazz;
	
	private InputStream stream;
	
	private String configName;
	
	public ConfigStrategyModule(String configName,InputStream stream ,Strategy strategy,Class<?> clazz){
		this.configName = configName;
		this.strategy = strategy;
		this.clazz = clazz;
		this.stream = stream;
	}
	
	public Strategy getConfigStrategy() {
		return strategy;
	}

	public Class<?> getModuleClass() {
		return clazz;
	}

	public InputStream getStream() {
		return stream;
	}

	public String getConfigName() {
		return configName;
	}

}
