package nari.Xml.bundle.strategy;

public abstract class ConfigSearchAbstractHandler implements ConfSearchStrategy {

	@SuppressWarnings("unchecked")
	public <T> T search(String confName) {
		return (T)get(confName);
	}
	
	public abstract Object get(String confName);
}
