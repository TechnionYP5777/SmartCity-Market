package testmocks;

import java.time.LocalDate;

import api.contracts.IStorePackage;
import api.types.Place;

public class StorePackageMock implements IStorePackage {

	ProductMock product;
	int amount;
	LocalDate expirationDate;
	Place place;
	
	public StorePackageMock(ProductMock product, int amount, LocalDate expirationDate, Place place) {
		super();
		this.product = product;
		this.amount = amount;
		this.expirationDate = expirationDate;
		this.place = place;
	}
	
	public StorePackageMock(long barcode, int amount, LocalDate expirationDate, Place place) {
		super();
		this.product = new ProductMock(barcode);
		this.amount = amount;
		this.expirationDate = expirationDate;
		this.place = place;
	}
	
	public StorePackageMock(long barcode, LocalDate ed){
		super();
		this.product = new ProductMock(barcode);
		this.amount = 1;
		this.expirationDate = ed;
		this.place = Place.STORE;
	}

	@Override
	public ProductMock getProduct() {
		return product;
	}

	@Override
	public int getAmount() {
		return amount;
	}

	@Override
	public LocalDate getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(LocalDate expirationDate) {
		this.expirationDate = expirationDate;
	}

	@Override
	public Place getPlace() {
		return place;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((expirationDate == null) ? 0 : expirationDate.hashCode());
		result = prime * result + ((place == null) ? 0 : place.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StorePackageMock other = (StorePackageMock) obj;
		if (expirationDate == null) {
			if (other.expirationDate != null)
				return false;
		} else if (!expirationDate.equals(other.expirationDate))
			return false;
		if (place != other.place)
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		return true;
	}
	

}
