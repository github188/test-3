package nari.MemCache;

public class CacheRecordInitHandler extends AbstractCacheHandler {

	@Override
	protected void doHandle(CacheTicket ticket) throws Exception {
		
		MemCluster cluster = ticket.getMemCluster();
		
		if(ticket instanceof AddCacheTicket){
			add((AddCacheTicket)ticket,cluster);
		}else if(ticket instanceof ModifyCacheTicket){
			modify((ModifyCacheTicket)ticket,cluster);
		}else if(ticket instanceof RemoveCacheTicket){
			remove((RemoveCacheTicket)ticket,cluster);
		}
		
	}

	private void add(AddCacheTicket ticket,MemCluster cluster) throws Exception{
		Pointer ptr = cluster.put(ticket.getPair(),ticket);
		ticket.setPointer(new Pointer[]{ptr});
	}
	
	private void modify(ModifyCacheTicket ticket,MemCluster cluster) throws Exception{
		Pointer[] ptr = cluster.modify(ticket.getMatcher(), ticket.getValue(),ticket);
		ticket.setPointer(ptr);
	}
	
	private void remove(RemoveCacheTicket ticket,MemCluster cluster) throws Exception{
		Pointer[] ptr = cluster.remove(ticket.getMatcher(),ticket);
		ticket.setPointer(ptr);
	}
}
