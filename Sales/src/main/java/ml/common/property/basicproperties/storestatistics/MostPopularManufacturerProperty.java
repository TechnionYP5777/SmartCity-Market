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
	
	private long amount;
	private IManufacturer manufacturer;

	public MostPopularManufacturerProperty(IManufacturer manufacturer, long amount) {
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
		return 31 + ((manufacturer == null) ? 0 : manufacturer.hashCode());
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		MostPopularManufacturerProperty other = (MostPopularManufacturerProperty) o;
		if (manufacturer == null) {
			if (other.manufacturer != null)
				return false;
		} else if (!manufacturer.equals(other.manufacturer))
			return false;
		return true;
	}
	
	@Override
	public String getDescription() {
		return "The manufacturer: " + manufacturer.getName() + 
				" is very popular in the store" ;
	}

}
