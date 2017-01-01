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
import SQLDatabase.SQLDatabaseException.CriticalError;
import SQLDatabase.SQLDatabaseException.ProductNotExistInCatalog;
import SQLDatabase.SQLDatabaseException.ClientNotConnected;

@RunWith(MockitoJUnitRunner.class)
public class CommandExecuterViewCatalogProductTest {

	@Mock
	private SQLDatabaseConnection sqlDatabaseConnection;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
	}
	
	@Test
	public void viewCatalogProductSuccessfulTest() {
		int senderID = 1;
		SmartCode smartCode = new SmartCode(0, null);
		String command = new CommandWrapper(senderID, CommandDescriptor.VIEW_PRODUCT_FROM_CATALOG,
				new Gson().toJson(smartCode, SmartCode.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(
					sqlDatabaseConnection.getProductFromCatalog(senderID, smartCode.getBarcode()))
					.thenReturn("");
		} catch (CriticalError e) {
			e.printStackTrace();
			fail();
		} catch (ProductNotExistInCatalog e) {
			e.printStackTrace();
			fail();
		} catch (ClientNotConnected e) {
			e.printStackTrace();
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_OK, out.getResultDescriptor());
	}
	
	@Test
	public void viewCatalogProductCriticalErrorTest() {
		int senderID = 1;
		SmartCode smartCode = new SmartCode(0, null);
		String command = new CommandWrapper(senderID, CommandDescriptor.VIEW_PRODUCT_FROM_CATALOG,
				new Gson().toJson(smartCode, SmartCode.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(
					sqlDatabaseConnection.getProductFromCatalog(senderID, smartCode.getBarcode()))
					.thenThrow(new CriticalError());
		} catch (CriticalError e) {
			e.printStackTrace();
			fail();
		} catch (ProductNotExistInCatalog e) {
			e.printStackTrace();
			fail();
		} catch (ClientNotConnected e) {
			e.printStackTrace();
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_ERR, out.getResultDescriptor());
	}
	
	@Test
	public void viewCatalogProductInvalidUserNameTest() {
		assertEquals(ResultDescriptor.SM_INVALID_PARAMETER,
				(new CommandExecuter(new CommandWrapper(0, CommandDescriptor.VIEW_PRODUCT_FROM_CATALOG,
						new Gson().toJson(new SmartCode(-1, null), SmartCode.class)).serialize()))
								.execute(sqlDatabaseConnection).getResultDescriptor());
	}
	
	@Test
	public void viewCatalogProductWorkerNotConnectedTest() {
		int senderID = 1;
		SmartCode smartCode = new SmartCode(0, null);
		String command = new CommandWrapper(senderID, CommandDescriptor.VIEW_PRODUCT_FROM_CATALOG,
				new Gson().toJson(smartCode, SmartCode.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(
					sqlDatabaseConnection.getProductFromCatalog(senderID, smartCode.getBarcode()))
					.thenThrow(new ClientNotConnected());
		} catch (CriticalError e) {
			e.printStackTrace();
			fail();
		} catch (ClientNotConnected e) {
			e.printStackTrace();
			fail();
		} catch (ProductNotExistInCatalog e) {
			e.printStackTrace();
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED, out.getResultDescriptor());
	}
	
	@Test
	public void viewCatalogProductProductDoesNotExistTest() {
		int senderID = 1;
		SmartCode smartCode = new SmartCode(0, null);
		String command = new CommandWrapper(senderID, CommandDescriptor.VIEW_PRODUCT_FROM_CATALOG,
				new Gson().toJson(smartCode, SmartCode.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(
					sqlDatabaseConnection.getProductFromCatalog(senderID, smartCode.getBarcode()))
					.thenThrow(new ProductNotExistInCatalog());
		} catch (CriticalError e) {
			e.printStackTrace();
			fail();
		} catch (ClientNotConnected e) {
			e.printStackTrace();
			fail();
		} catch (ProductNotExistInCatalog e) {
			e.printStackTrace();
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_CATALOG_PRODUCT_DOES_NOT_EXIST, out.getResultDescriptor());
	}
}
