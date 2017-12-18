package nari.SpatialIndex.index;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import nari.SpatialIndex.searcher.Record;

public class BlockingQueueCluster implements BlockingQueue<Record> {

	private InterrupBlockingQueue[] cluster;
	
	private int seek;
	
	private int queueCapacity;
	
	private int loop;
	
	private boolean isInterrupted = false;
	
	private final ReentrantLock lock = new ReentrantLock();
	
	private final Condition readCondition;
	
	private final Condition writeCondition;
	
	public BlockingQueueCluster(int clusterCapacity,int queueCapacity){
		cluster = new InterrupBlockingQueue[clusterCapacity];
		for(int i=0;i<clusterCapacity;i++){
			cluster[i] = new InterrupBlockingQueue(queueCapacity,false);
		}
		this.seek = clusterCapacity;
		this.queueCapacity = queueCapacity;
		this.loop = clusterCapacity*2;
		readCondition = lock.newCondition();
		writeCondition = lock.newCondition();
	}
	
	public BlockingQueueCluster(int clusterCapacity){
		this(clusterCapacity,500);
	}
	
	@Override
	public Record remove() {
		return null;
	}

	@Override
	public Record poll() {
		return null;
	}

	@Override
	public Record element() {
		return null;
	}

	@Override
	public Record peek() {
		return null;
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public Iterator<Record> iterator() {
		return null;
	}

	@Override
	public Object[] toArray() {
		return null;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return null;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends Record> c) {
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return false;
	}

	@Override
	public void clear() {
		for(int i=0;i<seek;i++){
			cluster[i].clear();
		}
		cluster = null;
	}

	@Override
	public boolean add(Record e) {
		return false;
	}

	@Override
	public boolean offer(Record e) {
		return false;
	}

	@Override
	public void put(Record e) throws InterruptedException {
		lock.lockInterruptibly();
		try {
			int index = 0;
			InterrupBlockingQueue queue = cluster[index];
			int i = 0;
			while(queue.size()==queueCapacity){
				index++;
				if(index==seek){
					index = 0;
				}
				queue = cluster[index];
				if(queue.size()==queueCapacity){
					if(i>=loop){
						if(queue.isInterrupted()){
							break;
						}else{
							writeCondition.await();
						}
					}
					i++;
					continue;
				}else{
					queue.put(e);
					readCondition.signal();
					return;
				}
			}
			
			queue.put(e);
			readCondition.signal();
		} finally{
			lock.unlock();
		}
	}

	@Override
	public boolean offer(Record e, long timeout, TimeUnit unit) throws InterruptedException {
		return false;
	}

	@Override
	public Record take() throws InterruptedException {
		lock.lockInterruptibly();
		Record r = null;
		try {
			int index = 0;
			InterrupBlockingQueue queue = cluster[index];
			int i = 0;
			while(queue.isEmpty()){
				index ++;
				if(index==seek){
					index = 0;
				}
				queue = cluster[index];
				if(queue.isEmpty()){
					if(i>=loop){
						if(queue.isInterrupted()){
							break;
						}else{
							readCondition.await();
						}
					}
					i++;
					continue;
				}else{
					r = queue.take();
					writeCondition.signal();
					return r;
				}
			}
			r = queue.take();
			writeCondition.signal();
		} finally{
			lock.unlock();
		}
		return r;
	}

	@Override
	public Record poll(long timeout, TimeUnit unit) throws InterruptedException {
		return null;
	}

	@Override
	public int remainingCapacity() {
		return 0;
	}

	@Override
	public boolean remove(Object o) {
		return false;
	}

	@Override
	public boolean contains(Object o) {
		return false;
	}

	@Override
	public int drainTo(Collection<? super Record> c) {
		return 0;
	}

	@Override
	public int drainTo(Collection<? super Record> c, int maxElements) {
		return 0;
	}
	
	public void interruper() throws InterruptedException{
		lock.lockInterruptibly();
		try {
			for(int i=0;i<seek;i++){
				cluster[i].interruper();
			}
			readCondition.signal();
			writeCondition.signal();
		} finally{
			lock.unlock();
		}
	}
	
	public boolean isInterrupted(){
		return isInterrupted;
	}
}
