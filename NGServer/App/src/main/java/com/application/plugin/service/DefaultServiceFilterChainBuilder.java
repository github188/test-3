package com.application.plugin.service;

import java.util.ArrayList;
import java.util.List;

import com.application.plugin.service.DefaultServiceFilterChain.Entry;
import com.application.plugin.service.DefaultServiceFilterChain.NextFilter;

public class DefaultServiceFilterChainBuilder implements ServiceFilterChainBuilder {

	private List<Entry> entries = new ArrayList<Entry>();
	
	@Override
	public void buildFilterChain(ServiceFilterChain filterChain) {
		for(Entry e:entries){
			filterChain.addLast(e.getName(), e.getFiter());
		}
	}

	@Override
	public void addFilter(String name,ServiceFilter filter) {
		registry(entries.size(),new EntryImpl(name,filter));
	}

	@Override
	public void removeFilter(String name) {
		int index = -1;
		for(Entry e:entries){
			if(e.getName().equalsIgnoreCase(name)){
				index = entries.indexOf(e);
				break;
			}
		}
		if(index>=0){
			unregistry(index);
		}
	}
	
	private void registry(int index,Entry entry){
		entries.add(index, entry);
	}
	
	private void unregistry(int index){
		entries.remove(index);
	}
	
	class EntryImpl implements Entry{

		private String name;
		
		private ServiceFilter filter;
		
		public EntryImpl(String name,ServiceFilter filter){
			this.name = name;
			this.filter = filter;
		}
		
		@Override
		public String getName() {
			return name;
		}

		@Override
		public ServiceFilter getFiter() {
			return filter;
		}

		@Override
		public NextFilter getNextFilter() {
			return null;
		}
		
	}
}
