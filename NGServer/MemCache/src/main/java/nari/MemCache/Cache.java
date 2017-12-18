package nari.MemCache;

import java.lang.reflect.Field;

import nari.MemCache.matcher.QueryMatcher;

public interface Cache extends CacheLifecycle {

	public boolean add(Object pair) throws Exception;
	
	public boolean modify(Key key,Value value) throws Exception;
	
	public boolean modify(QueryMatcher matcher,Value value) throws Exception;
	
	public CacheEntry search(QueryMatcher matcher) throws Exception;
	
	public CacheEntry preciseSearch(QueryMatcher matcher) throws Exception;
	
	public boolean remove(Key key) throws Exception;
	
	public boolean remove(QueryMatcher matcher) throws Exception;
	
	public void rebuild() throws Exception;
	
	public Key getKey() throws Exception;
	
	public boolean destory() throws Exception;
	
	public CacheFactory getFactory() throws Exception;
	
	public HandlerChain getHandlerChain() throws Exception;
	
	public void registCacheBodyClass(Class<?> beanClass) throws Exception;
	
	public void addIndex(String indexField,IndexType indexType,FieldType fieldType) throws Exception;
	
	public Class<?> getBeanClass() throws Exception;
	
	public Class<?> getWrapperBeanClass() throws Exception;
	
	public Field getBeanField(String field) throws Exception;
	
	public Field[] getBeanFields() throws Exception;
	
	public Field getWrapperBeanField(String field) throws Exception;
	
	public Field[] getWrapperBeanFields() throws Exception;
	
	public void registTypeProcesser(Class<?> klass,FieldProcesser processer) throws Exception;
	
	public void registFieldProcesser(String fieldName,FieldProcesser processer) throws Exception;
	
	public FieldProcesser getFieldProcesser(Class<?> klass) throws Exception;
	
	public FieldProcesser getFieldProcesser(String fieldName) throws Exception;
	
}
