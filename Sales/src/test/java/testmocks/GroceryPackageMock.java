package testmocks;

import api.contracts.IGroceryPackage;

public class GroceryPackageMock implements IGroceryPackage {

	ProductMock product;
	int amount;
	
	public GroceryPackageMock(ProductMock product, int amount) {
		this.product = product;
		this.amount = amount;
	}
	
	public GroceryPackageMock(ProductMock product) {
		this.product = product;
		this.amount = 1;
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
	public int hashCode() {
		return 31 + ((product == null) ? 0 : product.hashCode());
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		GroceryPackageMock other = (GroceryPackageMock) o;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		return true;
	}

}
