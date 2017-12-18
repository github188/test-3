package nari.Geometry;

import java.io.Serializable;

public interface CoordinateSequence extends Serializable{

	public abstract Coordinate getCoordinate(int index);

	public abstract Coordinate getCoordinateCopy(int index);

	public abstract void getCoordinate(int index, Coordinate coord);

	public abstract double getX(int index);

	public abstract double getY(int index);

	public abstract int size();

	public abstract Coordinate[] toCoordinateArray();

}
