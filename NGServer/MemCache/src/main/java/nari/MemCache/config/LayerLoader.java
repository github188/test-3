package nari.MemCache.config;

import nari.Xml.bundle.annotation.XmlAttribute;

public class LayerLoader {

	@XmlAttribute(name="strategy")
	private String strategy;
	
	@XmlAttribute(name="batchSize")
	private int batchSize;
	
	@XmlAttribute(name="tableFilter")
	private String tableFilter;
	
	@XmlAttribute(name="excludeTable")
	private String excludeTable;
	
	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}

	public String getTableFilter() {
		return tableFilter;
	}

	public void setTableFilter(String tableFilter) {
		this.tableFilter = tableFilter;
	}

	public String getExcludeTable() {
		return excludeTable;
	}

	public void setExcludeTable(String excludeTable) {
		this.excludeTable = excludeTable;
	}

}
