package nari.Xml.bundle;

import nari.Xml.bundle.strategy.ConfSearchStrategy;
import nari.Xml.bundle.strategy.ConfStrategyManager;

public final class ConfigSearchManager {

	public ConfigSearchManager(){
		
	}
	
	public final static <T> T search(String confName){
		ConfSearchStrategy strategy = createConfSearchStrategy();
		T model = strategy.search(confName);
		return model;
	}
	
	/**
	 * 创建配置加载策略
	 * @return
	 */
	private static ConfSearchStrategy createConfSearchStrategy(){
		ConfStrategyManager manager = ConfigHelper.createStrategyManager();
		return manager.getConfSearchStrategy();
	}
}
