package nari.model.bean;

public interface TopoDef {

	public static final TopoDef NONE = new TopoDef(){

		@Override
		public int nodeCount() {
			return 0;
		}

		@Override
		public long newConnectionNode() {
			return 0;
		}

		@Override
		public void setConnectionNode(int index, long node) {
			
		}

		@Override
		public long getConnectionNode(int index) {
			return 0;
		}

		@Override
		public long[] nodes() {
			return null;
		}

		@Override
		public void setConnectionNode(long[] nodes) {
			
		}
		
	};
	
	public int nodeCount();

	public long newConnectionNode();
	
	public void setConnectionNode(int index,long node);
	
	public void setConnectionNode(long[] nodes);
	
	public long getConnectionNode(int index);
	
	public long[] nodes();
	
}

