package com.application.plugin.tracker;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.application.plugin.AttributeKey;
import com.application.plugin.BundleContext;
import com.application.plugin.bundle.BundleException;
import com.application.plugin.service.ServiceReference;

public class TrackerSupport {

//	private final ConcurrentMap<Class<?> ,List<TrackerListener>> listeners = new ConcurrentHashMap<Class<?> ,List<TrackerListener>>();
	
	private final ConcurrentMap<AttributeKey ,Tracker> listeners = new ConcurrentHashMap<AttributeKey ,Tracker>();
	
	public <T> void serviceAdd(ServiceReference<T> ref,BundleContext context) throws BundleException{
		if(validate()){
			Tracker tracker = listeners.get(ref.getKey());
			if(tracker==null){
				return;
			}
			for(TrackerListener tl:tracker.getTrackerListeners()){
				tl.serviceAdd(ref,context);
			}
		}
	}
	
	public <T> void serviceModify(ServiceReference<T> ref,BundleContext context) throws BundleException{
		if(validate()){
			Tracker tracker = listeners.get(ref.getKey());
			if(tracker==null){
				return;
			}
			for(TrackerListener tl:tracker.getTrackerListeners()){
				tl.serviceModify(ref,context);
			}
		}
	}
	
	public <T> void serviceRemove(ServiceReference<T> ref,BundleContext context) throws BundleException{
		if(validate()){
			Tracker tracker = listeners.get(ref.getKey());
			if(tracker==null){
				return;
			}
			for(TrackerListener tl:tracker.getTrackerListeners()){
				tl.serviceRemove(ref,context);
			}
		}
	}
	
	private boolean validate(){
		if(listeners.size()==0){
			return false;
		}
		return true;
	}
	
	public void addTrackerListener(Class<?> clazz,TrackerListener listener,AttributeKey key){
		if(listeners.containsKey(key)){
//			List<TrackerListener> trackers = new ArrayList<TrackerListener>();
//			trackers.add(listener);
			listeners.get(key).getTrackerListeners().add(listener);
		}else{
			Tracker tracker = new Tracker(clazz,key);
			tracker.getTrackerListeners().add(listener);
			listeners.put(key, tracker);
		}
	}
}
