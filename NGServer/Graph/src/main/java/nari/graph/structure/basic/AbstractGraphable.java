package nari.graph.structure.basic;

import nari.graph.structure.Graphable;

/**
 * 抽象网络单元
 * @author birderyu
 * @date 2017/4/22
 */
public abstract class AbstractGraphable
    implements Graphable {

    /**
     * 图单元所关联的对象
     */
    private Object object = null;

    /**
     * 当前图单元的ID
     */
    private int id = -1;

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public Object getObject() {
        return this.object;
    }

    @Override
    public void setObject(Object obj) {
        this.object = obj;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Graphable)) {
        	return false;
        }
        Graphable other = (Graphable) obj;
        return id == other.getId();
    }
}
