package api.contracts;

import java.time.LocalDate;

import BasicCommonClasses.PlaceInMarket;

public interface IStorePackage {
	
	IProduct getProduct();
	
	int getAmount();
	
	LocalDate getExpirationDate();
	
	PlaceInMarket getPlace();
}
