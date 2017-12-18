package nari.graph.structure.simple;

import nari.graph.structure.basic.AbstractGraphable;

import java.io.Serializable;

/**
 * 简单的网络
 * 简单的网络不支持并发构建，但在单线程场景中可以获取更大的访问效率。
 * 若强制在多线程场景下使用该结构，会导致意想不到的后果。
 *
 * @author birderyu
 * @date 2017/4/22
 */
public abstract class SimpleGraphable
        extends AbstractGraphable implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -1190342334204794793L;

    /**
     * 当前图单元是否被访问过
     */
    private boolean visited = false;

    /**
     * 当前图单元被访问的次数
     */
    private int count = -1;
    
    public SimpleGraphable(int id) {
    	super();
    	setId(id);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}
