package nari.Xml.bundle.manager;

import nari.Xml.bundle.module.Strategy;
import nari.Xml.bundle.strategy.ConfLoadPropertyHandler;
import nari.Xml.bundle.strategy.ConfLoadStrategy;
import nari.Xml.bundle.strategy.ConfLoadXMLHandler;
import nari.Xml.bundle.strategy.ConfSearchDefHandler;
import nari.Xml.bundle.strategy.ConfSearchStrategy;
import nari.Xml.bundle.strategy.ConfStrategyManager;

public class ConfStrategyManagerHandler implements ConfStrategyManager {

	public ConfLoadStrategy getConfLoadStrategy(Strategy strategy) {
		ConfLoadStrategy loader = null;
		switch (strategy) {
			case XML:
				loader = new ConfLoadXMLHandler();
				break;
			case PROPERTIES:
				loader = new ConfLoadPropertyHandler();
				break;
			default:
				break;
		}
		return loader;
	}

	public ConfSearchStrategy getConfSearchStrategy() {
		return new ConfSearchDefHandler();
	}
}
