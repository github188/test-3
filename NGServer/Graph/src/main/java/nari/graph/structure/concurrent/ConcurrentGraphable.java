package nari.graph.structure.concurrent;

import nari.graph.structure.basic.AbstractGraphable;

import java.io.Serializable;

/**
 * 支持并发访问的网络单元
 */
public abstract class ConcurrentGraphable
        extends AbstractGraphable implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -1901177565939021194L;
    
    public ConcurrentGraphable(int id) {
    	super();
    	setId(id);
    }
}
