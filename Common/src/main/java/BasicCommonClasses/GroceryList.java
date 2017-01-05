package BasicCommonClasses;

import java.util.HashMap;
import java.util.Map;

public class GroceryList {
	HashMap<SmartCode, CartProduct> groceryList;

	public void addProduct(SmartCode c, CatalogProduct p) /*throws InvalidParameters*/ {
		if (c.getBarcode() != p.getBarcode()) 
			/*throw InvalidParameters*/
		if (groceryList == null)
			groceryList = new HashMap<SmartCode, CartProduct>();
		CartProduct cp = groceryList.get(c);
		if (cp == null) {
			cp = new CartProduct(p, c.getExpirationDate(), 0);
		}
		cp.incrementAmount();
		groceryList.put(c, cp);
	}
	
	public void removeOneProduct(SmartCode c) /*throws ProductNotInList*/{
		CartProduct cp = groceryList.get(c);
		if (cp == null)
			/*throw new ProductNotInList()*/;
		//remove last product of its type
		int amount = cp.getAmount() -1;
		if (cp.getAmount() == 0)
			groceryList.remove(c);
		cp.setAmount(amount);
		groceryList.put(c, cp);
	}
	
	public double getTotalSum() {
		if (groceryList == null) return 0;
		double $ = 0;
		for (Map.Entry<SmartCode, CartProduct> ¢: groceryList.entrySet())
			$ += ¢.getValue().getCatalogProduct().getPrice();
		return $;
	}
}
