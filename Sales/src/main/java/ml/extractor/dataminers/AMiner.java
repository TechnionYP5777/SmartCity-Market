package ml.extractor.dataminers;

import java.util.List;
import java.util.Set;

import api.contracts.IGroceryList;
import api.contracts.IGroceryPackage;
import api.contracts.IProduct;
import api.contracts.IStorePackage;
import api.preferences.InputPreferences;
import api.types.StoreData;
import ml.common.property.basicproperties.ABasicProperty;

public abstract class AMiner {

	StoreData storeDate;

	InputPreferences inputPreferences;
	
	IGroceryPackage purchasedProduct;
	
	public AMiner(InputPreferences inputPreferences, StoreData storeDate, IGroceryPackage purchasedProduct) {
		super();
		this.storeDate = storeDate;
		this.inputPreferences = inputPreferences;
		this.purchasedProduct = purchasedProduct;
	}
	
	public abstract Set<? extends ABasicProperty> extractProperties();

	
	public InputPreferences getInputPreferences() {
		return inputPreferences;
	}

	public IGroceryPackage getPurchasedProduct() {
		return purchasedProduct;
	}
	
	public List<? extends IGroceryList> getHistory() {
		return storeDate.getHistory();
	}
	
	public List<? extends IStorePackage> getStock() {
		return storeDate.getStock();
	}
	
	public List<? extends IProduct> getCatalog() {
		return storeDate.getCatalog();
	}
	
}
