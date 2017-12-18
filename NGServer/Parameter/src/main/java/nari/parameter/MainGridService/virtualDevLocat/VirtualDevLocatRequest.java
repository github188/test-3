package nari.parameter.MainGridService.virtualDevLocat;

import java.io.Serializable;

public class VirtualDevLocatRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7612039588109039645L;
	
	String[] sbId = null;

	public String[] getSbId() {
		return sbId;
	}

	public void setSbId(String[] sbId) {
		this.sbId = sbId;
	}
	
	public boolean validate(){
		if(sbId == null || sbId.length == 0){
			return true;
		}
		return false;
	}

}
