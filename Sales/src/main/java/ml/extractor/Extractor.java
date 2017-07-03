package ml.extractor;

import java.util.HashSet;
import java.util.Set;

import api.contracts.IGroceryList;
import api.contracts.IProduct;
import api.preferences.InputPreferences;
import api.types.StoreData;
import ml.common.property.basicproperties.ABasicProperty;
import ml.extractor.dataminers.CustomerMiner;
import ml.extractor.dataminers.StoreStatisticsMiner;

/**
 * This class extarcts basic properties from history, stock, and user
 *  
 * @author noam
 *
 */
public class Extractor {


	public static Set<ABasicProperty> extractProperties(InputPreferences p, StoreData d, 
			IGroceryList currentGrocery, IProduct currentProduct){
		Set<ABasicProperty> result = new HashSet<>(new StoreStatisticsMiner(p, d, currentGrocery, currentProduct).extractProperties());
		
		result.addAll(new CustomerMiner(p, d, currentGrocery, currentProduct).extractProperties());
		
		return result;

	}
}
