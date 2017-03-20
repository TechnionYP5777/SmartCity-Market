package CommandHandlerTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gson.Gson;

import BasicCommonClasses.Location;
import BasicCommonClasses.PlaceInMarket;
import BasicCommonClasses.ProductPackage;
import BasicCommonClasses.SmartCode;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import CommandHandler.CommandExecuter;
import SQLDatabase.SQLDatabaseConnection;
import SQLDatabase.SQLDatabaseException.ClientNotConnected;
import SQLDatabase.SQLDatabaseException.CriticalError;
import SQLDatabase.SQLDatabaseException.ProductNotExistInCatalog;
import SQLDatabase.SQLDatabaseException.ProductPackageAmountNotMatch;
import SQLDatabase.SQLDatabaseException.ProductPackageNotExist;

@RunWith(MockitoJUnitRunner.class)
public class CommandExectuerAddProductToGroceryListTest {

	@Mock
	private SQLDatabaseConnection sqlDatabaseConnection;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
	}
	
	@Test
	public void addProductToGroceryListSuccessfulTest() {
		int cartID = 1;
		ProductPackage productPackage = new ProductPackage(new SmartCode(1, null), 1, new Location(0, 0, PlaceInMarket.WAREHOUSE));
		String command = new CommandWrapper(cartID, CommandDescriptor.ADD_PRODUCT_TO_GROCERY_LIST,
				new Gson().toJson(productPackage, ProductPackage.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doNothing().when(sqlDatabaseConnection).addProductToGroceryList(cartID, productPackage);
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch
				| ProductPackageNotExist e) {
			e.printStackTrace();
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_OK, out.getResultDescriptor());
	}
	
	@Test
	public void addProductToGroceryListCriticalErrorTest() {
		int cartID = 1;
		ProductPackage productPackage = new ProductPackage(new SmartCode(1, null), 1, new Location(0, 0, PlaceInMarket.WAREHOUSE));
		String command = new CommandWrapper(cartID, CommandDescriptor.ADD_PRODUCT_TO_GROCERY_LIST,
				new Gson().toJson(productPackage, ProductPackage.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new CriticalError()).when(sqlDatabaseConnection).addProductToGroceryList(cartID, productPackage);
		} catch (ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch
				| ProductPackageNotExist e) {
			e.printStackTrace();
			fail();
		} catch (CriticalError __) {
			/* Successful */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_ERR, out.getResultDescriptor());
	}
	
	@Test
	public void addProductToGroceryListProductNotExistInCatalogTest() {
		int cartID = 1;
		ProductPackage productPackage = new ProductPackage(new SmartCode(1, null), 1, new Location(0, 0, PlaceInMarket.WAREHOUSE));
		String command = new CommandWrapper(cartID, CommandDescriptor.ADD_PRODUCT_TO_GROCERY_LIST,
				new Gson().toJson(productPackage, ProductPackage.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new ProductNotExistInCatalog()).when(sqlDatabaseConnection).addProductToGroceryList(cartID, productPackage);
		} catch (ClientNotConnected | CriticalError | ProductPackageAmountNotMatch
				| ProductPackageNotExist e) {
			e.printStackTrace();
			fail();
		} catch (ProductNotExistInCatalog __) {
			/* Successful */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_CATALOG_PRODUCT_DOES_NOT_EXIST, out.getResultDescriptor());
	}
	
	@Test
	public void addProductToGroceryListProductPackageAmountNotMatchTest() {
		int cartID = 1;
		ProductPackage productPackage = new ProductPackage(new SmartCode(1, null), 1, new Location(0, 0, PlaceInMarket.WAREHOUSE));
		String command = new CommandWrapper(cartID, CommandDescriptor.ADD_PRODUCT_TO_GROCERY_LIST,
				new Gson().toJson(productPackage, ProductPackage.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new ProductPackageAmountNotMatch()).when(sqlDatabaseConnection).addProductToGroceryList(cartID, productPackage);
		} catch (ClientNotConnected | CriticalError | ProductNotExistInCatalog
				| ProductPackageNotExist e) {
			e.printStackTrace();
			fail();
		} catch (ProductPackageAmountNotMatch __) {
			/* Successful */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_PRODUCT_PACKAGE_AMOUNT_BIGGER_THEN_AVAILABLE, out.getResultDescriptor());
	}
	
	@Test
	public void addProductToGroceryListProductPackageNotExistTest() {
		int cartID = 1;
		ProductPackage productPackage = new ProductPackage(new SmartCode(1, null), 1, new Location(0, 0, PlaceInMarket.WAREHOUSE));
		String command = new CommandWrapper(cartID, CommandDescriptor.ADD_PRODUCT_TO_GROCERY_LIST,
				new Gson().toJson(productPackage, ProductPackage.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new ProductPackageNotExist()).when(sqlDatabaseConnection).addProductToGroceryList(cartID, productPackage);
		} catch (ClientNotConnected | CriticalError | ProductNotExistInCatalog
				| ProductPackageAmountNotMatch e) {
			e.printStackTrace();
			fail();
		} catch (ProductPackageNotExist __) {
			/* Successful */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_PRODUCT_PACKAGE_DOES_NOT_EXIST, out.getResultDescriptor());
	}
}