package nari.SpatialIndex.handler;

public class DataObject {

	private String table;
	
	private String whereCaluse;
	
	private long minOid;
	
	private long maxOid;
	
	private long stepLength;

	public DataObject(String table,String whereCaluse,long stepLength){
		this.table = table;
		this.whereCaluse = whereCaluse;
		this.stepLength = stepLength;
		this.minOid = 0;
		this.maxOid = 0;
	}
	
	public DataObject(String table,String whereCaluse){
		this(table,whereCaluse,10000);
	}
	
	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getWhereCaluse() {
		return whereCaluse;
	}

	public void setWhereCaluse(String whereCaluse) {
		this.whereCaluse = whereCaluse;
	}

	public long getMinOid() {
		return minOid;
	}

	public void setMinOid(long minOid) {
		this.minOid = minOid;
	}

	public long getMaxOid() {
		return maxOid;
	}

	public void setMaxOid(long maxOid) {
		this.maxOid = maxOid;
	}

	public long getStepLength() {
		return stepLength;
	}

	public void setStepLength(long stepLength) {
		this.stepLength = stepLength;
	}
	
	public long nextOid(){
		return minOid + stepLength;
	}
	
	public void increateMinOid(){
		minOid = minOid + stepLength;
	}
}
