package nari.Dao.bundle.sequence;

import java.sql.SQLException;
import java.util.Map;

import nari.Dao.bundle.simple.SimpleDao;

public class GeostarSequence implements Sequence {

//	private SimpleDao dao = DbManager.getInstance();
	
	private SimpleDao dao = null;
	
	public GeostarSequence(SimpleDao dao){
		this.dao = dao;
	}
	
	public int getSequence(String tableName) throws SQLException{
		String sql = "select sequence_name from geostar_object_classes$ where table_name = ?";
		Map<String, Object> map = dao.findMap(sql, new String[] {tableName.toUpperCase()});
		String sequence="";
		if(map!=null){
			sequence = map.get("sequence_name") + ".NEXTVAL";
		}else{
			return -1;
		}
		sql = "select "+sequence+" from dual";
		map = dao.findMap(sql);
		return Integer.parseInt(String.valueOf(map.get("nextval")));
	}
}
