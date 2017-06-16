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
			productCatalog.put(i, new ProductMock(i).setName("" + i).setPrice(i + 0.5).setManufacturer(new ManufacturerMock("man_" + ((i+1) % 75) )));
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
