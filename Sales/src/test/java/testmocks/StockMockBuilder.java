package testmocks;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import api.types.Place;

public class StockMockBuilder {

	List<StorePackageMock> stock = new ArrayList<>();
	
	public StockMockBuilder addPackage(long barcode, LocalDate ed){
		stock.add(new StorePackageMock(barcode, ed));
		return this;
	}
	
	public StockMockBuilder addPackage(ProductMock product, int amount, LocalDate ed, Place place){
		stock.add(new StorePackageMock(product, amount, ed, place));
		return this;
	}
	
	public StockMockBuilder addPackage(StorePackageMock p){
		stock.add(p);
		return this;
	}
	
	public List<StorePackageMock> build(){
		return stock;
	}
}
