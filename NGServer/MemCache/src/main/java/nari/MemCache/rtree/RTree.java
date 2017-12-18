package nari.MemCache.rtree;

import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.stack.TIntStack;
import gnu.trove.stack.array.TIntArrayStack;

import java.io.Serializable;
import java.util.Properties;

import nari.MemCache.rtree.BuildProperties;
import nari.MemCache.rtree.Point;
import nari.MemCache.rtree.Rectangle;
import nari.MemCache.rtree.PriorityQueue;

public class RTree implements Serializable {
	
	private static final long serialVersionUID = 5946232781609920309L;
	
	private final static int DEFAULT_MAX_NODE_ENTRIES = 12;
	
	private final static int DEFAULT_MIN_NODE_ENTRIES = 6;
	
	int maxNodeEntries;
	
	int minNodeEntries;

	private TIntObjectHashMap<Node> nodeMap = new TIntObjectHashMap<Node>();

	private final static boolean INTERNAL_CONSISTENCY_CHECKING = false;

	private final static int ENTRY_STATUS_ASSIGNED = 0;
	
	private final static int ENTRY_STATUS_UNASSIGNED = 1;
	
	private byte[] entryStatus = null;
	
	private byte[] initialEntryStatus = null;

	private TIntStack parents = new TIntArrayStack();
	
	private TIntStack parentsEntry = new TIntArrayStack();

	private int treeHeight = 1;
	
	private int rootNodeId = 0;
	
	private int size = 0;

	private int highestUsedNodeId = rootNodeId;

	private TIntStack deletedNodeIds = new TIntArrayStack();

	public RTree() {
		
	}

	public void init(Properties props) {
		if (props == null) {
			maxNodeEntries = DEFAULT_MAX_NODE_ENTRIES;
			minNodeEntries = DEFAULT_MIN_NODE_ENTRIES;
		} else {
			maxNodeEntries = Integer.parseInt(props.getProperty("MaxNodeEntries", "0"));
			minNodeEntries = Integer.parseInt(props.getProperty("MinNodeEntries", "0"));

			if (maxNodeEntries < 2) {
				maxNodeEntries = DEFAULT_MAX_NODE_ENTRIES;
			}

			if (minNodeEntries < 1 || minNodeEntries > maxNodeEntries / 2) {
				minNodeEntries = maxNodeEntries / 2;
			}
		}

		entryStatus = new byte[maxNodeEntries];
		initialEntryStatus = new byte[maxNodeEntries];

		for (int i = 0; i < maxNodeEntries; i++) {
			initialEntryStatus[i] = ENTRY_STATUS_UNASSIGNED;
		}

		Node root = new Node(rootNodeId, 1, maxNodeEntries);
		nodeMap.put(rootNodeId, root);

	}

	public void add(Rectangle r, int id) {
		add(r.minX, r.minY, r.maxX, r.maxY, id, 1);

		size++;

		if (INTERNAL_CONSISTENCY_CHECKING) {
			checkConsistency();
		}
	}

	private void add(float minX, float minY, float maxX, float maxY, int id, int level) {
		Node n = chooseNode(minX, minY, maxX, maxY, level);
		Node newLeaf = null;

		if (n.entryCount < maxNodeEntries) {
			n.addEntry(minX, minY, maxX, maxY, id);
		} else {
			newLeaf = splitNode(n, minX, minY, maxX, maxY, id);
		}

		Node newNode = adjustTree(n, newLeaf);

		if (newNode != null) {
			int oldRootNodeId = rootNodeId;
			Node oldRoot = getNode(oldRootNodeId);

			rootNodeId = getNextNodeId();
			treeHeight++;
			Node root = new Node(rootNodeId, treeHeight, maxNodeEntries);
			root.addEntry(newNode.mbrMinX, newNode.mbrMinY, newNode.mbrMaxX, newNode.mbrMaxY, newNode.nodeId);
			root.addEntry(oldRoot.mbrMinX, oldRoot.mbrMinY, oldRoot.mbrMaxX, oldRoot.mbrMaxY, oldRoot.nodeId);
			nodeMap.put(rootNodeId, root);
		}
	}

