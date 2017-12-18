package nari.MainGridService.DbLinkBean;

import nari.Dao.bundle.bean.DataBase;
import nari.Xml.bundle.annotation.XmlAttribute;

public class DBLink {
	public static final DBLink NONE = new DBLink();
	
	@XmlAttribute(name="Name")
	private String Name = "";
	
//	@XmlAttribute(name="Connecter")
//	private String connecter = "";
//	
//	@XmlAttribute(name="Connecteder")
//	private String Connecteder = "";
	
//	@XmlNode(name="jndiDataSource",type=NodeType.Normal,clazz=Jndi.class)
//	private Jndi jndi = null;
	
	public String getName() {
		return Name;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

//	public String getConnecter() {
//		return connecter;
//	}
//
//	public void setConnecter(String connecter) {
//		this.connecter = connecter;
//	}

//	public String getConnecteder() {
//		return Connecteder;
//	}
//
//	public void setConnecteder(String connecteder) {
//		Connecteder = connecteder;
//	}
	
	
}
