package com.application.plugin.bundle.context;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class FileResourceLoader extends AbstractResourceLoader {

	@Override
	protected URL getSystemResource(String name) {
		String root = System.getProperty("user.dir");
		File file = new File(root);
		file = new File(file.getParentFile(),name);
		
		if(!file.exists()){
			return null;
		}
		try {
			return file.toURI().toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
