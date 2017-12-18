package nari.Dao.bundle.simple;

import java.util.List;

public class TransAction {
	
	private String action;
	
	private List<Object[]> params = null;
	
	public TransAction(String action,List<Object[]> params){
		this.action = action;
		this.params = params;
	}

	public final String getAction() {
		return action;
	}

	public final List<Object[]> getParams() {
		return params;
	}
}
