package nari.parameter.bean;

import java.io.Serializable;

public class TypeCondition implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1751174637069264348L;

	/**
	 * 设备类型
	 */
	private String psrType = "";
	
	/**
	 * 设备类型的体系，classId和equId
	 */
	private String psrTypeSys = "equId";  //默认台账ID
	
	/**
	 * 条件组合
	 */
	private Pair[] pairs = null;
	
	/**
	 * 多个条件的组合关系
	 */
	private Link link = null;
	
	/**
	 * 返回的结果字段
	 */
	private String[] returnField = null;
	
	/**
	 * 自定义返回字段
	 */
	private SelfDefField[] selfDefFields = null;

	public Link getLink() {
		return link;
	}

	public void setLink(Link link) {
		this.link = link;
	}

	public String[] getReturnField() {
		return returnField;
	}

	public void setReturnField(String[] returnField) {
		this.returnField = returnField;
	}

	public Pair[] getPairs() {
		return pairs;
	}

	public void setPairs(Pair[] pairs) {
		this.pairs = pairs;
	}

	public String getPsrType() {
		return psrType;
	}

	public void setPsrType(String psrType) {
		this.psrType = psrType;
	}

	public SelfDefField[] getSelfDefFields() {
		return selfDefFields;
	}

	public void setSelfDefFields(SelfDefField[] selfDefFields) {
		this.selfDefFields = selfDefFields;
	}

	public String getPsrTypeSys() {
		return psrTypeSys;
	}

	public void setPsrTypeSys(String psrTypeSys) {
		this.psrTypeSys = psrTypeSys;
	}

}
