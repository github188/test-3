package com.application.plugin.bundle;

import java.util.HashMap;
import java.util.Map;

import com.application.plugin.BundleContext;

public class DefaultFilterChain implements FilterChain {

	private Map<String,EntryImpl> name2entry = new HashMap<String,EntryImpl>();
	
	private EntryImpl head = null;
	
	private EntryImpl tail = null;
	
	public DefaultFilterChain(){
		tail = new EntryImpl(head,null,"tail",new TailFilter());
		head = new EntryImpl(null,tail,"head",new HeadFilter());
		tail.prevEntry = head;
	}
	
	private void register(EntryImpl prevEntry,String name,BundleFilter filter){
		
		try {
			filter.onPreAdd(this, name);
		} catch (BundleException e) {
			e.printStackTrace();
		}
		
		EntryImpl entry = new EntryImpl(prevEntry,prevEntry.nextEntry,name,filter);
		entry.nextEntry.prevEntry = entry;
		entry.nextEntry = entry;
		name2entry.put(name, entry);
		
		try {
			filter.onPostAdd(this, name);
		} catch (BundleException e) {
			e.printStackTrace();
		}
	}
	
	private void unregister(EntryImpl entry){
		EntryImpl nextEntry = entry.nextEntry;
		EntryImpl prevEntry = entry.prevEntry;
		prevEntry.nextEntry = nextEntry;
		nextEntry.prevEntry = prevEntry;
		
		try {
			entry.getFiter().onPreRemove(this, entry.name);
		} catch (BundleException e) {
			e.printStackTrace();
		}
		
		name2entry.remove(entry.name);
		
		try {
			entry.getFiter().onPostRemove(this, entry.name);
		} catch (BundleException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void fireExceptionCaught(Throwable exception) {
		callNextExceptionCaught(head,exception);
	}
	
	private void callNextExceptionCaught(Entry entry,Throwable exception){
		BundleFilter filter = entry.getFiter();
		NextFilter nextFilter = entry.getNextFilter();
		try {
			filter.fireExceptionCaught(exception,nextFilter);
		} catch (BundleException e) {
			fireExceptionCaught(e.getCause());
		}
	}

	@Override
	public void fireBundleInit(BundleContext context) {
		callNextBundleInit(head,context);
	}
	
	private void callNextBundleInit(Entry entry,BundleContext context){
		BundleFilter filter = entry.getFiter();
		NextFilter nextFilter = entry.getNextFilter();
		try {
			filter.fireBundleInit(context, nextFilter);
		} catch (BundleException e) {
			fireExceptionCaught(e.getCause());
		}
	}

	@Override
	public void fireBundleStart(BundleContext context) {
		callNextBundleStart(head,context);
	}
	
	private void callNextBundleStart(Entry entry,BundleContext context){
		BundleFilter filter = entry.getFiter();
		NextFilter nextFilter = entry.getNextFilter();
		try {
			filter.fireBundleStart(context, nextFilter);
		} catch (BundleException e) {
			fireExceptionCaught(e.getCause());
		}
	}

	@Override
	public void fireBundleStop(BundleContext context) {
		callNextBundleStop(head,context);
	}

	private void callNextBundleStop(Entry entry,BundleContext context){
		BundleFilter filter = entry.getFiter();
		NextFilter nextFilter = entry.getNextFilter();
		try {
			filter.fireBundleStop(context, nextFilter);
		} catch (BundleException e) {
			fireExceptionCaught(e.getCause());
		}
	}
	
	@Override
	public boolean contains(String name) {
		return false;
	}

	@Override
	public boolean contains(BundleFilter filter) {
		return false;
	}

	@Override
	public void addFirst(String name, BundleFilter filter) {
		register(head,name, filter);
	}

	@Override
	public void addLast(String name, BundleFilter filter) {
		register(tail.prevEntry,name, filter);
	}


	@Override
	public BundleFilter remove(String name) {
		EntryImpl entry = name2entry.get(name);
		unregister(entry);
		return entry.getFiter();
	}

	@Override
	public void remove(BundleFilter filter) {
		EntryImpl e = head.nextEntry;
		
		while(e!=tail){
			if(e.getFiter()==filter){
				unregister(e);
				return;
			}
			e = e.nextEntry;
		}
	}

	@Override
	public void clear() throws Exception {
		for(Map.Entry<String, EntryImpl> e:name2entry.entrySet()){
			unregister(e.getValue());
		}
		
	}

	class HeadFilter implements BundleFilter{

		@Override
		public void fireExceptionCaught(Throwable exception,NextFilter filter) throws BundleException {
			filter.fireExceptionCaught(exception);
		}

		@Override
		public void fireBundleInit(BundleContext context,NextFilter filter) throws BundleException {
			filter.fireBundleInit(context);
		}

		@Override
		public void fireBundleStart(BundleContext context,NextFilter filter) throws BundleException {
			filter.fireBundleStart(context);
		}

		@Override
		public void fireBundleStop(BundleContext context,NextFilter filter) throws BundleException {
			filter.fireBundleStop(context);
		}

		@Override
		public void onPreAdd(FilterChain chain, String filterName) throws BundleException {
			
		}

		@Override
		public void onPostAdd(FilterChain chain, String filterName) throws BundleException {
			
		}

		@Override
		public void onPreRemove(FilterChain chain, String filterName) throws BundleException {
			
		}

		@Override
		public void onPostRemove(FilterChain chain, String filterName) throws BundleException {
			
		}
		
	}
	
	class TailFilter implements BundleFilter{

		@Override
		public void fireExceptionCaught(Throwable exception,NextFilter filter) throws BundleException {
			exception.printStackTrace();
		}

		@Override
		public void fireBundleInit(BundleContext context,NextFilter filter) throws BundleException {
			
		}

		@Override
		public void fireBundleStart(BundleContext context,NextFilter filter) throws BundleException {
			
		}

		@Override
		public void fireBundleStop(BundleContext context,NextFilter filter) throws BundleException {
			
		}

		@Override
		public void onPreAdd(FilterChain chain, String filterName) throws BundleException {
			
		}

		@Override
		public void onPostAdd(FilterChain chain, String filterName) throws BundleException {
			
		}

		@Override
		public void onPreRemove(FilterChain chain, String filterName) throws BundleException {
			
		}

		@Override
		public void onPostRemove(FilterChain chain, String filterName) throws BundleException {
			
		}
		
	}
	
	interface NextFilter{
		
		public void fireExceptionCaught(Throwable exception) throws BundleException;
		
		public void fireBundleInit(BundleContext context) throws BundleException;
		
		public void fireBundleStart(BundleContext context) throws BundleException;
		
		public void fireBundleStop(BundleContext context) throws BundleException;
		
	}
	
	interface Entry{
		
		public String getName();
		
		public BundleFilter getFiter();
		
		public NextFilter getNextFilter();
		
	}
	
	class EntryImpl implements Entry{

		private EntryImpl prevEntry;
		
		private EntryImpl nextEntry;
		
		private String name;
		
		private BundleFilter filter;
		
		private NextFilter nextFilter;
		
		public EntryImpl(EntryImpl prevEntry,EntryImpl nextEntry,String name,BundleFilter filter){
			this.prevEntry = prevEntry;
			this.name = name;
			this.nextEntry = nextEntry;
			this.filter = filter;
			this.nextFilter = new NextFilter(){

				@Override
				public void fireExceptionCaught(Throwable exception) throws BundleException {
					if(EntryImpl.this.nextEntry==null){
						return;
					}
					BundleFilter filter = EntryImpl.this.nextEntry.getFiter();
					NextFilter next = EntryImpl.this.nextEntry.getNextFilter();
					
					filter.fireExceptionCaught(exception, next);
				}

				@Override
				public void fireBundleInit(BundleContext context) throws BundleException {
					if(EntryImpl.this.nextEntry==null){
						return;
					}
					BundleFilter filter = EntryImpl.this.nextEntry.getFiter();
					NextFilter next = EntryImpl.this.nextEntry.getNextFilter();
					
					filter.fireBundleInit(context, next);
				}

				@Override
				public void fireBundleStart(BundleContext context) throws BundleException {
					if(EntryImpl.this.nextEntry==null){
						return;
					}
					BundleFilter filter = EntryImpl.this.nextEntry.getFiter();
					NextFilter next = EntryImpl.this.nextEntry.getNextFilter();
					
					filter.fireBundleStart(context, next);
				}

				@Override
				public void fireBundleStop(BundleContext context) throws BundleException {
					if(EntryImpl.this.nextEntry==null){
						return;
					}
					BundleFilter filter = EntryImpl.this.nextEntry.getFiter();
					NextFilter next = EntryImpl.this.nextEntry.getNextFilter();
					
					filter.fireBundleStop(context, next);
				}
				
			};
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
			return nextFilter;
		}
		
	}
}
