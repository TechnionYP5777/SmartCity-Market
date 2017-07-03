package ml.common.property.basicproperties.storestatistics;

import api.contracts.IProduct;
import ml.common.property.basicproperties.ABasicProperty;
import ml.deducer.deductionrules.ADeductionRule;

/**
 * This class represents a property of "last popular product of customer"
 *  
 * @author noam
 *
 */
public class LastPopularProductOfCustomerProperty extends ABasicProperty {
	public static int numOfBottom = 15;
	
	private long amount;
	private IProduct product;
	
	public LastPopularProductOfCustomerProperty(IProduct product, long amount, ADeductionRule deducer) {
		super(deducer);
		this.product = product;
		this.amount = amount;
	}
	
	public LastPopularProductOfCustomerProperty(IProduct product, long amount) {
		super();
		this.product = product;
		this.amount = amount;
	}

	public long getAmount() {
		return amount;
	}

	public IProduct getProduct() {
		return product;
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
		LastPopularProductOfCustomerProperty other = (LastPopularProductOfCustomerProperty) obj;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		return true;
	}
	
	@Override
	public String getDescription() {
		return "The product: " + product.getName() + " (bracode: " + product.getBarcode() + ")" +
				" is not popular for the customer" ;
	}
}
