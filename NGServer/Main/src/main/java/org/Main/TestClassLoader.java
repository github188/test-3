package org.Main;

import java.net.URL;
import java.net.URLClassLoader;

public class TestClassLoader extends URLClassLoader {

	public TestClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

}
