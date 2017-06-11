package api.contracts;

import java.time.LocalDate;

import api.types.Place;

public interface IStorePackage<P extends IProduct> {
	
	P getProduct();
	
	int getAmount();
	
	LocalDate getExpirationDate();
	
	Place getPlace();
}
