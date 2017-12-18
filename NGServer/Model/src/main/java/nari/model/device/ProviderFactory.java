package nari.model.device;

public interface ProviderFactory {

	public static final ProviderFactory NONE = new ProviderFactory(){

		@Override
		public SelectorProvider getProvider() {
			return SelectorProvider.NONE;
		}
		
	};
	
	public SelectorProvider getProvider();
	
}
