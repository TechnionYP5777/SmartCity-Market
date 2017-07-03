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
		return monthAgo + 31 * super.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return o == this || (super.equals(o) && getClass() == o.getClass() && monthAgo == ((AverageCartPricePerMonthProperty) o).monthAgo);
	}
	
	@Override
	public String getDescription() {
		return "The average cart "
				+ (monthAgo == 0 ? "this month" : (monthAgo == 1 ? "1 month" : monthAgo + " months") + " ago") + " "
				+ "spent: " + average;
	}
	
}
