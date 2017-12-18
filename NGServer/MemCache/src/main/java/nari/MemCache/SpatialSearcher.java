package nari.MemCache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nari.MemCache.matcher.QueryMatcher;

public class SpatialSearcher extends AbstractSearcher {

	@Override
	protected Pointer[] doSearch(QueryMatcher matcher, IndexCluster indexCluster) throws Exception {
		if(matcher.getPolygon()==null){
			return null;
		}
		
		FieldIndex index = indexCluster.getIndex(matcher.getSpatialField());
		if(index==null){
			return null;
		}
		
		Pointer[] ptrs = index.get(matcher.getPolygon());
		
		Pointer[] result = null;
		
		List<QueryMatcher> matchers = matcher.getMatchers();
		if(matchers!=null && matchers.size()>0){
			List<Pointer> list = new ArrayList<Pointer>();
			for(Pointer ptr:ptrs){
				
				boolean[] matchArr = new boolean[matchers.size()];
				Arrays.fill(matchArr, false);
				
				int i = 0;
				
				for(QueryMatcher m:matchers){
					if(m.getOp().equalsIgnoreCase("in")){
						String[] vals = (String[])m.getValue();
						for(String v:vals){
							if(String.valueOf(ptr.getFieldValue(m.getKey(),null)).equals(v)){
								matchArr[i++] = true;
								break;
							}
						}
					}else if(m.getOp().equalsIgnoreCase("=")){
						if(String.valueOf(ptr.getFieldValue(m.getKey(),null)).equals(m.getValue())){
							matchArr[i++] = true;
						}
					}
				}
				
				boolean match = true;
				for(boolean b:matchArr){
					match = match & b;
					if(!match){
						break;
					}
				}
				
				if(match){
					list.add(ptr);
				}
			}
			result = new Pointer[list.size()];
			result = list.toArray(result);
		}else{
			result = ptrs;
		}
		
		return result;
	}

	@Override
	public Pointer[] doPreciseSearch(QueryMatcher matcher, IndexCluster indexCluster) throws Exception {
		if(matcher.getPolygon()==null){
			return null;
		}
		
		FieldIndex index = indexCluster.getIndex(matcher.getSpatialField());
		if(index==null){
			return null;
		}
		
		Pointer[] ptrs = index.get(matcher.getPolygon(),true);
		Pointer[] result = null;
		
		List<QueryMatcher> matchers = matcher.getMatchers();
		if(matchers!=null && matchers.size()>0){
			List<Pointer> list = new ArrayList<Pointer>();
			for(Pointer ptr:ptrs){
				
				boolean[] matchArr = new boolean[matchers.size()];
				Arrays.fill(matchArr, false);
				
				int i = 0;
				
				for(QueryMatcher m:matchers){
					if(m.getOp().equalsIgnoreCase("in")){
						String[] vals = (String[])m.getValue();
						for(String v:vals){
							if(String.valueOf(ptr.getFieldValue(m.getKey(),null)).equals(v)){
								matchArr[i++] = true;
								break;
							}
						}
					}else if(m.getOp().equalsIgnoreCase("=")){
						if(String.valueOf(ptr.getFieldValue(m.getKey(),null)).equals(m.getValue())){
							matchArr[i++] = true;
						}
					}
				}
				
				boolean match = true;
				for(boolean b:matchArr){
					match = match & b;
					if(!match){
						break;
					}
				}
				
				if(match){
					list.add(ptr);
				}
			}
			result = new Pointer[list.size()];
			result = list.toArray(result);
		}else{
			result = ptrs;
		}
		
		return result;
	}
}
