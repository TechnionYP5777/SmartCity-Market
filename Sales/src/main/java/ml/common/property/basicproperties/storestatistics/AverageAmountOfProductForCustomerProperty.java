package ml.common.property.basicproperties.storestatistics;

import api.contracts.IProduct;
import ml.common.property.basicproperties.ABasicProperty;
import ml.deducer.deductionrules.ADeductionRule;

/**
 * This class represents a property of "average amount bought by the customer"
 *  
 * @author noam
 *
 */
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
		this.product = product;
		this.averageAmount = averageAmount;
	}
	
	public double getAverageAmount() {
		return averageAmount;
	}

	@Override
	public int hashCode() {
		return 31 * super.hashCode() + ((product == null) ? 0 : product.hashCode());
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!super.equals(o) || getClass() != o.getClass())
			return false;
		AverageAmountOfProductForCustomerProperty other = (AverageAmountOfProductForCustomerProperty) o;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		return true;
	}
}
