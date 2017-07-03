package api.contracts;

import java.util.HashSet;

public interface IProduct {
	long getBarcode();
	
	String getName();
	
	IManufacturer getManufacturer();
	
	/**
	 * return number between 0 to 1 that represent the distance from given product,
	 * where 0 is the same place and 1 is most far 
	 * @param other the other product to compare to
	 * @return
	 */
	double getNormalizeDistanceFrom(IProduct other);
	
	double getPrice();
	

	HashSet<? extends IIngredient> getIngredients();

}
