package testmocks;

import java.time.LocalDate;

import BasicCommonClasses.PlaceInMarket;
import api.contracts.IStorePackage;

public class StorePackageMock implements IStorePackage {

	ProductMock product;
	int amount;
	LocalDate expirationDate;
	PlaceInMarket place;
	
	public StorePackageMock(ProductMock product, int amount, LocalDate expirationDate, PlaceInMarket place) {
		this.product = product;
		this.amount = amount;
		this.expirationDate = expirationDate;
		this.place = place;
	}
	
	public StorePackageMock(long barcode, int amount, LocalDate expirationDate, PlaceInMarket place) {
		this.product = new ProductMock(barcode);
		this.amount = amount;
		this.expirationDate = expirationDate;
		this.place = place;
	}
	
	public StorePackageMock(long barcode, LocalDate ed){
		this.product = new ProductMock(barcode);
		this.amount = 1;
		this.expirationDate = ed;
		this.place = PlaceInMarket.STORE;
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
	public PlaceInMarket getPlace() {
		return place;
	}

	@Override
	public int hashCode() {
		return 31 * (((place == null) ? 0 : place.hashCode())
				+ 31 * (((expirationDate == null) ? 0 : expirationDate.hashCode()) + 31)) + ((product == null) ? 0 : product.hashCode());
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		StorePackageMock other = (StorePackageMock) o;
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
