package nari.parameter.bean;

public class FilterExp {

	private String type;
	
	private String value;
	
	private String op;
	
	public FilterExp(String type,String value,String op){
		this.type = type;
		this.value = value;
		this.op = op;
	}
	
	public String getFilterString(){
		return type.toUpperCase()+","+value+","+op;
	}
}
