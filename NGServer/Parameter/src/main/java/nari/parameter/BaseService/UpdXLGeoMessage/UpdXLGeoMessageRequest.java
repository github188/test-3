package nari.parameter.BaseService.UpdXLGeoMessage;
import java.io.Serializable;

public class UpdXLGeoMessageRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5046962216048439293L;
	
	private String[] XLOid = null;

	
	public String[] getXLOid() {
		return XLOid;
	}

	public void setXLOid(String[] xLOid) {
		XLOid = xLOid;
	}


}
