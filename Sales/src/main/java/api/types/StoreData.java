package api.types;

import java.util.List;

import api.contracts.IGroceryList;
import api.contracts.IGroceryPackage;
import api.contracts.IProduct;
import api.contracts.IStorePackage;

public class StoreData<P extends IProduct> {
	
	private List<? extends IGroceryList<? extends IGroceryPackage<P>>> history;
	private List<? extends IStorePackage<P>> stock;

	public StoreData(List<? extends IGroceryList<? extends IGroceryPackage<P>>> history, List<? extends IStorePackage<P>> stock) {
		super();
		this.history = history;
		this.stock = stock;
	}

	public List<? extends IGroceryList<? extends IGroceryPackage<P>>> getHistory() {
		return history;
	}
	public List<? extends IStorePackage<P>> getStock() {
		return stock;
	}
	
}
