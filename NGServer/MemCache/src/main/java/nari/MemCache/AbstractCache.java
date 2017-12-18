package nari.MemCache;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.LoaderClassPath;
import nari.MemCache.matcher.QueryMatcher;

public abstract class AbstractCache implements Cache {
	
	private final AtomicBoolean init = new AtomicBoolean(false);

	private final AtomicBoolean start = new AtomicBoolean(false);

	private final AtomicBoolean stop = new AtomicBoolean(false);

	private MemCluster cluster = null;

	private MemAllocater allocater = null;

//	private final IndexCluster index = new FieldIndexCluster();
	
	private IndexCluster index = null;

	private final AtomicReference<HandlerChain> chain = new AtomicReference<HandlerChain>();

	private Updater updater = null;
	
	private Class<?> beanClass;
	
	private Class<?> wrapperBeanClass;
	
	private Field[] fields = null;

	private Field[] wrapperFields = null;
	
	private Map<String,Field> fieldClass;
	
	private Map<String,Field> wrapperFieldClass;

	private AddCacheTicket ticket = null;
	
	private PointerCluster ptrCluster = new PointerCluster();
	
	private Map<String,FieldProcesser> processerMap = null;
	
	private static final AtomicInteger it = new AtomicInteger(0);

	private int poolSize;
	
	public AbstractCache(int poolSize) {
		this.poolSize = poolSize;
	}
	
