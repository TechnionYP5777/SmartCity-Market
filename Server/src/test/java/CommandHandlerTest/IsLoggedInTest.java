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
public class IsLoggedInTest {
		
	@Mock
	private SQLDatabaseConnection sqlDatabaseConnection;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
	}
	
	@Test
	public void isLoggedInSuccessfulTest() {
		int senderID = 1;
		String command = new CommandWrapper(senderID, CommandDescriptor.IS_LOGGED_IN).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(
					sqlDatabaseConnection.isClientLoggedIn(senderID)).thenReturn(true);
		} catch (CriticalError e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(true, new Gson().fromJson(out.getData(), Boolean.class));
		assertEquals(ResultDescriptor.SM_OK, out.getResultDescriptor());
	}
	
	@Test
	public void isLoggedInCriticalErrorTest() {
		int senderID = 1;
		String command = new CommandWrapper(senderID, CommandDescriptor.IS_LOGGED_IN).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			
			Mockito.when(
					sqlDatabaseConnection.isClientLoggedIn(senderID)).thenThrow(new CriticalError());
		} catch (CriticalError e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_ERR, out.getResultDescriptor());
	}
}
