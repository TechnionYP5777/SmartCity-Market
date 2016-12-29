package SQLDatabaseTest;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Test;

import com.google.gson.Gson;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Ingredient;
import BasicCommonClasses.Location;
import BasicCommonClasses.Manufacturer;
import SQLDatabase.SQLDatabaseConnection;
import SQLDatabase.SQLDatabaseException.AuthenticationError;
import SQLDatabase.SQLDatabaseException.CriticalError;
import SQLDatabase.SQLDatabaseException.NumberOfConnectionsExceeded;
import SQLDatabase.SQLDatabaseException.ProductNotExistInCatalog;
import SQLDatabase.SQLDatabaseException.WorkerAlreadyConnected;
import SQLDatabase.SQLDatabaseException.WorkerNotConnected;

/**
 * @author Noam Yefet
 * @since 2016-12-14
 */
public class SQLDatabaseConnectionTest {

	@Test
	public void testInitialize() {
		new SQLDatabaseConnection().hashCode();
	}

	@Test
	public void testWorkerConnection() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();

		int session = 0;
		try {
			session = sqlConnection.WorkerLogin("admin", "admin");

		} catch (AuthenticationError | CriticalError | WorkerAlreadyConnected | NumberOfConnectionsExceeded e) {
			e.printStackTrace();
			fail();
		}

		try {
			sqlConnection.WorkerLogout(session, "admin");
		} catch (CriticalError | WorkerNotConnected e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testSimpleGetProductFromCatalog() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();

		HashSet<Ingredient> ingredients = new HashSet<Ingredient>();
		ingredients.add(new Ingredient(1, "חלב"));
		HashSet<Location> locations = new HashSet<Location>();
		// locations.add(new Location(1, 1, PlaceInMarket.STORE));
		// TODO: noam - check the imageUrl addition
		String milkImage = "";
		try {
			assertEquals(sqlConnection.getProductFromCatalog(null, 1234567890),
					new Gson().toJson((new CatalogProduct(1234567890L, "חלב", ingredients, new Manufacturer(1, "תנובה"),
							"", 10.5, milkImage, locations))));
		} catch (ProductNotExistInCatalog | WorkerNotConnected | CriticalError e) {
			e.printStackTrace();
			fail();
		}
	}

}
