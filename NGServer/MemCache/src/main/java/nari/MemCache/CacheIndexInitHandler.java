package nari.MemCache;

public class CacheIndexInitHandler extends AbstractCacheHandler {

	@Override
	protected void doHandle(CacheTicket ticket) throws Exception {
		
		MemCluster cluster = ticket.getMemCluster();
		
		if(ticket instanceof AddCacheTicket){
			createIndex((AddCacheTicket)ticket,cluster);
		}else if(ticket instanceof ModifyCacheTicket){
			modifyIndex((ModifyCacheTicket)ticket,cluster);
		}else if(ticket instanceof RemoveCacheTicket){
			removeIndex((RemoveCacheTicket)ticket,cluster);
		}		
	}

	private void createIndex(AddCacheTicket ticket,MemCluster cluster) throws Exception{
		IndexCluster indexCluster = cluster.getIndex();
		
		Pointer[] ptr = ticket.getPointer();
		
		String[] fields = indexCluster.getIndexFields();
		
		indexCluster.getPointerCluster().addPointer(ptr[0].getPointerId(), ptr[0]);
		
		for(String field:fields){
			indexCluster.add(field, ptr[0]);
		}
		
	}
	
	private void modifyIndex(ModifyCacheTicket ticket,MemCluster cluster) throws Exception{
		IndexCluster indexCluster = cluster.getIndex();
		
		Pointer[] ptrs = ticket.getPointer();
		
		Value val = ticket.getValue();
		
		for(Pointer ptr:ptrs){
			indexCluster.modify(ptr,val);
		}
	}
	
	private void removeIndex(RemoveCacheTicket ticket,MemCluster cluster) throws Exception{
		IndexCluster indexCluster = cluster.getIndex();
		
		Pointer[] ptrs = ticket.getPointer();
		
		for(Pointer ptr:ptrs){
			indexCluster.remove(ptr);
		}
	}
}
