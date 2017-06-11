package api.types;

import java.util.List;

import api.contracts.IGroceryList;
import api.contracts.IStorePackage;

public class StoreData {
	
	private List<? extends IGroceryList> history;
	private List<? extends IStorePackage> stock;

	public StoreData(List<? extends IGroceryList> history, List<? extends IStorePackage> stock) {
		super();
		this.history = history;
		this.stock = stock;
	}
	
	public List<? extends IGroceryList> getHistory() {
		return history;
	}
	public List<? extends IStorePackage> getStock() {
		return stock;
	}
	
}
