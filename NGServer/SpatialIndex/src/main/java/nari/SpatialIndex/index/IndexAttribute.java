package nari.SpatialIndex.index;

public class IndexAttribute {

	private String indexName;
	
	private String indexPath;
	
	private IndexType indexType = IndexType.GRID;
	
	public IndexAttribute(String indexName,String indexPath,IndexType indexType){
		this.indexName = indexName;
		this.indexPath = indexPath;
		this.indexType = indexType;
	}
	
	public IndexAttribute(String indexName,String indexPath){
		this.indexName = indexName;
		this.indexPath = indexPath;
	}
	
	public String name(){
		return indexName;
	}
	
	public String path(){
		return indexPath;
	}
	
	public IndexType getIndexType(){
		return indexType;
	}
	
	public void setIndexType(IndexType type){
		this.indexType = type;
	}
	
	public String getIndexPath(){
		return indexPath;
	}
	
	public static IndexAttribute attribute(String indexName,String indexPath){
		return new IndexAttribute(indexName,indexPath);
	}
	
	public static IndexAttribute attribute(String indexName,String indexPath,IndexType indexType){
		return new IndexAttribute(indexName,indexPath,indexType);
	}
}
