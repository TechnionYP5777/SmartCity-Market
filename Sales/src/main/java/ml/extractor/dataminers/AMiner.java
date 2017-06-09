package ml.extractor.dataminers;

import java.util.Set;

import api.contracts.IGroceryProduct;
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
	
	public abstract Set<ABasicProperty> extractProperties();
}
