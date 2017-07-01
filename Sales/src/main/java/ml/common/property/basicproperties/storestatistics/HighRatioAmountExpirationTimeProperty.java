package ml.common.property.basicproperties.storestatistics;

import api.contracts.IStorePackage;
import ml.common.basiccalsses.CombinedStorePackage;
import ml.common.property.basicproperties.ABasicProperty;
import ml.deducer.deductionrules.ADeductionRule;

public class HighRatioAmountExpirationTimeProperty extends ABasicProperty {
	public static double ratioThreshold = 1.0; // days time
	
	private double ratio; //holds the diff (in days) between the current date and the product's E.D (the ABS value)
	private CombinedStorePackage combinedPackage;
	
	public HighRatioAmountExpirationTimeProperty(double ratio, CombinedStorePackage storePackage, ADeductionRule deducer) {
		super(deducer);
		this.ratio = ratio;
		this.combinedPackage = storePackage;
	}
	
	public HighRatioAmountExpirationTimeProperty(double ratio, CombinedStorePackage storePackage) {
		super();
		this.ratio = ratio;
		this.combinedPackage = storePackage;
	}

	public double getRatio() {
		return ratio;
	}

	public IStorePackage getCombinedPackage() {
		return combinedPackage;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		long temp;
		temp = Double.doubleToLongBits(ratio);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((combinedPackage == null) ? 0 : combinedPackage.hashCode());
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
		HighRatioAmountExpirationTimeProperty other = (HighRatioAmountExpirationTimeProperty) obj;
		if (Double.doubleToLongBits(ratio) != Double.doubleToLongBits(other.ratio))
			return false;
		if (combinedPackage == null) {
			if (other.combinedPackage != null)
				return false;
		} else if (!combinedPackage.equals(other.combinedPackage))
			return false;
		return true;
	}
	
	@Override
	public String getDescription() {
		return "The product: " + combinedPackage.getProduct().getName() + " (bracode: " + combinedPackage.getProduct().getBarcode() + ")" +
				" has a high ratio of (amount/time left): " + ratio;
	}
	
}
