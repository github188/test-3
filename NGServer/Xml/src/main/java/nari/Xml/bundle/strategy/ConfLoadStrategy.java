package nari.Xml.bundle.strategy;

import nari.Xml.bundle.module.ConfigModule;
import nari.Xml.bundle.module.reader.StrategyReader;

public interface ConfLoadStrategy {
	public <T> T loadConfig(ConfigModule module,StrategyReader reader);
}
