package nari.MemCache.matcher;

import java.util.ArrayList;
import java.util.List;

public class QueryMatcher {
	
	private QueryPolygon polygon;
	
	private String spaialField;
	
	private List<QueryMatcher> matchers = new ArrayList<QueryMatcher>();
	
	private boolean isLeaf = false;
	
	private String key;
	
	private Object value;
	
	private String op;
	
	public QueryMatcher() {
		
	}
	
	public QueryMatcher(String key,Object value,String op) {
		this.key = key;
		this.value = value;
		this.op = op;
		this.isLeaf = true;
		matchers.add(this);
	}
	
	public QueryMatcher(String spaialField,QueryPolygon polygon) {
		this.spaialField = spaialField;
		this.polygon = polygon;
		this.isLeaf = true;
	}
	
	public QueryMatcher and(QueryMatcher matcher){
		matchers.add(matcher);
		return this;
	}
	
	public QueryMatcher spaial(String spaialField,QueryPolygon polygon){
		this.polygon = polygon;
		this.spaialField = spaialField;
		return this;
	}
	
	public QueryPolygon getPolygon(){
		return polygon;
	}
	
	public String getSpatialField(){
		return spaialField;
	}
	
	public boolean isLeaf(){
		return isLeaf;
	}
	
	public String getKey(){
		return key;
	}
	
	public Object getValue(){
		return value;
	}
	
	public String getOp(){
		return op;
	}
	
	public List<QueryMatcher> getMatchers(){
		return matchers;
	}
	
}
