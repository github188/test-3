package nari.MemCache;


public class PointerCluster {
	
//	private Pointer[][] ptrCluster;
//	
	private Pointer[] ptrArr;
//	
	private int size;
//	
	private int clusterIndex = 0;
	
//	private BTree<Integer, Pointer> tree;
	
	public void init(int size) throws Exception{
//		this.size = size;
//		if(size<=0){
//			throw new IllegalArgumentException("size must gretter than 0");
//		}
		this.size = size;
		ptrArr = new Pointer[size];
//		
//		ptrCluster = new Pointer[10][size];
//		ptrCluster[clusterIndex] = ptrArr;
		
		
//		tree = new BTree<Integer, Pointer>();
	}
	
	public void addPointer(int ptrId,Pointer ptr) {
//		ptrArr[ptrId-clusterIndex*size] = ptr;
//		if(ptrId==size-1){
//			ptrArr = new Pointer[size];
//			ptrCluster[clusterIndex+1] = ptrArr;
//			clusterIndex++;
//			if(clusterIndex==10){
//				Pointer[][] newPtrCluster = new Pointer[ptrCluster.length+5][size];
//				System.arraycopy(ptrCluster, 0, newPtrCluster, 0, ptrCluster.length);
//				ptrCluster = newPtrCluster;
//			}
//		}
		
//		tree.put(ptrId, ptr);
		
		ptrArr[ptrId] = ptr;
		clusterIndex++;
		
		if(clusterIndex%size==0){
			Pointer[] newPtrArr = new Pointer[ptrArr.length+100];
			System.arraycopy(ptrArr, 0, newPtrArr, 0, ptrArr.length);
			ptrArr = null;
			ptrArr = newPtrArr;
		}
		
	}
	
	public Pointer getPointer(int ptrId) {
//		int clusterIndex = ptrId/size;
//		int index = ptrId%size;
//		return ptrCluster[clusterIndex][index];
		
//		return tree.get(ptrId);
		return ptrArr[ptrId];
	}
	
	public void removePointer(int ptrId){
		ptrArr[ptrId] = null;
	}
	
}
