package nari.parameter.bean;

import java.io.Serializable;

public class QueryResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8840405061648019641L;

	/**
	 * 该设备类型的定义
	 */
	private PSRDef psrDef = null;
	
	/**
	 * 查询记录
	 */
	private QueryRecord[] records = null; 
	
	/**
	 * 记录数量
	 */
	private int count = 0;

	public PSRDef getPsrDef() {
		return psrDef;
	}

	public void setPsrDef(PSRDef psrDef) {
		this.psrDef = psrDef;
	}

	public QueryRecord[] getRecords() {
		return records;
	}

	public void setRecords(QueryRecord[] records) {
		this.records = records;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	
}
