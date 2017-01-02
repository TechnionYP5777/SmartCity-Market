package SQLDatabaseTest;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.HashSet;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Ingredient;
import BasicCommonClasses.Location;
import BasicCommonClasses.Manufacturer;
import BasicCommonClasses.SmartCode;
import SQLDatabase.SQLDatabaseConnection;
import SQLDatabase.SQLDatabaseException;
import SQLDatabase.SQLDatabaseException.AuthenticationError;
import SQLDatabase.SQLDatabaseException.CriticalError;
import SQLDatabase.SQLDatabaseException.IngredientNotExist;
import SQLDatabase.SQLDatabaseException.ManufacturerNotExist;
import SQLDatabase.SQLDatabaseException.NumberOfConnectionsExceeded;
import SQLDatabase.SQLDatabaseException.ProductAlreadyExistInCatalog;
import SQLDatabase.SQLDatabaseException.ProductNotExistInCatalog;
import SQLDatabase.SQLDatabaseException.ClientAlreadyConnected;
import SQLDatabase.SQLDatabaseException.ClientNotConnected;

/**
 * @author Noam Yefet
 * @since 2016-12-14
 */
public class SQLDatabaseConnectionTest {

	@Test
	public void testInitialize() {
		new SQLDatabaseConnection().hashCode();
	}

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
	}
	
	@Test
	public void testWorkerConnection() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();

		int session = 0;
		try {
			session = sqlConnection.workerLogin("admin", "admin");

		} catch (AuthenticationError | CriticalError | ClientAlreadyConnected | NumberOfConnectionsExceeded e) {
			e.printStackTrace();
			fail();
		}

		try {
			sqlConnection.workerLogout(session, "admin");
		} catch (CriticalError | ClientNotConnected e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testLogoutAll() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();

		int session = 0;
		try {
			session = sqlConnection.workerLogin("admin", "admin");
			sqlConnection.logoutAllUsers();
			session = sqlConnection.workerLogin("admin", "admin");
		} catch (AuthenticationError | CriticalError | ClientAlreadyConnected | NumberOfConnectionsExceeded e) {
			e.printStackTrace();
			fail();
		}

		try {
			sqlConnection.workerLogout(session, "admin");
		} catch (CriticalError | ClientNotConnected e) {
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
		} catch (ProductNotExistInCatalog | ClientNotConnected | CriticalError e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testSimpleGetProductFromCatalog2() {

		final long barcodeNum = 72900046;
		final int manufaturerID = 2;
		final String manufaturerName = "מאפיות ברמן";
		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		
		CatalogProduct product = null;
		
		try {
			product = new Gson().fromJson(sqlConnection.getProductFromCatalog(null, barcodeNum),
					CatalogProduct.class);
		} catch (ProductNotExistInCatalog | ClientNotConnected | CriticalError e) {
			e.printStackTrace();
			fail();
		}
		
		assertEquals(product.getBarcode(),barcodeNum);
		assertEquals(product.getManufacturer().getId(), manufaturerID);
		assertEquals(product.getManufacturer().getName(), manufaturerName);
	}
	
	@Test
	public void testSimpleAddRemoveProductFromCatalog() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();

		HashSet<Ingredient> ingredients = new HashSet<Ingredient>();
		HashSet<Location> locations = new HashSet<Location>();

		CatalogProduct newProduct = new CatalogProduct(123L,
				"name", ingredients, new Manufacturer(1, "תנובה")
				, "", 20, "", locations);
		try{
			sqlConnection.addProductToCatalog(null, newProduct);
			assertEquals(sqlConnection.getProductFromCatalog(null, newProduct.getBarcode()),
					new Gson().toJson(newProduct));
			sqlConnection.removeProductFromCatalog(null, 
					new SmartCode(newProduct.getBarcode(), null));
		} catch (SQLDatabaseException e) {
			e.printStackTrace();
			fail();
		}
		
		try {
			sqlConnection.getProductFromCatalog(null, newProduct.getBarcode());
			fail();
		} catch (ProductNotExistInCatalog e) {
			
		} catch (ClientNotConnected e) {
			e.printStackTrace();
			fail();
		} catch (CriticalError e) {
			e.printStackTrace();
			fail();
		}
		
	}

}
