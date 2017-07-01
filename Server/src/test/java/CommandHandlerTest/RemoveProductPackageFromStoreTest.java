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

/**
 * @author Aviad Cohen
 * @since 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class RemoveProductPackageFromStoreTest {

	@Mock
	private SQLDatabaseConnection sqlDatabaseConnection;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
	}
	
	@Test
	public void removeProductPackageFromStoreSuccessfulTest() {
		int senderID = 1;
		ProductPackage productPackage = new ProductPackage(new SmartCode(1, null), 1, new Location(0, 0, PlaceInMarket.STORE));
		String command = new CommandWrapper(senderID, CommandDescriptor.REMOVE_PRODUCT_PACKAGE_FROM_STORE,
				new Gson().toJson(productPackage, ProductPackage.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			if (productPackage.getLocation().equals(PlaceInMarket.STORE))
				Mockito.doNothing().when(sqlDatabaseConnection).removeProductPackageFromShelves(senderID, productPackage);
			else
				Mockito.doNothing().when(sqlDatabaseConnection).removeProductPackageFromWarehouse(senderID, productPackage);			
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch
				| ProductPackageNotExist e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_OK, out.getResultDescriptor());
	}
	
	@Test
	public void removeProductPackageFromStoreCriticalErrorTest() {
		int senderID = 1;
		ProductPackage productPackage = new ProductPackage(new SmartCode(1, null), 1, new Location(0, 0, PlaceInMarket.WAREHOUSE));
		String command = new CommandWrapper(senderID, CommandDescriptor.REMOVE_PRODUCT_PACKAGE_FROM_STORE,
				new Gson().toJson(productPackage, ProductPackage.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			if (productPackage.getLocation().equals(PlaceInMarket.STORE))
				Mockito.doThrow(new CriticalError()).when(sqlDatabaseConnection).removeProductPackageFromShelves(senderID, productPackage);
			else
				Mockito.doThrow(new CriticalError()).when(sqlDatabaseConnection).removeProductPackageFromWarehouse(senderID, productPackage);
		} catch (ClientNotConnected | ProductNotExistInCatalog | ProductPackageAmountNotMatch
				| ProductPackageNotExist e) {
			fail();
		} catch (CriticalError __) {
			/* Success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_ERR, out.getResultDescriptor());
	}
	
	@Test
	public void removeProductPackageFromStoreClientNotConnectedTest() {
		int senderID = 1;
		ProductPackage productPackage = new ProductPackage(new SmartCode(1, null), 1, new Location(0, 0, PlaceInMarket.WAREHOUSE));
		String command = new CommandWrapper(senderID, CommandDescriptor.REMOVE_PRODUCT_PACKAGE_FROM_STORE,
				new Gson().toJson(productPackage, ProductPackage.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			if (productPackage.getLocation().equals(PlaceInMarket.STORE))
				Mockito.doThrow(new ClientNotConnected()).when(sqlDatabaseConnection).removeProductPackageFromShelves(senderID, productPackage);
			else
				Mockito.doThrow(new ClientNotConnected()).when(sqlDatabaseConnection).removeProductPackageFromWarehouse(senderID, productPackage);		} catch (CriticalError | ProductNotExistInCatalog | ProductPackageAmountNotMatch
				| ProductPackageNotExist e) {
			fail();
		} catch (ClientNotConnected __) {
			/* Success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED, out.getResultDescriptor());
	}
	
	@Test
	public void placeProductPackageOnShelvesIllegalCatalogProductTest() {
		assertEquals(ResultDescriptor.SM_ERR,
				(new CommandExecuter(new CommandWrapper(1, CommandDescriptor.REMOVE_PRODUCT_PACKAGE_FROM_STORE,
						new Gson().toJson("", String.class)).serialize())).execute(sqlDatabaseConnection)
								.getResultDescriptor());
		
		assertEquals(ResultDescriptor.SM_INVALID_PARAMETER,
				(new CommandExecuter(new CommandWrapper(1, CommandDescriptor.REMOVE_PRODUCT_PACKAGE_FROM_STORE,
						new Gson().toJson(new ProductPackage(new SmartCode(1, null), -1, new Location(0, 0, PlaceInMarket.WAREHOUSE)), ProductPackage.class)).serialize())).execute(sqlDatabaseConnection)
								.getResultDescriptor());
	}
	
	@Test
	public void removeProductPackageFromStoreProductNotExistInCatalogTest() {
		int senderID = 1;
		ProductPackage productPackage = new ProductPackage(new SmartCode(1, null), 1, new Location(0, 0, PlaceInMarket.WAREHOUSE));
		String command = new CommandWrapper(senderID, CommandDescriptor.REMOVE_PRODUCT_PACKAGE_FROM_STORE,
				new Gson().toJson(productPackage, ProductPackage.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			if (productPackage.getLocation().equals(PlaceInMarket.STORE))
				Mockito.doThrow(new ProductNotExistInCatalog()).when(sqlDatabaseConnection).removeProductPackageFromShelves(senderID, productPackage);
			else
				Mockito.doThrow(new ProductNotExistInCatalog()).when(sqlDatabaseConnection).removeProductPackageFromWarehouse(senderID, productPackage);		} catch (CriticalError | ClientNotConnected | ProductPackageAmountNotMatch
				| ProductPackageNotExist e) {
			fail();
		} catch (ProductNotExistInCatalog __) {
			/* Success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_CATALOG_PRODUCT_DOES_NOT_EXIST, out.getResultDescriptor());
	}
	
	@Test
	public void removeProductPackageFromStoreProductPackageAmountNotMatchTest() {
		int senderID = 1;
		ProductPackage productPackage = new ProductPackage(new SmartCode(1, null), 1, new Location(0, 0, PlaceInMarket.WAREHOUSE));
		String command = new CommandWrapper(senderID, CommandDescriptor.REMOVE_PRODUCT_PACKAGE_FROM_STORE,
				new Gson().toJson(productPackage, ProductPackage.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			if (productPackage.getLocation().equals(PlaceInMarket.STORE))
				Mockito.doThrow(new ProductPackageAmountNotMatch()).when(sqlDatabaseConnection).removeProductPackageFromShelves(senderID, productPackage);
			else
				Mockito.doThrow(new ProductPackageAmountNotMatch()).when(sqlDatabaseConnection).removeProductPackageFromWarehouse(senderID, productPackage);		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog
				| ProductPackageNotExist e) {
			fail();
		} catch (ProductPackageAmountNotMatch __) {
			/* Success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_PRODUCT_PACKAGE_AMOUNT_BIGGER_THEN_AVAILABLE, out.getResultDescriptor());
	}
	
	@Test
	public void removeProductPackageFromStoreProductPackageNotExistTest() {
		int senderID = 1;
		ProductPackage productPackage = new ProductPackage(new SmartCode(1, null), 1, new Location(0, 0, PlaceInMarket.WAREHOUSE));
		String command = new CommandWrapper(senderID, CommandDescriptor.REMOVE_PRODUCT_PACKAGE_FROM_STORE,
				new Gson().toJson(productPackage, ProductPackage.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			if (productPackage.getLocation().equals(PlaceInMarket.STORE))
				Mockito.doThrow(new ProductPackageNotExist()).when(sqlDatabaseConnection).removeProductPackageFromShelves(senderID, productPackage);
			else
				Mockito.doThrow(new ProductPackageNotExist()).when(sqlDatabaseConnection).removeProductPackageFromWarehouse(senderID, productPackage);		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog
				| ProductPackageAmountNotMatch e) {
			fail();
		} catch (ProductPackageNotExist __) {
			/* Success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_PRODUCT_PACKAGE_DOES_NOT_EXIST, out.getResultDescriptor());
	}
}
