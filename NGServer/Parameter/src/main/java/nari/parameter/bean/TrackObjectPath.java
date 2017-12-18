package nari.parameter.bean;

public class TrackObjectPath {

	private TopoObjectId[] objectIds;
	
	private double lengths;

	public TopoObjectId[] getObjectIds() {
		return objectIds;
	}

	public void setObjectIds(TopoObjectId[] objectIds) {
		this.objectIds = objectIds;
	}

	public double getLengths() {
		return lengths;
	}

	public void setLengths(double lengths) {
		this.lengths = lengths;
	}
	
}
