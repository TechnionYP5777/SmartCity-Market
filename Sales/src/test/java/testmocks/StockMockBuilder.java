package testmocks;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import BasicCommonClasses.PlaceInMarket;

public class StockMockBuilder {

	private List<StorePackageMock> stock = new ArrayList<>();
	
	public StockMockBuilder addPackage(long barcode, LocalDate ed){
		stock.add(new StorePackageMock(barcode, ed));
		return this;
	}
	
	public StockMockBuilder addPackage(ProductMock m, int amount, LocalDate ed, PlaceInMarket place){
		stock.add(new StorePackageMock(m, amount, ed, place));
		return this;
	}
	
	public StockMockBuilder addPackage(StorePackageMock m){
		stock.add(m);
		return this;
	}
	
	public List<StorePackageMock> build(){
		return stock;
	}
}
