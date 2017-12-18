package com.application.plugin.bundle;

import java.util.ArrayList;
import java.util.List;

import com.application.plugin.bundle.DefaultFilterChain.Entry;
import com.application.plugin.bundle.DefaultFilterChain.NextFilter;

public class DefaultFilterChainBuilder implements FilterChainBuilder {

	private List<Entry> entries = new ArrayList<Entry>();
	
	@Override
	public void buildFilterChain(FilterChain filterChain) {
		for(Entry e:entries){
			filterChain.addLast(e.getName(), e.getFiter());
		}
	}

	@Override
	public void addFilter(String name,BundleFilter filter) {
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
		
		private BundleFilter filter;
		
		public EntryImpl(String name,BundleFilter filter){
			this.name = name;
			this.filter = filter;
		}
		
		@Override
		public String getName() {
			return name;
		}

		@Override
		public BundleFilter getFiter() {
			return filter;
		}

		@Override
		public NextFilter getNextFilter() {
			return null;
		}
		
	}

}