	public boolean delete(Rectangle r, int id) {
		parents.clear();
		parents.push(rootNodeId);

		parentsEntry.clear();
		parentsEntry.push(-1);
		Node n = null;
		int foundIndex = -1;

		while (foundIndex == -1 && parents.size() > 0) {
			n = getNode(parents.peek());
			int startIndex = parentsEntry.peek() + 1;

			if (!n.isLeaf()) {
				boolean contains = false;
				for (int i = startIndex; i < n.entryCount; i++) {
					if (Rectangle.contains(n.entriesMinX[i], n.entriesMinY[i], n.entriesMaxX[i], n.entriesMaxY[i], r.minX, r.minY, r.maxX, r.maxY)) {
						parents.push(n.ids[i]);
						parentsEntry.pop();
						parentsEntry.push(i);
						parentsEntry.push(-1);
						contains = true;
						break;
					}
				}
				if (contains) {
					continue;
				}
			} else {
				foundIndex = n.findEntry(r.minX, r.minY, r.maxX, r.maxY, id);
			}

			parents.pop();
			parentsEntry.pop();
		}

		if (foundIndex != -1 && n != null) {
			n.deleteEntry(foundIndex);
			condenseTree(n);
			size--;
		}

		Node root = getNode(rootNodeId);
		while (root.entryCount == 1 && treeHeight > 1) {
			deletedNodeIds.push(rootNodeId);
			root.entryCount = 0;
			rootNodeId = root.ids[0];
			treeHeight--;
			root = getNode(rootNodeId);
		}

		if (size == 0) {
			root.mbrMinX = Float.MAX_VALUE;
			root.mbrMinY = Float.MAX_VALUE;
			root.mbrMaxX = -Float.MAX_VALUE;
			root.mbrMaxY = -Float.MAX_VALUE;
		}

		if (INTERNAL_CONSISTENCY_CHECKING) {
			checkConsistency();
		}

		return (foundIndex != -1);
	}

	public void nearest(Point p, TIntProcedure v, float furthestDistance) {
		Node rootNode = getNode(rootNodeId);

		float furthestDistanceSq = furthestDistance * furthestDistance;
		TIntArrayList nearestIds = new TIntArrayList();
		nearest(p, rootNode, furthestDistanceSq, nearestIds);

		nearestIds.forEach(v);
		nearestIds.reset();
	}

	private void createNearestNDistanceQueue(Point p, int count, PriorityQueue distanceQueue, float furthestDistance) {
		if (count <= 0) {
			return;
		}

		TIntStack parents = new TIntArrayStack();
		parents.push(rootNodeId);

		TIntStack parentsEntry = new TIntArrayStack();
		parentsEntry.push(-1);

		TIntArrayList savedValues = new TIntArrayList();
		float savedPriority = 0;

		float furthestDistanceSq = furthestDistance * furthestDistance;

		while (parents.size() > 0) {
			Node n = getNode(parents.peek());
			int startIndex = parentsEntry.peek() + 1;

			if (!n.isLeaf()) {
				boolean near = false;
				for (int i = startIndex; i < n.entryCount; i++) {
					if (Rectangle.distanceSq(n.entriesMinX[i], n.entriesMinY[i], n.entriesMaxX[i], n.entriesMaxY[i], p.x, p.y) <= furthestDistanceSq) {
						parents.push(n.ids[i]);
						parentsEntry.pop();
						parentsEntry.push(i); 
						parentsEntry.push(-1);
						near = true;
						break;
					}
				}
				if (near) {
					continue;
				}
			} else {
				for (int i = 0; i < n.entryCount; i++) {
					float entryDistanceSq = Rectangle.distanceSq(n.entriesMinX[i], n.entriesMinY[i], n.entriesMaxX[i], n.entriesMaxY[i], p.x, p.y);
					int entryId = n.ids[i];

					if (entryDistanceSq <= furthestDistanceSq) {
						distanceQueue.insert(entryId, entryDistanceSq);

						while (distanceQueue.size() > count) {
							int value = distanceQueue.getValue();
							float distanceSq = distanceQueue.getPriority();
							distanceQueue.pop();

							if (distanceSq == distanceQueue.getPriority()) {
								savedValues.add(value);
								savedPriority = distanceSq;
							} else {
								savedValues.reset();
							}
						}

						if (savedValues.size() > 0 && savedPriority == distanceQueue.getPriority()) {
							for (int svi = 0; svi < savedValues.size(); svi++) {
								distanceQueue.insert(savedValues.get(svi), savedPriority);
							}
							savedValues.reset();
						}

						if (distanceQueue.getPriority() < furthestDistanceSq && distanceQueue.size() >= count) {
							furthestDistanceSq = distanceQueue.getPriority();
						}
					}
				}
			}
			parents.pop();
			parentsEntry.pop();
		}
	}

