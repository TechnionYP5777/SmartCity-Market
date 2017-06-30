package api.types;

import java.util.ArrayList;
import java.util.List;

import api.contracts.IGroceryList;
import api.contracts.IProduct;
import api.contracts.IStorePackage;

public class StoreData {
	
	private List<? extends IGroceryList> history; 
	private List<? extends IStorePackage> stock;
	private List<? extends IProduct> catalog;

	public StoreData(List<? extends IGroceryList> history, List<? extends IStorePackage> stock , List<? extends IProduct> catalog) {
		super();
		this.history = history;
		this.stock = stock;
		this.catalog = catalog;
	}
	
	public StoreData() {
		super();
		this.history = new ArrayList<>();
		this.stock = new ArrayList<>();
		this.catalog = new ArrayList<>();
	}

	public List<? extends IGroceryList> getHistory() {
		return history;
	}
	public List<? extends IStorePackage> getStock() {
		return stock;
	}

	public List<? extends IProduct> getCatalog() {
		return catalog;
	}
	
}
