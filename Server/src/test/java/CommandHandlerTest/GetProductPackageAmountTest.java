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

/**
 * @author Aviad Cohen
 * @since 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class GetProductPackageAmountTest {

	@Mock
	private SQLDatabaseConnection sqlDatabaseConnection;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
	}
	
	@Test
	public void getProductPackageAmountSuccessfulTest() {
		int senderID = 1;
		ProductPackage productPackage = new ProductPackage(new SmartCode(1, null), 1, new Location(0, 0, PlaceInMarket.WAREHOUSE));
		String command = new CommandWrapper(senderID, CommandDescriptor.GET_PRODUCT_PACKAGE_AMOUNT,
				new Gson().toJson(productPackage, ProductPackage.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(!productPackage.getLocation().equals(PlaceInMarket.WAREHOUSE)
					? sqlDatabaseConnection.getProductPackageAmonutOnShelves(senderID, productPackage)
					: sqlDatabaseConnection.getProductPackageAmonutInWarehouse(senderID, productPackage))
							.thenReturn("");
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog e1) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_OK, out.getResultDescriptor());
	}
	
	@Test
	public void getProductPackageAmountStoreSuccessfulTest() {
		int senderID = 1;
		ProductPackage productPackage = new ProductPackage(new SmartCode(1, null), 1, new Location(0, 0, PlaceInMarket.STORE));
		String command = new CommandWrapper(senderID, CommandDescriptor.GET_PRODUCT_PACKAGE_AMOUNT,
				new Gson().toJson(productPackage, ProductPackage.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(!productPackage.getLocation().equals(PlaceInMarket.STORE)
					? sqlDatabaseConnection.getProductPackageAmonutOnShelves(senderID, productPackage)
					: sqlDatabaseConnection.getProductPackageAmonutInWarehouse(senderID, productPackage))
							.thenReturn("");
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog e1) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_OK, out.getResultDescriptor());
	}
	
	@Test
	public void getProductPackageAmountCriticalErrorTest() {
		int senderID = 1;
		ProductPackage productPackage = new ProductPackage(new SmartCode(1, null), 1, new Location(0, 0, PlaceInMarket.WAREHOUSE));
		String command = new CommandWrapper(senderID, CommandDescriptor.GET_PRODUCT_PACKAGE_AMOUNT,
				new Gson().toJson(productPackage, ProductPackage.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(sqlDatabaseConnection.getProductPackageAmonutOnShelves(senderID, productPackage)).thenThrow(new CriticalError());
			Mockito.when(sqlDatabaseConnection.getProductPackageAmonutInWarehouse(senderID, productPackage)).thenThrow(new CriticalError());
		} catch (ClientNotConnected | ProductNotExistInCatalog e1) {
			fail();
		} catch (CriticalError __) {
			/* Success */
		}
				
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_ERR, out.getResultDescriptor());
	}
	
	@Test
	public void getProductPackageAmountIllegalCatalogProductTest() {
		assertEquals(ResultDescriptor.SM_ERR,
				(new CommandExecuter(new CommandWrapper(1, CommandDescriptor.GET_PRODUCT_PACKAGE_AMOUNT,
						new Gson().toJson("", String.class)).serialize())).execute(sqlDatabaseConnection)
								.getResultDescriptor());
		
		assertEquals(ResultDescriptor.SM_INVALID_PARAMETER,
				(new CommandExecuter(new CommandWrapper(1, CommandDescriptor.GET_PRODUCT_PACKAGE_AMOUNT,
						new Gson().toJson(new ProductPackage(new SmartCode(1, null), -1, new Location(0, 0, PlaceInMarket.WAREHOUSE)), ProductPackage.class)).serialize())).execute(sqlDatabaseConnection)
								.getResultDescriptor());
	}
	
	@Test
	public void getProductPackageAmountClientNotConnectedTest() {
		int senderID = 1;
		ProductPackage productPackage = new ProductPackage(new SmartCode(1, null), 1, new Location(0, 0, PlaceInMarket.WAREHOUSE));
		String command = new CommandWrapper(senderID, CommandDescriptor.GET_PRODUCT_PACKAGE_AMOUNT,
				new Gson().toJson(productPackage, ProductPackage.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(sqlDatabaseConnection.getProductPackageAmonutOnShelves(senderID, productPackage)).thenThrow(new ClientNotConnected());
			Mockito.when(sqlDatabaseConnection.getProductPackageAmonutInWarehouse(senderID, productPackage)).thenThrow(new ClientNotConnected());
		} catch (CriticalError | ProductNotExistInCatalog e1) {
			fail();
		} catch (ClientNotConnected __) {
			/* Success */
		}
				
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED, out.getResultDescriptor());
	}
	
	@Test
	public void getProductPackageAmountProductNotExistInCatalogTest() {
		int senderID = 1;
		ProductPackage productPackage = new ProductPackage(new SmartCode(1, null), 1, new Location(0, 0, PlaceInMarket.WAREHOUSE));
		String command = new CommandWrapper(senderID, CommandDescriptor.GET_PRODUCT_PACKAGE_AMOUNT,
				new Gson().toJson(productPackage, ProductPackage.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(sqlDatabaseConnection.getProductPackageAmonutOnShelves(senderID, productPackage)).thenThrow(new ProductNotExistInCatalog());
			Mockito.when(sqlDatabaseConnection.getProductPackageAmonutInWarehouse(senderID, productPackage)).thenThrow(new ProductNotExistInCatalog());
		} catch (CriticalError | ClientNotConnected e1) {
			fail();
		} catch (ProductNotExistInCatalog __) {
			/* Success */
		}
				
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_CATALOG_PRODUCT_DOES_NOT_EXIST, out.getResultDescriptor());
	}
}
