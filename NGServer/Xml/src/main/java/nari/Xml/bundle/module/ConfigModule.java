package nari.Xml.bundle.module;

import java.io.InputStream;

public interface ConfigModule {
	
	public Strategy getConfigStrategy();
	
	public Class<?> getModuleClass();
	
	public InputStream getStream();
	
	public String getConfigName();
}
