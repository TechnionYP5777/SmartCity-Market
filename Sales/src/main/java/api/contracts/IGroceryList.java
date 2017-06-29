package api.contracts;

import java.time.LocalDate;
import java.util.Set;

import api.types.basic.sales.ASale;

public interface IGroceryList {
	String getBuyer();
	
	LocalDate getPurchaseDate();
	
	Set<? extends IGroceryPackage> getProductsList();
	
	Set<ASale> getSalesList();
}

