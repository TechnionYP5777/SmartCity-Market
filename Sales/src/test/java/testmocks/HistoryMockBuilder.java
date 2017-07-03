package testmocks;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HistoryMockBuilder {
	List<GroceryListMock> history = new ArrayList<>();
	
	public HistoryMockBuilder addGroceryToNow(String buyername, ProductMock m, int amount){
		history.add(new GroceryListMock(buyername).addProdcut(m, amount));
		return this;
	}
	
	public HistoryMockBuilder addGrocery(String buyername, LocalDate d, ProductMock m, int amount){
		history.add(new GroceryListMock(buyername, d).addProdcut(m, amount));
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
