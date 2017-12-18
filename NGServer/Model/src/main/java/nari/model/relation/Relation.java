package nari.model.relation;

// 存储一种关系
public class Relation {

	// 表CONF_MODELRELATION
	private int pModelId;
	private int rModelId;
	private RelationDef relationDef;
	private String relationField;
	private String relationTypeField;
	
	public int getPModelId() {
		return pModelId;
	}
	public void setPModelId(int pModelId) {
		this.pModelId = pModelId;
	}
	public int getRModelId() {
		return rModelId;
	}
	public void setRModelId(int rModelId) {
		this.rModelId = rModelId;
	}
	public RelationDef getRelationDef() {
		return relationDef;
	}
	public void setRelationDef(RelationDef relationDef) {
		this.relationDef = relationDef;
	}
	public String getRelationField() {
		return relationField;
	}
	public void setRelationField(String relationField) {
		this.relationField = relationField;
	}
	public String getRelationTypeField() {
		return relationTypeField;
	}
	public void setRelationTypeField(String relationTypeField) {
		this.relationTypeField = relationTypeField;
	}
	
	@Override
	public String toString() {
		return pModelId + "-" + rModelId + ": " + relationDef;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + pModelId;
		result = prime * result + rModelId;
		result = prime * result
				+ ((relationDef == null) ? 0 : relationDef.hashCode());
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
		Relation other = (Relation) obj;
		if (pModelId != other.pModelId)
			return false;
		if (rModelId != other.rModelId)
			return false;
		if (relationDef == null) {
			if (other.relationDef != null)
				return false;
		} else if (!relationDef.equals(other.relationDef))
			return false;
		return true;
	}
}
