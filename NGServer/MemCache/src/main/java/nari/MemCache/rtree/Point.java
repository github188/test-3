package nari.MemCache.rtree;

public class Point {
	
	public float x, y;

	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void set(Point other) {
		x = other.x;
		y = other.y;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	public int xInt() {
		return Math.round(x);
	}

	public int yInt() {
		return Math.round(y);
	}
}