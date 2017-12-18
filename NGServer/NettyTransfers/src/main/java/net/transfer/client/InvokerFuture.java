package net.transfer.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class InvokerFuture implements ResponseFuture {

	private static final Map<Long,ResponseFuture> futures = new ConcurrentHashMap<Long,ResponseFuture>();
	
	private Response response = null;
	
	private final Lock lock = new ReentrantLock();
	
	private final Condition done = lock.newCondition();
	
	private Request req = null;
	
	public InvokerFuture(Request req) {
		this.req = req;
		futures.put(req.getId(), this);
	}
	
	public Object get() {
		return get(0);
	}

	public Object get(long timeout) {
		if(!isDone()){
			try {
				lock.lock();
				done.await(timeout==0?Long.MAX_VALUE:timeout,TimeUnit.MILLISECONDS);
			} catch (Exception e) {
				
			} finally{
				lock.unlock();
			}
		}
		
		return response.getResult();
	}

	public Request getRequest(){
		return req;
	}
	
	public static InvokerFuture getFuture(long id){
		return (InvokerFuture)futures.get(id);
	}
	
	public static void sent(Request request) {
        InvokerFuture future = (InvokerFuture)futures.get(request.getId());
        if (future != null) {
            future.doSent(request);
        }
    }
	
	private void doSent(Request request){
		
	}
	
	public static void received(Response response){
		InvokerFuture future = (InvokerFuture)futures.remove(response.getId());
		if(future!=null){
			future.doReceived(response);
		}
	}
	
	private void doReceived(Response response){
		try {
			this.response = response;
			lock.lock();
			if(done!=null){
				done.signal();
			}
		} catch(Throwable e){
			e.printStackTrace();
		}finally{
			lock.unlock();
		}
	}
	
	public boolean isDone() {
		return response!=null;
	}
	
}
