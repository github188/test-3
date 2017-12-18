package nari.model.device;

import nari.model.bean.FieldDef;
import nari.model.device.filter.CriteriaQuery;
import nari.model.device.filter.EntryManager;
import nari.model.device.filter.Expression;
import nari.model.device.filter.Order;

public interface GeneralModel {

	public FieldDef getFieldDef();
	
	public ResultSet search(String[] returnType,Expression exp,Order order) throws Exception;
	
	public ResultSet search(CriteriaQuery query) throws Exception;
	
	public EntryManager getEntryManager();
	
	public String getModelName();
	
}
