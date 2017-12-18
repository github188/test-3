package nari.Xml.interfaces;

import java.io.InputStream;

public interface ConfigSearch {
	
	public static final ConfigSearch NONE = new ConfigSearch(){

		@Override
		public <T> T search(String configName) {
			return null;
		}

		@Override
		public <T> T loadConfigCache(String configName, InputStream inputStream, String postFix, Class<T> clazz) {
			return null;
		}

		@Override
		public <T> T loadConfigCache(String configName, String configFullPath, Class<T> clazz) {
			return null;
		}
		
	};
	
	public <T> T search(String configName);
	
	public <T> T loadConfigCache(String configName,InputStream inputStream,String postFix,Class<T> clazz);
	
	public <T> T loadConfigCache(String configName,String configFullPath,Class<T> clazz);
}
