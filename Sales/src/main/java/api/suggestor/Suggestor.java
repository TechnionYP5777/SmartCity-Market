package api.suggestor;

import java.util.List;

import api.contracts.IGroceryList;
import api.contracts.IGroceryPackage;
import api.contracts.IProduct;
import api.contracts.IStorePackage;
import api.types.StoreData;
import api.types.sales.ASale;

public class Suggestor {
	private volatile static StoreData<? extends IProduct> storeData;

	@SuppressWarnings("unchecked")
	public static<P extends IProduct, T extends IGroceryPackage<P>> void updateHistory(List<? extends IGroceryList<T>> history) {
		storeData = new StoreData<P>(history, (List<? extends IStorePackage<P>>) storeData.getStock());
	}

	@SuppressWarnings("unchecked")
	public static<P extends IProduct> void updateStock(List<? extends IStorePackage<P>> stock) {
		storeData = new StoreData<P>((List<? extends IGroceryList<? extends IGroceryPackage<P>>>) storeData.getHistory(), stock);
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
	public static<P extends IProduct> ASale suggestSale(IGroceryList<? extends IGroceryPackage<P>> currentGrocery,
			IGroceryPackage<P> purchasedProduct) {
		@SuppressWarnings("unused")
		StoreData<?> currentData = storeData;
		
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
	public static ASale examineOffer(IGroceryList<? extends IGroceryPackage<? extends IProduct>> currentGrocery,
			ASale purchasedProduct) {
		@SuppressWarnings("unused")
		StoreData<?> currentData = storeData;
		
		return null;
	}

}
