package nari.MemCache;

public class CacheTicket {

	private MemCluster cluster = null;
	
//	private CachePair pair;
	
//	private OperateType type;
	
	private Pointer[] ptr;
	
//	private Class<?> beanClass;
//	
//	private Field[] fields;
//	
//	private Class<?> wrapperBeanClass;
//	
//	private Field[] wrapperFields;
	
	public CacheTicket(MemCluster cluster/**,Class<?> beanClass,Field[] fields,Class<?> wrapperBeanClass,Field[] wrapperFields**/){
		this.cluster = cluster;
//		this.beanClass = beanClass;
//		this.fields = fields;
//		this.wrapperBeanClass = wrapperBeanClass;
//		this.wrapperFields = wrapperFields;
	}
	
//	public void addPair(CachePair pair,OperateType type) throws Exception{
//		this.pair = pair;
//		this.type = type;
//	}
	
	public MemAllocater getMemAllocater() throws Exception{
		return cluster.getMemAllocater();
	}
	
	public MemCluster getMemCluster() throws Exception{
		return cluster;
	}
	
	public void setPointer(Pointer[] ptr) throws Exception{
		this.ptr = ptr;
	}
	
	public Pointer[] getPointer() throws Exception{
		return ptr;
	}
	
//	public Class<?> getCacheBodyClass() throws Exception{
//		return beanClass;
//	}
//	
//	public Field[] getCacheBodyFields() throws Exception{
//		return fields;
//	}
//	
//	public Class<?> getWrapperBodyClass() throws Exception{
//		return wrapperBeanClass;
//	}
//	
//	public Field[] getCacheWrapperFields() throws Exception{
//		return wrapperFields;
//	}
}
