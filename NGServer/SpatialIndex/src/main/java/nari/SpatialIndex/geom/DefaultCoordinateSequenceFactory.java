package nari.SpatialIndex.geom;

public class DefaultCoordinateSequenceFactory implements CoordinateSequenceFactory {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2822982158756372102L;

	@Override
	public CoordinateSequence createSequence(Coordinate[] coords) {
		return new DefaultCoordinateSequence(coords);
	}

	@Override
	public CoordinateSequence createSequence(double[] coords) {
		return new DefaultCoordinateSequence(coords);
	}

	@Override
	public CoordinateSequence createSequence(Geometry geometry) {
		return new DefaultCoordinateSequence(geometry.getCoordinates());
	}

}
