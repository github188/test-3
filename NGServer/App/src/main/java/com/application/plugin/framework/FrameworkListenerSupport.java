package com.application.plugin.framework;

import java.util.ArrayList;
import java.util.List;

import com.application.plugin.bundle.BundleException;

public class FrameworkListenerSupport {

	private List<FrameworkListener> listeners = null;
	
	private Framework framework = null;
	
	public FrameworkListenerSupport(Framework framework) {
		this.framework = framework;
		listeners = new ArrayList<FrameworkListener>();
	}

	public void addFrameworkListener(FrameworkListener listener){
		listeners.add(listener);
	}
	
	public void fireFrameWorkInit(){
		if(listeners==null || listeners.size() ==0){
			return ;
		}
		for(FrameworkListener listener:listeners){
			try {
				listener.onInit(framework);
			} catch (BundleException e) {
				fireFrameWorkExceptionCaught(e.getCause());
			}
		}
	}
	
	public void fireFrameWorkStart(){
		if(listeners==null || listeners.size() ==0){
			return ;
		}
		for(FrameworkListener listener:listeners){
			try {
				listener.onStart(framework);
			} catch (BundleException e) {
				fireFrameWorkExceptionCaught(e.getCause());
			}
		}
	}
	
	public void fireFrameWorkStop(){
		if(listeners==null || listeners.size() ==0){
			return ;
		}
		for(FrameworkListener listener:listeners){
			try {
				listener.onStop(framework);
			} catch (BundleException e) {
				fireFrameWorkExceptionCaught(e.getCause());
			}
		}
	}
	
	public void fireFrameWorkExceptionCaught(Throwable t){
		if(listeners==null || listeners.size() ==0){
			return ;
		}
		for(FrameworkListener listener:listeners){
			try {
				listener.onExceptionCaught(framework, t);
			} catch (BundleException e) {
				fireFrameWorkExceptionCaught(e.getCause());
			}
		}
	}
	
}
