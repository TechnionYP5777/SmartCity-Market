package ml.common.property.basicproperties.storestatistics;

import api.contracts.IProduct;
import ml.common.property.basicproperties.ABasicProperty;
import ml.deducer.deductionrules.ADeductionRule;

/**
 * This class property represents one of the most 50 bought products
 * 
 * @author noam
 *
 */
public class MostPopularProductProperty extends ABasicProperty  {

	public static int numOfTop = 50;
	
	private long amount;
	private IProduct product;
	
	
	public MostPopularProductProperty(IProduct product, long amount) {
		this.amount = amount;
		this.product = product;
	}
	
	public MostPopularProductProperty(IProduct product, long amount, ADeductionRule rule) {
		super(rule);
		this.amount = amount;
		this.product = product;
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
		MostPopularProductProperty other = (MostPopularProductProperty) o;
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
				" is very popular in the store" ;
	}
}
