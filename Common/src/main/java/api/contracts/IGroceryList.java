package api.contracts;

import java.time.LocalDate;
import java.util.Set;


public interface IGroceryList {
	String getBuyer();
	
	LocalDate getPurchaseDate();

//	Set<? extends IGroceryPackage> getProductsList();
	
	Set<? extends ISale> getSalesList();
}

