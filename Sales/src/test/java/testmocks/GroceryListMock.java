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
		this.buyerName = buyerName;
		this.purchaseDate = purchaseDate;
		prodcutsSet = new HashSet<>();
		salesSet = new HashSet<>();
	}
	
	public GroceryListMock(String buyerName) {
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

	public GroceryListMock addSale(ProductSale s) {
		this.salesSet.add(s);
		
		return this;
	}

	@Override
	public int hashCode() {
		return 31 * (((buyerName == null) ? 0 : buyerName.hashCode()) + 31)
				+ ((purchaseDate == null) ? 0 : purchaseDate.hashCode());
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		GroceryListMock other = (GroceryListMock) o;
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

