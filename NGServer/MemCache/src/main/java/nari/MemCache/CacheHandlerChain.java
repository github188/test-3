package nari.MemCache;

import java.util.HashMap;
import java.util.Map;

public class CacheHandlerChain implements HandlerChain {

	private EntryImpl head;
	
	private EntryImpl tail;
	
	private final Map<String,EntryImpl> entrys = new HashMap<String,EntryImpl>();
	
	public CacheHandlerChain() {
		head = new EntryImpl(null,tail,"head",new HeadHandler());
		tail = new EntryImpl(head,null,"tail",new TailHandler());
		head.nextEntry = tail;
	}
	
	@Override
	public void addFirst(String name, CacheHandler handler) throws Exception {
		handler.onPreAdd(this, name);
		
		EntryImpl entry = new EntryImpl(head,head.nextEntry,name,handler);
		entry.nextEntry.preEntry = entry;
		entry.preEntry.nextEntry = entry;
		
		entrys.put(name, entry);
		handler.onPostAdd(this, name);
	}

	@Override
	public void addLast(String name, CacheHandler handler) throws Exception {
		handler.onPreAdd(this, name);
		
		EntryImpl entry = new EntryImpl(tail.preEntry,tail,name,handler);
		entry.nextEntry.preEntry = entry;
		entry.preEntry.nextEntry = entry;
		
		entrys.put(name, entry);
		handler.onPostAdd(this, name);
	}

	@Override
	public void remove(String name) throws Exception {
		EntryImpl entry = entrys.get(name);
		
		entry.getHandler().onPreRemove(this, name);
		entry.preEntry.nextEntry = entry.nextEntry;
		entry.nextEntry.preEntry = entry.preEntry;
		entry.getHandler().onPostRemove(this, name);
	}

//	interface NextHandler{
//		
//		public boolean handle(CacheTicket ticket) throws Exception;
//		
//	}
	
	interface Entry{
		
		public String getName() throws Exception;
		
		public CacheHandler getHandler() throws Exception;
		
		public NextHandler getNextHandler() throws Exception;
	}
	
	class EntryImpl implements Entry{

		private EntryImpl preEntry;
		
		private EntryImpl nextEntry;
		
		private String name;
		
		private CacheHandler handler;
		
		private NextHandler nextHandler;
		
		public EntryImpl(EntryImpl preEntry,EntryImpl nextEntry,String name,CacheHandler handler) {
			this.preEntry = preEntry;
			this.nextEntry = nextEntry;
			this.name = name;
			this.handler = handler;
			this.nextHandler = new NextHandler() {
				
				@Override
				public boolean handle(CacheTicket ticket) throws Exception {
					
					CacheHandler handler = EntryImpl.this.nextEntry.getHandler();
					NextHandler next = EntryImpl.this.nextEntry.getNextHandler();
					
					handler.handle(ticket, next);
					
					return true;
				}
				
			};
		}
		
		@Override
		public String getName() throws Exception {
			return name;
		}

		@Override
		public CacheHandler getHandler() throws Exception {
			return handler;
		}

		@Override
		public NextHandler getNextHandler() throws Exception {
			return nextHandler;
		}
		
	}
	
	class HeadHandler implements CacheHandler{

		@Override
		public boolean handle(CacheTicket ticket,NextHandler next) throws Exception {
			next.handle(ticket);
			return true;
		}

		@Override
		public void onPreAdd(HandlerChain chain, String name) {
			
		}

		@Override
		public void onPostAdd(HandlerChain chain, String name) {
			
		}

		@Override
		public void onPreRemove(HandlerChain chain, String name) throws Exception {
			
		}

		@Override
		public void onPostRemove(HandlerChain chain, String name) throws Exception {
			
		}
		
	}
	
	class TailHandler implements CacheHandler{

		@Override
		public boolean handle(CacheTicket ticket,NextHandler next) throws Exception {
			return true;
		}

		@Override
		public void onPreAdd(HandlerChain chain, String name) {
			
		}

		@Override
		public void onPostAdd(HandlerChain chain, String name) {
			
		}

		@Override
		public void onPreRemove(HandlerChain chain, String name) throws Exception {
			
		}

		@Override
		public void onPostRemove(HandlerChain chain, String name) throws Exception {
			
		}
		
	}

	@Override
	public boolean handle(CacheTicket ticket) throws Exception {
		CacheHandler handler = head.getHandler();
		NextHandler next = head.getNextHandler();
		
		return handler.handle(ticket, next);
	}

	@Override
	public void buildHandlerChain() throws Exception {
		
	}
}
