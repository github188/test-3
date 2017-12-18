package com.application.plugin.tracker;

import java.util.ArrayList;
import java.util.List;

import com.application.plugin.AttributeKey;

public class Tracker {

	private Class<?> clazz;
	
	private List<TrackerListener> listeners = new ArrayList<TrackerListener>();
	
	private AttributeKey key;
	
	public Tracker(Class<?> clazz,AttributeKey key) {
		this.clazz = clazz;
		this.key = key;
	}
	
	public Class<?> getTrackerClass(){
		return clazz;
	}
	
	public List<TrackerListener> getTrackerListeners(){
		return listeners;
	}
	
	public AttributeKey getAttributeKey(){
		return key;
	}
}
