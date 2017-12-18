package nari.MemCache.rtree;

public class Rectangle {

	public float minX, minY, maxX, maxY;

	public Rectangle() {
		minX = Float.MAX_VALUE;
		minY = Float.MAX_VALUE;
		maxX = -Float.MAX_VALUE;
		maxY = -Float.MAX_VALUE;
	}

	public Rectangle(float x1, float y1, float x2, float y2) {
		set(x1, y1, x2, y2);
	}

	public void set(float x1, float y1, float x2, float y2) {
		minX = Math.min(x1, x2);
		maxX = Math.max(x1, x2);
		minY = Math.min(y1, y2);
		maxY = Math.max(y1, y2);
	}

	public void set(Rectangle r) {
		minX = r.minX;
		minY = r.minY;
		maxX = r.maxX;
		maxY = r.maxY;
	}

	public Rectangle copy() {
		return new Rectangle(minX, minY, maxX, maxY);
	}

	public boolean edgeOverlaps(Rectangle r) {
		return minX == r.minX || maxX == r.maxX || minY == r.minY || maxY == r.maxY;
	}

	public boolean intersects(Rectangle r) {
		return maxX >= r.minX && minX <= r.maxX && maxY >= r.minY && minY <= r.maxY;
	}

	static public boolean intersects(float r1MinX, float r1MinY, float r1MaxX, float r1MaxY, float r2MinX, float r2MinY, float r2MaxX, float r2MaxY) {
		return r1MaxX >= r2MinX && r1MinX <= r2MaxX && r1MaxY >= r2MinY && r1MinY <= r2MaxY;
	}

	public boolean contains(Rectangle r) {
		return maxX >= r.maxX && minX <= r.minX && maxY >= r.maxY && minY <= r.minY;
	}

	static public boolean contains(float r1MinX, float r1MinY, float r1MaxX, float r1MaxY, float r2MinX, float r2MinY, float r2MaxX, float r2MaxY) {
		return r1MaxX >= r2MaxX && r1MinX <= r2MinX && r1MaxY >= r2MaxY && r1MinY <= r2MinY;
	}

	public boolean containedBy(Rectangle r) {
		return r.maxX >= maxX && r.minX <= minX && r.maxY >= maxY && r.minY <= minY;
	}

	public float distance(Point p) {
		float distanceSquared = 0;

		float temp = minX - p.x;
		if (temp < 0) {
			temp = p.x - maxX;
		}

		if (temp > 0) {
			distanceSquared += (temp * temp);
		}

		temp = minY - p.y;
		if (temp < 0) {
			temp = p.y - maxY;
		}

		if (temp > 0) {
			distanceSquared += (temp * temp);
		}

		return (float) Math.sqrt(distanceSquared);
	}

	static public float distance(float minX, float minY, float maxX, float maxY, float pX, float pY) {
		return (float) Math.sqrt(distanceSq(minX, minY, maxX, maxY, pX, pY));
	}

	static public float distanceSq(float minX, float minY, float maxX, float maxY, float pX, float pY) {
		float distanceSqX = 0;
		float distanceSqY = 0;

		if (minX > pX) {
			distanceSqX = minX - pX;
			distanceSqX *= distanceSqX;
		} else if (pX > maxX) {
			distanceSqX = pX - maxX;
			distanceSqX *= distanceSqX;
		}

		if (minY > pY) {
			distanceSqY = minY - pY;
			distanceSqY *= distanceSqY;
		} else if (pY > maxY) {
			distanceSqY = pY - maxY;
			distanceSqY *= distanceSqY;
		}

		return distanceSqX + distanceSqY;
	}

	public float distance(Rectangle r) {
		float distanceSquared = 0;
		float greatestMin = Math.max(minX, r.minX);
		float leastMax = Math.min(maxX, r.maxX);
		if (greatestMin > leastMax) {
			distanceSquared += ((greatestMin - leastMax) * (greatestMin - leastMax));
		}
		greatestMin = Math.max(minY, r.minY);
		leastMax = Math.min(maxY, r.maxY);
		if (greatestMin > leastMax) {
			distanceSquared += ((greatestMin - leastMax) * (greatestMin - leastMax));
		}
		return (float) Math.sqrt(distanceSquared);
	}

	public float enlargement(Rectangle r) {
		float enlargedArea = (Math.max(maxX, r.maxX) - Math.min(minX, r.minX)) * (Math.max(maxY, r.maxY) - Math.min(minY, r.minY));

		return enlargedArea - area();
	}

	static public float enlargement(float r1MinX, float r1MinY, float r1MaxX, float r1MaxY, float r2MinX, float r2MinY, float r2MaxX, float r2MaxY) {
		float r1Area = (r1MaxX - r1MinX) * (r1MaxY - r1MinY);

		if (r1Area == Float.POSITIVE_INFINITY) {
			return 0;
		}

		if (r2MinX < r1MinX)
			r1MinX = r2MinX;
		if (r2MinY < r1MinY)
			r1MinY = r2MinY;
		if (r2MaxX > r1MaxX)
			r1MaxX = r2MaxX;
		if (r2MaxY > r1MaxY)
			r1MaxY = r2MaxY;

		float r1r2UnionArea = (r1MaxX - r1MinX) * (r1MaxY - r1MinY);

		if (r1r2UnionArea == Float.POSITIVE_INFINITY) {
			return Float.POSITIVE_INFINITY;
		}
		return r1r2UnionArea - r1Area;
	}

	public float area() {
		return (maxX - minX) * (maxY - minY);
	}

	static public float area(float minX, float minY, float maxX, float maxY) {
		return (maxX - minX) * (maxY - minY);
	}

	public void add(Rectangle r) {
		if (r.minX < minX)
			minX = r.minX;
		if (r.maxX > maxX)
			maxX = r.maxX;
		if (r.minY < minY)
			minY = r.minY;
		if (r.maxY > maxY)
			maxY = r.maxY;
	}

	public void add(Point p) {
		if (p.x < minX)
			minX = p.x;
		if (p.x > maxX)
			maxX = p.x;
		if (p.y < minY)
			minY = p.y;
		if (p.y > maxY)
			maxY = p.y;
	}

	public Rectangle union(Rectangle r) {
		Rectangle union = this.copy();
		union.add(r);
		return union;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(this.maxX);
		result = prime * result + Float.floatToIntBits(this.maxY);
		result = prime * result + Float.floatToIntBits(this.minX);
		result = prime * result + Float.floatToIntBits(this.minY);
		return result;
	}
	
	@Override
	public boolean equals(Object o) {
		boolean equals = false;
		if (o instanceof Rectangle) {
			Rectangle r = (Rectangle) o;
			if (minX == r.minX && minY == r.minY && maxX == r.maxX && maxY == r.maxY) {
				equals = true;
			}
		}
		return equals;
	}

	public boolean sameObject(Object o) {
		return super.equals(o);
	}

	@Override
	public String toString() {
		return "(" + minX + ", " + minY + "), (" + maxX + ", " + maxY + ")";
	}

	public float width() {
		return maxX - minX;
	}

	public float height() {
		return maxY - minY;
	}

	public float aspectRatio() {
		return width() / height();
	}

	public Point centre() {
		return new Point((minX + maxX) / 2, (minY + maxY) / 2);
	}

}