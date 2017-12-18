package nari.graph.structure;

import java.util.Iterator;

/**
 * 图中的一个单元
 * 图中的单元仅有两种结构：节点（Node）和边（Edge），它们共同派生自Graphable结构
 *
 * @author birderyu
 * @date 2017/4/22
 */
public interface Graphable {

    /**
     * 获取当前网络单元的ID
     * @return
     */
    int getId();

    /**
     * 设置当前网络单元的ID
     * @param id
     */
    void setId(int id);

    /**
     * 获取当前网络单元关联的对象
     * @return
     */
    Object getObject();

    /**
     * 设置当前网络单元关联的对象
     * @param object
     */
    void setObject(Object object);

    /**
     * 获取当前网络单元所邻接的单元
     * @return
     */
    Iterator<Graphable> getRelated();

    /**
     * 是否与单元有邻接关系
     * @param component
     * @return
     */
    boolean relateTo(Graphable component);
    
    /**
     * 获取当前单元的权重
     * @return
     */
    int getWeight();

}
