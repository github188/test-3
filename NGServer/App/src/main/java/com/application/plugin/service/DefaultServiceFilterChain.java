package com.application.plugin.service;

import java.util.HashMap;
import java.util.Map;

import com.application.plugin.Invoker;
import com.application.plugin.Result;
import com.application.plugin.bundle.BundleException;

public class DefaultServiceFilterChain implements ServiceFilterChain {

	private Map<String,EntryImpl> name2entry = new HashMap<String,EntryImpl>();
	
	private EntryImpl head = null;
	
	private EntryImpl tail = null;
	
	public DefaultServiceFilterChain(){
		ServiceFilter headFilter = new HeadServiceFilter();
		ServiceFilter tailFilter = new TailServiceFilter();
		head = new EntryImpl(null,tail,"head",headFilter);
		tail = new EntryImpl(head,null,"tail",tailFilter);
		head.nextEntry = tail;
	}
	
	@Override
	public void firePrevInvoke(Invoker invoker) throws BundleException{
		callNextPrevInvoker(head,invoker);
	}

	@Override
	public void firePostInvoke(Invoker invoker,Result result) throws BundleException{
		callNextPostInvoker(invoker,head,result);
	}

	private void callNextPrevInvoker(Entry entry,Invoker invoker) throws BundleException{
		ServiceFilter filter = entry.getFiter();
		NextFilter nextFilter = entry.getNextFilter();
		filter.prevInvoke(invoker,nextFilter);
	}
	
	private void callNextPostInvoker(Invoker invoker,Entry entry,Result result) throws BundleException{
		ServiceFilter filter = entry.getFiter();
		NextFilter nextFilter = entry.getNextFilter();
		filter.postInvoke(invoker,result,nextFilter);
	}
	
	@Override
	public boolean contains(String name) {
		return name2entry.containsKey(name);
	}

	private void register(EntryImpl prevEntry,String name,ServiceFilter filter){
		EntryImpl entry = new EntryImpl(prevEntry,prevEntry.nextEntry,name,filter);
		entry.nextEntry.prevEntry = entry;
		entry.nextEntry = entry;
		name2entry.put(name, entry);
	}
	
	private void unregister(EntryImpl entry){
		EntryImpl nextEntry = entry.nextEntry;
		EntryImpl prevEntry = entry.prevEntry;
		prevEntry.nextEntry = nextEntry;
		nextEntry.prevEntry = prevEntry;
		
		name2entry.remove(entry.name);
	}
	
	@Override
	public void addFirst(String name, ServiceFilter filter) {
		register(head,name, filter);
	}

	@Override
	public void addLast(String name, ServiceFilter filter) {
		register(tail.prevEntry,name, filter);
	}


	@Override
	public ServiceFilter remove(String name) {
		EntryImpl entry = name2entry.get(name);
		unregister(entry);
		return entry.getFiter();
	}

	@Override
	public void remove(ServiceFilter filter) {
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

	class HeadServiceFilter implements ServiceFilter{

		@Override
		public void prevInvoke(Invoker invoker,NextFilter nextFilter) throws BundleException{
			nextFilter.prevInvoke(invoker);
		}

		@Override
		public void postInvoke(Invoker invoker,Result result,NextFilter nextFilter) throws BundleException{
			if(result.hasException()){
				throw new BundleException("",result.getException());
			}
			nextFilter.postInvoke(invoker,result);
		}

		@Override
		public String getName() {
			return "head";
		}
		
	}
	
	class TailServiceFilter implements ServiceFilter{

		@Override
		public void prevInvoke(Invoker invoker,NextFilter nextFilter) {
			
		}

		@Override
		public void postInvoke(Invoker invoker,Result result,NextFilter nextFilter) {
			
		}

		@Override
		public String getName() {
			return "tail";
		}

	}
	
	interface NextFilter{
		
		public void prevInvoke(Invoker invoker) throws BundleException;

		public void postInvoke(Invoker invoker,Result result) throws BundleException;
		
	}
	
	interface Entry{
		
		public String getName();
		
		public ServiceFilter getFiter();
		
		public NextFilter getNextFilter();
		
	}
	
	class EntryImpl implements Entry{

		private EntryImpl prevEntry;
		
		private EntryImpl nextEntry;
		
		private String name;
		
		private ServiceFilter filter;
		
		private NextFilter nextFilter;
		
		public EntryImpl(EntryImpl prevEntry,EntryImpl nextEntry,String name,ServiceFilter filter){
			this.prevEntry = prevEntry;
			this.name = name;
			this.nextEntry = nextEntry;
			this.filter = filter;
			this.nextFilter = new NextFilter(){

				@Override
				public void prevInvoke(Invoker invoker) throws BundleException {
					ServiceFilter filter = EntryImpl.this.nextEntry.getFiter();
					NextFilter nextFilter = EntryImpl.this.nextEntry.getNextFilter();
					filter.prevInvoke(invoker, nextFilter);
				}

				@Override
				public void postInvoke(Invoker invoker,Result result) throws BundleException{
					ServiceFilter filter = EntryImpl.this.nextEntry.getFiter();
					NextFilter nextFilter = EntryImpl.this.nextEntry.getNextFilter();
					filter.postInvoke(invoker,result, nextFilter);
				}

			};
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
			return nextFilter;
		}
		
	}

}
