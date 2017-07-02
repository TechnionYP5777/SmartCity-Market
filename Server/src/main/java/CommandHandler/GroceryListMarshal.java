package CommandHandler;

import java.time.LocalDate;
import java.util.Set;

import BasicCommonClasses.GroceryList;
import BasicCommonClasses.ProductPackage;
import api.contracts.IGroceryList;
import api.contracts.IGroceryPackage;
import api.contracts.ISale;

public class GroceryListMarshal implements IGroceryList {

	String buyer;
	LocalDate date;
	GroceryList list;
	
	public GroceryListMarshal(String buyer, LocalDate date, GroceryList list) {
		this.buyer = buyer;
		this.date = date;
		this.list = list;
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
		//return new HashSet<ProductPackage>(list.getList().values());
		return null;
	}
	

	public void addPackage(ProductPackage newProductPackage) {
		//list.addProduct(newProductPackage);
	}
	

	@Override
	public Set<? extends ISale> getSalesList() {
		return null;
	}



}
