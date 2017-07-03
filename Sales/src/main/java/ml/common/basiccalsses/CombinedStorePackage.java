package ml.common.basiccalsses;

import java.time.LocalDate;

import BasicCommonClasses.PlaceInMarket;
import api.contracts.IProduct;
import api.contracts.IStorePackage;

public class CombinedStorePackage implements IStorePackage {

	IProduct product; 
	LocalDate expirationDate;
	int amount;
	
	public CombinedStorePackage(IStorePackage p) {
		this.product = p.getProduct();
		this.expirationDate = p.getExpirationDate();
		this.amount = p.getAmount();
	}
	
	public CombinedStorePackage(IProduct product, LocalDate expirationDate, int amount) {
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
		return 31 * (31 * (amount + 31) + ((expirationDate == null) ? 0 : expirationDate.hashCode()))
				+ ((product == null) ? 0 : product.hashCode());
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		CombinedStorePackage other = (CombinedStorePackage) o;
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
	public PlaceInMarket getPlace() {
		return null;
	}
	
	

}
