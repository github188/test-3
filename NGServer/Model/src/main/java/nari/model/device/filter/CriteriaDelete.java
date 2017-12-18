package nari.model.device.filter;

public interface CriteriaDelete {

	public CriteriaDelete delete(Selection select);

	public CriteriaDelete where(Expression exp);

	public Expression toExpression();

	public Root getRoot();
}
