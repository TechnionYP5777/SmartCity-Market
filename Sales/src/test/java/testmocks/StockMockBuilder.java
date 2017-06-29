package testmocks;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StockMockBuilder {

	List<StorePackageMock> stock = new ArrayList<>();
	
	public StockMockBuilder addPackage(long barcode, LocalDate ed){
		stock.add(new StorePackageMock(barcode, ed));
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
