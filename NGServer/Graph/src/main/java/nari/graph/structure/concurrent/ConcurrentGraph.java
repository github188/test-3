package nari.graph.structure.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

import nari.graph.structure.basic.AbstractGraph;

public class ConcurrentGraph 
	extends AbstractGraph {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5451776346291722877L;
	
	/**
     * ID分配器
     */
    private static AtomicInteger currentId = new AtomicInteger(0);

	@Override
	public int createComponentId() {
		return currentId.getAndIncrement();
	}
}
