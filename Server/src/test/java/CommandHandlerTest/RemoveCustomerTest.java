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

import ClientServerApi.ClientServerDefs;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import CommandHandler.CommandExecuter;
import SQLDatabase.SQLDatabaseConnection;
import SQLDatabase.SQLDatabaseException.ClientNotExist;
import SQLDatabase.SQLDatabaseException.CriticalError;

@RunWith(MockitoJUnitRunner.class)
public class RemoveCustomerTest {

	@Mock
	private SQLDatabaseConnection sqlDatabaseConnection;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
	}
	
	private static final String username = "username";
	private static final int senderID = 1;
	
	@Test
	public void removeCustomerSuccessfulTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.REMOVE_CUSTOMER,
				new Gson().toJson(username, String.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doNothing().when(sqlDatabaseConnection).removeCustomer(username);
		} catch (CriticalError | ClientNotExist e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_OK, out.getResultDescriptor());
	}
	
	@Test
	public void removeCustomerCriticalErrorTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.REMOVE_CUSTOMER,
				new Gson().toJson(username, String.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new CriticalError()).when(sqlDatabaseConnection).removeCustomer(username);
		} catch (ClientNotExist e) {
			fail();
		} catch (CriticalError e) {
			/* success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_ERR, out.getResultDescriptor());
	}
	
	@Test
	public void removeCustomerClientNotExistTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.REMOVE_CUSTOMER,
				new Gson().toJson(username, String.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new ClientNotExist()).when(sqlDatabaseConnection).removeCustomer(username);
		} catch (CriticalError e) {
			fail();
		} catch (ClientNotExist e) {
			/* success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_USERNAME_DOES_NOT_EXIST, out.getResultDescriptor());
	}
	
	@Test
	public void removeCatalogProductIllegalCatalogProductTest() {
		assertEquals(ResultDescriptor.SM_INVALID_PARAMETER,
				(new CommandExecuter(new CommandWrapper(1, CommandDescriptor.REMOVE_CUSTOMER,
						new Gson().toJson(ClientServerDefs.anonymousCustomerUsername, String.class)).serialize())).execute(sqlDatabaseConnection)
								.getResultDescriptor());
	}
}