package ml.common.property.basicproperties.storestatistics;

import api.contracts.IProduct;
import ml.common.property.basicproperties.ABasicProperty;
import ml.deducer.deductionrules.ADeductionRule;

public class AverageAmountOfProductForCustomerProperty extends ABasicProperty {

	public static int goMonthesBackLimit = 6;
	
	double averageAmount;
	IProduct product;
	
	@Override
	public String getDescription() {
		return "The average amount of product " + product.getName() + " (bracode: " + product.getBarcode() + ")" +
				" the customer buying is: " + (int)averageAmount;
	}

	public AverageAmountOfProductForCustomerProperty(IProduct product, double averageAmount, ADeductionRule deducer) {
		super(deducer);
		this.product = product;
		this.averageAmount = averageAmount;
	}

	public AverageAmountOfProductForCustomerProperty(IProduct product, double averageAmount) {
		super();
		this.product = product;
		this.averageAmount = averageAmount;
	}
	
	public double getAverageAmount() {
		return averageAmount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AverageAmountOfProductForCustomerProperty other = (AverageAmountOfProductForCustomerProperty) obj;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		return true;
	}
}
