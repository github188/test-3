package nari.MemCache;

import gnu.trove.procedure.TIntProcedure;

import java.util.ArrayList;
import java.util.List;

import nari.Geometry.CoordinateSequence;
import nari.Geometry.DefaultCoordinateSequence;
import nari.Geometry.DefaultPolygon;
import nari.Geometry.Envelope;
import nari.Geometry.Geometry;
import nari.Geometry.GeometryBuilder;
import nari.Logger.LoggerManager;
import nari.MemCache.matcher.QueryPolygon;
import nari.MemCache.rtree.RTree;
import nari.MemCache.rtree.Rectangle;

public class SpatialFieldIndex extends AbstractFieldIndex {

	private nari.Logger.Logger logger = LoggerManager.getLogger(this.getClass());
	
	private FieldType fieldType;
	
	private String indexField;
	
	private RTree tree = null;
	
//	private final List<Pointer> ptrList = new ArrayList<Pointer>();
	
	public SpatialFieldIndex(String indexField,FieldType fieldType,PointerCluster ptrCluster) {
		super(ptrCluster);
		this.indexField = indexField;
		this.fieldType = fieldType;
	}
	
	@Override
	protected Pointer[] doGet(Object key,final boolean precise) throws Exception {
		
		final QueryPolygon polygon = (QueryPolygon)key;
		
		Rectangle rect = new Rectangle((float)polygon.getMinX(),(float)polygon.getMinY(),(float)polygon.getMaxX(),(float)polygon.getMaxY());
		
		final List<Pointer> result = new ArrayList<Pointer>();
		
		tree.intersects(rect, new TIntProcedure() {
			
			@Override
			public boolean execute(int ptrId) {
				if(precise){
					Pointer ptr = getPointerCluster().getPointer(ptrId);
					Object val = null;
					try {
						val = ptr.getFieldValue("shape", null);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					if(val!=null && val.getClass()==Shape.class){
						Shape shape = (Shape)val;
						Geometry g = GeometryBuilder.getBuilder().buildGeometry(shape.getType(), shape.getElementInfo(), shape.getCoordinates());
						
						CoordinateSequence seq = new DefaultCoordinateSequence(polygon.getCoordinates());
						Geometry target = new DefaultPolygon(seq);
						if(g.intersects(target)){
							result.add(ptr);
						}
					}
				}else{
					result.add(getPointerCluster().getPointer(ptrId));
				}
				return true;
			}
		});
		
		Pointer[] arr = new Pointer[result.size()];
		arr = result.toArray(arr);
		
		result.clear();
		return arr;
	}

	@Override
	public IndexType getIndexType() throws Exception {
		return IndexType.SPATIAL;
	}

	@Override
	protected String getField() throws Exception {
		return indexField;
	}

	@Override
	public FieldType getFieldType() throws Exception {
		return fieldType;
	}

	@Override
	protected void doAdd(Object key, Pointer ptr) throws Exception {
		Shape geom = (Shape)key;
		
		Rectangle rect = new Rectangle((float)geom.getMinX(),(float)geom.getMinY(),(float)geom.getMaxX(),(float)geom.getMaxY());
//		ptrList.add(ptr.getPointerId(),ptr);
		tree.add(rect, ptr.getPointerId());
	}
	
	@Override
	protected void doModify(String keyField,Object key, Pointer ptr) throws Exception {
		Geometry geom = (Geometry)key;
		Envelope env = geom.getEnvelope();
		
		Rectangle rect = new Rectangle((float)env.getMinX(),(float)env.getMinY(),(float)env.getMaxX(),(float)env.getMinY());
		
		tree.delete(rect, ptr.getPointerId());
//		getPointerCluster().removePointer(ptr.getPointerId());
		
//		ptrList.add(ptr.getPointerId(),ptr);
//		getPointerCluster().addPointer(ptr.getPointerId(), ptr);
		tree.add(rect, ptr.getPointerId());
	}

	@Override
	protected void doRemove(String keyField,Object key, Pointer ptr) throws Exception {
		Geometry geom = (Geometry)key;
		Envelope env = geom.getEnvelope();
		
		Rectangle rect = new Rectangle((float)env.getMinX(),(float)env.getMinY(),(float)env.getMaxX(),(float)env.getMinY());
		tree.delete(rect, ptr.getPointerId());
//		ptrList.remove(ptr.getPointerId());
	}

	@Override
	protected boolean doInit() throws Exception {
		tree = new RTree();
		tree.init(null);
		return true;
	}

	@Override
	protected boolean doStart() throws Exception {
		return true;
	}

	@Override
	protected boolean doStop() throws Exception {
		return true;
	}

	@Override
	protected void doPersistence() throws Exception {
		
	}
	
//	public static void main(String[] args) {
//		BTree<Integer,Integer> b = new BTree<Integer,Integer>();
//		Integer[] arr = new Integer[4000000];
//		for(int i=0;i<4000000;i++){
////			b.put(i,i);
//			arr[i] = i;
//		}
//		logger.info("start");
//		long s = System.currentTimeMillis();
//		
//		for(int i=0;i<4000000;i++){
////			b.get(i);
//			Integer v = arr[i];
//		}
//		
//		long e = System.currentTimeMillis();
//		logger.info(e-s);
//		
//		
//		
//	}
}
