package SQLDatabaseTest;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.CustomerProfile;
import BasicCommonClasses.ForgotPasswordData;
import BasicCommonClasses.Ingredient;
import BasicCommonClasses.Location;
import BasicCommonClasses.Login;
import BasicCommonClasses.Manufacturer;
import BasicCommonClasses.PlaceInMarket;
import BasicCommonClasses.ProductPackage;
import BasicCommonClasses.SmartCode;
import ClientServerApi.ClientServerDefs;
import CommonDefs.CLIENT_TYPE;
import SQLDatabase.SQLDatabaseConnection;
import SQLDatabase.SQLDatabaseException;
import SQLDatabase.SQLDatabaseException.AuthenticationError;
import SQLDatabase.SQLDatabaseException.CriticalError;
import SQLDatabase.SQLDatabaseException.GroceryListIsEmpty;
import SQLDatabase.SQLDatabaseException.IngredientNotExist;
import SQLDatabase.SQLDatabaseException.IngredientStillUsed;
import SQLDatabase.SQLDatabaseException.ManufacturerNotExist;
import SQLDatabase.SQLDatabaseException.ManufacturerStillUsed;
import SQLDatabase.SQLDatabaseException.NumberOfConnectionsExceeded;
import SQLDatabase.SQLDatabaseException.ProductNotExistInCatalog;
import SQLDatabase.SQLDatabaseException.ProductPackageAmountNotMatch;
import SQLDatabase.SQLDatabaseException.ProductPackageNotExist;
import SQLDatabase.SQLDatabaseException.ProductStillForSale;
import UtilsImplementations.Serialization;
import SQLDatabase.SQLDatabaseException.ClientAlreadyConnected;
import SQLDatabase.SQLDatabaseException.ClientAlreadyExist;
import SQLDatabase.SQLDatabaseException.ClientNotConnected;
import SQLDatabase.SQLDatabaseException.ClientNotExist;

/**
 * @author Noam Yefet
 * @since 2016-12-14
 */
public class SQLDatabaseConnectionTest {

	final long barcodeDebug = 423324;
	final String workerName = "workdebug";
	final String customerName = "customerdebug";
	
	final LocalDate date112000 = LocalDate.of(2000, 1, 1);
	final LocalDate date232015 = LocalDate.of(2015, 3, 2);
	final Location locationWarehouse = new Location(0, 0, PlaceInMarket.WAREHOUSE);
	final Location locationStore = new Location(0, 0, PlaceInMarket.STORE);

	private CatalogProduct createDummyProduct(long barcode, String name, int manufacturerId, String manufacturerName,
			double price) {
		return new CatalogProduct(barcode, name, new HashSet<Ingredient>(),
				new Manufacturer(manufacturerId, manufacturerName), "", price, "", new HashSet<Location>());
	}

