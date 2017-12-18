package nari.MainGridService.DbLinkBean;

import nari.Xml.bundle.annotation.XmlAttribute;

public class CharSet {

public static final CharSet NONE = new CharSet();
	
	@XmlAttribute(name="Method")
	private String Method = "";

	public String getMethod() {
		return Method;
	}

	public void setMethod(String method) {
		Method = method;
	}
	
	
}
