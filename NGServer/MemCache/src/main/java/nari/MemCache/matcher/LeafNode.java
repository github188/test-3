package nari.MemCache.matcher;

public class LeafNode extends Node {

	private Node lNode;
	
	private Node rNode;
	
	private String op;
	
	public LeafNode(Node lNode,Node rNode,String op) {
		super(lNode.getDeep()>rNode.getDeep()?lNode.getDeep()+1:rNode.getDeep()+1);
		this.lNode = lNode;
		this.rNode = rNode;
		this.op = op;
	}
	
	public Node getLeft() {
		return lNode;
	}
	
	public Node getRight(){
		return rNode;
	}
	
	public String getOp(){
		return op;
	}
	
}
