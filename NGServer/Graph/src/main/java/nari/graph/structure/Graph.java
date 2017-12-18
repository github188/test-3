package nari.graph.structure;

import java.util.List;
import java.util.Set;

/**
 * 图的定义
 * 
 * @author birderyu
 *
 */
public interface Graph {

    /**
     * 获取节点的集合
     * 
     * @return
     */
    Set<Node> getNodes();
    
    /**
     * 设置边的集合
     * 
     * @param edges
     */
    void setNodes(Set<Node> nodes);
    
    /**
     * 获取边的集合
     * @return
     */
    Set<Edge> getEdges();
    
    /**
     * 设置边的集合
     * 
     * @param edges
     */
    void setEdges(Set<Edge> edges);
    
    /**
     * 查询，返回查询结果
     * 
     * @param visitor
     * @return
     */
    List<Graphable> query(GraphVisitor visitor);

    /**
     * 查询节点，返回查询结果
     * 
     * @param visitor
     * @return
     */
    List<Node> queryNodes(GraphVisitor visitor);

    /**
     * 查询边，返回查询结果
     * 
     * @param visitor
     * @return
     */
    List<Edge> queryEdges(GraphVisitor visitor);
    
    /**
     * 访问所有单元
     * 
     * @param visitor
     */
    void visit(GraphVisitor visitor);

    /**
     * 访问所有节点
     * 
     * @param visitor
     */
    void visitNodes(GraphVisitor visitor);

    /**
     * 访问所有边
     * 
     * @param visitor
     */
    void visitEdges(GraphVisitor visitor);

    /**
     * 获取度为特定值的节点的集合
     * 
     * @param degree 节点的度
     * @return
     */
    List<Node> getNodesOfDegree(int degree);
        
    /**
     * 获取属于这张网络的单元数量
     * 
     * @return
     */
    int size();
    
    /**
     * 为网络单元赋予一个ID
     * 
     * @return
     */
    int createComponentId();
}
