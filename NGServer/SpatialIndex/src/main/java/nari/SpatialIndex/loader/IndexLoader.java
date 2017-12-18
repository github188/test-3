package nari.SpatialIndex.loader;

import nari.SpatialIndex.index.IndexAttribute;

public interface IndexLoader {

	public Disk loadStorage(IndexAttribute attribute) throws Exception;
}
