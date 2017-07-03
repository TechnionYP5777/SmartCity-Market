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
				2, 
				new Location(0, 0, PlaceInMarket.STORE)));
		
		gl.addProduct(new ProductPackage(new SmartCode(7290000461625L, LocalDate.of(2017, 7, 7)),
				3, 
				new Location(0, 0, PlaceInMarket.STORE)));
		
		gl.addProduct(new ProductPackage(new SmartCode(7290106724419L, LocalDate.of(2017, 7, 3)),
				1, 
				new Location(0, 0, PlaceInMarket.STORE)));
		
		gl.addProduct(new ProductPackage(new SmartCode(7290106724419L, LocalDate.of(2017, 7, 2)),
				4, 
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
				3, 
				new Location(0, 0, PlaceInMarket.STORE)));
		
		gl.addProduct(new ProductPackage(new SmartCode(7290106724419L, LocalDate.of(2017, 7, 22)),
				2, 
				new Location(0, 0, PlaceInMarket.STORE)));
		
		gl.addProduct(new ProductPackage(new SmartCode(7290106724419L, LocalDate.of(2017, 7, 21)),
				2, 
				new Location(0, 0, PlaceInMarket.STORE)));
		
		gl.addProduct(new ProductPackage(new SmartCode(7290000171470L, LocalDate.of(2017, 7, 25)),
				1, 
				new Location(0, 0, PlaceInMarket.STORE)));
		

		glm = new GroceryListMarshal("bob", LocalDate.now().minusMonths(2), gl, mapCatalog);
		
		result.add(glm);
		
		gl = new GroceryList();
		gl.addProduct(new ProductPackage(new SmartCode(7290000461625L, LocalDate.of(2017, 9, 1)),
				2, 
				new Location(0, 0, PlaceInMarket.STORE)));
		
		gl.addProduct(new ProductPackage(new SmartCode(7290000461625L, LocalDate.of(2017, 9, 2)),
				1, 
				new Location(0, 0, PlaceInMarket.STORE)));
		
		gl.addProduct(new ProductPackage(new SmartCode(7290106724419L, LocalDate.of(2017, 9, 3)),
				1, 
				new Location(0, 0, PlaceInMarket.STORE)));
		
		gl.addProduct(new ProductPackage(new SmartCode(7290106724419L, LocalDate.of(2017, 8, 21)),
				1, 
				new Location(0, 0, PlaceInMarket.STORE)));
		
		gl.addProduct(new ProductPackage(new SmartCode(7290000171470L, LocalDate.of(2017, 9, 25)),
				2, 
				new Location(0, 0, PlaceInMarket.STORE)));
		

		glm = new GroceryListMarshal("bob", LocalDate.now().minusMonths(2), gl, mapCatalog);
		
		result.add(glm);
		
		gl = new GroceryList();

		gl.addProduct(new ProductPackage(new SmartCode(7290000066318L, LocalDate.of(2017, 9, 1)),
				1, 
				new Location(0, 0, PlaceInMarket.STORE)));
		gl.addProduct(new ProductPackage(new SmartCode(7290000171470L, LocalDate.of(2017, 9, 2)),
				2, 
				new Location(0, 0, PlaceInMarket.STORE)));
		gl.addProduct(new ProductPackage(new SmartCode(7290000174044L, LocalDate.of(2017, 9, 3)),
				1, 
				new Location(0, 0, PlaceInMarket.STORE)));
		gl.addProduct(new ProductPackage(new SmartCode(7290000461625L, LocalDate.of(2017, 9, 4)),
				1, 
				new Location(0, 0, PlaceInMarket.STORE)));
		gl.addProduct(new ProductPackage(new SmartCode(7290004685195L, LocalDate.of(2017, 9, 5)),
				1, 
				new Location(0, 0, PlaceInMarket.STORE)));
		gl.addProduct(new ProductPackage(new SmartCode(7290106724419L, LocalDate.of(2017, 9, 6)),
				2, 
				new Location(0, 0, PlaceInMarket.STORE)));
		gl.addProduct(new ProductPackage(new SmartCode(7290107937658L, LocalDate.of(2017, 9, 7)),
				3, 
				new Location(0, 0, PlaceInMarket.STORE)));
		

		glm = new GroceryListMarshal("bob", LocalDate.now().minusMonths(3), gl, mapCatalog);
		
		result.add(glm);
		
		/////////////////////////////////ALICE
		gl = new GroceryList();
		gl.addProduct(new ProductPackage(new SmartCode(1234567890L, LocalDate.of(2017, 9, 1)),
				5, 
				new Location(0, 0, PlaceInMarket.STORE)));
		
		

		glm = new GroceryListMarshal("alice", LocalDate.now().minusMonths(2), gl, mapCatalog);
		
		result.add(glm);
		
		gl = new GroceryList();
		gl.addProduct(new ProductPackage(new SmartCode(1234567890L, LocalDate.of(2017, 8, 1)),
				5, 
				new Location(0, 0, PlaceInMarket.STORE)));
		
		

		glm = new GroceryListMarshal("alice", LocalDate.now().minusMonths(1), gl, mapCatalog);
		
		result.add(glm);
		
		gl = new GroceryList();
		gl.addProduct(new ProductPackage(new SmartCode(1234567890L, LocalDate.of(2017, 7, 25)),
				5, 
				new Location(0, 0, PlaceInMarket.STORE)));
		
		

		glm = new GroceryListMarshal("alice", LocalDate.now().minusMonths(1), gl, mapCatalog);
		
		result.add(glm);
		
		
		///////////////////////////////C1
		gl = new GroceryList();
		gl.addProduct(new ProductPackage(new SmartCode(1234567890L, LocalDate.of(2017, 9, 1)),
				5, 
				new Location(0, 0, PlaceInMarket.STORE)));
		gl.addProduct(new ProductPackage(new SmartCode(7290000066318L, LocalDate.of(2017, 9, 1)),
				5, 
				new Location(0, 0, PlaceInMarket.STORE)));
		gl.addProduct(new ProductPackage(new SmartCode(7290000171470L, LocalDate.of(2017, 9, 1)),
				5, 
				new Location(0, 0, PlaceInMarket.STORE)));
		gl.addProduct(new ProductPackage(new SmartCode(7290000174044L, LocalDate.of(2017, 9, 1)),
				5, 
				new Location(0, 0, PlaceInMarket.STORE)));
		gl.addProduct(new ProductPackage(new SmartCode(7290000461625L, LocalDate.of(2017, 9, 1)),
				5, 
				new Location(0, 0, PlaceInMarket.STORE)));
		gl.addProduct(new ProductPackage(new SmartCode(7290004685195L, LocalDate.of(2017, 9, 1)),
				5, 
				new Location(0, 0, PlaceInMarket.STORE)));
		gl.addProduct(new ProductPackage(new SmartCode(7290106724419L, LocalDate.of(2017, 9, 1)),
				5, 
				new Location(0, 0, PlaceInMarket.STORE)));
		gl.addProduct(new ProductPackage(new SmartCode(7290107937658L, LocalDate.of(2017, 9, 1)),
				5, 
				new Location(0, 0, PlaceInMarket.STORE)));
		

		glm = new GroceryListMarshal("c1", LocalDate.now().minusMonths(2), gl, mapCatalog);
		
		result.add(glm);
		
		return result;
	}
	

}