	public void nearestNUnsorted(Point p, TIntProcedure v, int count, float furthestDistance) {
		PriorityQueue distanceQueue = new PriorityQueue(PriorityQueue.SORT_ORDER_DESCENDING);
		createNearestNDistanceQueue(p, count, distanceQueue, furthestDistance);

		while (distanceQueue.size() > 0) {
			v.execute(distanceQueue.getValue());
			distanceQueue.pop();
		}
	}

	public void nearestN(Point p, TIntProcedure v, int count, float furthestDistance) {
		PriorityQueue distanceQueue = new PriorityQueue(PriorityQueue.SORT_ORDER_DESCENDING);
		createNearestNDistanceQueue(p, count, distanceQueue, furthestDistance);
		distanceQueue.setSortOrder(PriorityQueue.SORT_ORDER_ASCENDING);

		while (distanceQueue.size() > 0) {
			v.execute(distanceQueue.getValue());
			distanceQueue.pop();
		}
	}

	public void intersects(Rectangle r, TIntProcedure v) {
		Node rootNode = getNode(rootNodeId);
		intersects(r, v, rootNode);
	}

	public void contains(Rectangle r, TIntProcedure v) {
		TIntStack parents = new TIntArrayStack();
		parents.push(rootNodeId);

		TIntStack parentsEntry = new TIntArrayStack();
		parentsEntry.push(-1);

		while (parents.size() > 0) {
			Node n = getNode(parents.peek());
			int startIndex = parentsEntry.peek() + 1;

			if (!n.isLeaf()) {
				boolean intersects = false;
				for (int i = startIndex; i < n.entryCount; i++) {
					if (Rectangle.intersects(r.minX, r.minY, r.maxX, r.maxY, n.entriesMinX[i], n.entriesMinY[i], n.entriesMaxX[i], n.entriesMaxY[i])) {
						parents.push(n.ids[i]);
						parentsEntry.pop();
						parentsEntry.push(i);
						parentsEntry.push(-1);
						intersects = true;
						break;
					}
				}
				if (intersects) {
					continue;
				}
			} else {
				for (int i = 0; i < n.entryCount; i++) {
					if (Rectangle.contains(r.minX, r.minY, r.maxX, r.maxY, n.entriesMinX[i], n.entriesMinY[i],n.entriesMaxX[i], n.entriesMaxY[i])) {
						if (!v.execute(n.ids[i])) {
							return;
						}
					}
				}
			}
			parents.pop();
			parentsEntry.pop();
		}
	}

	public int size() {
		return size;
	}

	public Rectangle getBounds() {
		Rectangle bounds = null;

		Node n = getNode(getRootNodeId());
		if (n != null && n.entryCount > 0) {
			bounds = new Rectangle();
			bounds.minX = n.mbrMinX;
			bounds.minY = n.mbrMinY;
			bounds.maxX = n.mbrMaxX;
			bounds.maxY = n.mbrMaxY;
		}
		return bounds;
	}

	public String getVersion() {
		return "RTree-" + BuildProperties.getVersion();
	}

	private int getNextNodeId() {
		int nextNodeId = 0;
		if (deletedNodeIds.size() > 0) {
			nextNodeId = deletedNodeIds.pop();
		} else {
			nextNodeId = 1 + highestUsedNodeId++;
		}
		return nextNodeId;
	}

	public Node getNode(int id) {
		return nodeMap.get(id);
	}

	public int getHighestUsedNodeId() {
		return highestUsedNodeId;
	}

	public int getRootNodeId() {
		return rootNodeId;
	}

