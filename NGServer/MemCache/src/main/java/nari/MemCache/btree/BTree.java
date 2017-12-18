package nari.MemCache.btree;


public class BTree<Key extends Comparable<Key>, Value> {
	private static final int NODESIZE = 100;

	private BTreeNode root;
	private int height;
	private int num; 

	private static final class BTreeNode {
		private int m;
		private Entry[] children = new Entry[NODESIZE];

		private BTreeNode(int k) {
			m = k;
		}
	}

	private static class Entry {
		private Comparable<?> key;
		private Object value;
		private BTreeNode next;

		public Entry(Comparable<?> key, Object value, BTreeNode next) {
			this.key = key;
			this.value = value;
			this.next = next;
		}
	}

	public BTree() {
		root = new BTreeNode(0);
	}

	public int size() {
		return num;
	}

	public int height() {
		return height;
	}

	public Value get(Key key) {
		return search(root, key, height);
	}

	@SuppressWarnings("unchecked")
	private Value search(BTreeNode x, Key key, int ht) {
		Entry[] children = x.children;

		if (ht == 0) {
			for (int j = 0; j < x.m; j++) {
				if (eq(key, children[j].key)){
					return (Value)children[j].value;
				}
			}
		}else {
			for (int j = 0; j < x.m; j++) {
				if (j + 1 == x.m || less(key, children[j + 1].key)){
					return search(children[j].next, key, ht - 1);
				}
			}
		}
		return null;
	}

	public void put(Key key, Value value) {
		BTreeNode u = insert(root, key, value, height);
		num++;
		if (u == null){
			return;
		}

		BTreeNode t = new BTreeNode(2);
		t.children[0] = new Entry(root.children[0].key, null, root);
		t.children[1] = new Entry(u.children[0].key, null, u);
		root = t;
		height++;
	}

	private BTreeNode insert(BTreeNode h, Key key, Value value, int ht) {
		int j;
		Entry t = new Entry(key, value, null);
		if (ht == 0) {
			for (j = 0; j < h.m; j++) {
				if (less(key, h.children[j].key)){
					break;
				}
			}
		} else {
			for (j = 0; j < h.m; j++) {
				if ((j + 1 == h.m) || less(key, h.children[j + 1].key)) {
					BTreeNode u = insert(h.children[j++].next, key, value, ht - 1);
					if (u == null){
						return null;
					}
					t.key = u.children[0].key;
					t.next = u;
					break;
				}
			}
		}

		for (int i = h.m; i > j; i--){
			h.children[i] = h.children[i - 1];
		}
		h.children[j] = t;
		h.m++;
		if (h.m < NODESIZE){
			return null;
		}else{
			return split(h);
		}
	}

	private BTreeNode split(BTreeNode h) {
		BTreeNode t = new BTreeNode(NODESIZE / 2);
		h.m = NODESIZE / 2;
		for (int j = 0; j < NODESIZE / 2; j++){
			t.children[j] = h.children[NODESIZE / 2 + j];
		}
		return t;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean less(Comparable k1, Comparable k2) {
		return k1.compareTo(k2) < 0;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean eq(Comparable k1, Comparable k2) {
		return k1.compareTo(k2) == 0;
	}

	public static void main(String[] args) {
		
		
	}
}