package api.contracts;

import java.time.LocalDate;
import java.util.Set;

import api.types.sales.ASale;

public interface IGroceryList {
	String getBuyer();
	
	LocalDate getPurchaseDate();
	
	Set<? extends IProduct> getProductsList();
	
	Set<? extends ASale> getSalesList();
}

