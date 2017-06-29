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
		long temp;
		temp = Double.doubleToLongBits(urgency);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		if (Double.doubleToLongBits(urgency) != Double.doubleToLongBits(other.urgency))
			return false;
		return true;
	}
	
	
}
