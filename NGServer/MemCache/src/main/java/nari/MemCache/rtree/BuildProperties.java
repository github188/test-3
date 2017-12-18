package nari.MemCache.rtree;

import java.io.IOException;
import java.util.Properties;

public class BuildProperties {
	
	private static final BuildProperties instance = new BuildProperties();

	private String version = null;
	private String scmRevisionId = null;

	private BuildProperties() {
		Properties p = new Properties();
		try {
			p.load(getClass().getClassLoader().getResourceAsStream("build.properties"));
			version = p.getProperty("version", "");
			scmRevisionId = p.getProperty("scmRevisionId", "");
		} catch (IOException e) {
			
		}
	}

	public static String getVersion() {
		return instance.version;
	}

	public static String getScmRevisionId() {
		return instance.scmRevisionId;
	}
}