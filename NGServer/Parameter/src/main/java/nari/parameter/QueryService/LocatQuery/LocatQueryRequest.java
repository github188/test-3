package nari.parameter.QueryService.LocatQuery;

import java.io.Serializable;

import nari.parameter.bean.TypeCondition;

/**
 * 带条件的 属性查询
 * @author zwl
 *
 */
public class LocatQueryRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 493112663956521925L;

	private String token = "";
	
	/**
	 * 属性查询的条件
	 */
	private TypeCondition[] condition = null;
	
	/**
	 * 是否查询拓扑信息
	 */
	private boolean queryTopo = false;
	
	/**
	 * 是否查询空间信息
	 */
	private boolean queryGeometry = false;
	
	/**
	 * 是否查询版本表
	 */
	private boolean isVersion = false;
	
	//返回的GEOJSON格式
	private String ty = "";
	

	public String getTy() {
		return ty;
	}

	public void setTy(String ty) {
		this.ty = ty;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public TypeCondition[] getCondition() {
		return condition;
	}

	public void setCondition(TypeCondition[] condition) {
		this.condition = condition;
	}

	public boolean isVersion() {
		return isVersion;
	}

	public void setVersion(boolean isVersion) {
		this.isVersion = isVersion;
	}

	public boolean isQueryTopo() {
		return queryTopo;
	}

	public void setQueryTopo(boolean queryTopo) {
		this.queryTopo = queryTopo;
	}

	public boolean isQueryGeometry() {
		return queryGeometry;
	}

	public void setQueryGeometry(boolean queryGeometry) {
		this.queryGeometry = queryGeometry;
	}
	
	public boolean validate(){
		for(int i=0;i<condition.length;i++){
//			Class<?> cClass = condition[i].getClass();
//			cClass.getField("psrType") != null;
			if(condition[i] == null ){
				return false;
			}
		}
		if(condition.length == 0){
			return false;
		}
		return true;
	}
}
