package nari.model.relation;

public class RelationDef {
	
	private int relationId;
	private String relationName;
	private String describe;
	
	public RelationDef(int relationId, String relationName, String describe) {
		this.relationId = relationId;
		this.relationName = relationName;
		this.describe = describe;
	}
	
	public int getRelationId() {
		return relationId;
	}
	
	public void setRelationId(int relationId) {
		this.relationId = relationId;
	}
	
	public String getRelationName() {
		return relationName;
	}
	
	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}
	
	public String getDescribe() {
		return describe;
	}
	
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	
	@Override
	public String toString() {
		return relationId + " " + relationName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + relationId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RelationDef other = (RelationDef) obj;
		if (relationId != other.relationId)
			return false;
		return true;
	}
}
