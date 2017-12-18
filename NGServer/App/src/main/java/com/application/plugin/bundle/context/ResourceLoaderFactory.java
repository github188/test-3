package com.application.plugin.bundle.context;

import com.application.plugin.framework.DevelopModel;

public class ResourceLoaderFactory {

	private ResourceLoader loader = null;
	
	public ResourceLoader createResourceLoader(DevelopModel model){
		if(model==DevelopModel.DEVELOP){
			loader = new FileResourceLoader();
		}else if(model==DevelopModel.TOOLKIT){
			loader = new ClassLoaderResourceLoader();
		}
		return loader;
	}
}
