package CommandHandler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.GroceryList;
import BasicCommonClasses.Location;
import BasicCommonClasses.PlaceInMarket;
import BasicCommonClasses.ProductPackage;
import BasicCommonClasses.SmartCode;
import api.contracts.IGroceryList;

public class GroceryListHistory {

	
	public static List<? extends IGroceryList> getHistory(Map<Long, CatalogProduct> mapCatalog){
		
		ArrayList<GroceryListMarshal> result = new ArrayList<>();
		
		GroceryList gl = new GroceryList();
		gl.addProduct(new ProductPackage(new SmartCode(7290000461625L, LocalDate.of(2017, 8, 8)),
				5, 
				new Location(0, 0, PlaceInMarket.STORE)));
		
		gl.addProduct(new ProductPackage(new SmartCode(7290000461625L, LocalDate.of(2017, 7, 7)),
				5, 
				new Location(0, 0, PlaceInMarket.STORE)));
		
		gl.addProduct(new ProductPackage(new SmartCode(7290004120832L, LocalDate.of(2017, 7, 3)),
				5, 
				new Location(0, 0, PlaceInMarket.STORE)));
		
		gl.addProduct(new ProductPackage(new SmartCode(7290004120832L, LocalDate.of(2017, 7, 2)),
				5, 
				new Location(0, 0, PlaceInMarket.STORE)));
		
		gl.addProduct(new ProductPackage(new SmartCode(7290000171470L, LocalDate.of(2017, 7, 1)),
				5, 
				new Location(0, 0, PlaceInMarket.STORE)));
		

		GroceryListMarshal glm = new GroceryListMarshal("bob", LocalDate.now().minusMonths(1), gl, mapCatalog);
		
		
		
		result.add(glm);
		
		gl = new GroceryList();
		gl.addProduct(new ProductPackage(new SmartCode(7290000461625L, LocalDate.of(2017, 8, 9)),
				5, 
				new Location(0, 0, PlaceInMarket.STORE)));
		
		gl.addProduct(new ProductPackage(new SmartCode(7290000461625L, LocalDate.of(2017, 7, 20)),
				5, 
				new Location(0, 0, PlaceInMarket.STORE)));
		
		gl.addProduct(new ProductPackage(new SmartCode(7290004120832L, LocalDate.of(2017, 7, 22)),
				5, 
				new Location(0, 0, PlaceInMarket.STORE)));
		
		gl.addProduct(new ProductPackage(new SmartCode(7290004120832L, LocalDate.of(2017, 7, 21)),
				5, 
				new Location(0, 0, PlaceInMarket.STORE)));
		
		gl.addProduct(new ProductPackage(new SmartCode(7290000171470L, LocalDate.of(2017, 7, 25)),
				5, 
				new Location(0, 0, PlaceInMarket.STORE)));
		

		glm = new GroceryListMarshal("bob", LocalDate.now().minusMonths(2), gl, mapCatalog);
		
		result.add(glm);
		
		gl = new GroceryList();
		gl.addProduct(new ProductPackage(new SmartCode(7290000461625L, LocalDate.of(2017, 9, 1)),
				5, 
				new Location(0, 0, PlaceInMarket.STORE)));
		
		gl.addProduct(new ProductPackage(new SmartCode(7290000461625L, LocalDate.of(2017, 9, 2)),
				5, 
				new Location(0, 0, PlaceInMarket.STORE)));
		
		gl.addProduct(new ProductPackage(new SmartCode(7290004120832L, LocalDate.of(2017, 9, 3)),
				5, 
				new Location(0, 0, PlaceInMarket.STORE)));
		
		gl.addProduct(new ProductPackage(new SmartCode(7290004120832L, LocalDate.of(2017, 8, 21)),
				5, 
				new Location(0, 0, PlaceInMarket.STORE)));
		
		gl.addProduct(new ProductPackage(new SmartCode(7290000171470L, LocalDate.of(2017, 9, 25)),
				5, 
				new Location(0, 0, PlaceInMarket.STORE)));
		

		glm = new GroceryListMarshal("bob", LocalDate.now().minusMonths(2), gl, mapCatalog);
		
		result.add(glm);
		
		
		gl = new GroceryList();
		gl.addProduct(new ProductPackage(new SmartCode(7290000461625L, LocalDate.of(2017, 9, 1)),
				5, 
				new Location(0, 0, PlaceInMarket.STORE)));
		
		

		glm = new GroceryListMarshal("alice", LocalDate.now().minusMonths(2), gl, mapCatalog);
		
		result.add(glm);
		
		gl = new GroceryList();
		gl.addProduct(new ProductPackage(new SmartCode(7290000461625L, LocalDate.of(2017, 8, 1)),
				5, 
				new Location(0, 0, PlaceInMarket.STORE)));
		
		

		glm = new GroceryListMarshal("alice", LocalDate.now().minusMonths(1), gl, mapCatalog);
		
		result.add(glm);
		
		gl = new GroceryList();
		gl.addProduct(new ProductPackage(new SmartCode(7290000461625L, LocalDate.of(2017, 7, 25)),
				5, 
				new Location(0, 0, PlaceInMarket.STORE)));
		
		

		glm = new GroceryListMarshal("alice", LocalDate.now().minusMonths(1), gl, mapCatalog);
		
		result.add(glm);
		
		return result;
	}
	

}
