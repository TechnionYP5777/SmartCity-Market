package ml.common.property.basicproperties.storestatistics;

import ml.common.property.basicproperties.ABasicProperty;
import ml.deducer.deductionrules.ADeductionRule;

/**
 * This class represents the number of buyers in given month
 * @author noam
 *
 */
public class NumOfBuyersPerMonthProperty extends ABasicProperty {

	public static int goMonthesBackLimit = 6;
	
	int monthAgo;
	int numOfBuyers;
	
	public NumOfBuyersPerMonthProperty(int monthAgo, int numOfBuyers, ADeductionRule deducer) {
		super(deducer);
		this.monthAgo = monthAgo;
		this.numOfBuyers = numOfBuyers;
	}
	
	public NumOfBuyersPerMonthProperty(int monthAgo, int numOfBuyers) {
		this.monthAgo = monthAgo;
		this.numOfBuyers = numOfBuyers;
	}

	public int getMonthAgo() {
		return monthAgo;
	}

	public int getNumOfBuyers() {
		return numOfBuyers;
	}

	@Override
	public int hashCode() {
		return monthAgo + 31 * super.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return o == this || (super.equals(o) && getClass() == o.getClass() && monthAgo == ((NumOfBuyersPerMonthProperty) o).monthAgo);
	}
	
	@Override
	public String getDescription() {
		return "The number of buyers "
				+ (monthAgo == 0 ? "this month" : (monthAgo == 1 ? "1 month" : monthAgo + " months") + " ago") + " "
				+ "is: " + numOfBuyers;
	}
	
}
