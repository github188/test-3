package nari.MemCache.loader;

public class CacheParameter {

	public String modelId = "";
	
	public String modelName = "";
	
	public String modelAlias = "";
	
	public String dydj;
	
	public String filter;
	
	public int count = 0;
	
//	public double ox;
//	
//	public double oy;
//	
//	public boolean isOffsetValid;
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj){
			return true;
		}
		if(obj==null || !(obj instanceof CacheParameter)){
			return false;
		}
		
		CacheParameter d = (CacheParameter)obj;
		
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