	private Node splitNode(Node n, float newRectMinX, float newRectMinY,float newRectMaxX, float newRectMaxY, int newId) {
//		float initialArea = 0;
//		if (log.isDebugEnabled()) {
//			float unionMinX = Math.min(n.mbrMinX, newRectMinX);
//			float unionMinY = Math.min(n.mbrMinY, newRectMinY);
//			float unionMaxX = Math.max(n.mbrMaxX, newRectMaxX);
//			float unionMaxY = Math.max(n.mbrMaxY, newRectMaxY);
//
//			initialArea = (unionMaxX - unionMinX) * (unionMaxY - unionMinY);
//		}

		System.arraycopy(initialEntryStatus, 0, entryStatus, 0, maxNodeEntries);

		Node newNode = null;
		newNode = new Node(getNextNodeId(), n.level, maxNodeEntries);
		nodeMap.put(newNode.nodeId, newNode);

		pickSeeds(n, newRectMinX, newRectMinY, newRectMaxX, newRectMaxY, newId,newNode); 

		while (n.entryCount + newNode.entryCount < maxNodeEntries + 1) {
			if (maxNodeEntries + 1 - newNode.entryCount == minNodeEntries) {
				for (int i = 0; i < maxNodeEntries; i++) {
					if (entryStatus[i] == ENTRY_STATUS_UNASSIGNED) {
						entryStatus[i] = ENTRY_STATUS_ASSIGNED;

						if (n.entriesMinX[i] < n.mbrMinX)
							n.mbrMinX = n.entriesMinX[i];
						if (n.entriesMinY[i] < n.mbrMinY)
							n.mbrMinY = n.entriesMinY[i];
						if (n.entriesMaxX[i] > n.mbrMaxX)
							n.mbrMaxX = n.entriesMaxX[i];
						if (n.entriesMaxY[i] > n.mbrMaxY)
							n.mbrMaxY = n.entriesMaxY[i];

						n.entryCount++;
					}
				}
				break;
			}
			if (maxNodeEntries + 1 - n.entryCount == minNodeEntries) {
				for (int i = 0; i < maxNodeEntries; i++) {
					if (entryStatus[i] == ENTRY_STATUS_UNASSIGNED) {
						entryStatus[i] = ENTRY_STATUS_ASSIGNED;
						newNode.addEntry(n.entriesMinX[i], n.entriesMinY[i],n.entriesMaxX[i], n.entriesMaxY[i], n.ids[i]);
						n.ids[i] = -1;
					}
				}
				break;
			}

			pickNext(n, newNode);
		}

		n.reorganize(this);

		if (INTERNAL_CONSISTENCY_CHECKING) {
			Rectangle nMBR = new Rectangle(n.mbrMinX, n.mbrMinY, n.mbrMaxX,n.mbrMaxY);
//			if (!nMBR.equals(calculateMBR(n))) {
//				log.error("Error: splitNode old node MBR wrong");
//			}
			Rectangle newNodeMBR = new Rectangle(newNode.mbrMinX,newNode.mbrMinY, newNode.mbrMaxX, newNode.mbrMaxY);
//			if (!newNodeMBR.equals(calculateMBR(newNode))) {
//				log.error("Error: splitNode new node MBR wrong");
//			}
		}

//		if (log.isDebugEnabled()) {
//			float newArea = Rectangle.area(n.mbrMinX, n.mbrMinY, n.mbrMaxX,n.mbrMaxY)+ Rectangle.area(newNode.mbrMinX, newNode.mbrMinY,newNode.mbrMaxX, newNode.mbrMaxY);
//			float percentageIncrease = (100 * (newArea - initialArea))/ initialArea;
//			log.debug("Node " + n.nodeId + " split. New area increased by "+ percentageIncrease + "%");
//		}

		return newNode;
	}

