package nari.SpatialIndex.searcher;

import java.util.List;

public class GridDataResultSet extends AbstractResultSet {

	private List<SerialObject> objs;
	
	public GridDataResultSet(List<SerialObject> objs){
		this.objs = objs;
	}

	@Override
	public int getResultCount() throws Exception {
		return objs==null?0:objs.size();
	}

	@Override
	public SerialObject getObject(int index) throws Exception {
		if(objs==null || index>=objs.size()){
			return null;
		}
		return objs.get(index);
	}
}
