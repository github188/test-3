package nari.parameter.bean;

import java.io.Serializable;

public class SimpleQueryResult implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7727285964747189761L;

	/**
	 * 设备类型定义
	 */
	private PSRDef psrDef = null;
	
	/**
	 * OID结果集合
	 */
	private String[] records = null;
	
	/**
	 * 结果数量
	 */
	private int count = 0;

	public PSRDef getPsrDef() {
		return psrDef;
	}

	public void setPsrDef(PSRDef psrDef) {
		this.psrDef = psrDef;
	}

	public String[] getRecords() {
		return records;
	}

	public void setRecords(String[] records) {
		this.records = records;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