	public boolean add(Object pair) throws Exception {
		HandlerChain chain = getHandlerChain();
//		AddCacheTicket ticket = new AddCacheTicket(this.cluster);
		ticket.addPair(pair);
		try {
			return chain.handle(ticket);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean modify(Key key, Value value) throws Exception {
		HandlerChain chain = getHandlerChain();

		ModifyCacheTicket ticket = new ModifyCacheTicket(this.cluster);

		return chain.handle(ticket);
	}

	public boolean modify(QueryMatcher matcher, Value value) throws Exception {
		HandlerChain chain = getHandlerChain();

		ModifyCacheTicket ticket = new ModifyCacheTicket(this.cluster);

		ticket.addMatcher(matcher);
		ticket.addValue(value);

		return chain.handle(ticket);
	}

	public boolean remove(Key key) throws Exception {
		HandlerChain chain = getHandlerChain();

		RemoveCacheTicket ticket = new RemoveCacheTicket(this.cluster);

		return chain.handle(ticket);
	}

	public boolean remove(QueryMatcher matcher) throws Exception {
		HandlerChain chain = getHandlerChain();

		RemoveCacheTicket ticket = new RemoveCacheTicket(this.cluster);

		return chain.handle(ticket);
	}

	public CacheEntry search(QueryMatcher matcher) throws Exception {
		return this.cluster.search(matcher);
	}
	
	@Override
	public CacheEntry preciseSearch(QueryMatcher matcher) throws Exception {
		return this.cluster.preciseSearch(matcher);
	}

	public void rebuild() throws Exception {
	}

	public boolean destory() throws Exception {
		return true;
	}

	public HandlerChain getHandlerChain() throws Exception {
		if (this.chain.get() == null) {
			HandlerChain c = getCacheHandlerChain();
			this.chain.compareAndSet(null, c);
		}

		return (HandlerChain) this.chain.get();
	}

	public boolean init() throws Exception {
		if (this.init.get()) {
			throw new RuntimeException("cache " + getKey().toString() + " has inited");
		}
		this.allocater = getFactory().getMemAllocater();

		ptrCluster.init(poolSize);
		
		index = new FieldIndexCluster(ptrCluster);
		
		this.cluster = new StdMemCluster(this, this.allocater, this.index);

		this.cluster.init();
		
		ticket = new AddCacheTicket(this.cluster);

		HandlerChain chain = getHandlerChain();

		chain.buildHandlerChain();

		this.updater = getFactory().createUpdater(this);

		this.updater.init();

		this.init.compareAndSet(false, true);
		return true;
	}

	public boolean start() throws Exception {
		if (this.start.get()) {
			throw new RuntimeException("cache " + getKey().toString() + " has started");
		}

		this.cluster.start();

		this.updater.start();

		this.start.compareAndSet(false, true);
		return true;
	}

	public boolean stop() throws Exception {
		if (this.stop.get()) {
			throw new RuntimeException("cache " + getKey().toString() + " has stoped");
		}

		this.cluster.stop();

		this.updater.stop();

		this.stop.compareAndSet(false, true);
		return true;
	}

	public void addIndex(String indexField, IndexType indexType, FieldType fieldType) throws Exception {
		this.index.register(indexField, indexType, fieldType,ptrCluster);
	}

	public void registCacheBodyClass(Class<?> beanClass) throws Exception {
		this.beanClass = beanClass;
		this.fields = beanClass.getDeclaredFields();

		fieldClass = new HashMap<String,Field>(fields.length);
		
		wrapperFieldClass = new HashMap<String,Field>();
		
		for(Field f:fields){
			f.setAccessible(true);
			fieldClass.put(f.getName(), f);
			if(f.getType()==Shape.class || f.getType()==String.class){
				wrapperFieldClass.put(f.getName(), f);
			}
		}
		
		ClassPool pool = new ClassPool(true);
		pool.appendClassPath(new LoaderClassPath(super.getClass().getClassLoader()));

		CtClass cls = pool.makeClass(beanClass.getName() + "$" + it.getAndIncrement());
		
		cls.addField(CtField.make("private int _id;", cls));
		
		for (Field field : this.fields) {
			field.setAccessible(true);
			if ((field.getType() == Integer.TYPE) || (field.getType() == Integer.class))
				cls.addField(CtField.make("private int " + field.getName()+ ";", cls));
			else if ((field.getType() == Long.TYPE) || (field.getType() == Long.class))
				cls.addField(CtField.make("private long " + field.getName() + ";", cls));
			else if ((field.getType() == Double.TYPE) || (field.getType() == Double.class))
				cls.addField(CtField.make("private double " + field.getName() + ";", cls));
			else if ((field.getType() == Short.TYPE) || (field.getType() == Short.class))
				cls.addField(CtField.make("private short " + field.getName() + ";", cls));
			else if ((field.getType() == Float.TYPE) || (field.getType() == Float.class)) {
				cls.addField(CtField.make("private float " + field.getName() + ";", cls));
			}
		}

		this.wrapperBeanClass = cls.toClass(super.getClass().getClassLoader(),super.getClass().getProtectionDomain());

		this.wrapperFields = this.wrapperBeanClass.getDeclaredFields();
		
		for (Field field : this.wrapperFields){
			field.setAccessible(true);
			wrapperFieldClass.put(field.getName(), field);
		}
		
	}

	public Class<?> getBeanClass() throws Exception {
		return this.beanClass;
	}

	public Field getBeanField(String field) throws Exception {
//		Field[] fields = beanClass.getDeclaredFields();
//		for (Field f : fields) {
//			if (f.getName().equals(field)) {
//				f.setAccessible(true);
//				return f;
//			}
//		}
		return fieldClass.get(field);
	}

	public Field[] getBeanFields() throws Exception {
		return fields;
	}

	public Class<?> getWrapperBeanClass() throws Exception {
		return this.wrapperBeanClass;
	}

	public Field getWrapperBeanField(String field) throws Exception {
//		for (Field f : this.wrapperFields) {
//			if (f.getName().equals(field)) {
//				return f;
//			}
//		}
		return wrapperFieldClass.get(field);
	}

	public Field[] getWrapperBeanFields() throws Exception {
		return this.wrapperFields;
	}
	
	@Override
	public void registFieldProcesser(String fieldName, FieldProcesser processer) throws Exception {
		if(processerMap==null){
			processerMap = new HashMap<String,FieldProcesser>();
		}
		processerMap.put(fieldName, processer);
	}
	
	@Override
	public void registTypeProcesser(Class<?> klass, FieldProcesser processer) throws Exception {
		if(processerMap==null){
			processerMap = new HashMap<String,FieldProcesser>();
		}
		processerMap.put(klass.getName(), processer);
	}
	
	@Override
	public FieldProcesser getFieldProcesser(Class<?> klass) throws Exception {
		return processerMap==null?null:processerMap.get(klass.getName());
	}

	@Override
	public FieldProcesser getFieldProcesser(String fieldName) throws Exception {
		return processerMap==null?null:processerMap.get(fieldName);
	}

	protected abstract HandlerChain getCacheHandlerChain() throws Exception;
}