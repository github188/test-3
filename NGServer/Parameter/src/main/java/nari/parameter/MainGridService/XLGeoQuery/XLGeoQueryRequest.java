package nari.parameter.MainGridService.XLGeoQuery;

import java.io.Serializable;

import nari.parameter.bean.SelfDefField;

public class XLGeoQueryRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5653540591672243988L;
	
	private String[] SBID = null;
	
	private String[] DYDJ = null;
	
	private String[] OID = null;
	
	//可不传
	private String[] returnFeilds = null;
	
	/**
	 * 自定义返回字段
	 */
	private SelfDefField[] selfDefFields = null;


	public String[] getSBID() {
		return SBID;
	}

	public void setSBID(String[] sBID) {
		SBID = sBID;
	}

	public String[] getOID() {
		return OID;
	}

	public void setOID(String[] oID) {
		OID = oID;
	}

	public String[] getDYDJ() {
		return DYDJ;
	}

	public void setDYDJ(String[] dYDJ) {
		DYDJ = dYDJ;
	}

	public String[] getReturnFeilds() {
		return returnFeilds;
	}

	public void setReturnFeilds(String[] returnFeilds) {
		this.returnFeilds = returnFeilds;
	}

	public SelfDefField[] getSelfDefFields() {
		return selfDefFields;
	}

	public void setSelfDefFields(SelfDefField[] selfDefFields) {
		this.selfDefFields = selfDefFields;
	}

	//判断必要条件是否为空（sbId不能为空）
	public boolean validate(){
		if((SBID != null && SBID.length == 0)){
			return true;
		}
		return false;
	}
}
