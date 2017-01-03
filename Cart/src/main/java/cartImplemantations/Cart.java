package cartImplemantations;

import java.util.HashMap;
import java.util.Map;

import BasicCommonClasses.CartProduct;
import BasicCommonClasses.SmartCode;

/**
 * Cart class represents shopping cart in the SmartMarket. 
 */
public class Cart {
	int id = -1;
	HashMap<SmartCode, CartProduct> groceryList;
	
	public int getId() {
		return id;
	}

	public HashMap<SmartCode, CartProduct> getGroceryList() {
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
	
	public void returnProductToShelf(SmartCode smartCode) /*throws ProductNotInCart*/{
		CartProduct cp = groceryList.get(smartCode);
		if (cp == null)
			/*throw new ProductNotInCart()*/;
		//remove last product of its type
		int amount = cp.getAmount() -1;
		if (cp.getAmount() == 0)
			groceryList.remove(smartCode);
		cp.setAmount(amount);
		groceryList.put(smartCode, cp);
		//TODO: update server;
	}
	
	public double getTotalSum() {
		if (groceryList == null) return 0;
		double $ = 0;
		for (Map.Entry<SmartCode, CartProduct> ¢: groceryList.entrySet())
			$ += ¢.getValue().getCatalogProduct().getPrice();
		return $;
	}
}
