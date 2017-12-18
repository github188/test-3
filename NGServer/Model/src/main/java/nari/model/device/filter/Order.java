package nari.model.device.filter;

public interface Order {

	public static final Order NONE = new Order(){

		@Override
		public String getOrder() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Field getOrderField() {
			// TODO Auto-generated method stub
			return null;
		}
		
	};
	
	public String getOrder();
	
	public Field getOrderField();
}
