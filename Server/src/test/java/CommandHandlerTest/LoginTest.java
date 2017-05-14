package CommandHandlerTest;

import static org.junit.Assert.*;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gson.Gson;

import BasicCommonClasses.Login;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import CommandHandler.CommandExecuter;
import SQLDatabase.SQLDatabaseConnection;
import SQLDatabase.SQLDatabaseException.AuthenticationError;
import SQLDatabase.SQLDatabaseException.CriticalError;
import SQLDatabase.SQLDatabaseException.NumberOfConnectionsExceeded;
import SQLDatabase.SQLDatabaseException.ClientAlreadyConnected;

@RunWith(MockitoJUnitRunner.class)
public class LoginTest {
		
	@Mock
	private SQLDatabaseConnection sqlDatabaseConnection;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
	}
	
	@Test
	public void loginSuccessfulTest() {
		int senderID = 0;
		Login login = new Login("admin", "admin");
		String command = new CommandWrapper(0, CommandDescriptor.LOGIN,
				new Gson().toJson(login, Login.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(
					sqlDatabaseConnection.login(login.getUserName(), login.getPassword()))
					.thenReturn(senderID);
		} catch (AuthenticationError e) {
			fail();
		} catch (CriticalError e) {
			fail();
		} catch (ClientAlreadyConnected e) {
			fail();
		} catch (NumberOfConnectionsExceeded e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(senderID, out.getSenderID());
		assertEquals(ResultDescriptor.SM_OK, out.getResultDescriptor());
	}
	
	@Test
	public void loginUsernameDoesNotExistWrongPasswordTest() {
		Login login = new Login("unknown", "unknown");
		String command = new CommandWrapper(0, CommandDescriptor.LOGIN,
				new Gson().toJson(login, Login.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(
					sqlDatabaseConnection.login(login.getUserName(), login.getPassword()))
			       .thenThrow(new AuthenticationError());
		} catch (AuthenticationError e) {
			fail();
		} catch (CriticalError e) {
			fail();
		} catch (ClientAlreadyConnected e) {
			fail();
		} catch (NumberOfConnectionsExceeded e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_USERNAME_DOES_NOT_EXIST_WRONG_PASSWORD, out.getResultDescriptor());
	}
	
	@Test
	public void loginCriticalErrorTest() {
		Login login = new Login("unknown", "unknown");
		String command = new CommandWrapper(0, CommandDescriptor.LOGIN,
				new Gson().toJson(login, Login.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(
					sqlDatabaseConnection.login(login.getUserName(), login.getPassword()))
			       .thenThrow(new CriticalError());
		} catch (AuthenticationError e) {
			fail();
		} catch (CriticalError e) {
			fail();
		} catch (ClientAlreadyConnected e) {
			fail();
		} catch (NumberOfConnectionsExceeded e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_ERR, out.getResultDescriptor());
	}
	
	@Test
	public void loginInvalidUserNameTest() {
		assertEquals(ResultDescriptor.SM_INVALID_PARAMETER,
				(new CommandExecuter(new CommandWrapper(0, CommandDescriptor.LOGIN,
						new Gson().toJson((new Login("", "admin")), Login.class)).serialize()))
								.execute(sqlDatabaseConnection).getResultDescriptor());
	}
	
	@Test
	public void loginInvalidPasswordTest() {
		assertEquals(ResultDescriptor.SM_INVALID_PARAMETER,
				(new CommandExecuter(new CommandWrapper(0, CommandDescriptor.LOGIN,
						new Gson().toJson((new Login("admin", "")), Login.class)).serialize()))
								.execute(sqlDatabaseConnection).getResultDescriptor());
	}
}
