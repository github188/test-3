package nari.Xml.bundle;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import nari.Xml.bundle.manager.ConfStrategyManagerHandler;
import nari.Xml.bundle.strategy.ConfStrategyManager;

public class ConfigHelper {
	
	/**
	 * 数据缓存对象
	 */
	private static final Map<String, Object> cache = new ConcurrentHashMap<String,Object>();
	
	/**
	 * 获取配置缓存对象
	 * @return
	 */
	public final static Map<String, Object> getConfigCache(){
		synchronized (cache) {
			return cache;
		}
	}
	
	/**
	 * 清空配置缓存
	 */
	public final static void cleanCache(){
		synchronized (cache) {
			cache.clear();
		}
	}
	
	/**
	 * 创建配置缓存策略管理对象
	 * @return
	 */
	public final static ConfStrategyManager createStrategyManager() {
		return new ConfStrategyManagerHandler();
	}
}
