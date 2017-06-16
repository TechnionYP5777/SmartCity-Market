package ml.common.property.basicproperties.storestatistics;

import api.contracts.IManufacturer;
import ml.common.property.basicproperties.ABasicProperty;
import ml.deducer.deductionrules.ADeductionRule;

/**
 * @author idan atias
 *
 * @since Jun 16, 2017
 * 
 * class for representing popular manufactures property
 */
public class MostPopularManufacturerProperty extends ABasicProperty {
	
	public static int numOfTop = 25;
	
	long amount;
	IManufacturer manufacturer;

	public MostPopularManufacturerProperty(IManufacturer manufacturer, long amount) {
		super();
		this.amount = amount;
		this.manufacturer = manufacturer;
	}
	
	public MostPopularManufacturerProperty(IManufacturer manufacturer, long amount, ADeductionRule rule) {
		super(rule);
		this.amount = amount;
		this.manufacturer = manufacturer;
	}

	public long getAmount() {
		return amount;
	}

	public IManufacturer getManufacturer() {
		return manufacturer;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((manufacturer == null) ? 0 : manufacturer.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MostPopularManufacturerProperty other = (MostPopularManufacturerProperty) obj;
		if (manufacturer == null) {
			if (other.manufacturer != null)
				return false;
		} else if (!manufacturer.equals(other.manufacturer))
			return false;
		return true;
	}

}
