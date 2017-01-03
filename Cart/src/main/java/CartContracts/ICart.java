package CartContracts;

import java.util.HashMap;

import BasicCommonClasses.CartProduct;
import BasicCommonClasses.SmartCode;

public interface ICart {
	/**
	 * getId - returns the cart's id
	 * @return int
	 */
	int getId();

	/**
	 * getGroceryList - returns the current grocery list.
	 * @return HashMap<SmartCode, CartProduct> 
	 */
	HashMap<SmartCode, CartProduct> getGroceryList();
	
	/**
	 * login - the cart login to the server and gets it's own id;
	 */
	void login();
	
	/**
	 * logout - the cart logout from  the server. To use in the end of the shopping.
	 */
	void logout();
	
	/**
	 * resume - saves the data of the cart from the server (to use in case of collapse)
	 */
	void resume();
	
	/**
	 * addPtoductToCart - Adds product to the cart
	 * 
	 *  @param smartCode
	 */
	void addPtoductToCart(SmartCode smartCode);
	
	/**
	 * returnProductToShelf - Adds product to the cart
	 * 
	 *  @param smartCode
	 *  @throws ProductNotInCart
	 */
	void returnProductToShelf(SmartCode smartCode) /*throws ProductNotInCart*/;
	
	/**
	 * getTotalSum - returns the total sum of the shopping
	 * @return double
	 */
	double getTotalSum(); 
}
