package nari.model.device.filter;

public interface Selection {

	public static final Selection NONE = new Selection(){

		@Override
		public Selection alias(String paramString) {
			return null;
		}

		@Override
		public boolean isCompoundSelection() {
			return false;
		}

		@Override
		public String getSelection() {
			return null;
		}

	};
	
	public abstract Selection alias(String paramString);

	public abstract boolean isCompoundSelection();

	public String getSelection();
}
