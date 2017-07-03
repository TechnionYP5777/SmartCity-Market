package ml.common.property.basicproperties.storestatistics;

import ml.common.property.basicproperties.ABasicProperty;
import ml.deducer.deductionrules.ADeductionRule;

/**
 * This class represents the number of purchases of the customer in given month
 * @author noam
 *
 */
public class NumOfCustomerPurchasesPerMonthProperty extends ABasicProperty {

	public static int goMonthesBackLimit = 6;
	
	int monthAgo;
	int purchases;
	
	
	@Override
	public String getDescription() {
		return "The customer bought " + purchases + " times "
				+ (monthAgo == 0 ? "this month" : (monthAgo == 1 ? "1 month" : monthAgo + " months") + " ago") + " ";
	}


	public NumOfCustomerPurchasesPerMonthProperty(int monthAgo, int numOfBuyers, ADeductionRule deducer) {
		super(deducer);
		this.monthAgo = monthAgo;
		this.purchases = numOfBuyers;
	}
	
	public NumOfCustomerPurchasesPerMonthProperty(int monthAgo, int numOfBuyers) {
		this.monthAgo = monthAgo;
		this.purchases = numOfBuyers;
	}


	public int getMonthAgo() {
		return monthAgo;
	}


	public int getNumOfBuyers() {
		return purchases;
	}


	@Override
	public int hashCode() {
		return monthAgo + 31 * super.hashCode();
	}


	@Override
	public boolean equals(Object o) {
		return o == this || (super.equals(o) && getClass() == o.getClass()
				&& monthAgo == ((NumOfCustomerPurchasesPerMonthProperty) o).monthAgo);
	}

}
