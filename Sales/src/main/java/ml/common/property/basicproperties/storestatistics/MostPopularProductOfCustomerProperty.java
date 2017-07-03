package ml.common.property.basicproperties.storestatistics;

import api.contracts.IProduct;
import ml.common.property.basicproperties.ABasicProperty;
import ml.deducer.deductionrules.ADeductionRule;

/**
 * This class represents a property of "most popular product of customer"
 *  
 * @author noam
 *
 */
public class MostPopularProductOfCustomerProperty extends ABasicProperty {

	public static int numOfTop = 15;
	
	private long amount;
	private IProduct product;
	
	public MostPopularProductOfCustomerProperty(IProduct product, long amount, ADeductionRule deducer) {
		super(deducer);
		this.product = product;
		this.amount = amount;
	}
	
	public MostPopularProductOfCustomerProperty(IProduct product, long amount) {
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
		return 31 * super.hashCode() + ((product == null) ? 0 : product.hashCode());
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!super.equals(o) || getClass() != o.getClass())
			return false;
		MostPopularProductOfCustomerProperty other = (MostPopularProductOfCustomerProperty) o;
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
				" is very popular for the cutomer" ;
	}
	
}
