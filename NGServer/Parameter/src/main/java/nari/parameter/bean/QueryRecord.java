package nari.parameter.bean;

import java.io.Serializable;

public class QueryRecord implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8568055244261114348L;

	/**
	 * 字段信息集合
	 */
	private QueryField[] fields = null;

	/**
	 * 拓扑信息
	 */
	private TopoPair topo = null;
	
	/**
	 * 空间对象信息
	 */
	private GeometryPair geom = null;
	
	//符号信息
	private SymbolPair symbol = null;
	
	//MongoGeometry
	private String geoJson;
	
	public SymbolPair getSymbol() {
		return symbol;
	}

	public void setSymbol(SymbolPair symbol) {
		this.symbol = symbol;
	}


	public QueryField[] getFields() {
		return fields;
	}

	public void setFields(QueryField[] fields) {
		this.fields = fields;
	}

	public TopoPair getTopo() {
		return topo;
	}

	public void setTopo(TopoPair topo) {
		this.topo = topo;
	}

	public GeometryPair getGeom() {
		return geom;
	}

	public void setGeom(GeometryPair geom) {
		this.geom = geom;
	}

	public String getGeoJson() {
		return geoJson;
	}

	public void setGeoJson(String geoJson) {
		this.geoJson = geoJson;
	}
	
}
