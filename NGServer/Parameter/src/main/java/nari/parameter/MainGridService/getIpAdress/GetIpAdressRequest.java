package nari.parameter.MainGridService.getIpAdress;

import java.io.Serializable;

public class GetIpAdressRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1506445736640570798L;
	
	private String[] ipType = null;

	public String[] getIpType() {
		return ipType;
	}

	public void setIpType(String[] ipType) {
		this.ipType = ipType;
	}

}
