package ml.extractor;

import java.util.HashSet;
import java.util.Set;

import api.contracts.IGroceryList;
import api.contracts.IGroceryProduct;
import api.preferences.InputPreferences;
import api.types.StoreData;
import ml.common.property.basicproperties.ABasicProperty;
import ml.extractor.dataminers.StoreStatisticsMiner;

public class Extractor {


	public static Set<ABasicProperty> extractProperties(InputPreferences inputPreferences, StoreData storeData, 
			IGroceryList currentGrocery, IGroceryProduct currentProduct){
		Set<ABasicProperty> result = new HashSet<>();
		
		result.addAll(new StoreStatisticsMiner(inputPreferences, storeData, currentProduct).extractProperties());
		
		return result;

	}
}
