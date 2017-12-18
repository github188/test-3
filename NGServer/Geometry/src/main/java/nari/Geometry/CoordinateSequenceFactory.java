package nari.Geometry;

import java.io.Serializable;

public interface CoordinateSequenceFactory extends Serializable{

	public CoordinateSequence createSequence(Coordinate[] coords);
	
	public CoordinateSequence createSequence(double[] coords);
	
	public CoordinateSequence createSequence(Geometry geometry);
}
