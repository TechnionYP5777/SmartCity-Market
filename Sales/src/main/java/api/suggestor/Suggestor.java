package api.suggestor;

import java.util.List;

import api.contracts.IGroceryList;
import api.contracts.IGroceryProduct;
import api.contracts.IStorePackage;
import api.types.StoreData;
import api.types.sales.ASale;

public class Suggestor {
	private volatile static StoreData storeData;

	public static void updateHistory(List<? extends IGroceryList> history) {
		storeData = new StoreData(history, storeData.getStock());
	}

	public static void updateStock(List<? extends IStorePackage> stock) {
		storeData = new StoreData(storeData.getHistory(), stock);
	}

	/**
	 * try to generate offer for customer
	 * 
	 * @param currentGrocery
	 *            the current grocery lift of the costumer
	 * @param purchasedProduct
	 *            the product that the customer bought
	 * @return sale if succeeded, null otherwise (note: the sale must be one of the
	 *            types under {@link api.types.sales})
	 */
	public static ASale suggestSale(IGroceryList currentGrocery, IGroceryProduct purchasedProduct) {
		@SuppressWarnings("unused")
		StoreData currentData = storeData;
		
		return null;
	}

	/**
	 * examine sale suggested by customer
	 * 
	 * @param currentGrocery
	 *            the current grocery lift of the costumer
	 * @param purchasedProduct
	 *            the Sale that that the customer suggested (must be one of the
	 *            types under {@link api.types.sales})
	 * @return same sale if agreed, another suggest or null if not.
	 */
	public static ASale examineOffer(IGroceryList currentGrocery, ASale purchasedProduct) {
		@SuppressWarnings("unused")
		StoreData currentData = storeData;
		
		return null;
	}

}
