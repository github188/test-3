package nari.model.device.expression;

import nari.model.device.filter.Field;
import nari.model.device.filter.Order;

public class AscOrder implements Order {

	private Field field = null;
	
	public AscOrder(Field field) {
		this.field = field;
	}

	@Override
	public String getOrder() {
		return "asc";
	}

	@Override
	public Field getOrderField() {
		return field;
	}
}