	private void pickSeeds(Node n, float newRectMinX, float newRectMinY,float newRectMaxX, float newRectMaxY, int newId, Node newNode) {
		float maxNormalizedSeparation = -1;
		int highestLowIndex = -1;
		int lowestHighIndex = -1;

		if (newRectMinX < n.mbrMinX)
			n.mbrMinX = newRectMinX;
		if (newRectMinY < n.mbrMinY)
			n.mbrMinY = newRectMinY;
		if (newRectMaxX > n.mbrMaxX)
			n.mbrMaxX = newRectMaxX;
		if (newRectMaxY > n.mbrMaxY)
			n.mbrMaxY = newRectMaxY;

		float mbrLenX = n.mbrMaxX - n.mbrMinX;
		float mbrLenY = n.mbrMaxY - n.mbrMinY;

		float tempHighestLow = newRectMinX;
		int tempHighestLowIndex = -1;

		float tempLowestHigh = newRectMaxX;
		int tempLowestHighIndex = -1;

		for (int i = 0; i < n.entryCount; i++) {
			float tempLow = n.entriesMinX[i];
			if (tempLow >= tempHighestLow) {
				tempHighestLow = tempLow;
				tempHighestLowIndex = i;
			} else {
				float tempHigh = n.entriesMaxX[i];
				if (tempHigh <= tempLowestHigh) {
					tempLowestHigh = tempHigh;
					tempLowestHighIndex = i;
				}
			}
			float normalizedSeparation = mbrLenX == 0 ? 1: (tempHighestLow - tempLowestHigh) / mbrLenX;
//			if (normalizedSeparation > 1 || normalizedSeparation < -1) {
//				log.error("Invalid normalized separation X");
//			}

//			if (log.isDebugEnabled()) {
//				log.debug("Entry " + i + ", dimension X: HighestLow = "+ tempHighestLow + " (index " + tempHighestLowIndex+ ")" + ", LowestHigh = " + tempLowestHigh + " (index "+ tempLowestHighIndex + ", NormalizedSeparation = "+ normalizedSeparation);
//			}

			if (normalizedSeparation >= maxNormalizedSeparation) {
				highestLowIndex = tempHighestLowIndex;
				lowestHighIndex = tempLowestHighIndex;
				maxNormalizedSeparation = normalizedSeparation;
			}
		}

		tempHighestLow = newRectMinY;
		tempHighestLowIndex = -1;

		tempLowestHigh = newRectMaxY;
		tempLowestHighIndex = -1;

		for (int i = 0; i < n.entryCount; i++) {
			float tempLow = n.entriesMinY[i];
			if (tempLow >= tempHighestLow) {
				tempHighestLow = tempLow;
				tempHighestLowIndex = i;
			} else {
				float tempHigh = n.entriesMaxY[i];
				if (tempHigh <= tempLowestHigh) {
					tempLowestHigh = tempHigh;
					tempLowestHighIndex = i;
				}
			}

			float normalizedSeparation = mbrLenY == 0 ? 1: (tempHighestLow - tempLowestHigh) / mbrLenY;
//			if (normalizedSeparation > 1 || normalizedSeparation < -1) {
//				log.error("Invalid normalized separation Y");
//			}
//
//			if (log.isDebugEnabled()) {
//				log.debug("Entry " + i + ", dimension Y: HighestLow = "+ tempHighestLow + " (index " + tempHighestLowIndex+ ")" + ", LowestHigh = " + tempLowestHigh + " (index "+ tempLowestHighIndex + ", NormalizedSeparation = "+ normalizedSeparation);
//			}			
			if (normalizedSeparation >= maxNormalizedSeparation) {
				highestLowIndex = tempHighestLowIndex;
				lowestHighIndex = tempLowestHighIndex;
				maxNormalizedSeparation = normalizedSeparation;
			}
		}

		if (highestLowIndex == lowestHighIndex) {
			highestLowIndex = -1;
			float tempMinY = newRectMinY;
			lowestHighIndex = 0;
			float tempMaxX = n.entriesMaxX[0];

			for (int i = 1; i < n.entryCount; i++) {
				if (n.entriesMinY[i] < tempMinY) {
					tempMinY = n.entriesMinY[i];
					highestLowIndex = i;
				} else if (n.entriesMaxX[i] > tempMaxX) {
					tempMaxX = n.entriesMaxX[i];
					lowestHighIndex = i;
				}
			}
		}

		if (highestLowIndex == -1) {
			newNode.addEntry(newRectMinX, newRectMinY, newRectMaxX,newRectMaxY, newId);
		} else {
			newNode.addEntry(n.entriesMinX[highestLowIndex],n.entriesMinY[highestLowIndex],	n.entriesMaxX[highestLowIndex],n.entriesMaxY[highestLowIndex], n.ids[highestLowIndex]);
			n.ids[highestLowIndex] = -1;

			n.entriesMinX[highestLowIndex] = newRectMinX;
			n.entriesMinY[highestLowIndex] = newRectMinY;
			n.entriesMaxX[highestLowIndex] = newRectMaxX;
			n.entriesMaxY[highestLowIndex] = newRectMaxY;

			n.ids[highestLowIndex] = newId;
		}

		if (lowestHighIndex == -1) {
			lowestHighIndex = highestLowIndex;
		}

		entryStatus[lowestHighIndex] = ENTRY_STATUS_ASSIGNED;
		n.entryCount = 1;
		n.mbrMinX = n.entriesMinX[lowestHighIndex];
		n.mbrMinY = n.entriesMinY[lowestHighIndex];
		n.mbrMaxX = n.entriesMaxX[lowestHighIndex];
		n.mbrMaxY = n.entriesMaxY[lowestHighIndex];
	}

