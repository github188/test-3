package nari.parameter.bean;

import java.io.Serializable;

public class Pair implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8851018652394317222L;

	/**
	 * 键
	 */
	private String key = "";
	
	/**
	 * 值
	 */
	private String value = "";
	
	/**
	 * 比较条件
	 */
	private Operator op = null;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Operator getOp() {
		return op;
	}

	public void setOp(Operator op) {
		this.op = op;
	}

}
