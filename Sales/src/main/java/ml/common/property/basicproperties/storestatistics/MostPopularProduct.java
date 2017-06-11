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
public class MostPopularProduct extends ABasicProperty  {

	public static int numOfTop = 50;
	
	long amount;
	IProduct product;
	
	
	public MostPopularProduct(long amount, IProduct product) {
		super();
		this.amount = amount;
		this.product = product;
	}
	
	public MostPopularProduct(long amount, IProduct product, ADeductionRule rule) {
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
		MostPopularProduct other = (MostPopularProduct) obj;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		return true;
	}

	
}
