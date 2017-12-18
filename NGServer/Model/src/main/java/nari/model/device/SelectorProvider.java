package nari.model.device;

public interface SelectorProvider {

	public static final SelectorProvider NONE = new SelectorProvider(){

		@Override
		public ModelSelector get() {
			return ModelSelector.NONE;
		}

		@Override
		public int getPriority() {
			return 0;
		}

		@Override
		public boolean selfCheck() {
			return false;
		}
		
	};
	
	public ModelSelector get();
	
	public int getPriority();
	
	public boolean selfCheck();
	
}
