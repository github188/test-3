package nari.parameter.bean;

/*
 * 通过杆塔查询导线段的杆塔条件
 */
public class GTpairCondition {
	
	//起始杆塔设备id
	private String QSGTSBID = "";
	
	//终止杆塔设备id
	private String ZZGTSBID = "";
	
	//导线段颜色
	private String color = "";

	public String getQSGTSBID() {
		return QSGTSBID;
	}

	public void setQSGTSBID(String qSGTSBID) {
		QSGTSBID = qSGTSBID;
	}

	public String getZZGTSBID() {
		return ZZGTSBID;
	}

	public void setZZGTSBID(String zZGTSBID) {
		ZZGTSBID = zZGTSBID;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	
}
