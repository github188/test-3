package nari.parameter.MapService.GetVectorMap;

import java.io.Serializable;

import nari.parameter.bean.PSRCondition;

public class GetVectorMapRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2124796637633071382L;


	//窗口表示的视图坐标数组
	private double[] bbox = null;
	
	private PSRCondition[] classCondition = null;
	
	private String[] retrunField = null;
	
	private String requestExtend = "";



	public double[] getBbox() {
		return bbox;
	}



	public void setBbox(double[] bbox) {
		this.bbox = bbox;
	}



	public PSRCondition[] getClassCondition() {
		return classCondition;
	}



	public void setClassCondition(PSRCondition[] classCondition) {
		this.classCondition = classCondition;
	}



	public String[] getRetrunField() {
		return retrunField;
	}



	public void setRetrunField(String[] retrunField) {
		this.retrunField = retrunField;
	}



	public String getRequestExtend() {
		return requestExtend;
	}



	public void setRequestExtend(String requestExtend) {
		this.requestExtend = requestExtend;
	}



	public boolean validate(){
		if(bbox == null || bbox.length == 0 ){
			return true;
		}
		if(classCondition == null || classCondition.length == 0 ){
			return true;
		}	
		return false;
	}
}
