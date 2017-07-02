package ml.extractor;

import java.util.HashSet;
import java.util.Set;

import api.contracts.IGroceryList;
import api.contracts.IGroceryPackage;
import api.contracts.IProduct;
import api.preferences.InputPreferences;
import api.types.StoreData;
import ml.common.property.basicproperties.ABasicProperty;
import ml.extractor.dataminers.StoreStatisticsMiner;

public class Extractor {


	public static Set<ABasicProperty> extractProperties(InputPreferences inputPreferences, StoreData storeData, 
			IGroceryList currentGrocery, IProduct currentProduct){
		Set<ABasicProperty> result = new HashSet<>();
		
		result.addAll(new StoreStatisticsMiner(inputPreferences, storeData, currentGrocery, currentProduct).extractProperties());
		
		return result;

	}
}
