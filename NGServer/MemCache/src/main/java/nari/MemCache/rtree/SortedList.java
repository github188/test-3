package nari.MemCache.rtree;

import java.io.Serializable;

import gnu.trove.list.array.TFloatArrayList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.procedure.TIntProcedure;

public class SortedList implements Serializable {
	private static final long serialVersionUID = -1549539544212731131L;

	private static final int DEFAULT_PREFERRED_MAXIMUM_SIZE = 10;

	private int preferredMaximumSize = 1;
	private TIntArrayList ids = null;
	private TFloatArrayList priorities = null;

	public void init(int preferredMaximumSize) {
		this.preferredMaximumSize = preferredMaximumSize;
		ids.clear(preferredMaximumSize);
		priorities.clear(preferredMaximumSize);
	}

	public void reset() {
		ids.reset();
		priorities.reset();
	}

	public SortedList() {
		ids = new TIntArrayList(DEFAULT_PREFERRED_MAXIMUM_SIZE);
		priorities = new TFloatArrayList(DEFAULT_PREFERRED_MAXIMUM_SIZE);
	}

	public void add(int id, float priority) {
		float lowestPriority = Float.NEGATIVE_INFINITY;

		if (priorities.size() > 0) {
			lowestPriority = priorities.get(priorities.size() - 1);
		}

		if ((priority == lowestPriority)|| (priority < lowestPriority && ids.size() < preferredMaximumSize)) {
			ids.add(id);
			priorities.add(priority);
		} else if (priority > lowestPriority) {
			if (ids.size() >= preferredMaximumSize) {
				int lowestPriorityIndex = ids.size() - 1;
				while ((lowestPriorityIndex - 1 >= 0)&& (priorities.get(lowestPriorityIndex - 1) == lowestPriority)) {
					lowestPriorityIndex--;
				}

				if (lowestPriorityIndex >= preferredMaximumSize - 1) {
					ids.remove(lowestPriorityIndex, ids.size()- lowestPriorityIndex);
					priorities.remove(lowestPriorityIndex, priorities.size()- lowestPriorityIndex);
				}
			}

			int insertPosition = ids.size();
			while (insertPosition - 1 >= 0&& priority > priorities.get(insertPosition - 1)) {
				insertPosition--;
			}

			ids.insert(insertPosition, id);
			priorities.insert(insertPosition, priority);
		}
	}

	public float getLowestPriority() {
		float lowestPriority = Float.NEGATIVE_INFINITY;
		if (priorities.size() >= preferredMaximumSize) {
			lowestPriority = priorities.get(priorities.size() - 1);
		}
		return lowestPriority;
	}

	public void forEachId(TIntProcedure v) {
		for (int i = 0; i < ids.size(); i++) {
			if (!v.execute(ids.get(i))) {
				break;
			}
		}
	}

	public int[] toNativeArray() {
		return ids.toArray();
	}
}