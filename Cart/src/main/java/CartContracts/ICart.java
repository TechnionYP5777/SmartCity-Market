package CartContracts;

import BasicCommonClasses.SmartCode;
import CartContracts.ACartExceptions.CartNotConnected;
import CartContracts.ACartExceptions.CriticalError;
import CartContracts.ACartExceptions.ProductNotInCart;
import CartImplemantations.GroceryList;

/**
 * ACart - This interface is the contract for Cart Type.
 *
 * @author Lior Ben Ami
 * @since 2017-01-04
 */
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
	GroceryList getGroceryList();
	
	/**
	 * login - the cart login to the server and gets it's own id;
	 * @throws CriticalError 
	 */
	void login() throws CriticalError;
	
	/**
	 * logout - the cart logout from  the server. To use in the end of the shopping.
	 * @throws CartNotConnected 
	 * @throws CriticalError 
	 */
	void logout() throws CartNotConnected, CriticalError;
	
	/**
	 * resume - saves the data of the cart from the server (to use in case of collapse)
	 * @throws CriticalError 
	 * @throws CartNotConnected 
	 */
	void resume(int _id) throws CriticalError, CartNotConnected;
	
	/**
	 * addPtoductToCart - Adds product to the cart
	 * 
	 *  @param c
	 */
	void addPtoductToCart(SmartCode c);
	
	/**
	 * returnProductToShelf - Adds product to the cart
	 * 
	 *  @param c
	 *  @throws ProductNotInCart
	 */
	void returnProductToShelf(SmartCode c) throws ProductNotInCart;
	
	/**
	 * getTotalSum - returns the total sum of the shopping
	 * @return double
	 */
	double getTotalSum(); 
	
	/**
	 * checkOutGroceryList - returns the finale total sum of the shopping and initialize grocery list
	 * @return
	 */
	double checkOutGroceryList();
}
