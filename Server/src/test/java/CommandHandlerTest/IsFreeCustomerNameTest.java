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

import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import CommandHandler.CommandExecuter;
import SQLDatabase.SQLDatabaseConnection;
import SQLDatabase.SQLDatabaseException.CriticalError;

@RunWith(MockitoJUnitRunner.class)
public class IsFreeCustomerNameTest {

	@Mock
	private SQLDatabaseConnection sqlDatabaseConnection;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
	}
	
	private static final String username = "username";
	private static final int senderID = 1;
	
	@Test
	public void isFreeCustomerNameSuccessfulTest1() {
		String command = new CommandWrapper(senderID, CommandDescriptor.IS_FREE_CUSTOMER_NAME,
				new Gson().toJson(username, String.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(
					sqlDatabaseConnection.isCustomerUsernameAvailable(username)).thenReturn(true);
		} catch (CriticalError e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_OK, out.getResultDescriptor());
	}
	
	@Test
	public void isFreeCustomerNameSuccessfulTest2() {
		String command = new CommandWrapper(senderID, CommandDescriptor.IS_FREE_CUSTOMER_NAME,
				new Gson().toJson(username, String.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(
					sqlDatabaseConnection.isCustomerUsernameAvailable(username)).thenReturn(false);
		} catch (CriticalError e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_USERNAME_ALREADY_EXISTS, out.getResultDescriptor());
	}
	
	@Test
	public void isFreeCustomerNameCriticalErrorTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.IS_FREE_CUSTOMER_NAME,
				new Gson().toJson(username, String.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(
					sqlDatabaseConnection.isCustomerUsernameAvailable(username)).thenThrow(new CriticalError());
		} catch (CriticalError e) {
			/* success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_ERR, out.getResultDescriptor());
	}
}