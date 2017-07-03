package ml.common.property.deducedproperties;

import ml.deducer.deductionrules.ADeductionRule;

/**
 * This class represent the average amount of purchasing given product for customer
 * 
 * @author noam
 * 
 */
public class AverageCartPricePerMonthProperty extends ADeducedProperty {

	public static int goMonthesBackLimit = 6;
	
	int monthAgo;
	double average;
	
	
	public AverageCartPricePerMonthProperty(int monthAgo, double average, ADeductionRule deducer) {
		super(deducer);
		this.monthAgo = monthAgo;
		this.average = average;
	}
	
	public AverageCartPricePerMonthProperty(int monthAgo, double average) {
		super();
		this.monthAgo = monthAgo;
		this.average = average;
	}

	public int getMonthAgo() {
		return monthAgo;
	}

	public double getAverage() {
		return average;
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
		AverageCartPricePerMonthProperty other = (AverageCartPricePerMonthProperty) obj;
		if (monthAgo != other.monthAgo)
			return false;
		return true;
	}
	
	@Override
	public String getDescription() {
		return "The average cart " +
				(monthAgo == 0 ? "this month " : 
					monthAgo == 1 ? "1 month ago " : monthAgo + " months ago ") + 
				"spent: " + average;
	}
	
}
