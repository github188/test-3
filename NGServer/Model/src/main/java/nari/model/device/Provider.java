package nari.model.device;

public interface Provider {

	public static final Provider NONE = new Provider(){

		@Override
		public ModelSelector get() {
			return ModelSelector.NONE;
		}
		
	};
	
	public ModelSelector get();
	
}
