package nari.parameter.MainGridService.GetViewGeometry;

import java.io.Serializable;

import nari.parameter.bean.ClassCondition;
import nari.parameter.bean.SelfDefField;

public class GetViewGeometryRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5836965913254187345L;
	
	//窗口表示的视图坐标数组
	private double[] bbox = null;
	
	private ClassCondition[] classConditions = null;

	/**
	 * 自定义字段值
	 */
	private SelfDefField[] selfDefFields = null;

	public double[] getBbox() {
		return bbox;
	}

	public void setBbox(double[] bbox) {
		this.bbox = bbox;
	}

	public ClassCondition[] getClassConditions() {
		return classConditions;
	}

	public void setClassConditions(ClassCondition[] classConditions) {
		this.classConditions = classConditions;
	}

	public SelfDefField[] getSelfDefFields() {
		return selfDefFields;
	}

	public void setSelfDefFields(SelfDefField[] selfDefFields) {
		this.selfDefFields = selfDefFields;
	}

	public boolean validate(){
		if(bbox ==null || bbox.length == 0){
			return true;
		}
		return false;
	}
}
