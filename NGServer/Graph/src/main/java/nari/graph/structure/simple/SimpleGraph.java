package nari.graph.structure.simple;

import nari.graph.structure.basic.AbstractGraph;

public class SimpleGraph 
	extends AbstractGraph {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7216919996378151009L;
	
	/**
     * ID分配器
     */
	private int currentId = 0;

	@Override
	public int createComponentId() {
		return currentId++;
	}
}
