package ml.common.property.basicproperties.storestatistics;

import ml.common.property.basicproperties.ABasicProperty;
import ml.deducer.deductionrules.ADeductionRule;

/**
 * This class represents the sum of purchases made in a given month
 * @author noam
 *
 */
public class SumOfPurchasesPerMonthProperty extends ABasicProperty {

	public static int goMonthesBackLimit = 6;
	
	int monthAgo;
	double sumOfPurchases;
	
	
	public SumOfPurchasesPerMonthProperty(int monthAgo, double sumOfPurchases, ADeductionRule deducer) {
		super(deducer);
		this.monthAgo = monthAgo;
		this.sumOfPurchases = sumOfPurchases;
	}
	
	public SumOfPurchasesPerMonthProperty(int monthAgo, double sumOfPurchases) {
		super();
		this.monthAgo = monthAgo;
		this.sumOfPurchases = sumOfPurchases;
	}

	public int getMonthAgo() {
		return monthAgo;
	}

	public double getSumOfPurchases() {
		return sumOfPurchases;
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
		SumOfPurchasesPerMonthProperty other = (SumOfPurchasesPerMonthProperty) obj;
		if (monthAgo != other.monthAgo)
			return false;
		return true;
	}
	
	@Override
	public String getDescription() {
		return "The sum of purchases " +
				(monthAgo == 0 ? "this month " : 
					monthAgo == 1 ? "1 month ago " : monthAgo + " months ago ") + 
				"is: " + sumOfPurchases;
	}
}
