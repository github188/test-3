package nari.Dao.bundle.sequence;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import nari.Dao.bundle.simple.SimpleDao;

public class SequenceFactory {
	
	private static final ConcurrentHashMap<SimpleDao, SequenceFactory> map = new ConcurrentHashMap<SimpleDao,SequenceFactory>();
	
//	private SimpleDao dao = DbManager.getInstance();
	
	private SimpleDao dao = null;
	
	private SequenceFactory(SimpleDao dao){
		this.dao = dao;
	}
	
	public static SequenceFactory getFactory(SimpleDao dao){
		return map.putIfAbsent(dao, new SequenceFactory(dao));
	}
	
	public Sequence createSequence() throws SQLException{
		String sql = "select gisservertype from conf_serviceparam";
		Map<String,Object> map = dao.findMap(sql);
		if(map==null){
			return null;
		}
		String servtype = map.get("gisservertype")==null?"":String.valueOf(map.get("gisservertype"));
		if(servtype==null || "".equals(servtype)){
			return null;
		}
		
		Sequence seq = null;
		if(servtype.equalsIgnoreCase("soap")){
			seq = new SoapSequence(dao);
		}else if(servtype.equalsIgnoreCase("geostar")){
			seq = new GeostarSequence(dao);
		}
		return seq;
	}
}
