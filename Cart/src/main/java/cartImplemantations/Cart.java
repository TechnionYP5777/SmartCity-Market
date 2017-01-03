package cartImplemantations;

import BasicCommonClasses.SmartCode;

/**
 * Cart class represents shopping cart in the SmartMarket. 
 */
public class Cart {
	int id = -1;
	GroceryList groceryList = new GroceryList();
	
	public int getId() {
		return id;
	}

	public GroceryList getGroceryList() {
		return groceryList;
	}

	public void login() {
		
	}
	
	public void logout() {
		
	}
	
	/**
	 * saves the data of the cart from the server (to use in case of collapse)
	 */
	public void resume() {
		
	}
	
	public void addPtoductToCart(SmartCode smartCode) {
		
	}
	
	public void returnProductToShelf(SmartCode ¢) /*throws ProductNotInCart*/{
		if (groceryList == null) 
			/*throw ProductNotInCart*/
		groceryList.removeOneProduct(¢);
		
		//TODO: update server;
	}
	
	public double getTotalSum() {
		return groceryList == null ? 0 : groceryList.getTotalSum();
	}
	
	public double checkOutGroceryList() {
		//TODO: update server;
		double $ = this.getTotalSum();
		groceryList = null;
		return $;
	}
}
