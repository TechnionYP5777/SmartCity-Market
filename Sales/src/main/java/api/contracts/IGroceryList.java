package api.contracts;

import java.time.LocalDate;
import java.util.Set;

import api.types.sales.ASale;

public interface IGroceryList<T extends IGroceryPackage<? extends IProduct> > {
	String getBuyer();
	
	LocalDate getPurchaseDate();
	
	Set<T> getProductsList();
	
	Set<ASale> getSalesList();
}

