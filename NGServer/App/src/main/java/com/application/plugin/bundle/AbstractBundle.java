package com.application.plugin.bundle;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import com.application.plugin.Activator;
import com.application.plugin.ActivatorGenerator;
import com.application.plugin.AttributeKey;
import com.application.plugin.BundleContext;
import com.application.plugin.Provider;
import com.application.plugin.Version;
import com.application.plugin.bundle.context.StandardBundleContext;
import com.application.plugin.framework.DevelopModel;
import com.application.plugin.service.DefaultServiceTracker;
import com.application.plugin.service.ServiceFilter;
import com.application.plugin.service.ServiceTracker;
import com.application.plugin.tracker.TrackerBundleContext;

public abstract class AbstractBundle implements Bundle{

	private String name = null;
	
	private Class<?> bundleClass = null;
	
	private String bundleId = null;
	
	private Activator activator = null;
	
	private BundleContext context = null;
	
	private DevelopModel model = null;
	
	private final AtomicBoolean initialized = new AtomicBoolean(false);
	
	private FilterChain filterChain = new DefaultFilterChain();
	
	private static Provider<ServiceTracker> provider = new Provider<ServiceTracker>() {
		
		private ServiceTracker tracker = null;
		
		public ServiceTracker get() throws BundleException {
			if(tracker==null){
				synchronized (ServiceTracker.class) {
					if(tracker==null){
						tracker = new DefaultServiceTracker();
						tracker.open();
					}
				}
			}
			return tracker;
		}

		@Override
		public AttributeKey getKey() throws BundleException {
			AttributeKey key = AttributeKey.key(ServiceTracker.class, version());
			return key;
		}

		@Override
		public Version version() throws BundleException {
			return Version.version("1.0");
		}

		@Override
		public ServiceFilter[] getFilter() throws BundleException {
			return null;
		}
	};
	
	public AbstractBundle(String name,Class<?> bundleClass,DevelopModel model) {
		this.name = name;
		this.bundleClass = bundleClass;
		this.model = model;
	}
	
	public boolean init() throws BundleException {
		if(initialized.getAndSet(true)){
			return true;
		}
		
		if(bundleId==null){
			bundleId = UUID.randomUUID().toString();
		}
		createActivator();
		
		BundleConfig config = new StandardBundleConfig(this, provider);
		context = wrapperContext(config);
		boolean suc = doInit(activator,config);
		if(suc){
			config.getFilterChian().buildFilterChain(filterChain);
			filterChain.fireBundleInit(context);
		}
		return suc;
	}

	private void createActivator() throws BundleException {
		try {
			activator = ActivatorGenerator.newInstance(bundleClass);
		} catch (Exception e) {
			filterChain.fireExceptionCaught(new BundleException("caught exception on create activator"));
		}
		
		if(activator==null){
			filterChain.fireExceptionCaught(new BundleException("null activator"));
		}
	}
	
	private BundleContext wrapperContext(BundleConfig config) throws BundleException {
		context = new TrackerBundleContext<ServiceTracker>(provider,new StandardBundleContext(this,config,model));
		context.registService(ServiceTracker.class, provider);
		return context;
	}
	
	public boolean initialized() {
		return initialized.get();
	}

	public boolean start() throws BundleException {
		boolean suc = false;
		try {
			suc = doStart(activator,context);
		} catch (BundleException e) {
			suc = false;
			filterChain.fireExceptionCaught(e);
		}
		
		if(suc){
			filterChain.fireBundleStart(context);
		}
		return suc;
	}

	public boolean stop() throws BundleException {
		boolean suc = false;
		try {
			suc = doStop(activator,context);
		} catch (BundleException e) {
			suc = false;
			filterChain.fireExceptionCaught(e);
		}
		
		if(suc){
			filterChain.fireBundleStop(context);
		}
		return suc;
	}

	protected String getBundleId(){
		return bundleId;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	protected abstract boolean doInit(Activator activator,BundleConfig config) throws BundleException;
	
	protected abstract boolean doStart(Activator activator,BundleContext context) throws BundleException;
	
	protected abstract boolean doStop(Activator activator,BundleContext context) throws BundleException;
}
