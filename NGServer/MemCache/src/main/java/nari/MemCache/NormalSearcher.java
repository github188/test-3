package nari.MemCache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nari.MemCache.matcher.QueryMatcher;

public class NormalSearcher extends AbstractSearcher {

	@Override
	protected Pointer[] doSearch(QueryMatcher matcher, IndexCluster indexCluster) throws Exception {
		if(matcher.getMatchers()==null || matcher.getMatchers().size()==0){
			return null;
		}
		QueryMatcher mat = matcher.getMatchers().get(0);
		FieldIndex index = indexCluster.getIndex(mat.getKey());
		
		Object value = mat.getValue();
		
		List<Pointer> list = new ArrayList<Pointer>();
		
		if(mat.getOp().equalsIgnoreCase("in")){
			String[] arr = (String[])mat.getValue();
			
			for(String val:arr){
				Pointer[] ptrs = index.get(val);
				if(ptrs!=null && ptrs.length>0){
					list.addAll(Arrays.asList(ptrs));
				}
			}
		}else if(mat.getOp().equalsIgnoreCase("and")){
			Pointer[] ptrs = index.get(value);
			list.addAll(Arrays.asList(ptrs));
		}
		
		List<Pointer> resultList = new ArrayList<Pointer>();
		
		List<QueryMatcher> matchers = matcher.getMatchers();
		if(matchers!=null && matchers.size()>0){
			QueryMatcher m = null;
			for(Pointer ptr:list){
				boolean match = true;
				for(int i=1;i<matchers.size();i++){
					m = matchers.get(i);
					if(m.getOp().equalsIgnoreCase("in")){
						
						String[] vol = (String[])m.getValue();
						boolean b = false;
						for(String v:vol){
//							if(String.valueOf(ptr.getIndexKey(m.getKey())).equals(v)){
//								b = true;
//								break;
//							}
							if(String.valueOf(ptr.getFieldValue(m.getKey(),null)).equals(v)){
								b = true;
								break;
							}
						}
						if(!b){
							match = false;
						}
						break;
					}else if(m.getOp().equalsIgnoreCase("and")){
//						if(!String.valueOf(ptr.getIndexKey(m.getKey())).equals(m.getValue())){
//							match = false;
//							break;
//						}
						if(!String.valueOf(ptr.getFieldValue(m.getKey(),null)).equals(m.getValue())){
							match = false;
							break;
						}
					}
				}
				
				if(match){
					resultList.add(ptr);
				}
			}
		}
		
		Pointer[] result = new Pointer[resultList.size()];
		result = resultList.toArray(result);
		return result;
	}

	@Override
	public Pointer[] doPreciseSearch(QueryMatcher matcher, IndexCluster indexCluster) throws Exception {
		if(matcher.getMatchers()==null || matcher.getMatchers().size()==0){
			return null;
		}
		QueryMatcher mat = matcher.getMatchers().get(0);
		FieldIndex index = indexCluster.getIndex(mat.getKey());
		
		Object value = mat.getValue();
		
		List<Pointer> list = new ArrayList<Pointer>();
		
		if(mat.getOp().equalsIgnoreCase("in")){
			String[] arr = (String[])mat.getValue();
			
			for(String val:arr){
				Pointer[] ptrs = index.get(val);
				if(ptrs!=null && ptrs.length>0){
					list.addAll(Arrays.asList(ptrs));
				}
			}
		}else if(mat.getOp().equalsIgnoreCase("and")){
			Pointer[] ptrs = index.get(value);
			list.addAll(Arrays.asList(ptrs));
		}
		
		List<Pointer> resultList = new ArrayList<Pointer>();
		
		List<QueryMatcher> matchers = matcher.getMatchers();
		if(matchers!=null && matchers.size()>0){
			QueryMatcher m = null;
			for(Pointer ptr:list){
				boolean match = true;
				for(int i=1;i<matchers.size();i++){
					m = matchers.get(i);
					if(m.getOp().equalsIgnoreCase("in")){
						
						String[] vol = (String[])m.getValue();
						boolean b = false;
						for(String v:vol){
							if(String.valueOf(ptr.getFieldValue(m.getKey(),null)).equals(v)){
								b = true;
								break;
							}
						}
						if(!b){
							match = false;
						}
						break;
					}else if(m.getOp().equalsIgnoreCase("and")){
						if(!String.valueOf(ptr.getFieldValue(m.getKey(),null)).equals(m.getValue())){
							match = false;
							break;
						}
					}
				}
				
				if(match){
					resultList.add(ptr);
				}
			}
		}
		
		Pointer[] result = new Pointer[resultList.size()];
		result = resultList.toArray(result);
		return result;
	}

}
