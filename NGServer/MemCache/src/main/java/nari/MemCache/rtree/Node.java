package nari.MemCache.rtree;

import java.io.Serializable;

public class Node implements Serializable {
	private static final long serialVersionUID = -2823316966528817396L;
	int nodeId = 0;
	float mbrMinX = Float.MAX_VALUE;
	float mbrMinY = Float.MAX_VALUE;
	float mbrMaxX = -Float.MAX_VALUE;
	float mbrMaxY = -Float.MAX_VALUE;

	float[] entriesMinX = null;
	float[] entriesMinY = null;
	float[] entriesMaxX = null;
	float[] entriesMaxY = null;

	int[] ids = null;
	int level;
	int entryCount;

	Node(int nodeId, int level, int maxNodeEntries) {
		this.nodeId = nodeId;
		this.level = level;
		entriesMinX = new float[maxNodeEntries];
		entriesMinY = new float[maxNodeEntries];
		entriesMaxX = new float[maxNodeEntries];
		entriesMaxY = new float[maxNodeEntries];
		ids = new int[maxNodeEntries];
	}

	void addEntry(float minX, float minY, float maxX, float maxY, int id) {
		ids[entryCount] = id;
		entriesMinX[entryCount] = minX;
		entriesMinY[entryCount] = minY;
		entriesMaxX[entryCount] = maxX;
		entriesMaxY[entryCount] = maxY;

		if (minX < mbrMinX)
			mbrMinX = minX;
		if (minY < mbrMinY)
			mbrMinY = minY;
		if (maxX > mbrMaxX)
			mbrMaxX = maxX;
		if (maxY > mbrMaxY)
			mbrMaxY = maxY;

		entryCount++;
	}

	int findEntry(float minX, float minY, float maxX, float maxY, int id) {
		for (int i = 0; i < entryCount; i++) {
			if (id == ids[i] && entriesMinX[i] == minX && entriesMinY[i] == minY && entriesMaxX[i] == maxX && entriesMaxY[i] == maxY) {
				return i;
			}
		}
		return -1;
	}

	void deleteEntry(int i) {
		int lastIndex = entryCount - 1;
		float deletedMinX = entriesMinX[i];
		float deletedMinY = entriesMinY[i];
		float deletedMaxX = entriesMaxX[i];
		float deletedMaxY = entriesMaxY[i];

		if (i != lastIndex) {
			entriesMinX[i] = entriesMinX[lastIndex];
			entriesMinY[i] = entriesMinY[lastIndex];
			entriesMaxX[i] = entriesMaxX[lastIndex];
			entriesMaxY[i] = entriesMaxY[lastIndex];
			ids[i] = ids[lastIndex];
		}
		entryCount--;

		recalculateMBRIfInfluencedBy(deletedMinX, deletedMinY, deletedMaxX,deletedMaxY);
	}

	void recalculateMBRIfInfluencedBy(float deletedMinX, float deletedMinY, float deletedMaxX, float deletedMaxY) {
		if (mbrMinX == deletedMinX || mbrMinY == deletedMinY || mbrMaxX == deletedMaxX || mbrMaxY == deletedMaxY) {
			recalculateMBR();
		}
	}

	void recalculateMBR() {
		mbrMinX = entriesMinX[0];
		mbrMinY = entriesMinY[0];
		mbrMaxX = entriesMaxX[0];
		mbrMaxY = entriesMaxY[0];

		for (int i = 1; i < entryCount; i++) {
			if (entriesMinX[i] < mbrMinX)
				mbrMinX = entriesMinX[i];
			if (entriesMinY[i] < mbrMinY)
				mbrMinY = entriesMinY[i];
			if (entriesMaxX[i] > mbrMaxX)
				mbrMaxX = entriesMaxX[i];
			if (entriesMaxY[i] > mbrMaxY)
				mbrMaxY = entriesMaxY[i];
		}
	}

	void reorganize(RTree rtree) {
		int countdownIndex = rtree.maxNodeEntries - 1;
		for (int index = 0; index < entryCount; index++) {
			if (ids[index] == -1) {
				while (ids[countdownIndex] == -1 && countdownIndex > index) {
					countdownIndex--;
				}
				entriesMinX[index] = entriesMinX[countdownIndex];
				entriesMinY[index] = entriesMinY[countdownIndex];
				entriesMaxX[index] = entriesMaxX[countdownIndex];
				entriesMaxY[index] = entriesMaxY[countdownIndex];
				ids[index] = ids[countdownIndex];
				ids[countdownIndex] = -1;
			}
		}
	}

	public int getEntryCount() {
		return entryCount;
	}

	public int getId(int index) {
		if (index < entryCount) {
			return ids[index];
		}
		return -1;
	}

	boolean isLeaf() {
		return (level == 1);
	}

	public int getLevel() {
		return level;
	}
}