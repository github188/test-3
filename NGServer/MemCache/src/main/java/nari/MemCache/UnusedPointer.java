package nari.MemCache;

import java.util.ArrayList;
import java.util.List;

public class UnusedPointer {
	
	private final List<MemRange> list = new ArrayList<MemRange>();
	
	public void use(Pointer ptr) throws Exception{
		list.add(new MemRange(ptr.getAddress(),ptr.getSize()));
	}
	
	public void free(Pointer ptr) throws Exception{
		list.remove(new MemRange(ptr.getAddress(),ptr.getSize()));
	}
	
}
