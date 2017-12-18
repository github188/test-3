package nari.MemCache.matcher;

public class ChildNode extends Node {

	private String key;
	
	private Object value;
	
	private String op;
	
	public ChildNode(String key,Object value,String op) {
		super(1);
		this.key = key;
		this.value = value;
		this.op = op;
	}
	
	public String getKey() {
		return key;
	}
	
	public Object getValue() {
		return value;
	}
	
	public String getOp() {
		return op;
	}
}
