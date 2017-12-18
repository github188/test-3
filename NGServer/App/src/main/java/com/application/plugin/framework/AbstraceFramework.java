package com.application.plugin.framework;

import com.application.plugin.RegistorProvider;
import com.application.plugin.State;
import com.application.plugin.bundle.BundleException;
import com.application.plugin.service.ServiceRegistry;

public abstract class AbstraceFramework implements Framework {

	private State state = State.NONE;
	
	private FrameworkListenerSupport support = new FrameworkListenerSupport(this);
	
	private DevelopModel model;
	
	private final FrameworkListener defaultListener = new FrameworkListener() {
		
		@Override
		public void onStop(Framework framework) throws BundleException {
			state = State.STOPED;
		}
		
		@Override
		public void onStart(Framework framework) throws BundleException {
			state = State.STARTED;
		}
		
		@Override
		public void onInit(Framework framework) throws BundleException {
			state = State.INITIALIZED;
		}
		
		@Override
		public void onExceptionCaught(Framework framework, Throwable t) throws BundleException {
			state = State.FAILED;
//			throw new BundleException(t.getMessage());
			t.printStackTrace();
		}
	};
	
	public void init(DevelopModel model) throws BundleException {
		if(model==null){
			model = DevelopModel.TOOLKIT;
		}
		this.model = model;
		
		support.addFrameworkListener(defaultListener);
		ServiceRegistry serv = RegistorProvider.get();
		try {
			support.addFrameworkListener((FrameworkListener)serv);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(state==State.STARTED){
			support.fireFrameWorkExceptionCaught(new BundleException("HAS STARTED"));
		}
		
		try {
			doInit();
		} catch (BundleException e) {
			support.fireFrameWorkExceptionCaught(e.getCause());
		}
		support.fireFrameWorkInit();
	}
	
	public void start() throws BundleException {
		if(state!=State.INITIALIZED){
			support.fireFrameWorkExceptionCaught(new BundleException("NOT INITED"));
		}
		
		try {
			doStart();
		} catch (BundleException e) {
			support.fireFrameWorkExceptionCaught(e.getCause());
		}
		support.fireFrameWorkStart();
	}

	public void stop() throws BundleException {
		if(state!=State.STARTED){
			support.fireFrameWorkExceptionCaught(new BundleException("NOT STARTED"));
		}
		
		try {
			doStop();
		} catch (BundleException e) {
			support.fireFrameWorkExceptionCaught(e.getCause());
		}
		support.fireFrameWorkStop();
	}
	
	public State getState(){
		return state;
	}
	
	protected DevelopModel getModel(){
		return this.model;
	}
	
	protected abstract void doInit() throws BundleException;
	
	protected abstract void doStart() throws BundleException;
	
	protected abstract void doStop() throws BundleException;
}
