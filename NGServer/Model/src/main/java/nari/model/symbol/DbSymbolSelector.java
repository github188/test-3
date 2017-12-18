package nari.model.symbol;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nari.Dao.interfaces.DbAdaptor;
import nari.model.TableName;
import nari.model.bean.DefaultSymbolDef;
import nari.model.bean.SymbolDef;
import nari.model.device.AbstractDeviceModel;
import nari.model.device.filter.CriteriaQuery;
import nari.model.device.filter.Expression;
import nari.model.device.filter.Field;
import nari.model.device.filter.SQLCreator;

public class DbSymbolSelector extends AbstractDeviceModel implements SymbolSelector {
	
	private DbAdaptor dbAdapter;
	
	public DbSymbolSelector(DbAdaptor dbAdapter) {
		this.dbAdapter = dbAdapter;
	}
	
	@Override
	public Iterator<SymbolDef> search(String modelId) throws Exception {
		CriteriaQuery query = getEntryManager().getCriteriaBuilder().createQuery();
		query = query.select(query.getRoot().select(TableName.CONF_MODELSYMBOL));
		List<Field> fieldList = new ArrayList<Field>(); 
		
		fieldList.add(query.getRoot().get("modelid", String.class));
		fieldList.add(query.getRoot().get("symbolid", String.class));
		fieldList.add(query.getRoot().get("symbolvalue", String.class));
		fieldList.add(query.getRoot().get("symbolName", String.class));
		
		Field[] field = new Field[fieldList.size()];
		field = fieldList.toArray(field);
		query = query.field(field);
		
		Expression exp = getEntryManager().getCriteriaBuilder().equal(query.getRoot().get("modelid", String.class), modelId);

		query = query.where(exp);
		SQLCreator creator = (SQLCreator)query;
		String sql = creator.createSQL();
		
		List<Map<String,Object>> maps1 = null;
//		List<Map<String,Object>> maps2 = null;
		try {
			maps1 = dbAdapter.findAllMap(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		if(maps1==null || maps1.size()==0){
			return null;
		}
		
//		List<String> symbols = new ArrayList<String>();
//		
//		for(Map<String,Object> rs:maps1){
//			if(rs.get("symbolid")==null){
//				continue;
//			}
//			symbols.add(String.valueOf(rs.get("symbolid")));
//		}
		
		List<SymbolDef> defs = new ArrayList<SymbolDef>();
		
		for(int i=0;i<maps1.size();i++){
			defs.add(new DefaultSymbolDef(maps1.get(i)));
		}
		
		return defs.iterator();
	}

}