	private int pickNext(Node n, Node newNode) {
		float maxDifference = Float.NEGATIVE_INFINITY;
		int next = 0;
		int nextGroup = 0;

		maxDifference = Float.NEGATIVE_INFINITY;

//		if (log.isDebugEnabled()) {
//			log.debug("pickNext()");
//		}

		for (int i = 0; i < maxNodeEntries; i++) {
			if (entryStatus[i] == ENTRY_STATUS_UNASSIGNED) {

//				if (n.ids[i] == -1) {
//					log.error("Error: Node " + n.nodeId + ", entry " + i+ " is null");
//				}

				float nIncrease = Rectangle.enlargement(n.mbrMinX, n.mbrMinY,n.mbrMaxX, n.mbrMaxY, n.entriesMinX[i],n.entriesMinY[i], n.entriesMaxX[i], n.entriesMaxY[i]);
				float newNodeIncrease = Rectangle.enlargement(newNode.mbrMinX,newNode.mbrMinY, newNode.mbrMaxX, newNode.mbrMaxY,n.entriesMinX[i], n.entriesMinY[i], n.entriesMaxX[i],n.entriesMaxY[i]);

				float difference = Math.abs(nIncrease - newNodeIncrease);

				if (difference > maxDifference) {
					next = i;

					if (nIncrease < newNodeIncrease) {
						nextGroup = 0;
					} else if (newNodeIncrease < nIncrease) {
						nextGroup = 1;
					} else if (Rectangle.area(n.mbrMinX, n.mbrMinY, n.mbrMaxX,n.mbrMaxY) < Rectangle.area(newNode.mbrMinX,newNode.mbrMinY, newNode.mbrMaxX, newNode.mbrMaxY)) {
						nextGroup = 0;
					} else if (Rectangle.area(newNode.mbrMinX, newNode.mbrMinY,newNode.mbrMaxX, newNode.mbrMaxY) < Rectangle.area(n.mbrMinX, n.mbrMinY, n.mbrMaxX, n.mbrMaxY)) {
						nextGroup = 1;
					} else if (newNode.entryCount < maxNodeEntries / 2) {
						nextGroup = 0;
					} else {
						nextGroup = 1;
					}
					maxDifference = difference;
				}
//				if (log.isDebugEnabled()) {
//					log.debug("Entry " + i + " group0 increase = " + nIncrease+ ", group1 increase = " + newNodeIncrease+ ", diff = " + difference + ", MaxDiff = "+ maxDifference + " (entry " + next + ")");
//				}
			}
		}

		entryStatus[next] = ENTRY_STATUS_ASSIGNED;

		if (nextGroup == 0) {
			if (n.entriesMinX[next] < n.mbrMinX)
				n.mbrMinX = n.entriesMinX[next];
			if (n.entriesMinY[next] < n.mbrMinY)
				n.mbrMinY = n.entriesMinY[next];
			if (n.entriesMaxX[next] > n.mbrMaxX)
				n.mbrMaxX = n.entriesMaxX[next];
			if (n.entriesMaxY[next] > n.mbrMaxY)
				n.mbrMaxY = n.entriesMaxY[next];
			n.entryCount++;
		} else {
			newNode.addEntry(n.entriesMinX[next], n.entriesMinY[next],n.entriesMaxX[next], n.entriesMaxY[next], n.ids[next]);
			n.ids[next] = -1;
		}

		return next;
	}

	private float nearest(Point p, Node n, float furthestDistanceSq,TIntArrayList nearestIds) {
		for (int i = 0; i < n.entryCount; i++) {
			float tempDistanceSq = Rectangle.distanceSq(n.entriesMinX[i],n.entriesMinY[i], n.entriesMaxX[i], n.entriesMaxY[i], p.x,p.y);
			if (n.isLeaf()) {
				if (tempDistanceSq < furthestDistanceSq) {
					furthestDistanceSq = tempDistanceSq;
					nearestIds.reset();
				}
				if (tempDistanceSq <= furthestDistanceSq) {
					nearestIds.add(n.ids[i]);
				}
			} else {
				if (tempDistanceSq <= furthestDistanceSq) {
					furthestDistanceSq = nearest(p, getNode(n.ids[i]),furthestDistanceSq, nearestIds);
				}
			}
		}
		return furthestDistanceSq;
	}

