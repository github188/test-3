package nari.Xml.bundle;

import nari.Xml.bundle.module.ConfigModule;
import nari.Xml.bundle.module.Strategy;
import nari.Xml.bundle.module.reader.StrategyReader;
import nari.Xml.bundle.strategy.ConfLoadStrategy;
import nari.Xml.bundle.strategy.ConfStrategyManager;

public final class ConfigLoadManager {
	
	/**
	 * 初始化配置缓存
	 */
	public static <T> T load(ConfigModule module,StrategyReader reader){
		ConfLoadStrategy strategy = createConfLoadStrategy(module.getConfigStrategy());
		return strategy.loadConfig(module,reader);
	}
	
	/**
	 * 重新加载配置缓存
	 */
	public static void reLoad(){
//		ConfigHelper.cleanCache();
//		load();
	}
	
	/**
	 * 创建配置加载策略
	 * @return
	 */
	private static ConfLoadStrategy createConfLoadStrategy(Strategy strategy){
		ConfStrategyManager manager = ConfigHelper.createStrategyManager();
		return manager.getConfLoadStrategy(strategy);
	}
}
