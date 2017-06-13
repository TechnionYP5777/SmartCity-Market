package ml.common.property.basicproperties.storestatistics;

import api.contracts.IProduct;
import ml.common.property.basicproperties.ABasicProperty;
import ml.deducer.deductionrules.ADeductionRule;

/**
 * @author idan atias
 *
 * @since Jun 13, 2017
 * 
 *        This class represents a property of an "about to expire" product.
 */
public class AboutToExpireProductProperty extends ABasicProperty {

	public static int threshold = 2 * 24 * 60 * 60; // 2 days time in seconds
	public static int numOfTop = 25;

	int diff; //holds the diff (in secs) between the current date and the product's E.D (the ABS value)
	IProduct product;

	public AboutToExpireProductProperty(IProduct product, int diff) {
		super();
		this.diff = diff;
		this.product = product;
	}
	
	public AboutToExpireProductProperty(IProduct product, int diff, ADeductionRule rule) {
		super(rule);
		this.diff = diff;
		this.product = product;
	}

	public int getDiff() {
		return diff;
	}

	public IProduct getProduct() {
		return product;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + diff;
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
		AboutToExpireProductProperty other = (AboutToExpireProductProperty) obj;
		if (diff != other.diff)
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		return true;
	}

}
