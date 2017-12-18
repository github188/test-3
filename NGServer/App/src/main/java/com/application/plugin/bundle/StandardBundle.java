package com.application.plugin.bundle;

import com.application.plugin.Activator;
import com.application.plugin.BundleContext;
import com.application.plugin.framework.DevelopModel;

public class StandardBundle extends AbstractBundle {

	public StandardBundle(String name,Class<?> bundleClass,DevelopModel model){
		super(name,bundleClass,model);
	}

	@Override
	protected boolean doInit(Activator activator,BundleConfig config) throws BundleException {
		return activator.init(config);
	}

	@Override
	protected boolean doStart(Activator activator,BundleContext context) throws BundleException {
		return activator.start(context);
	}

	@Override
	protected boolean doStop(Activator activator,BundleContext context) throws BundleException {
		return activator.stop(context);
	}

	public String getId() {
		return getBundleId();
	}
}
