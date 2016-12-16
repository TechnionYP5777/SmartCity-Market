package SQLDatabaseTest;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Test;

import com.google.gson.Gson;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Ingredient;
import BasicCommonClasses.Location;
import BasicCommonClasses.Manufacturer;
import BasicCommonClasses.PlaceInMarket;
import SQLDatabase.SQLDatabaseConnection;

/**
 * @author Noam Yefet
 */
public class SQLDatabaseConnectionTest {

	@Test
	public void testInitialize() {
		@SuppressWarnings("unused")
		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
	}

	@Test
	public void testSimpleGetProductFromCatalog() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();

		HashSet<Ingredient> ingredients = new HashSet<Ingredient>();
		ingredients.add(new Ingredient(123, "milk"));
		HashSet<Location> locations = new HashSet<Location>();
		locations.add(new Location(1, 1, PlaceInMarket.STORE));

		assertEquals(sqlConnection.getProductFromCatalog(1234567890), new Gson().toJson((new CatalogProduct(1234567890L,
				"Milk 3%", ingredients, new Manufacturer(334, "Tnuva"), "", 10.0, locations))));
	}

}
