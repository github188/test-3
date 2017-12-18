package nari.Dao.bundle.bean;

import java.util.List;

import nari.Xml.bundle.annotation.NodeType;
import nari.Xml.bundle.annotation.XmlNode;

public class DataBase {
	
	public static final DataBase NONE = new DataBase();
	
//	@XmlNode(name="jdbcDataSource",type=NodeType.Normal,clazz=Jdbc.class)
//	private Jdbc jdbc = null;
//	
//	@XmlNode(name="jndiDataSource",type=NodeType.Normal,clazz=Jndi.class)
//	private Jndi jndi = null;
	
	@XmlNode(name="jdbcDataSource",type=NodeType.List,clazz=Jdbc.class)
	private List<Jdbc> jdbcList = null;
	
	@XmlNode(name="jndiDataSource",type=NodeType.List,clazz=Jndi.class)
	private List<Jndi> jndiList = null;

//	@XmlNode(name="jdbc",type=NodeType.List,clazz=Jdbc.class)
//	private List<Jdbc> jdbcList;
	

	public static DataBase getNone() {
		return NONE;
	}

	public List<Jdbc> getJdbcList() {
		return jdbcList;
	}

	public void setJdbcList(List<Jdbc> jdbcList) {
		this.jdbcList = jdbcList;
	}

	public List<Jndi> getJndiList() {
		return jndiList;
	}

	public void setJndiList(List<Jndi> jndiList) {
		this.jndiList = jndiList;
	}

}
