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
import SQLDatabase.SQLDatabaseException.ClientNotConnected;

@RunWith(MockitoJUnitRunner.class)
public class LogoutTest {

	@Mock
	private SQLDatabaseConnection sqlDatabaseConnection;
	
	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
	}
	
	@Test
	public void logoutSuccessfulTest() {
		int senderID = 0;
		String userName = "admin", command = new CommandWrapper(senderID, CommandDescriptor.LOGOUT,
				new Gson().toJson(userName, String.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doNothing().when(sqlDatabaseConnection).logout(senderID, userName);
		} catch (CriticalError | ClientNotConnected e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_OK, out.getResultDescriptor());
	}
	
	@Test
	public void logoutWorkerNotConnectedTest() {
		int senderID = 99999999;
		String userName = "admin", command = new CommandWrapper(senderID, CommandDescriptor.LOGOUT,
				new Gson().toJson(userName, String.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new ClientNotConnected()).when(sqlDatabaseConnection).logout(senderID, userName);
		} catch (ClientNotConnected | CriticalError e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED, out.getResultDescriptor());
	}
	
	@Test
	public void logoutCriticalErrorTest() {
		int senderID = 0;
		String userName = "unknown", command = new CommandWrapper(senderID, CommandDescriptor.LOGOUT,
				new Gson().toJson(userName, String.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new CriticalError()).when(sqlDatabaseConnection).logout(senderID, userName);
		} catch (ClientNotConnected | CriticalError e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_ERR, out.getResultDescriptor());
	}
	
	@Test
	public void logoutInvalidUserNameTest() {
		assertEquals(ResultDescriptor.SM_INVALID_PARAMETER,
				(new CommandExecuter(new CommandWrapper(0, CommandDescriptor.LOGOUT,
						new Gson().toJson("", String.class)).serialize()))
								.execute(sqlDatabaseConnection).getResultDescriptor());
	}
}
