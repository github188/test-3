package com.application.plugin.bundle.context;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public abstract class AbstractResourceLoader implements ResourceLoader {

	@Override
	public InputStream getResourceAsStream(String name) {
		URL url = getSystemResource(name);
		if(url==null){
			return null;
		}
		try {
			return url.openStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public URL getResource(String name) {
		return getSystemResource(name);
	}

	protected abstract URL getSystemResource(String name);
}
