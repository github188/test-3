package com.application.plugin.bundle;

import java.util.ArrayList;
import java.util.List;

import com.application.plugin.BundleContext;

public class BundleListenerSupport {

	private List<BundleListener> listeners = new ArrayList<BundleListener>();
	
	public void addListener(BundleListener listener){
		listeners.add(listener);
	}
	
	public void fireOperationComplate(BundleContext context){
		for(BundleListener listener:listeners){
			try {
				listener.onCreationComplate(context);
			} catch (BundleException e) {
				e.printStackTrace();
			}
		}
	}
}
