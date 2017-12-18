package nari.SpatialIndex.loader;

import nari.SpatialIndex.index.IndexAttribute;

public class GridIndexLoader implements IndexLoader {

	@Override
	public Disk loadStorage(IndexAttribute attribute) throws Exception {
		Disk disk = new PhysicsDisk(null,attribute.getIndexPath());
		
		return disk;
	}

}
