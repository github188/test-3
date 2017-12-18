package nari.model.device.expression;

import nari.model.device.filter.Field;
import nari.model.device.filter.Order;

public class DescOrder implements Order {
	
	private Field field = null;
	
	public DescOrder(Field field) {
		this.field = field;
	}

	@Override
	public String getOrder() {
		return "desc";
	}

	@Override
	public Field getOrderField() {
		return field;
	}
	
}