	@Test
	public void testInitialize() {
		new SQLDatabaseConnection().hashCode();
	}

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");

	}
	
	@After
	public void teardown() {
		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		ProductPackage productPackage = new ProductPackage(new SmartCode(barcodeDebug, date112000), 5,
				locationWarehouse),
				productPackage2 = new ProductPackage(new SmartCode(barcodeDebug, date232015), 5, locationWarehouse);
		try {
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("0", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage2));
			assertEquals("0", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage2));
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog e) {
			fail();
		}
		

	}

	@Test
	public void testWorkerConnection() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();

		int session = 0;
		try {
			session = sqlConnection.loginWorker("admin", "admin");

		} catch (AuthenticationError | CriticalError | ClientAlreadyConnected | NumberOfConnectionsExceeded e) {
			fail();
		}

		try {
			sqlConnection.logout(session, "admin");
		} catch (CriticalError | ClientNotConnected e) {
			fail();
		}
	}

	@Test
	public void testLogoutAll() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();

		int session = 0;
		try {
			session = sqlConnection.loginWorker("admin", "admin");
			sqlConnection.logoutAllUsers();
			session = sqlConnection.loginWorker("admin", "admin");
		} catch (AuthenticationError | CriticalError | ClientAlreadyConnected | NumberOfConnectionsExceeded e) {
			fail();
		}

		try {
			sqlConnection.logout(session, "admin");
		} catch (CriticalError | ClientNotConnected e) {
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
		String milkImage = "";
		try {
			assertEquals(sqlConnection.getProductFromCatalog(null, 1234567890),
					new Gson().toJson(new CatalogProduct(1234567890L, "חלב", ingredients, new Manufacturer(1, "תנובה"),
							"", 10.5, milkImage, locations)));
		} catch (ProductNotExistInCatalog | ClientNotConnected | CriticalError e) {
			fail();
		}
	}

	@Test
	public void testSimpleGetProductFromCatalog2() {

		final long barcodeNum = 7290004685195L;
		final int manufaturerID = 2;
		final String manufaturerName = "מאפיות ברמן";
		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();

		CatalogProduct product = null;

		try {
			product = new Gson().fromJson(sqlConnection.getProductFromCatalog(null, barcodeNum), CatalogProduct.class);
		} catch (ProductNotExistInCatalog | ClientNotConnected | CriticalError e) {
			fail();
		}

		assertEquals(product.getBarcode(), barcodeNum);
		assertEquals(product.getManufacturer().getId(), manufaturerID);
		assertEquals(product.getManufacturer().getName(), manufaturerName);
	}

	@Test
	public void testGetProductFromCatalogAfterLogoutWorker() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();

		int session = 0;

		try {
			session = sqlConnection.loginWorker("admin", "admin");
		} catch (AuthenticationError | ClientAlreadyConnected | CriticalError | NumberOfConnectionsExceeded e1) {
			fail();
		}

		try {
			sqlConnection.getProductFromCatalog(session, 1234567890);
		} catch (ClientNotConnected | ProductNotExistInCatalog | CriticalError e) {
			fail();
		}

		try {
			sqlConnection.logout(session, "admin");
		} catch (ClientNotConnected | CriticalError e1) {
			fail();
		}

		try {
			sqlConnection.getProductFromCatalog(session, 1234567890);
			fail();
		} catch (ProductNotExistInCatalog | CriticalError e) {
			fail();
		} catch (ClientNotConnected e) {
		}
	}

	@Test
	public void testGetProductFromCatalogAfterLogoutCart() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();

		int session = 0;

		try {
			session = sqlConnection.loginCustomer(ClientServerDefs.anonymousCustomerUsername, ClientServerDefs.anonymousCustomerPassword);
		} catch (AuthenticationError | ClientAlreadyConnected | CriticalError | NumberOfConnectionsExceeded e1) {
			fail();
		}

		try {
			sqlConnection.getProductFromCatalog(session, 1234567890);
		} catch (ClientNotConnected | ProductNotExistInCatalog | CriticalError e) {
			fail();
		}

		try {
			sqlConnection.logout(session, ClientServerDefs.anonymousCustomerUsername);
		} catch (ClientNotConnected | CriticalError e1) {
			fail();
		}

		try {
			sqlConnection.getProductFromCatalog(session, 1234567890);
			fail();
		} catch (ProductNotExistInCatalog | CriticalError e) {
			fail();
		} catch (ClientNotConnected e) {
		}
	}

	@Test
	public void testSimpleAddRemoveProductFromCatalog() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();

		HashSet<Ingredient> ingredients = new HashSet<Ingredient>();
		HashSet<Location> locations = new HashSet<Location>();

		CatalogProduct newProduct = new CatalogProduct(123L, "name", ingredients, new Manufacturer(1, "תנובה"), "", 20,
				"", locations);
		try {
			sqlConnection.addProductToCatalog(null, newProduct);
			assertEquals(sqlConnection.getProductFromCatalog(null, newProduct.getBarcode()),
					new Gson().toJson(newProduct));
			sqlConnection.removeProductFromCatalog(null, new SmartCode(newProduct.getBarcode(), null));
		} catch (SQLDatabaseException e) {
			fail();
		}

		try {
			sqlConnection.getProductFromCatalog(null, newProduct.getBarcode());
			fail();
		} catch (ProductNotExistInCatalog e) {
		} catch (CriticalError | ClientNotConnected e) {
			fail();
		}

	}

	@Test
	public void testAddRemoveProductRealBarcodeFromCatalog() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();

		HashSet<Ingredient> ingredients = new HashSet<Ingredient>();
		HashSet<Location> locations = new HashSet<Location>();

		CatalogProduct newProduct = new CatalogProduct(7290010328246L, "thini", ingredients,
				new Manufacturer(1, "תנובה"), "", 20, "", locations);
		try {
			sqlConnection.addProductToCatalog(null, newProduct);
			assertEquals(sqlConnection.getProductFromCatalog(null, newProduct.getBarcode()),
					new Gson().toJson(newProduct));
			sqlConnection.removeProductFromCatalog(null, new SmartCode(newProduct.getBarcode(), null));
		} catch (SQLDatabaseException e) {
			fail();
		}

		try {
			sqlConnection.getProductFromCatalog(null, newProduct.getBarcode());
			fail();
		} catch (ProductNotExistInCatalog e) {
		} catch (CriticalError | ClientNotConnected e) {
			fail();
		}

	}

	@Test
	public void testRemoveCatalogProductStillForSell() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();

		CatalogProduct newProduct = createDummyProduct(456L, "testRemoveCatalogProductStillForSell", 1, "תנובה", 3.0);
		ProductPackage newPackage = new ProductPackage(new SmartCode(newProduct.getBarcode(), date232015), 5,
				locationWarehouse);

		// add catalog-product and add it to warehouse
		try {
			sqlConnection.addProductToCatalog(null, newProduct);
			assertEquals(sqlConnection.getProductFromCatalog(null, newProduct.getBarcode()),
					new Gson().toJson(newProduct));
			sqlConnection.addProductPackageToWarehouse(null, newPackage);
			assertEquals("5", sqlConnection.getProductPackageAmonutInWarehouse(null, newPackage));
		} catch (SQLDatabaseException e) {
			fail();
		}

		// try to remove
		try {
			sqlConnection.removeProductFromCatalog(null, new SmartCode(newProduct.getBarcode(), null));
			fail();
		} catch (ProductStillForSale e1) {

		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog e1) {
			fail();
		}

		// move to shelf
		try {
			sqlConnection.placeProductPackageOnShelves(null, newPackage);
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, newPackage));
			assertEquals("5", sqlConnection.getProductPackageAmonutOnShelves(null, newPackage));
		} catch (SQLDatabaseException e) {
			fail();
		}

		// try to remove
		try {
			sqlConnection.removeProductFromCatalog(null, new SmartCode(newProduct.getBarcode(), null));
			fail();
		} catch (ProductStillForSale e1) {

		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog e1) {
			fail();
		}

		// move to shelf
		try {
			sqlConnection.removeProductPackageFromShelves(null, newPackage);
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, newPackage));
			assertEquals("0", sqlConnection.getProductPackageAmonutOnShelves(null, newPackage));
			sqlConnection.removeProductFromCatalog(null, new SmartCode(newProduct.getBarcode(), null));
		} catch (SQLDatabaseException e) {
			fail();
		}

	}

	@Test
	public void testSimpleAddRemovePakage() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();

		ProductPackage productPackage = new ProductPackage(new SmartCode(barcodeDebug, date112000), 5,
				locationWarehouse);

		try {
			sqlConnection.addProductPackageToWarehouse(null, productPackage);
			assertEquals("5", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			sqlConnection.removeProductPackageFromWarehouse(null, productPackage);
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch
				| ProductPackageNotExist e) {
			fail();
		}

	}

	@Test
	public void testAddTwicePakageToWarehouse() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();

		ProductPackage productPackage = new ProductPackage(new SmartCode(barcodeDebug, date112000), 5,
				locationWarehouse);

		try {
			sqlConnection.addProductPackageToWarehouse(null, productPackage);
			assertEquals("5", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			sqlConnection.addProductPackageToWarehouse(null, productPackage);
			assertEquals("10", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));

			productPackage.setAmount(10);
			sqlConnection.removeProductPackageFromWarehouse(null, productPackage);
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch
				| ProductPackageNotExist e) {
			fail();
		}

	}

	@Test
	public void testRemovePakageFromWarehouseTwice() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();

		ProductPackage productPackage = new ProductPackage(new SmartCode(barcodeDebug, date112000), 10,
				locationWarehouse);

		try {
			sqlConnection.addProductPackageToWarehouse(null, productPackage);
			assertEquals("10", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));

			productPackage.setAmount(5);
			sqlConnection.removeProductPackageFromWarehouse(null, productPackage);
			assertEquals("5", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			sqlConnection.removeProductPackageFromWarehouse(null, productPackage);
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch
				| ProductPackageNotExist e) {
			fail();
		}

	}

	@Test
	public void testRemoveMoreThanHaveFromWarehouse() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();

		ProductPackage productPackage = new ProductPackage(new SmartCode(barcodeDebug, date112000), 10,
				locationWarehouse);

		try {
			sqlConnection.addProductPackageToWarehouse(null, productPackage);
			assertEquals("10", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));

		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog e) {
			fail();
		}

		productPackage.setAmount(11);
		try {
			sqlConnection.removeProductPackageFromWarehouse(null, productPackage);
			fail();
		} catch (ProductPackageAmountNotMatch e) {
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductPackageNotExist e) {
			fail();
		}

		productPackage.setAmount(10);
		try {
			sqlConnection.removeProductPackageFromWarehouse(null, productPackage);
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch
				| ProductPackageNotExist e) {
			fail();
		}

	}

	@Test
	public void testRemoveNotExistedPakageFromWarehouse() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();

		ProductPackage productPackage = new ProductPackage(new SmartCode(barcodeDebug, date112000), 10,
				locationWarehouse);

		try {
			sqlConnection.removeProductPackageFromWarehouse(null, productPackage);
			fail();
		} catch (ProductPackageNotExist e) {

		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch e) {
			fail();
		}

	}

	@Test
	public void testSimpleAddMoveToShelfRemovePakage() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();

		ProductPackage productPackage = new ProductPackage(new SmartCode(barcodeDebug, date112000), 5,
				locationWarehouse);

		try {
			sqlConnection.addProductPackageToWarehouse(null, productPackage);
			assertEquals("5", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("0", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));

			sqlConnection.placeProductPackageOnShelves(null, productPackage);
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("5", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch
				| ProductPackageNotExist e) {
			fail();
		}

		try {
			sqlConnection.removeProductPackageFromWarehouse(null, productPackage);
			fail();
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch e) {
			fail();
		} catch (ProductPackageNotExist e) {

		}

		try {
			sqlConnection.removeProductPackageFromShelves(null, productPackage);
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("0", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch
				| ProductPackageNotExist e) {
			fail();
		}

		try {
			sqlConnection.close();
		} catch (CriticalError e) {
			fail();
		}
	}

	@Test
	public void testMovePakageToShelfAndRemoveTwice() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();

		ProductPackage productPackage = new ProductPackage(new SmartCode(barcodeDebug, date112000), 5,
				locationWarehouse);

		try {
			sqlConnection.addProductPackageToWarehouse(null, productPackage);
			sqlConnection.addProductPackageToWarehouse(null, productPackage);
			assertEquals("10", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("0", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));

			sqlConnection.placeProductPackageOnShelves(null, productPackage);
			assertEquals("5", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("5", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));
			sqlConnection.placeProductPackageOnShelves(null, productPackage);
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("10", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch
				| ProductPackageNotExist e) {
			fail();
		}

		try {
			sqlConnection.removeProductPackageFromWarehouse(null, productPackage);
			fail();
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch e) {
			fail();
		} catch (ProductPackageNotExist e) {

		}

		try {
			sqlConnection.removeProductPackageFromShelves(null, productPackage);
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("5", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));
			sqlConnection.removeProductPackageFromShelves(null, productPackage);
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("0", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch
				| ProductPackageNotExist e) {
			fail();
		}

	}

	@Test
	public void testSplitPackageToShelfPakage() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();

		ProductPackage productPackage = new ProductPackage(new SmartCode(barcodeDebug, date112000), 5,
				locationWarehouse),
				productPackageShelf = new ProductPackage(new SmartCode(barcodeDebug, date112000), 2, locationWarehouse);
		try {
			sqlConnection.addProductPackageToWarehouse(null, productPackage);
			assertEquals("5", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("0", sqlConnection.getProductPackageAmonutOnShelves(null, productPackageShelf));

			sqlConnection.placeProductPackageOnShelves(null, productPackageShelf);
			assertEquals(productPackage.getAmount() - productPackageShelf.getAmount() + "",
					sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals(productPackageShelf.getAmount() + "",
					sqlConnection.getProductPackageAmonutOnShelves(null, productPackageShelf));
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch
				| ProductPackageNotExist e) {
			fail();
		}

		productPackage.setAmount(3);
		try {
			sqlConnection.removeProductPackageFromWarehouse(null, productPackage);
			sqlConnection.removeProductPackageFromShelves(null, productPackageShelf);
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("0", sqlConnection.getProductPackageAmonutOnShelves(null, productPackageShelf));
		} catch (ProductPackageNotExist | CriticalError | ClientNotConnected | ProductNotExistInCatalog
				| ProductPackageAmountNotMatch e) {
			fail();
		}

	}

	@Test
	public void testCartConnection() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();

		int session = 0;
		try {
			session = sqlConnection.loginCustomer(ClientServerDefs.anonymousCustomerUsername, ClientServerDefs.anonymousCustomerPassword);
			assert sqlConnection.isClientLoggedIn(session);
		} catch (AuthenticationError | CriticalError | ClientAlreadyConnected | NumberOfConnectionsExceeded e) {
			fail();
		}

		try {
			sqlConnection.logout(session, ClientServerDefs.anonymousCustomerUsername);
			assert !sqlConnection.isClientLoggedIn(session);
		} catch (CriticalError | ClientNotConnected e) {
			fail();
		}
	}

	@Test
	public void testCartClientType() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();

		int session = 0;

		try {
			session = sqlConnection.loginCustomer(ClientServerDefs.anonymousCustomerUsername, ClientServerDefs.anonymousCustomerPassword);
			assert sqlConnection.isClientLoggedIn(session);
			assertEquals(new Gson().toJson(CLIENT_TYPE.CART), sqlConnection.getClientType(session));
		} catch (AuthenticationError | CriticalError | ClientAlreadyConnected | NumberOfConnectionsExceeded
				| ClientNotConnected e) {
			fail();
		}

		try {
			sqlConnection.logout(session, ClientServerDefs.anonymousCustomerUsername);
			assert !sqlConnection.isClientLoggedIn(session);
		} catch (CriticalError | ClientNotConnected e) {
			fail();
		}

		try {
			sqlConnection.getClientType(session);
			fail();
		} catch (ClientNotConnected e) {

		} catch (CriticalError e) {
			fail();
		}
	}

	@Test
	public void testGetManufacturers() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();

		try {

			HashSet<Manufacturer> list = new HashSet<>();

			list = Serialization.deserializeManufacturersHashSet(sqlConnection.getManufacturersList(null));

			assert list.contains(new Manufacturer(1, "תנובה"));
			assert list.contains(new Manufacturer(2, "מאפיות ברמן"));
		} catch (CriticalError | ClientNotConnected e) {
			fail();
		}
	}

	// TODO: test manufacturers methods

	@Test
	public void testRemoveMoreThanHaveFromCart() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();

		ProductPackage productPackage = new ProductPackage(new SmartCode(barcodeDebug, date112000), 10,
				locationWarehouse);

		int sessionCart = 0;
		try {

			sessionCart = sqlConnection.loginCustomer(ClientServerDefs.anonymousCustomerUsername, ClientServerDefs.anonymousCustomerPassword);
			sqlConnection.addProductPackageToWarehouse(null, productPackage);
			assertEquals("10", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));

			sqlConnection.placeProductPackageOnShelves(null, productPackage);
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("10", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));

			sqlConnection.addProductToGroceryList(sessionCart, productPackage);
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("0", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));

		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | AuthenticationError
				| ClientAlreadyConnected | NumberOfConnectionsExceeded | ProductPackageAmountNotMatch
				| ProductPackageNotExist e) {
			fail();
		}

		productPackage.setAmount(11);
		try {
			sqlConnection.removeProductFromGroceryList(sessionCart, productPackage);
			fail();
		} catch (ProductPackageAmountNotMatch e) {
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductPackageNotExist e) {
			fail();
		}

		productPackage.setAmount(10);
		try {
			sqlConnection.removeProductFromGroceryList(sessionCart, productPackage);
			sqlConnection.removeProductPackageFromShelves(null, productPackage);
			assertEquals("0", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));
			sqlConnection.logout(sessionCart, ClientServerDefs.anonymousCustomerUsername);
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch
				| ProductPackageNotExist e) {
			fail();
		}

	}

	@Test
	public void testRemoveNotExistedPakageFromCart() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();

		ProductPackage productPackage = new ProductPackage(new SmartCode(barcodeDebug, date112000), 10,
				locationWarehouse);

		int sessionCart = 0;

		try {
			sessionCart = sqlConnection.loginCustomer(ClientServerDefs.anonymousCustomerUsername, ClientServerDefs.anonymousCustomerPassword);
		} catch (AuthenticationError | ClientAlreadyConnected | CriticalError | NumberOfConnectionsExceeded e1) {
			fail();
		}

		try {
			sqlConnection.removeProductFromGroceryList(sessionCart, productPackage);
			fail();
		} catch (ProductPackageNotExist e) {

		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch e) {
			fail();
		}

		try {
			sqlConnection.addProductPackageToWarehouse(null, productPackage);
			assertEquals("10", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("0", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog e1) {
			fail();
		}

		try {
			sqlConnection.removeProductFromGroceryList(sessionCart, productPackage);
			fail();
		} catch (ProductPackageNotExist e) {
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch e) {
			fail();
		}

		try {
			sqlConnection.placeProductPackageOnShelves(null, productPackage);
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("10", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch
				| ProductPackageNotExist e1) {
			fail();
		}

		try {
			sqlConnection.removeProductFromGroceryList(sessionCart, productPackage);
			fail();
		} catch (ProductPackageNotExist e) {
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch e) {
			fail();
		}

		try {
			sqlConnection.addProductToGroceryList(sessionCart, productPackage);
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("0", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));

			sqlConnection.removeProductFromGroceryList(sessionCart, productPackage);
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("10", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch
				| ProductPackageNotExist e1) {
			fail();
		}

		try {
			sqlConnection.removeProductFromGroceryList(sessionCart, productPackage);
			fail();
		} catch (ProductPackageNotExist e) {
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch e) {
			fail();
		}

		try {
			sqlConnection.removeProductPackageFromShelves(null, productPackage);
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("0", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch
				| ProductPackageNotExist e1) {
			fail();
		}

		try {
			sqlConnection.removeProductFromGroceryList(sessionCart, productPackage);
			fail();
		} catch (ProductPackageNotExist e) {
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch e) {
			fail();
		}

		try {
			sqlConnection.logout(sessionCart, ClientServerDefs.anonymousCustomerUsername);
		} catch (ClientNotConnected | CriticalError e) {
			fail();
		}

	}

	@Test
	public void testSimpleAddMoveToCartRemovePakage() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();

		ProductPackage productPackage = new ProductPackage(new SmartCode(barcodeDebug, date112000), 5,
				locationWarehouse);

		int cartSession = 0;
		try {
			cartSession = sqlConnection.loginCustomer(ClientServerDefs.anonymousCustomerUsername, ClientServerDefs.anonymousCustomerPassword);
			sqlConnection.addProductPackageToWarehouse(null, productPackage);
			sqlConnection.placeProductPackageOnShelves(null, productPackage);
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("5", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));

			sqlConnection.addProductToGroceryList(cartSession, productPackage);
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("0", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));

			sqlConnection.removeProductFromGroceryList(cartSession, productPackage);
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("5", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));

			sqlConnection.removeProductPackageFromShelves(null, productPackage);
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("0", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch
				| ProductPackageNotExist | AuthenticationError | ClientAlreadyConnected
				| NumberOfConnectionsExceeded e) {
			fail();
		}

		try {
			sqlConnection.removeProductFromGroceryList(cartSession, productPackage);
			fail();
		} catch (ProductPackageNotExist e) {

		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch e) {
			fail();
		}

		try {
			sqlConnection.removeProductPackageFromShelves(null, productPackage);
			fail();
		} catch (ProductPackageNotExist e) {

		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch e) {
			fail();
		}

		try {
			sqlConnection.logout(cartSession, ClientServerDefs.anonymousCustomerUsername);
		} catch (ClientNotConnected | CriticalError e) {
			fail();
		}
	}

	@Test
	public void testMovePakageToCartAndRemoveTwice() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();

		ProductPackage productPackage = new ProductPackage(new SmartCode(barcodeDebug, date112000), 5,
				locationWarehouse);

		int cartSession = 0;
		try {
			cartSession = sqlConnection.loginCustomer(ClientServerDefs.anonymousCustomerUsername, ClientServerDefs.anonymousCustomerPassword);

			sqlConnection.addProductPackageToWarehouse(null, productPackage);
			sqlConnection.addProductPackageToWarehouse(null, productPackage);
			assertEquals("10", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("0", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));

			sqlConnection.placeProductPackageOnShelves(null, productPackage);
			assertEquals("5", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("5", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));
			sqlConnection.placeProductPackageOnShelves(null, productPackage);
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("10", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));

			sqlConnection.addProductToGroceryList(cartSession, productPackage);
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("5", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));

			sqlConnection.addProductToGroceryList(cartSession, productPackage);
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("0", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));

		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch
				| ProductPackageNotExist | AuthenticationError | ClientAlreadyConnected
				| NumberOfConnectionsExceeded e) {
			fail();
		}

		try {
			sqlConnection.removeProductPackageFromShelves(null, productPackage);
			fail();
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch e) {
			fail();
		} catch (ProductPackageNotExist e) {

		}

		try {
			sqlConnection.removeProductFromGroceryList(cartSession, productPackage);
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("5", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));

			sqlConnection.removeProductFromGroceryList(cartSession, productPackage);
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("10", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));

			sqlConnection.removeProductPackageFromShelves(null, productPackage);
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("5", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));

			sqlConnection.removeProductPackageFromShelves(null, productPackage);
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("0", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));

			sqlConnection.logout(cartSession, ClientServerDefs.anonymousCustomerUsername);
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch
				| ProductPackageNotExist e) {
			fail();
		}

	}

	@Test
	public void testSplitPackageToCartPakage() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();

		ProductPackage productPackage = new ProductPackage(new SmartCode(barcodeDebug, date112000), 5,
				locationWarehouse),
				productPackageCart = new ProductPackage(new SmartCode(barcodeDebug, date112000), 2, locationWarehouse);
		int cartSession = 0;

		try {
			cartSession = sqlConnection.loginCustomer(ClientServerDefs.anonymousCustomerUsername, ClientServerDefs.anonymousCustomerPassword);

			sqlConnection.addProductPackageToWarehouse(null, productPackage);
			sqlConnection.placeProductPackageOnShelves(null, productPackage);
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("5", sqlConnection.getProductPackageAmonutOnShelves(null, productPackageCart));

			sqlConnection.addProductToGroceryList(cartSession, productPackageCart);
			assertEquals(productPackage.getAmount() - productPackageCart.getAmount() + "",
					sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch
				| ProductPackageNotExist | AuthenticationError | ClientAlreadyConnected
				| NumberOfConnectionsExceeded e) {
			fail();
		}

		productPackage.setAmount(3);
		try {
			sqlConnection.removeProductPackageFromShelves(null, productPackage);
			sqlConnection.removeProductFromGroceryList(cartSession, productPackageCart);
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("2", sqlConnection.getProductPackageAmonutOnShelves(null, productPackageCart));
			sqlConnection.removeProductPackageFromShelves(null, productPackageCart);
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("0", sqlConnection.getProductPackageAmonutOnShelves(null, productPackageCart));
			sqlConnection.logout(cartSession, ClientServerDefs.anonymousCustomerUsername);
		} catch (ProductPackageNotExist | CriticalError | ClientNotConnected | ProductNotExistInCatalog
				| ProductPackageAmountNotMatch e) {
			fail();
		}

	}

	/**
	 * [[SuppressWarningsSpartan]]
	 */
	@Test
	public void testCartCheckoutDoLogout() {

		ProductPackage productPackage = new ProductPackage(new SmartCode(barcodeDebug, date112000), 5,
				locationWarehouse);

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();

		int session = 0;
		try {
			session = sqlConnection.loginCustomer(ClientServerDefs.anonymousCustomerUsername, ClientServerDefs.anonymousCustomerPassword);
			assertTrue(sqlConnection.isClientLoggedIn(session));
			sqlConnection.addProductPackageToWarehouse(null, productPackage);
			sqlConnection.placeProductPackageOnShelves(null, productPackage);
			sqlConnection.addProductToGroceryList(session, productPackage);
		} catch (AuthenticationError | CriticalError | ClientAlreadyConnected | NumberOfConnectionsExceeded
				| ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch
				| ProductPackageNotExist e) {
			fail();
		}

		try {
			sqlConnection.cartCheckout(session);
			assertFalse(sqlConnection.isClientLoggedIn(session));
		} catch (CriticalError | ClientNotConnected | GroceryListIsEmpty e) {
			fail();
		}

		try {
			sqlConnection.logout(session,ClientServerDefs.anonymousCustomerUsername);
			fail();
		} catch (ClientNotConnected e) {

		} catch (CriticalError e) {
			fail();
		}

		try {
			sqlConnection.clearGroceryListsHistory();
		} catch (CriticalError e) {
			fail();
		}
	}

	@Test
	public void testCartCantCheckoutWithEmptyList() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();

		int session = 0;
		try {
			session = sqlConnection.loginCustomer(ClientServerDefs.anonymousCustomerUsername, ClientServerDefs.anonymousCustomerPassword);
			assert sqlConnection.isClientLoggedIn(session);
		} catch (AuthenticationError | CriticalError | ClientAlreadyConnected | NumberOfConnectionsExceeded e) {
			fail();
		}

		try {
			sqlConnection.cartCheckout(session);
			fail();
		} catch (CriticalError | ClientNotConnected e) {
			fail();
		} catch (GroceryListIsEmpty e) {
		}

		try {
			sqlConnection.logout(session, ClientServerDefs.anonymousCustomerUsername);
		} catch (CriticalError | ClientNotConnected e) {
			fail();
		}
	}

	@Test
	public void testCartLogoutReturnsProductsToShelves() {

		ProductPackage productPackage = new ProductPackage(new SmartCode(barcodeDebug, date112000), 5,
				locationWarehouse);

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();

		int session = 0;
		try {
			session = sqlConnection.loginCustomer(ClientServerDefs.anonymousCustomerUsername, ClientServerDefs.anonymousCustomerPassword);
			sqlConnection.addProductPackageToWarehouse(null, productPackage);
			sqlConnection.placeProductPackageOnShelves(null, productPackage);
			assertEquals("5", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));
			sqlConnection.addProductToGroceryList(session, productPackage);
			assertEquals("0", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));
		} catch (AuthenticationError | CriticalError | ClientAlreadyConnected | NumberOfConnectionsExceeded
				| ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch
				| ProductPackageNotExist e) {
			fail();
		}

		try {
			sqlConnection.logout(session, ClientServerDefs.anonymousCustomerUsername);
			assert !sqlConnection.isClientLoggedIn(session);
			assertEquals("5", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));
			sqlConnection.removeProductPackageFromShelves(null, productPackage);
			assertEquals("0", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch
				| ProductPackageNotExist e) {
			fail();
		}
	}

	@Test
	public void testWhenCartCheckoutItProductsNotReturnToStore() {

		ProductPackage productPackage = new ProductPackage(new SmartCode(barcodeDebug, date112000), 5,
				locationWarehouse);

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();

		int session = 0;
		try {
			session = sqlConnection.loginCustomer(ClientServerDefs.anonymousCustomerUsername, ClientServerDefs.anonymousCustomerPassword);
			assert sqlConnection.isClientLoggedIn(session);
			sqlConnection.addProductPackageToWarehouse(null, productPackage);
			sqlConnection.placeProductPackageOnShelves(null, productPackage);
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("5", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));
			sqlConnection.addProductToGroceryList(session, productPackage);
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("0", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));
		} catch (AuthenticationError | CriticalError | ClientAlreadyConnected | NumberOfConnectionsExceeded
				| ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch
				| ProductPackageNotExist e) {
			fail();
		}

		try {
			sqlConnection.cartCheckout(session);
			assertEquals("0", sqlConnection.getProductPackageAmonutInWarehouse(null, productPackage));
			assertEquals("0", sqlConnection.getProductPackageAmonutOnShelves(null, productPackage));
			assert !sqlConnection.isClientLoggedIn(session);
		} catch (CriticalError | ClientNotConnected | GroceryListIsEmpty | ProductNotExistInCatalog e) {
			fail();
		}

		try {
			sqlConnection.clearGroceryListsHistory();
		} catch (CriticalError e) {
			fail();
		}
	}

	
	public void testGetEmptyIngredientsList() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		
		String result = null;
		try {
			result = sqlConnection.getIngredientsList();
		} catch (CriticalError e) {
			fail();
		}
		
		assert result != null;
		HashSet<Ingredient> set = Serialization.deserializeIngredientHashSet(result);
		assert set != null;
		assert set.isEmpty();
	}
	
	
	
	@Test
	public void testAddRemoveIngredient() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		final String ingredientName = "glotendebug";
		String result = null;
		Ingredient ingredient = null;
		
		//test add ingredient
		try {
			String tempID = sqlConnection.addIngredient(null, ingredientName);
			ingredient = Serialization.deserialize(tempID, Ingredient.class);
			
			result = sqlConnection.getIngredientsList();
		} catch (CriticalError | ClientNotConnected e) {
			fail();
		}
		
		assert result != null;
		HashSet<Ingredient> set = Serialization.deserializeIngredientHashSet(result);
		assert set != null;	
		assert set.contains(ingredient);
		
		//test remove ingredient
		try {
			sqlConnection.removeIngredient(null, ingredient);
			
			result = sqlConnection.getIngredientsList();
		} catch (CriticalError | ClientNotConnected | IngredientNotExist | IngredientStillUsed e) {
			fail();
		}
		
		assert result != null;
		set = Serialization.deserializeIngredientHashSet(result);
		assert set != null;
		assert !set.contains(ingredient);
	}
	
	@Test
	public void testEditIngredient() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		final String ingredientName = "glotendebug";
		String result = null;
		Ingredient ingredient = null;
		
		//test add ingredient
		try {
			String tempID = sqlConnection.addIngredient(null, ingredientName);
			ingredient = Serialization.deserialize(tempID, Ingredient.class);
			
			ingredient.setName("newIngredient");
			sqlConnection.editIngredient(null, ingredient);
			result = sqlConnection.getIngredientsList();
		} catch (CriticalError | ClientNotConnected | IngredientNotExist e) {
			fail();
		}
		
		assert result != null;
		HashSet<Ingredient> set = Serialization.deserializeIngredientHashSet(result);
		assert set != null;	
		assert set.contains(ingredient);
		
		//remove ingredient
		try {
			sqlConnection.removeIngredient(null, ingredient);
		} catch (CriticalError | ClientNotConnected | IngredientNotExist | IngredientStillUsed e) {
			fail();
		}
	}
	
	@Test
	public void testCantRemoveNotExistedIngredient() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		final String ingredientName = "glotendebug";
		Ingredient ingredient = new Ingredient(999,ingredientName);
		
		try {
			sqlConnection.removeIngredient(null, ingredient);
			fail();
		} catch (CriticalError | ClientNotConnected | IngredientStillUsed e) {
			fail();
		} catch (IngredientNotExist e){
		}
		

	}
	
	@Test
	public void testCantEditNotExistedIngredient() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		final String ingredientName = "glotendebug";
		Ingredient ingredient = new Ingredient(999,ingredientName);
		
		try {
			sqlConnection.editIngredient(null, ingredient);
			fail();
		} catch (CriticalError | ClientNotConnected e) {
			fail();
		} catch (IngredientNotExist e){
		}
		

	}
	
	@Test
	public void testAddRemoveManufacturer() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		final String manufacturerName = "manydebug";
		String result = null;
		Manufacturer manufacturer = null;
		
		//test add ingredient
		try {
			String tempID = sqlConnection.addManufacturer(null, manufacturerName);
			manufacturer = Serialization.deserialize(tempID, Manufacturer.class);
			
			result = sqlConnection.getManufacturersList(null);
		} catch (CriticalError | ClientNotConnected e) {
			fail();
		}
		
		assert result != null;
		HashSet<Manufacturer> set = Serialization.deserializeManufacturersHashSet(result);
		assert set != null;	
		assert set.contains(manufacturer);
		
		//test remove ingredient
		try {
			sqlConnection.removeManufacturer(null, manufacturer);
			
			result = sqlConnection.getManufacturersList(null);
		} catch (CriticalError | ClientNotConnected | ManufacturerNotExist | ManufacturerStillUsed e) {
			fail();
		}
		
		assert result != null;
		set = Serialization.deserializeManufacturersHashSet(result);
		assert set != null;
		assert !set.contains(manufacturer);
	}
	
	@Test
	public void testEditManufacturer() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		final String manufacturerName = "manydebug";
		String result = null;
		Manufacturer manufacturer = null;
		
		try {
			String tempID = sqlConnection.addManufacturer(null, manufacturerName);
			manufacturer = Serialization.deserialize(tempID, Manufacturer.class);
			manufacturer.setName("newManufacturer");
			sqlConnection.editManufacturer(null, manufacturer);
			
			result = sqlConnection.getManufacturersList(null);
		} catch (CriticalError | ClientNotConnected | ManufacturerNotExist e) {
			fail();
		}
		
		assert result != null;
		HashSet<Manufacturer> set = Serialization.deserializeManufacturersHashSet(result);
		assert set != null;	
		assert set.contains(manufacturer);
		
		try {
			sqlConnection.removeManufacturer(null, manufacturer);
		} catch (CriticalError | ClientNotConnected | ManufacturerNotExist | ManufacturerStillUsed e) {
			fail();
		}

	}
	
	@Test
	public void testCantEditNotExistedManufacturer() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		final String manufacturerName = "manydebug";
		Manufacturer manufacturer = new Manufacturer(999,manufacturerName);
		
			try {
				sqlConnection.editManufacturer(null, manufacturer);
				fail();
			} catch (CriticalError | ClientNotConnected e) {
				fail();
			} catch (ManufacturerNotExist e) {
			} 
		

	}
	
	@Test
	public void testCantRemoveNotExistedManufacturer() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		final String manufacturerName = "manydebug";
		Manufacturer manufacturer = new Manufacturer(999,manufacturerName);
		
			try {
				sqlConnection.removeManufacturer(null, manufacturer);
				fail();
			} catch (CriticalError | ClientNotConnected | ManufacturerStillUsed e) {
				fail();
			} catch (ManufacturerNotExist e) {
			} 
		

	}
	
	@Test
	public void testAddRemoveWorker() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		String result = null;
		
		//test add worker
		try {
			sqlConnection.addWorker(null, new Login(workerName, workerName), new ForgotPasswordData("", ""));
			
			result = sqlConnection.getWorkersList(null);
		} catch (CriticalError | ClientNotConnected | ClientAlreadyExist e) {
			fail();
		}
		
		assert result != null;
		HashMap<String, Boolean> map = Serialization.deserializeWorkersHashMap(result);
		assert map != null;	
		assert map.containsKey(workerName);
		assertEquals(false, map.get(workerName));
		
		//test remove worker
		try {
			sqlConnection.removeWorker(null, workerName);
			
			result = sqlConnection.getWorkersList(null);
		} catch (CriticalError | ClientNotConnected | ClientNotExist  e) {
			fail();
		}
		
		
		assert result != null;
		map = Serialization.deserializeWorkersHashMap(result);
		assert map != null;	
		assert !map.containsKey(workerName);

	}
	
	@Test
	public void testCantRemoveNotExistedWorker() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		
		try {
			sqlConnection.removeWorker(null, workerName);
			fail();
		} catch (CriticalError | ClientNotConnected e) {
			fail();
		} catch (ClientNotExist e) {
		}
		
	}
	
	@Test
	public void testCantAddWorkerAlreadyExisted() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		
		String result = null;
		
		//test add worker
		try {
			sqlConnection.addWorker(null, new Login(workerName, workerName), new ForgotPasswordData("", ""));
			
			result = sqlConnection.getWorkersList(null);
		} catch (CriticalError | ClientNotConnected | ClientAlreadyExist e) {
			fail();
		}
		
		assert result != null;
		HashMap<String, Boolean> map = Serialization.deserializeWorkersHashMap(result);
		assert map != null;	
		assert map.containsKey(workerName);
		assertEquals(false, map.get(workerName));
		
		//test add again the same worker
		try {
			sqlConnection.addWorker(null, new Login(workerName, workerName), new ForgotPasswordData("", ""));
			fail();
		} catch (CriticalError | ClientNotConnected e) {
			fail();
		}
		catch (ClientAlreadyExist e) {
		}
		
		//remove worker
		try {
			sqlConnection.removeWorker(null, workerName);
			
			result = sqlConnection.getWorkersList(null);
		} catch (CriticalError | ClientNotConnected | ClientNotExist e) {
			fail();
		}
		
		
		assert result != null;
		map = Serialization.deserializeWorkersHashMap(result);
		assert map != null;	
		assert !map.containsKey(workerName);

	}
	
	@Test
	public void testWorkerCanSetSecurityQA() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		ForgotPasswordData p = new ForgotPasswordData("question", "answer");
		String result = null;
		
		try {
			
			sqlConnection.addWorker(null, new Login(workerName, workerName), new ForgotPasswordData("", ""));
			
		} catch (CriticalError | ClientAlreadyExist | ClientNotConnected e) {
			fail();
		}
		
		try {
			sqlConnection.setSecurityQAWorker(workerName, p);
			result = sqlConnection.getSecurityQuestionWorker(workerName);
			assertTrue(sqlConnection.verifySecurityAnswerWorker(workerName, "answer"));
			assertEquals("question", result);
		} catch (CriticalError | ClientNotExist e1) {
			fail();
		} finally{
			try {
				sqlConnection.removeWorker(null, workerName);
			} catch (CriticalError | ClientNotExist | ClientNotConnected e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@Test
	public void testWorkerCanSetPassword() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		
		try {
			
			sqlConnection.addWorker(null, new Login(workerName, workerName), new ForgotPasswordData("", ""));
			
		} catch (CriticalError | ClientAlreadyExist | ClientNotConnected e) {
			fail();
		}
		
		try {
			sqlConnection.setPasswordWorker(workerName, "newPass");
		} catch (CriticalError | ClientNotExist e1) {
			fail();
		} finally{
			try {
				sqlConnection.removeWorker(null, workerName);
			} catch (CriticalError | ClientNotExist | ClientNotConnected e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@Test
	public void testWorkerCanLoginWithNewPassword() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		
		try {
			
			sqlConnection.addWorker(null, new Login(workerName, workerName), new ForgotPasswordData("", ""));
			
		} catch (CriticalError | ClientAlreadyExist | ClientNotConnected e) {
			fail();
		}
		
		try {
			sqlConnection.setPasswordWorker(workerName, "newPass");
			
			//try to login with new password
			int sessionID = sqlConnection.loginWorker(workerName, "newPass");
			sqlConnection.logout(sessionID, workerName);
		} catch (CriticalError | ClientNotExist | AuthenticationError | ClientAlreadyConnected | NumberOfConnectionsExceeded | ClientNotConnected e1) {
			fail();
		} finally{
			try {
				sqlConnection.removeWorker(null, workerName);
			} catch (CriticalError | ClientNotExist | ClientNotConnected e) {
				e.printStackTrace();
			}
		}		
		
	}
	
	@Test
	public void testCantSetSecurityQAToNotExistedWorker() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		ForgotPasswordData p = new ForgotPasswordData("question", "answer");
		
		try {
			sqlConnection.setSecurityQAWorker(workerName, p);
			fail();
		} catch (CriticalError e1) {
			fail();
		} catch (ClientNotExist e2){
		}
		
	}
	
	@Test
	public void testCantGetSecurityQusetionOfNotExistedWorker() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		
		try {
			sqlConnection.getSecurityQuestionWorker(workerName);
			fail();
		} catch (CriticalError e1) {
			fail();
		} catch (ClientNotExist e2){
		}
		
	}
	
	@Test
	public void testCantVerifySecurityAnswerOfNotExistedWorker() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		
		try {
			sqlConnection.verifySecurityAnswerWorker(workerName, "answer");
			fail();
		} catch (CriticalError e1) {
			fail();
		} catch (ClientNotExist e2){
		}
		
	}
	
	@Test
	public void testCantSetPasswordToNotExistedWorker() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		
		try {
			sqlConnection.setPasswordWorker(workerName, "newPass");
		} catch (CriticalError e1) {
			fail();
		} catch (ClientNotExist e2){
		}
		
	}
	
	
	/*
	 * test customer operations
	 */
	@Test
	public void testCanRegisterAndRemoveCustomer() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		
		try {
			
			sqlConnection.registerCustomer(customerName, customerName);
			
		} catch (CriticalError | ClientAlreadyExist e) {
			fail();
		}
		
		try {
			
			sqlConnection.removeCustomer(customerName);
			
		} catch (CriticalError |ClientNotExist e) {
			fail();
		}
	}
	
	@Test
	public void testCustomerCanLoginLogout() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		
		try {
			
			sqlConnection.registerCustomer(customerName, customerName);
			
		} catch (CriticalError | ClientAlreadyExist e) {
			fail();
		}
		
		try {
			int sessionID = sqlConnection.loginCustomer(customerName, customerName);
			sqlConnection.logout(sessionID, customerName);
		} catch (AuthenticationError | ClientAlreadyConnected | CriticalError | NumberOfConnectionsExceeded | ClientNotConnected e1) {
			fail();
		} finally{
			try {
				sqlConnection.removeCustomer(customerName);
			} catch (CriticalError | ClientNotExist e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testNotExistedCustomerCantLogin() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		
		try {
			sqlConnection.loginCustomer(customerName, customerName);
			fail();
		} catch (ClientAlreadyConnected | CriticalError | NumberOfConnectionsExceeded e1) {
			fail();
		} catch (AuthenticationError e){
		}
	}
	
	@Test
	public void testNotExistedCustomerCantLogout() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		int sessionID = 34624;
		
		try{
			sqlConnection.logout(sessionID, customerName);
		} catch (CriticalError e1) {
			fail();
		} catch (ClientNotConnected e){
		}
	}
	
	@Test
	public void testCustomerCanSetProfile() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		CustomerProfile p = new CustomerProfile(customerName, customerName, "name", "last", "number", "email", "city", "street",
				date112000,new HashSet<>(),new ForgotPasswordData("question", "answer"));
		CustomerProfile result = null;
		
		try {
			
			sqlConnection.registerCustomer(customerName, customerName);
			
		} catch (CriticalError | ClientAlreadyExist e) {
			fail();
		}
		
		try {
			sqlConnection.setCustomerProfile(customerName, p);
			result = Serialization.deserialize(sqlConnection.getCustomerProfile(customerName), CustomerProfile.class);

		} catch (CriticalError | ClientNotExist | IngredientNotExist e1) {
			fail();
		} finally{
			try {
				sqlConnection.removeCustomer(customerName);
			} catch (CriticalError | ClientNotExist e) {
				e.printStackTrace();
			}
		}
		
		assertEquals(p.getBirthdate(), result.getBirthdate());
		assertEquals(p.getCity(), result.getCity());
		assertEquals(p.getEmailAddress(), result.getEmailAddress());
		assertEquals(p.getFirstName(), result.getFirstName());
		assertEquals(p.getLastName(), result.getLastName());
		assertEquals(p.getPhoneNumber(), result.getPhoneNumber());
		assertEquals(p.getStreet(), result.getStreet());
		assertEquals(p.getUserName(), result.getUserName());
	}
	
	@Test
	public void testCustomerCanSetSecurityQA() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		ForgotPasswordData p = new ForgotPasswordData("question", "answer");
		String result = null;
		
		try {
			
			sqlConnection.registerCustomer(customerName, customerName);
			
		} catch (CriticalError | ClientAlreadyExist e) {
			fail();
		}
		
		try {
			sqlConnection.setSecurityQACustomer(customerName, p);
			result = sqlConnection.getSecurityQuestionCustomer(customerName);
			assertTrue(sqlConnection.verifySecurityAnswerCustomer(customerName, "answer"));
			assertEquals("question", result);
		} catch (CriticalError | ClientNotExist e1) {
			fail();
		} finally{
			try {
				sqlConnection.removeCustomer(customerName);
			} catch (CriticalError | ClientNotExist e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@Test
	public void testCustomerCanSetPassword() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		
		try {
			
			sqlConnection.registerCustomer(customerName, customerName);
			
		} catch (CriticalError | ClientAlreadyExist e) {
			fail();
		}
		
		try {
			sqlConnection.setPasswordCustomer(customerName, "newPass");
		} catch (CriticalError | ClientNotExist e1) {
			fail();
		} finally{
			try {
				sqlConnection.removeCustomer(customerName);
			} catch (CriticalError | ClientNotExist e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@Test
	public void testCustomerCanLoginWithNewPassword() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		
		try {
			
			sqlConnection.registerCustomer(customerName, customerName);
			
		} catch (CriticalError | ClientAlreadyExist e) {
			fail();
		}
		
		try {
			sqlConnection.setPasswordCustomer(customerName, "newPass");
			
			//try to login with new password
			int sessionID = sqlConnection.loginCustomer(customerName, "newPass");
			sqlConnection.logout(sessionID, customerName);
		} catch (CriticalError | ClientNotExist | AuthenticationError | ClientAlreadyConnected | NumberOfConnectionsExceeded | ClientNotConnected e1) {
			fail();
		} finally{
			try {
				sqlConnection.removeCustomer(customerName);
			} catch (CriticalError | ClientNotExist e) {
				e.printStackTrace();
			}
		}
		
		
		
	}
	
	@Test
	public void testCantSetProfileToNotExistedCustomer() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		CustomerProfile p = new CustomerProfile(customerName, customerName, "name", "last", "number", "email", "city", "street",
				date112000,new HashSet<>(),new ForgotPasswordData("question", "answer"));
		
		try {
			sqlConnection.setCustomerProfile(customerName, p);
			fail();

		} catch (CriticalError | IngredientNotExist e1) {
			fail();
		} catch (ClientNotExist e2){
		}
		
	}
	
	@Test
	public void testCantGetProfileToNotExistedCustomer() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		
		try {
			
			Serialization.deserialize(sqlConnection.getCustomerProfile(customerName), CustomerProfile.class);
			fail();
		} catch (CriticalError e1) {
			fail();
		} catch (ClientNotExist e2){
		}
		
	}
	
	@Test
	public void testCantSetSecurityQAToNotExistedCustomer() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		ForgotPasswordData p = new ForgotPasswordData("question", "answer");
		
		try {
			sqlConnection.setSecurityQACustomer(customerName, p);
			fail();
		} catch (CriticalError e1) {
			fail();
		} catch (ClientNotExist e2){
		}
		
	}
	
	@Test
	public void testCantGetSecurityQusetionOfNotExistedCustomer() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		
		try {
			sqlConnection.getSecurityQuestionCustomer(customerName);
			fail();
		} catch (CriticalError e1) {
			fail();
		} catch (ClientNotExist e2){
		}
		
	}
	
	@Test
	public void testCantVerifySecurityAnswerOfNotExistedCustomer() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		
		try {
			sqlConnection.verifySecurityAnswerCustomer(customerName, "answer");
			fail();
		} catch (CriticalError e1) {
			fail();
		} catch (ClientNotExist e2){
		}
		
	}
	
	@Test
	public void testCantSetPasswordToNotExistedCustomer() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		
		try {
			sqlConnection.setPasswordCustomer(customerName, "newPass");
		} catch (CriticalError e1) {
			fail();
		} catch (ClientNotExist e2){
		}
		
	}
	
	@Test
	public void testCantRemoveNotExistedCustomer() {

		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
		
		try {
			sqlConnection.removeCustomer(customerName);
		} catch (CriticalError e1) {
			fail();
		} catch (ClientNotExist e2){
		}
		
	}
	
}
