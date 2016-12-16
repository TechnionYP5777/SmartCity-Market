package SQLDatabase;

import java.util.HashSet;

import com.google.gson.Gson;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Ingredient;
import BasicCommonClasses.Location;
import BasicCommonClasses.Manufacturer;
import BasicCommonClasses.PlaceInMarket;

/**
 * SqlDBConnection - Handles the server request to the SQL database.
 * 
 * 
 * @author Noam Yefet
 */
public class SQLDatabaseConnection {

	public SQLDatabaseConnection() {

	}

	public String getProductFromCatalog(long barcode) {
		HashSet<Ingredient> ingredients = new HashSet<Ingredient>();
		ingredients.add(new Ingredient(123, "milk"));
		HashSet<Location> locations = new HashSet<Location>();
		locations.add(new Location(1, 1, PlaceInMarket.STORE));

		return new Gson().toJson((new CatalogProduct(1234567890L, "Milk 3%", ingredients,
				new Manufacturer(334, "Tnuva"), "", 10.0, locations)));
	}

}
