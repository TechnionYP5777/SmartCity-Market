package ml.common.property.basicproperties.storestatistics;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import api.contracts.IStorePackage;
import ml.common.property.basicproperties.ABasicProperty;
import ml.deducer.deductionrules.ADeductionRule;

/**
 * @author noam yefet
 *
 * @since Jun 13, 2017
 * 
 *        This class represents a property of an "about to expire late" product.
 *        about to expire late is later then {@link AboutToExpireSoonStorePackageProperty}
 */
public class AboutToExpireLateStorePackageProperty extends ABasicProperty {
	public static int minDaysThreshold = AboutToExpireSoonStorePackageProperty.threshold; // days time
	public static int maxDaysThreshold = minDaysThreshold + 30; // days time
	

	private long diff; //holds the diff (in days) between the current date and the product's E.D (the ABS value)
	private IStorePackage storePackage;
	
	
	public AboutToExpireLateStorePackageProperty(IStorePackage storePackage) {
		super();
		this.diff = ChronoUnit.DAYS.between(LocalDate.now(), storePackage.getExpirationDate());
		this.storePackage = storePackage;
	}
	
	public AboutToExpireLateStorePackageProperty(IStorePackage storePackage, ADeductionRule rule) {
		super(rule);
		this.diff = ChronoUnit.DAYS.between(LocalDate.now(), storePackage.getExpirationDate());;
		this.storePackage = storePackage;
	}

	public long getDiff() {
		return diff;
	}

	public IStorePackage getStorePackage() {
		return storePackage;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (diff ^ (diff >>> 32));
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
		AboutToExpireLateStorePackageProperty other = (AboutToExpireLateStorePackageProperty) obj;
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
