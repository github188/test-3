package nari.SpatialIndex.index;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import nari.SpatialIndex.searcher.Record;

public class InterrupBlockingQueue implements BlockingQueue<Record> {

	private final BlockingQueue<Record> queue;
	
//	private final ReentrantLock lock = new ReentrantLock();
	
//	private final Condition readCondition;
//	
//	private final Condition writeCondition;
	
//	private boolean isInterrupted = false;
	private final AtomicBoolean isInterrupted = new AtomicBoolean(false);
	
//	private int capacity;
	
	public InterrupBlockingQueue(int capacity, boolean fair){
		queue = new ArrayBlockingQueue<Record>(capacity, fair);
//		this.capacity = capacity;
//		readCondition = lock.newCondition();
//		writeCondition = lock.newCondition();
	}
	
	@Override
	public Record remove() {
		return queue.remove();
	}

	@Override
	public Record poll() {
		return queue.poll();
	}

	@Override
	public Record element() {
		return queue.element();
	}

	@Override
	public Record peek() {
		return queue.peek();
	}

	@Override
	public int size() {
		return queue.size();
	}

	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	@Override
	public Iterator<Record> iterator() {
		return queue.iterator();
	}

	@Override
	public Object[] toArray() {
		return queue.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return queue.toArray(a);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return queue.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends Record> c) {
		return queue.addAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return queue.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return queue.retainAll(c);
	}

	@Override
	public void clear() {
		queue.clear();
	}

	@Override
	public boolean add(Record e) {
		return queue.add(e);
	}

	@Override
	public boolean offer(Record e) {
		return queue.offer(e);
	}

	@Override
	public void put(Record e) throws InterruptedException {
//		lock.lockInterruptibly();
		
//		int size = queue.size();
		try {
//			while(size==capacity && !isInterrupted.get()){
//				writeCondition.await();
//				size = queue.size();
//				if(isInterrupted.get()){
//					break;
//				}
//			}
//			if(isInterrupted.get()){
//				return;
//			}
			queue.offer(e);
//			readCondition.signal();
		} finally {
//			lock.unlock();
		}
	}

	@Override
	public boolean offer(Record e, long timeout, TimeUnit unit) throws InterruptedException {
		return queue.offer(e, timeout, unit);
	}

	@Override
	public Record take() throws InterruptedException {
//		lock.lockInterruptibly();
		
//		int size = queue.size();
		try {
//			while(size==0 && !isInterrupted.get()){
//				readCondition.await();
//				size = queue.size();
//				if(isInterrupted.get()){
//					break;
//				}
//			}
//			if(isInterrupted.get() && size==0){
//				System.out.println("return null");
//				writeCondition.signal();
//				return null;
//			}
			Record record = queue.poll();
//			writeCondition.signal();
			return record;
		} finally {
//			lock.unlock();
		}
	}

	@Override
	public Record poll(long timeout, TimeUnit unit) throws InterruptedException {
		return queue.poll(timeout, unit);
	}

	@Override
	public int remainingCapacity() {
		return queue.remainingCapacity();
	}

	@Override
	public boolean remove(Object o) {
		return queue.remove(o);
	}

	@Override
	public boolean contains(Object o) {
		return queue.contains(o);
	}

	@Override
	public int drainTo(Collection<? super Record> c) {
		return queue.drainTo(c);
	}

	@Override
	public int drainTo(Collection<? super Record> c, int maxElements) {
		return queue.drainTo(c);
	}

	public void interruper() throws InterruptedException{
//		lock.lockInterruptibly();
		try {
			isInterrupted.compareAndSet(false, true);
//			readCondition.signal();
//			writeCondition.signal();
		} finally {
//			lock.unlock();
		}
	}
	
	public boolean isInterrupted(){
		return isInterrupted.get();
	}
}
