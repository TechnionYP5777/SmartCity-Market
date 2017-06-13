package ml.common.property.basicproperties.storestatistics;

import api.contracts.IStorePackage;
import ml.common.property.basicproperties.ABasicProperty;
import ml.deducer.deductionrules.ADeductionRule;

/**
 * @author idan atias
 *
 * @since Jun 13, 2017
 * 
 *        This class represents a property of an "about to expire" product.
 */
public class AboutToExpireStorePackageProperty extends ABasicProperty {

	public static int threshold = 2; // 2 days time
	public static int numOfTop = 25;

	int diff; //holds the diff (in days) between the current date and the product's E.D (the ABS value)
	IStorePackage storePackage;

	public AboutToExpireStorePackageProperty(IStorePackage storePackage, int diff) {
		super();
		this.diff = diff;
		this.storePackage = storePackage;
	}
	
	public AboutToExpireStorePackageProperty(IStorePackage storePackage, int diff, ADeductionRule rule) {
		super(rule);
		this.diff = diff;
		this.storePackage = storePackage;
	}

	public int getDiff() {
		return diff;
	}

	public IStorePackage getStorePackage() {
		return storePackage;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + diff;
		result = prime * result + ((storePackage == null) ? 0 : storePackage.hashCode());
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
		AboutToExpireStorePackageProperty other = (AboutToExpireStorePackageProperty) obj;
		if (diff != other.diff)
			return false;
		if (storePackage == null) {
			if (other.storePackage != null)
				return false;
		} else if (!storePackage.equals(other.storePackage))
			return false;
		return true;
	}

}
