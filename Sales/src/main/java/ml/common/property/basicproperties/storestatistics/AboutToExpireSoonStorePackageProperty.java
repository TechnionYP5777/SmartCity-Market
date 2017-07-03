package ml.common.property.basicproperties.storestatistics;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import api.contracts.IStorePackage;
import ml.common.property.basicproperties.ABasicProperty;
import ml.deducer.deductionrules.ADeductionRule;

/**
 * @author idan atias
 *
 * @since Jun 13, 2017
 * 
 *        This class represents a property of an "about to expire soon" product.
 */
public class AboutToExpireSoonStorePackageProperty extends ABasicProperty {

	public static int threshold = 14; // 2 days time
	public static int numOfTop = 25;

	private int diff; //holds the diff (in days) between the current date and the product's E.D (the ABS value)
	private IStorePackage storePackage;

	public AboutToExpireSoonStorePackageProperty(IStorePackage storePackage) {
		this.diff = (int) ChronoUnit.DAYS.between(LocalDate.now(), storePackage.getExpirationDate());
		this.storePackage = storePackage;
	}
	
	public AboutToExpireSoonStorePackageProperty(IStorePackage storePackage, ADeductionRule rule) {
		super(rule);
		this.diff = (int) ChronoUnit.DAYS.between(LocalDate.now(), storePackage.getExpirationDate());
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
		return 31 * super.hashCode() + ((storePackage == null) ? 0 : storePackage.hashCode());
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!super.equals(o) || getClass() != o.getClass())
			return false;
		AboutToExpireSoonStorePackageProperty other = (AboutToExpireSoonStorePackageProperty) o;
		if (storePackage == null) {
			if (other.storePackage != null)
				return false;
		} else if (!storePackage.equals(other.storePackage))
			return false;
		return true;
	}
	
	@Override
	public String getDescription() {
		return "The product: " + storePackage.getProduct().getName() + " (bracode: " + storePackage.getProduct().getBarcode() + ")" +
				" is about to expired soon (exp. " + storePackage.getExpirationDate() + ")";
	}

}
