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
public class CommandExectuerRemoveProductPackageFromGroceryListTest {

	@Mock
	private SQLDatabaseConnection sqlDatabaseConnection;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
	}
	
	@Test
	public void removeProductPackageFromGroceryListSuccessfulTest() {
		int cartID = 1;
		ProductPackage productPackage = new ProductPackage(new SmartCode(1, null), 1, new Location(0, 0, PlaceInMarket.WAREHOUSE));
		String command = new CommandWrapper(cartID, CommandDescriptor.REMOVE_PRODUCT_FROM_GROCERY_LIST,
				new Gson().toJson(productPackage, ProductPackage.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doNothing().when(sqlDatabaseConnection).removeProductFromGroceryList(cartID, productPackage);
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch
				| ProductPackageNotExist e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_OK, out.getResultDescriptor());
	}
	
	@Test
	public void removeProductPackageFromGroceryListCriticalErrorTest() {
		int cartID = 1;
		ProductPackage productPackage = new ProductPackage(new SmartCode(1, null), 1, new Location(0, 0, PlaceInMarket.WAREHOUSE));
		String command = new CommandWrapper(cartID, CommandDescriptor.REMOVE_PRODUCT_FROM_GROCERY_LIST,
				new Gson().toJson(productPackage, ProductPackage.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new CriticalError()).when(sqlDatabaseConnection).removeProductFromGroceryList(cartID, productPackage);
		} catch (ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch
				| ProductPackageNotExist e) {
			fail();
		} catch (CriticalError __) {
			/* Successful */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_ERR, out.getResultDescriptor());
	}
	
	@Test
	public void removeProductPackageFromGroceryListClientNotConnectedTest() {
		int cartID = 1;
		ProductPackage productPackage = new ProductPackage(new SmartCode(1, null), 1, new Location(0, 0, PlaceInMarket.WAREHOUSE));
		String command = new CommandWrapper(cartID, CommandDescriptor.REMOVE_PRODUCT_FROM_GROCERY_LIST,
				new Gson().toJson(productPackage, ProductPackage.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new ClientNotConnected()).when(sqlDatabaseConnection).removeProductFromGroceryList(cartID, productPackage);
		} catch (CriticalError | ProductNotExistInCatalog | ProductPackageAmountNotMatch
				| ProductPackageNotExist e) {
			fail();
		} catch (ClientNotConnected __) {
			/* Successful */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED, out.getResultDescriptor());
	}
	
	@Test
	public void removeProductToGroceryListIllegalCatalogProductTest() {
		assertEquals(ResultDescriptor.SM_ERR,
				(new CommandExecuter(new CommandWrapper(1, CommandDescriptor.REMOVE_PRODUCT_FROM_GROCERY_LIST,
						new Gson().toJson("", String.class)).serialize())).execute(sqlDatabaseConnection)
								.getResultDescriptor());
		
		assertEquals(ResultDescriptor.SM_INVALID_PARAMETER,
				(new CommandExecuter(new CommandWrapper(1, CommandDescriptor.REMOVE_PRODUCT_FROM_GROCERY_LIST,
						new Gson().toJson(new ProductPackage(new SmartCode(1, null), -1, new Location(0, 0, PlaceInMarket.WAREHOUSE)), ProductPackage.class)).serialize())).execute(sqlDatabaseConnection)
								.getResultDescriptor());
	}
	
	@Test
	public void removeProductPackageFromGroceryListProductNotExistInCatalogTest() {
		int cartID = 1;
		ProductPackage productPackage = new ProductPackage(new SmartCode(1, null), 1, new Location(0, 0, PlaceInMarket.WAREHOUSE));
		String command = new CommandWrapper(cartID, CommandDescriptor.REMOVE_PRODUCT_FROM_GROCERY_LIST,
				new Gson().toJson(productPackage, ProductPackage.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new ProductNotExistInCatalog()).when(sqlDatabaseConnection).removeProductFromGroceryList(cartID, productPackage);
		} catch (CriticalError | ClientNotConnected | ProductPackageAmountNotMatch
				| ProductPackageNotExist e) {
			fail();
		} catch (ProductNotExistInCatalog __) {
			/* Successful */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_CATALOG_PRODUCT_DOES_NOT_EXIST, out.getResultDescriptor());
	}
	
	@Test
	public void removeProductPackageFromGroceryListProductPackageAmountNotMatchTest() {
		int cartID = 1;
		ProductPackage productPackage = new ProductPackage(new SmartCode(1, null), 1, new Location(0, 0, PlaceInMarket.WAREHOUSE));
		String command = new CommandWrapper(cartID, CommandDescriptor.REMOVE_PRODUCT_FROM_GROCERY_LIST,
				new Gson().toJson(productPackage, ProductPackage.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new ProductPackageAmountNotMatch()).when(sqlDatabaseConnection).removeProductFromGroceryList(cartID, productPackage);
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog
				| ProductPackageNotExist e) {
			fail();
		} catch (ProductPackageAmountNotMatch __) {
			/* Successful */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_PRODUCT_PACKAGE_AMOUNT_BIGGER_THEN_AVAILABLE, out.getResultDescriptor());
	}
	
	@Test
	public void removeProductPackageFromGroceryListProductPackageNotExistTest() {
		int cartID = 1;
		ProductPackage productPackage = new ProductPackage(new SmartCode(1, null), 1, new Location(0, 0, PlaceInMarket.WAREHOUSE));
		String command = new CommandWrapper(cartID, CommandDescriptor.REMOVE_PRODUCT_FROM_GROCERY_LIST,
				new Gson().toJson(productPackage, ProductPackage.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new ProductPackageNotExist()).when(sqlDatabaseConnection).removeProductFromGroceryList(cartID, productPackage);
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog
				| ProductPackageAmountNotMatch e) {
			fail();
		} catch (ProductPackageNotExist __) {
			/* Successful */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_PRODUCT_PACKAGE_DOES_NOT_EXIST, out.getResultDescriptor());
	}
}