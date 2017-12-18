package com.application.plugin.bundle.context;

import java.io.InputStream;
import java.net.URL;

public interface ResourceLoader {

	public InputStream getResourceAsStream(String name);
	
	public URL getResource(String name);
	
}
