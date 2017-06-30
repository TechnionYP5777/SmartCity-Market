package testmocks;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HistoryMockBuilder {
	List<GroceryListMock> history = new ArrayList<>();
	
	public HistoryMockBuilder addGroceryToNow(String buyername, ProductMock product, int amount){
		history.add(new GroceryListMock(buyername).addProdcut(product, amount));
		return this;
	}
	
	public HistoryMockBuilder addGrocery(String buyername, LocalDate date, ProductMock product, int amount){
		history.add(new GroceryListMock(buyername, date).addProdcut(product, amount));
		return this;
	}
	
	public HistoryMockBuilder addGrocery(GroceryListMock list){
		history.add(list);
		return this;
	}
	
	public List<GroceryListMock> build(){
		return history;
	}
}