	private boolean intersects(Rectangle r, TIntProcedure v, Node n) {
		for (int i = 0; i < n.entryCount; i++) {
			if (Rectangle.intersects(r.minX, r.minY, r.maxX, r.maxY,n.entriesMinX[i], n.entriesMinY[i], n.entriesMaxX[i],n.entriesMaxY[i])) {
				if (n.isLeaf()) {
					if (!v.execute(n.ids[i])) {
						return false;
					}
				} else {
					Node childNode = getNode(n.ids[i]);
					if (!intersects(r, v, childNode)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private void condenseTree(Node l) {
		Node n = l;
		Node parent = null;
		int parentEntry = 0;

		TIntStack eliminatedNodeIds = new TIntArrayStack();

		while (n.level != treeHeight) {
			parent = getNode(parents.pop());
			parentEntry = parentsEntry.pop();

			if (n.entryCount < minNodeEntries) {
				parent.deleteEntry(parentEntry);
				eliminatedNodeIds.push(n.nodeId);
			} else {
				if (n.mbrMinX != parent.entriesMinX[parentEntry]|| n.mbrMinY != parent.entriesMinY[parentEntry]|| n.mbrMaxX != parent.entriesMaxX[parentEntry]|| n.mbrMaxY != parent.entriesMaxY[parentEntry]) {
					float deletedMinX = parent.entriesMinX[parentEntry];
					float deletedMinY = parent.entriesMinY[parentEntry];
					float deletedMaxX = parent.entriesMaxX[parentEntry];
					float deletedMaxY = parent.entriesMaxY[parentEntry];
					parent.entriesMinX[parentEntry] = n.mbrMinX;
					parent.entriesMinY[parentEntry] = n.mbrMinY;
					parent.entriesMaxX[parentEntry] = n.mbrMaxX;
					parent.entriesMaxY[parentEntry] = n.mbrMaxY;
					parent.recalculateMBRIfInfluencedBy(deletedMinX,deletedMinY, deletedMaxX, deletedMaxY);
				}
			}
			n = parent;
		}

		while (eliminatedNodeIds.size() > 0) {
			Node e = getNode(eliminatedNodeIds.pop());
			for (int j = 0; j < e.entryCount; j++) {
				add(e.entriesMinX[j], e.entriesMinY[j], e.entriesMaxX[j],e.entriesMaxY[j], e.ids[j], e.level);
				e.ids[j] = -1;
			}
			e.entryCount = 0;
			deletedNodeIds.push(e.nodeId);
		}
	}

	private Node chooseNode(float minX, float minY, float maxX, float maxY,int level) {
		Node n = getNode(rootNodeId);
		parents.clear();
		parentsEntry.clear();

		while (true) {
//			if (n == null) {
//				log.error("Could not get root node (" + rootNodeId + ")");
//			}

			if (n.level == level) {
				return n;
			}

			float leastEnlargement = Rectangle.enlargement(n.entriesMinX[0],n.entriesMinY[0], n.entriesMaxX[0], n.entriesMaxY[0], minX,minY, maxX, maxY);
			int index = 0;
			for (int i = 1; i < n.entryCount; i++) {
				float tempMinX = n.entriesMinX[i];
				float tempMinY = n.entriesMinY[i];
				float tempMaxX = n.entriesMaxX[i];
				float tempMaxY = n.entriesMaxY[i];
				float tempEnlargement = Rectangle.enlargement(tempMinX,tempMinY, tempMaxX, tempMaxY, minX, minY, maxX, maxY);
				if ((tempEnlargement < leastEnlargement)|| ((tempEnlargement == leastEnlargement) && (Rectangle.area(tempMinX, tempMinY, tempMaxX, tempMaxY) < Rectangle.area(n.entriesMinX[index],n.entriesMinY[index],n.entriesMaxX[index],n.entriesMaxY[index])))) {
					index = i;
					leastEnlargement = tempEnlargement;
				}
			}

			parents.push(n.nodeId);
			parentsEntry.push(index);

			n = getNode(n.ids[index]);
		}
	}

	private Node adjustTree(Node n, Node nn) {
		while (n.level != treeHeight) {
			Node parent = getNode(parents.pop());
			int entry = parentsEntry.pop();

//			if (parent.ids[entry] != n.nodeId) {
//				log.error("Error: entry " + entry + " in node " + parent.nodeId+ " should point to node " + n.nodeId+ "; actually points to node " + parent.ids[entry]);
//			}

			if (parent.entriesMinX[entry] != n.mbrMinX|| parent.entriesMinY[entry] != n.mbrMinY|| parent.entriesMaxX[entry] != n.mbrMaxX|| parent.entriesMaxY[entry] != n.mbrMaxY) {

				parent.entriesMinX[entry] = n.mbrMinX;
				parent.entriesMinY[entry] = n.mbrMinY;
				parent.entriesMaxX[entry] = n.mbrMaxX;
				parent.entriesMaxY[entry] = n.mbrMaxY;

				parent.recalculateMBR();
			}

			Node newNode = null;
			if (nn != null) {
				if (parent.entryCount < maxNodeEntries) {
					parent.addEntry(nn.mbrMinX, nn.mbrMinY, nn.mbrMaxX,nn.mbrMaxY, nn.nodeId);
				} else {
					newNode = splitNode(parent, nn.mbrMinX, nn.mbrMinY,nn.mbrMaxX, nn.mbrMaxY, nn.nodeId);
				}
			}

			n = parent;
			nn = newNode;

			parent = null;
			newNode = null;
		}

		return nn;
	}

	public boolean checkConsistency() {
		return checkConsistency(rootNodeId, treeHeight, null);
	}

	private boolean checkConsistency(int nodeId, int expectedLevel,Rectangle expectedMBR) {
		Node n = getNode(nodeId);

		if (n == null) {
//			log.error("Error: Could not read node " + nodeId);
			return false;
		}

		if (nodeId == rootNodeId && size() == 0) {
			if (n.level != 1) {
//				log.error("Error: tree is empty but root node is not at level 1");
				return false;
			}
		}

		if (n.level != expectedLevel) {
//			log.error("Error: Node " + nodeId + ", expected level "+ expectedLevel + ", actual level " + n.level);
			return false;
		}

		Rectangle calculatedMBR = calculateMBR(n);
		Rectangle actualMBR = new Rectangle();
		actualMBR.minX = n.mbrMinX;
		actualMBR.minY = n.mbrMinY;
		actualMBR.maxX = n.mbrMaxX;
		actualMBR.maxY = n.mbrMaxY;
		if (!actualMBR.equals(calculatedMBR)) {
//			log.error("Error: Node " + nodeId+ ", calculated MBR does not equal stored MBR");
//			if (actualMBR.minX != n.mbrMinX)
//				log.error("  actualMinX=" + actualMBR.minX + ", calc="+ calculatedMBR.minX);
//			if (actualMBR.minY != n.mbrMinY)
//				log.error("  actualMinY=" + actualMBR.minY + ", calc="+ calculatedMBR.minY);
//			if (actualMBR.maxX != n.mbrMaxX)
//				log.error("  actualMaxX=" + actualMBR.maxX + ", calc="+ calculatedMBR.maxX);
//			if (actualMBR.maxY != n.mbrMaxY)
//				log.error("  actualMaxY=" + actualMBR.maxY + ", calc="+ calculatedMBR.maxY);
			return false;
		}

		if (expectedMBR != null && !actualMBR.equals(expectedMBR)) {
//			log.error("Error: Node " + nodeId+ ", expected MBR (from parent) does not equal stored MBR");
			return false;
		}

		if (expectedMBR != null && actualMBR.sameObject(expectedMBR)) {
//			log.error("Error: Node " + nodeId+ " MBR using same rectangle object as parent's entry");
			return false;
		}

		for (int i = 0; i < n.entryCount; i++) {
			if (n.ids[i] == -1) {
//				log.error("Error: Node " + nodeId + ", Entry " + i + " is null");
				return false;
			}

			if (n.level > 1) {
				if (!checkConsistency(n.ids[i], n.level - 1, new Rectangle(n.entriesMinX[i], n.entriesMinY[i], n.entriesMaxX[i],n.entriesMaxY[i]))) {
					return false;
				}
			}
		}
		return true;
	}

	private Rectangle calculateMBR(Node n) {
		Rectangle mbr = new Rectangle();

		for (int i = 0; i < n.entryCount; i++) {
			if (n.entriesMinX[i] < mbr.minX)
				mbr.minX = n.entriesMinX[i];
			if (n.entriesMinY[i] < mbr.minY)
				mbr.minY = n.entriesMinY[i];
			if (n.entriesMaxX[i] > mbr.maxX)
				mbr.maxX = n.entriesMaxX[i];
			if (n.entriesMaxY[i] > mbr.maxY)
				mbr.maxY = n.entriesMaxY[i];
		}
		return mbr;
	}
}