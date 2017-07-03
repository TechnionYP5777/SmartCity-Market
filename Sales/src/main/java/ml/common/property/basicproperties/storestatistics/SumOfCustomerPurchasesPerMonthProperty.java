package ml.common.property.basicproperties.storestatistics;

import ml.common.property.basicproperties.ABasicProperty;
import ml.deducer.deductionrules.ADeductionRule;

/**
 * This class represents the sum of purchases of the customer in given month
 * @author noam
 *
 */
public class SumOfCustomerPurchasesPerMonthProperty extends ABasicProperty {

	public static int goMonthesBackLimit = 6;
	
	int monthAgo;
	double sumOfPurchases;
	
	@Override
	public String getDescription() {
		return "The customer purchased " +
				(monthAgo == 0 ? "this month " : 
					monthAgo == 1 ? "1 month ago " : monthAgo + " months ago ") + 
				"with total price of: " + sumOfPurchases;
	}

	public SumOfCustomerPurchasesPerMonthProperty(int monthAgo, double sumOfPurchases, ADeductionRule deducer) {
		super(deducer);
		this.monthAgo = monthAgo;
		this.sumOfPurchases = sumOfPurchases;
	}
	
	public SumOfCustomerPurchasesPerMonthProperty(int monthAgo, double sumOfPurchases) {
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
		SumOfCustomerPurchasesPerMonthProperty other = (SumOfCustomerPurchasesPerMonthProperty) obj;
		if (monthAgo != other.monthAgo)
			return false;
		return true;
	}
	
	
	
}
