package ml.common.property.deducedproperties;

import api.contracts.IStorePackage;
import ml.deducer.deductionrules.ADeductionRule;

/**
 * This class represent the conclusion: you must get rid of the containing package
 * 
 * @author noam
 * 
 */
public class MustGetRidOfPackageProperty extends ADeducedProperty {

	IStorePackage storePackage;
	
	double urgency;

	public MustGetRidOfPackageProperty(IStorePackage storePackage, double urgency, ADeductionRule deducer) {
		super(deducer);
		this.storePackage = storePackage;
		this.urgency = urgency;
	}
	
	public MustGetRidOfPackageProperty(IStorePackage storePackage, double urgency) {
		super();
		this.storePackage = storePackage;
		this.urgency = urgency;
	}

	public IStorePackage getStorePackage() {
		return storePackage;
	}

	public double getUrgency() {
		return urgency;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
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
		MustGetRidOfPackageProperty other = (MustGetRidOfPackageProperty) obj;
		if (storePackage == null) {
			if (other.storePackage != null)
				return false;
		} else if (!storePackage.equals(other.storePackage))
			return false;
		return true;
	}

	@Override
	public String getDescription() {
		return "The packge of product: " + storePackage.getProduct().getName() + " (barcode: " + storePackage.getProduct().getBarcode() + ")" +
				" with exp. date: " + storePackage.getExpirationDate() +
				" must gets rid off the store";
	}
	
}
