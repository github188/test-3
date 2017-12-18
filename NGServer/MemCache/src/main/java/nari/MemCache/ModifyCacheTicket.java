package nari.MemCache;

import nari.MemCache.matcher.QueryMatcher;

public class ModifyCacheTicket extends CacheTicket {

	private QueryMatcher matcher;
	
	private Value value;
	
	public ModifyCacheTicket(MemCluster cluster/**,Class<?> beanClass,Field[] fields,Class<?> wrapperBeanClass,Field[] wrapperFields**/) {
		super(cluster/**,beanClass,fields,wrapperBeanClass,wrapperFields**/);
	}

	public void addMatcher( QueryMatcher matcher){
		this.matcher = matcher;
	}
	
	public QueryMatcher getMatcher(){
		return matcher;
	}
	
	public void addValue(Value value){
		this.value = value;
	}
	
	public Value getValue(){
		return value;
	}
}
