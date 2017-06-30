package testmocks;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import api.contracts.IGroceryList;
import api.types.sales.ProductSale;

public class GroceryListMock implements IGroceryList {

	String buyerName;
	LocalDate purchaseDate;
	Set<GroceryPackageMock> prodcutsSet;
	Set<ProductSale> salesSet;
	
	public GroceryListMock(String buyerName, LocalDate purchaseDate) {
		super();
		this.buyerName = buyerName;
		this.purchaseDate = purchaseDate;
		prodcutsSet = new HashSet<>();
		salesSet = new HashSet<>();
	}
	
	public GroceryListMock(String buyerName) {
		super();
		this.buyerName = buyerName;
		this.purchaseDate = LocalDate.now();
		prodcutsSet = new HashSet<>();
		salesSet = new HashSet<>();
	}
	
	@Override
	public String getBuyer() {
		return buyerName;
	}

	@Override
	public LocalDate getPurchaseDate() {
		return purchaseDate;
	}

	@Override
	public Set<GroceryPackageMock> getProductsList() {
		return prodcutsSet;
	}

	@Override
	public Set<ProductSale> getSalesList() {
		return salesSet;
	}

	public GroceryListMock setProdcutsSet(Set<GroceryPackageMock> prodcutsSet) {
		this.prodcutsSet = prodcutsSet;
		
		return this;
	}

	public GroceryListMock setSalesSet(Set<ProductSale> salesSet) {
		this.salesSet = salesSet;
		
		return this;
	}
	
	public GroceryListMock addProdcut(GroceryPackageMock prodcut) {
		this.prodcutsSet.add(prodcut);
		
		return this;
	}
	
	public GroceryListMock addProdcut(ProductMock prodcut, int amount) {
		this.prodcutsSet.add(new GroceryPackageMock(prodcut, amount));
		
		return this;
	}
	
	public GroceryListMock addProdcut(ProductMock prodcut) {
		this.prodcutsSet.add(new GroceryPackageMock(prodcut));
		
		return this;
	}

	public GroceryListMock addSale(ProductSale sale) {
		this.salesSet.add(sale);
		
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((buyerName == null) ? 0 : buyerName.hashCode());
		result = prime * result + ((purchaseDate == null) ? 0 : purchaseDate.hashCode());
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
		GroceryListMock other = (GroceryListMock) obj;
		if (buyerName == null) {
			if (other.buyerName != null)
				return false;
		} else if (!buyerName.equals(other.buyerName))
			return false;
		if (purchaseDate == null) {
			if (other.purchaseDate != null)
				return false;
		} else if (!purchaseDate.equals(other.purchaseDate))
			return false;
		return true;
	}

	
}

