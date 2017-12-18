package com.application.plugin.bundle.context;

import java.net.URL;

public class ClassLoaderResourceLoader extends AbstractResourceLoader {

	@Override
	protected URL getSystemResource(String name) {
		return getClass().getClassLoader().getResource(name);
	}
	
}
