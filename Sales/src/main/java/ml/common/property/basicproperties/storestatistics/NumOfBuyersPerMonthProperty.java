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
		super();
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
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + monthAgo;
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
		NumOfBuyersPerMonthProperty other = (NumOfBuyersPerMonthProperty) obj;
		if (monthAgo != other.monthAgo)
			return false;
		return true;
	}
	
	@Override
	public String getDescription() {
		return "The number of buyers " +
				(monthAgo == 0 ? "this month " : 
					monthAgo == 1 ? "1 month ago " : monthAgo + " months ago ") + 
				"is: " + numOfBuyers;
	}
	
}
