package api.contracts;

import java.time.LocalDate;

import api.types.Place;

public interface IStorePackage {
	
	IProduct getProduct();
	
	int getAmount();
	
	LocalDate getExpirationDate();
	
	Place getPlace();
}
