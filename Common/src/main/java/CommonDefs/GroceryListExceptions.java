package CommonDefs;

import SMExceptions.SMException;

/**
 * This class define all the Exceptions {@link GroceryList} could throw.
 * 
 * @author Lior Ben Ami
 * @since 2017-01-05
 */
public class GroceryListExceptions extends SMException {

	private static final long serialVersionUID = 6779023320480887717L;
	/**
	 * Thrown when one of the parameters is illegal.
	 */
	public static class InvalidParameter extends SMException {
		private static final long serialVersionUID = -3673113467427632509L;
	}
	
	/**
	 * Thrown when the given product isn't in the grocery list
	 */
	public static class ProductNotInList extends SMException {
		private static final long serialVersionUID = -834148119635166705L;
	}
	
	/**
	 * Thrown when the given amount of specific product is bigger then available in the grocery list
	 */
	public static class AmountIsBiggerThanAvailable extends SMException {
		private static final long serialVersionUID = -943165516562357164L;
	}
	
}