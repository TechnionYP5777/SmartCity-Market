package api.types.basic;

import java.time.LocalDate;

import api.contracts.IProduct;
import api.contracts.IStorePackage;
import api.types.Place;

public class CombinedStorePackage implements IStorePackage {

	IProduct product; 
	LocalDate expirationDate;
	int amount;
	
	public CombinedStorePackage(IStorePackage p) {
		super();
		this.product = p.getProduct();
		this.expirationDate = p.getExpirationDate();
		this.amount = p.getAmount();
	}
	
	public CombinedStorePackage(IProduct product, LocalDate expirationDate, int amount) {
		super();
		this.product = product;
		this.expirationDate = expirationDate;
		this.amount = amount;
	}

	@Override
	public IProduct getProduct() {
		return product;
	}

	@Override
	public LocalDate getExpirationDate() {
		return expirationDate;
	}

	@Override
	public int getAmount() {
		return amount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + amount;
		result = prime * result + ((expirationDate == null) ? 0 : expirationDate.hashCode());
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
		CombinedStorePackage other = (CombinedStorePackage) obj;
		if (amount != other.amount)
			return false;
		if (expirationDate == null) {
			if (other.expirationDate != null)
				return false;
		} else if (!expirationDate.equals(other.expirationDate))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		return true;
	}

	@Override
	public Place getPlace() {
		return null;
	}
	
	

}
