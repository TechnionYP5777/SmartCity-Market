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
import SQLDatabase.SQLDatabaseException.CriticalError;
import SQLDatabase.SQLDatabaseException.ProductNotExistInCatalog;
import SQLDatabase.SQLDatabaseException.ClientNotConnected;

@RunWith(MockitoJUnitRunner.class)
public class CommandExectuerAddProductPackageToWarehouseTest {

	@Mock
	private SQLDatabaseConnection sqlDatabaseConnection;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
	}
	
	@Test
	public void addProductPackageToWarehouseSuccessfulTest() {
		int senderID = 1;
		ProductPackage productPackage = new ProductPackage(new SmartCode(1, null), 1, new Location(0, 0, PlaceInMarket.WAREHOUSE));
		String command = new CommandWrapper(senderID, CommandDescriptor.ADD_PRODUCT_PACKAGE_TO_WAREHOUSE,
				new Gson().toJson(productPackage, ProductPackage.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doNothing().when(sqlDatabaseConnection).addProductPackageToWarehouse(senderID, productPackage);
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog e) {
			e.printStackTrace();
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_OK, out.getResultDescriptor());
	}
	
	@Test
	public void addProductPackageToWarehouseCriticalErrorTest() {
		int senderID = 1;
		ProductPackage productPackage = new ProductPackage(new SmartCode(1, null), 1, new Location(0, 0, PlaceInMarket.WAREHOUSE));
		String command = new CommandWrapper(senderID, CommandDescriptor.ADD_PRODUCT_PACKAGE_TO_WAREHOUSE,
				new Gson().toJson(productPackage, ProductPackage.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new CriticalError()).when(sqlDatabaseConnection).addProductPackageToWarehouse(senderID, productPackage);
		} catch (ClientNotConnected | ProductNotExistInCatalog e) {
			e.printStackTrace();
			fail();
		} catch (CriticalError __) {
			/* Success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_ERR, out.getResultDescriptor());
	}
	
	@Test
	public void addProductPackageToWarehouseClientNotConnectedTest() {
		int senderID = 1;
		ProductPackage productPackage = new ProductPackage(new SmartCode(1, null), 1, new Location(0, 0, PlaceInMarket.WAREHOUSE));
		String command = new CommandWrapper(senderID, CommandDescriptor.ADD_PRODUCT_PACKAGE_TO_WAREHOUSE,
				new Gson().toJson(productPackage, ProductPackage.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new ClientNotConnected()).when(sqlDatabaseConnection).addProductPackageToWarehouse(senderID, productPackage);
		} catch (CriticalError | ProductNotExistInCatalog e) {
			e.printStackTrace();
			fail();
		} catch (ClientNotConnected __) {
			/* Success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED, out.getResultDescriptor());
	}
	
	@Test
	public void addProductPackageToWarehouseProductNotExistInCatalogTest() {
		int senderID = 1;
		ProductPackage productPackage = new ProductPackage(new SmartCode(1, null), 1, new Location(0, 0, PlaceInMarket.WAREHOUSE));
		String command = new CommandWrapper(senderID, CommandDescriptor.ADD_PRODUCT_PACKAGE_TO_WAREHOUSE,
				new Gson().toJson(productPackage, ProductPackage.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new ProductNotExistInCatalog()).when(sqlDatabaseConnection).addProductPackageToWarehouse(senderID, productPackage);
		} catch (CriticalError | ClientNotConnected e) {
			e.printStackTrace();
			fail();
		} catch (ProductNotExistInCatalog __) {
			/* Success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_CATALOG_PRODUCT_DOES_NOT_EXIST, out.getResultDescriptor());
	}
}