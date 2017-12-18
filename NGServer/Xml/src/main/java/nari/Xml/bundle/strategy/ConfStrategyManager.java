package nari.Xml.bundle.strategy;

import nari.Xml.bundle.module.Strategy;

public interface ConfStrategyManager {
	
	/**
	 * 获取缓存加载策略
	 */
	public ConfLoadStrategy getConfLoadStrategy(Strategy strategy);
	
	/**
	 * 获取缓存查询策略
	 */
	public ConfSearchStrategy getConfSearchStrategy();
}
