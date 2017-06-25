package ExpiredProducts;

import java.util.HashSet;

import BasicCommonClasses.ProductPackage;

/**
 * ExpiredProductsEvent - this class returns expired products from the event
 * 
 * @author Lior Ben Ami 
 * @since  2017-06-25
 *
 */
public class ExpiredProductsEvent {

	HashSet<ProductPackage> expiryProducts;
	
	public ExpiredProductsEvent(HashSet<ProductPackage> expiryProducts) {
		this.expiryProducts = expiryProducts;
	}
	
	public HashSet<ProductPackage> getExpiryProducts() {
		return expiryProducts;
	}
	
}