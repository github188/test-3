package com.application.plugin.bundle;

import com.application.plugin.framework.DevelopModel;


public class Bundles {

	public static Bundle createBundle(String name,Class<?> bundleClass,DevelopModel model){
		Bundle bundle = new StandardBundle(name,bundleClass,model);
		return bundle;
	}
}
