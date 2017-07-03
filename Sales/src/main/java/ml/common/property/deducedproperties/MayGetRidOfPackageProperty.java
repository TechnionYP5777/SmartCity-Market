package ml.common.property.deducedproperties;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import api.contracts.IStorePackage;
import ml.deducer.deductionrules.ADeductionRule;

/**
 * This class represent the conclusion: you may get rid of the containing package
 * 
 * @author noam
 * 
 */
public class MayGetRidOfPackageProperty extends ADeducedProperty {

	IStorePackage storePackage;
	int diff;
	double urgency;
	
	
	public MayGetRidOfPackageProperty(IStorePackage storePackage, double urgency, ADeductionRule deducer) {
		super(deducer);
		this.storePackage = storePackage;
		this.diff = (int) ChronoUnit.DAYS.between(LocalDate.now(), storePackage.getExpirationDate());
		this.urgency = urgency;
	}
	
	public MayGetRidOfPackageProperty(IStorePackage storePackage, double urgency) {
		this.storePackage = storePackage;
		this.diff = (int) ChronoUnit.DAYS.between(LocalDate.now(), storePackage.getExpirationDate());
		this.urgency = urgency;
	}


	public IStorePackage getStorePackage() {
		return storePackage;
	}

	public int getDiff() {
		return diff;
	}

	public double getUrgency() {
		return urgency;
	}

	
	@Override
	public String getDescription() {
		return "The packge of product: " + storePackage.getProduct().getName() + " (barcode: " + storePackage.getProduct().getBarcode() + ")" +
				" with exp. date: " + storePackage.getExpirationDate() +
				" needs attention and gets rid off the store";
	}

	@Override
	public int hashCode() {
		return 31 * super.hashCode() + ((storePackage == null) ? 0 : storePackage.hashCode());
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!super.equals(o) || getClass() != o.getClass())
			return false;
		MayGetRidOfPackageProperty other = (MayGetRidOfPackageProperty) o;
		if (storePackage == null) {
			if (other.storePackage != null)
				return false;
		} else if (!storePackage.equals(other.storePackage))
			return false;
		return true;
	}
}
