package CartContracts;

import BasicCommonClasses.GroceryList;
import BasicCommonClasses.SmartCode;
import CartContracts.ACartExceptions.CartNotConnected;
import CartContracts.ACartExceptions.CriticalError;
import CartContracts.ACartExceptions.ProductNotInCart;

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
	 * addPtoductToCart - Adds product with amount to the cart
	 * 
	 *  @param c
	 * @throws CriticalError 
	 * @throws CartNotConnected 
	 */
	void addPtoductToCart(SmartCode c, int amount) throws CriticalError, CartNotConnected;
	
	/**
	 * returnProductToShelf - removes product with amount from the cart
	 * 
	 *  @param c
	 *  @throws ProductNotInCart
	 */
	void returnProductToShelf(SmartCode c, int amount) throws ProductNotInCart;
	
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
