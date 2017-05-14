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

import BasicCommonClasses.SmartCode;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import CommandHandler.CommandExecuter;
import SQLDatabase.SQLDatabaseConnection;
import SQLDatabase.SQLDatabaseException.ClientNotConnected;
import SQLDatabase.SQLDatabaseException.CriticalError;
import SQLDatabase.SQLDatabaseException.ProductNotExistInCatalog;
import SQLDatabase.SQLDatabaseException.ProductStillForSale;

@RunWith(MockitoJUnitRunner.class)
public class RemoveProductFromCatalogTest {

	@Mock
	private SQLDatabaseConnection sqlDatabaseConnection;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
	}
	
	@Test
	public void removeCatalogProductSuccessfulTest() {
		int senderID = 1;
		SmartCode smartCode = new SmartCode(1, null);
		String command = new CommandWrapper(senderID, CommandDescriptor.REMOVE_PRODUCT_FROM_CATALOG,
				new Gson().toJson(smartCode, SmartCode.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doNothing().when(sqlDatabaseConnection).removeProductFromCatalog(senderID, smartCode);
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductStillForSale e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_OK, out.getResultDescriptor());
	}
	
	@Test
	public void removeCatalogProductInvalidParamTest() {
		int senderID = 1;
		SmartCode smartCode = new SmartCode(-1, null);
		String command = new CommandWrapper(senderID, CommandDescriptor.REMOVE_PRODUCT_FROM_CATALOG,
				new Gson().toJson(smartCode, SmartCode.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doNothing().when(sqlDatabaseConnection).removeProductFromCatalog(senderID, smartCode);
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | ProductStillForSale e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_INVALID_PARAMETER, out.getResultDescriptor());
	}
	
	@Test
	public void removeCatalogProductIllegalCatalogProductTest() {
		assertEquals(ResultDescriptor.SM_ERR,
				(new CommandExecuter(new CommandWrapper(1, CommandDescriptor.REMOVE_PRODUCT_FROM_CATALOG,
						new Gson().toJson("", String.class)).serialize())).execute(sqlDatabaseConnection)
								.getResultDescriptor());
	}
	
	@Test
	public void removeCatalogProductClientNotConnectedTest() {
		int senderID = 1;
		SmartCode smartCode = new SmartCode(1, null);
		String command = new CommandWrapper(senderID, CommandDescriptor.REMOVE_PRODUCT_FROM_CATALOG,
				new Gson().toJson(smartCode, SmartCode.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new ClientNotConnected()).when(sqlDatabaseConnection).removeProductFromCatalog(senderID, smartCode);
		} catch (CriticalError | ProductNotExistInCatalog | ProductStillForSale e) {
			fail();
		} catch (ClientNotConnected e) {
			/* success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED, out.getResultDescriptor());
	}
	
	@Test
	public void removeCatalogProductCriticalErrorTest() {
		int senderID = 1;
		SmartCode smartCode = new SmartCode(1, null);
		String command = new CommandWrapper(senderID, CommandDescriptor.REMOVE_PRODUCT_FROM_CATALOG,
				new Gson().toJson(smartCode, SmartCode.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new CriticalError()).when(sqlDatabaseConnection).removeProductFromCatalog(senderID, smartCode);
		} catch (ClientNotConnected | ProductNotExistInCatalog | ProductStillForSale e) {
			fail();
		} catch (CriticalError e) {
			/* success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_ERR, out.getResultDescriptor());
	}
	
	@Test
	public void removeCatalogProductNotExistInCatalogTest() {
		int senderID = 1;
		SmartCode smartCode = new SmartCode(1, null);
		String command = new CommandWrapper(senderID, CommandDescriptor.REMOVE_PRODUCT_FROM_CATALOG,
				new Gson().toJson(smartCode, SmartCode.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new ProductNotExistInCatalog()).when(sqlDatabaseConnection).removeProductFromCatalog(senderID, smartCode);
		} catch (CriticalError | ClientNotConnected | ProductStillForSale e) {
			fail();
		} catch (ProductNotExistInCatalog e) {
			/* success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_CATALOG_PRODUCT_DOES_NOT_EXIST, out.getResultDescriptor());
	}
	
	@Test
	public void removeCatalogProductStillForSaleTest() {
		int senderID = 1;
		SmartCode smartCode = new SmartCode(1, null);
		String command = new CommandWrapper(senderID, CommandDescriptor.REMOVE_PRODUCT_FROM_CATALOG,
				new Gson().toJson(smartCode, SmartCode.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new ProductStillForSale()).when(sqlDatabaseConnection).removeProductFromCatalog(senderID, smartCode);
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog e) {
			fail();
		} catch (ProductStillForSale e) {
			/* success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_CATALOG_PRODUCT_STILL_FOR_SALE, out.getResultDescriptor());
	}
}