package nari.BaseService.bean;

public class ToolBoxModel {

	private String name = "";
	
	private String displayorder = "";
	
	private String modelid = "";
	
	private String typeParentid = "";
	
	private ToolBoxSymbol symbol= new ToolBoxSymbol();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayorder() {
		return displayorder;
	}

	public void setDisplayorder(String displayorder) {
		this.displayorder = displayorder;
	}

	public String getModelid() {
		return modelid;
	}

	public void setModelid(String modelid) {
		this.modelid = modelid;
	}

	public String getTypeParentid() {
		return typeParentid;
	}

	public void setTypeParentid(String typeParentid) {
		this.typeParentid = typeParentid;
	}

	public ToolBoxSymbol getSymbol() {
		return symbol;
	}

	public void setSymbol(ToolBoxSymbol symbol) {
		this.symbol = symbol;
	}
	
}
