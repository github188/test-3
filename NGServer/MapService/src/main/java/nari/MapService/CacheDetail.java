package nari.MapService;

public class CacheDetail {

	public String modelId = "";
	
	public String modelName = "";
	
	public String dydj;
	
	public double ox;
	
	public double oy;
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj){
			return true;
		}
		if(obj==null || !(obj instanceof CacheDetail)){
			return false;
		}
		
		CacheDetail d = (CacheDetail)obj;
		
		if(d.modelId.equals(modelId) && d.dydj == dydj){
			return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return 31*dydj.hashCode() + modelId.hashCode();
	}
	
}
