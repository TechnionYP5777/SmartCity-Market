package ml.extractor.dataminers;

import java.util.List;
import java.util.Set;

import api.contracts.IGroceryList;
import api.contracts.IGroceryProduct;
import api.contracts.IStorePackage;
import api.preferences.InputPreferences;
import api.types.StoreData;
import ml.common.property.basicproperties.ABasicProperty;

public abstract class AMiner {

	StoreData storeDate;

	InputPreferences inputPreferences;
	
	IGroceryProduct purchasedProduct;
	
	public AMiner(InputPreferences inputPreferences, StoreData storeDate, IGroceryProduct purchasedProduct) {
		super();
		this.storeDate = storeDate;
		this.inputPreferences = inputPreferences;
		this.purchasedProduct = purchasedProduct;
	}
	
	public abstract Set<? extends ABasicProperty> extractProperties();

	
	public InputPreferences getInputPreferences() {
		return inputPreferences;
	}

	public IGroceryProduct getPurchasedProduct() {
		return purchasedProduct;
	}
	
	public List<? extends IGroceryList> getHistory() {
		return storeDate.getHistory();
	}
	public List<? extends IStorePackage> getStock() {
		return storeDate.getStock();
	}
}
