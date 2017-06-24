package testmocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import api.preferences.InputPreferences;

public class DBMock {

	public static final long NUM_OF_PRODUCTS = 100;
	private static Map<Long, ProductMock> productCatalog = new HashMap<>();
	
	static {
		for (long i=1; i<=NUM_OF_PRODUCTS; i++){
			productCatalog.put(i, new ProductMock(i)
					.setName("" + i)
					.setPrice(i + 0.5)
					.setManufacturer(new ManufacturerMock("man_" + ((i+1) % 75) ))
					.addIngredients(DBMock.getFakeIngredientName(i)));
		}
	}

	private static String getFakeIngredientName(long i){
		switch ((int)i % 5) {
			case 0:
				return String.valueOf("beans");
			case 1:
				return String.valueOf("corn");
			case 2:
				return String.valueOf("rice");
			case 3:
				return String.valueOf("rice");
			case 4:
				return String.valueOf("junk");
			default:
				return String.valueOf("wont get here");
		}
	}
	
	public static ProductMock getProduct(long barcode){
		return productCatalog.get(barcode);
	}
	
	public static List<ProductMock> getCatalog(){
		return new ArrayList<>(productCatalog.values());
	}
	
	public static InputPreferences getInputPref(){
		return new InputPreferences();
	}
}
