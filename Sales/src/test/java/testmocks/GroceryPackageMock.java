package testmocks;

import api.contracts.IGroceryPackage;

public class GroceryPackageMock implements IGroceryPackage {

	ProductMock product;
	int amount;
	
	public GroceryPackageMock(ProductMock product, int amount) {
		super();
		this.product = product;
		this.amount = amount;
	}
	
	public GroceryPackageMock(ProductMock product) {
		super();
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
		final int prime = 31;
		int result = 1;
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
		GroceryPackageMock other = (GroceryPackageMock) obj;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		return true;
	}

}
