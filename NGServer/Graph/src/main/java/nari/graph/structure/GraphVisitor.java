package nari.graph.structure;

/**
 * 图访问者
 * 
 * 网络访问者用于访问当前元素
 * 相当于一个回调函数
 * 
 * @author birderyu
 *
 */
public interface GraphVisitor {
	
	int PASS_AND_CONTINUE = 1;
    int PASS_AND_STOP = 2;
    int FAIL_QUERY = 0;
	
	int visit(Graphable component);
}
