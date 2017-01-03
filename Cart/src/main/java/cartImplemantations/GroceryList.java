package cartImplemantations;

import java.util.HashMap;
import java.util.Map;

import BasicCommonClasses.CartProduct;
import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.SmartCode;

public class GroceryList {
	HashMap<SmartCode, CartProduct> groceryList;

	public void addProduct(SmartCode smartCode, CatalogProduct catalogProduct) /*throws InvalidParameters*/ {
		if (smartCode.getBarcode() != catalogProduct.getBarcode()) 
			/*throw InvalidParameters*/
		if (groceryList == null)
			groceryList = new HashMap<SmartCode, CartProduct>();
		CartProduct cp = groceryList.get(smartCode);
		if (cp == null) {
			cp = new CartProduct(catalogProduct, smartCode.getExpirationDate(), 0);
		}
		cp.incrementAmount();
		groceryList.put(smartCode, cp);
	}
	
	public void removeOneProduct(SmartCode smartCode) /*throws ProductNotInList*/{
		CartProduct cp = groceryList.get(smartCode);
		if (cp == null)
			/*throw new ProductNotInList()*/;
		//remove last product of its type
		int amount = cp.getAmount() -1;
		if (cp.getAmount() == 0)
			groceryList.remove(smartCode);
		cp.setAmount(amount);
		groceryList.put(smartCode, cp);
	}
	
	public double getTotalSum() {
		if (groceryList == null) return 0;
		double $ = 0;
		for (Map.Entry<SmartCode, CartProduct> ¢: groceryList.entrySet())
			$ += ¢.getValue().getCatalogProduct().getPrice();
		return $;
	}
}
