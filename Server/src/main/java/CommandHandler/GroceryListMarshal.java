package CommandHandler;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.GroceryList;
import BasicCommonClasses.ProductPackage;
import api.contracts.IGroceryList;
import api.contracts.IGroceryPackage;
import api.contracts.ISale;

public class GroceryListMarshal implements IGroceryList {

	String buyer;
	LocalDate date;
	GroceryList list;
	Map<Long, CatalogProduct> catalog;
	
	public GroceryListMarshal(String buyer, LocalDate date, GroceryList list, Map<Long, CatalogProduct> catalog) {
		this.buyer = buyer;
		this.date = date;
		this.list = list;
		this.catalog = catalog;
	}
	
	public GroceryListMarshal(String buyer, LocalDate date) {
		this.buyer = buyer;
		this.date = date;
		this.list = new GroceryList();
	}
	
	@Override
	public String getBuyer() {
		return buyer;
	}

	@Override
	public LocalDate getPurchaseDate() {
		return date;
	}

	@Override
	public Set<? extends IGroceryPackage> getProductsList() {
		
		Set<ProductPackageMarshal> result;
		
		result = list.getList().values().stream().map(p -> new ProductPackageMarshal(p, catalog)).collect(Collectors.toSet());
		
		return result;
	}
	

	public void addPackage(ProductPackage newProductPackage) {
		list.addProduct(newProductPackage);
	}
	

	@Override
	public Set<? extends ISale> getSalesList() {
		return null;
	}



}
