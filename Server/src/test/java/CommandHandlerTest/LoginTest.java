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


/** 
 * @author Aviad Cohen
 * @author Lior Ben Ami
 * @since 2017
 * */
@RunWith(MockitoJUnitRunner.class)
public class LoginTest {
		
	private static int EMPLOYEE_SENDER_ID; //( = 0)
	private static int CUSTOMER_SENDER_ID = 1;
	
	@Mock
	private SQLDatabaseConnection sqlDatabaseConnection;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
	}
	
	@Test
	public void loginEmployeeSuccessfulTest() {
		Login login = new Login("admin", "admin");
		String command = new CommandWrapper(0, CommandDescriptor.LOGIN_EMPLOYEE,
				new Gson().toJson(login, Login.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(sqlDatabaseConnection.loginWorker(login.getUserName(), login.getPassword())).thenReturn(EMPLOYEE_SENDER_ID);
		} catch (NumberOfConnectionsExceeded | ClientAlreadyConnected | CriticalError | AuthenticationError e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_OK, out.getResultDescriptor());
	}
	
	@Test
	public void loginCustomerSuccessfulTest() {
		Login login = new Login("admin", "admin");
		String command = new CommandWrapper(0, CommandDescriptor.LOGIN_EMPLOYEE,
				new Gson().toJson(login, Login.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(sqlDatabaseConnection.loginCustomer(login.getUserName(), login.getPassword())).thenReturn(CUSTOMER_SENDER_ID);
		} catch (NumberOfConnectionsExceeded | ClientAlreadyConnected | CriticalError | AuthenticationError e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_OK, out.getResultDescriptor());
	}
	
	@Test
	public void loginEmployeeUsernameDoesNotExistWrongPasswordTest() {
		Login login = new Login("unknown", "unknown");
		String command = new CommandWrapper(0, CommandDescriptor.LOGIN_EMPLOYEE,
				new Gson().toJson(login, Login.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(sqlDatabaseConnection.loginWorker(login.getUserName(), login.getPassword()))
					.thenThrow(new AuthenticationError());
		} catch (NumberOfConnectionsExceeded | ClientAlreadyConnected | CriticalError | AuthenticationError e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_USERNAME_DOES_NOT_EXIST_WRONG_PASSWORD, out.getResultDescriptor());
	}
	
	@Test
	public void loginCustomerUsernameDoesNotExistWrongPasswordTest() {
		Login login = new Login("unknown", "unknown");
		String command = new CommandWrapper(0, CommandDescriptor.LOGIN_CUSTOMER,
				new Gson().toJson(login, Login.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(sqlDatabaseConnection.loginCustomer(login.getUserName(), login.getPassword()))
					.thenThrow(new AuthenticationError());
		} catch (NumberOfConnectionsExceeded | ClientAlreadyConnected | CriticalError | AuthenticationError e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_USERNAME_DOES_NOT_EXIST_WRONG_PASSWORD, out.getResultDescriptor());
	}
	
	@Test
	public void loginEmployeeCriticalErrorTest() {
		Login login = new Login("unknown", "unknown");
		String command = new CommandWrapper(0, CommandDescriptor.LOGIN_EMPLOYEE,
				new Gson().toJson(login, Login.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(sqlDatabaseConnection.loginWorker(login.getUserName(), login.getPassword()))
					.thenThrow(new CriticalError());
		} catch (NumberOfConnectionsExceeded | ClientAlreadyConnected | CriticalError | AuthenticationError e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_ERR, out.getResultDescriptor());
	}
	
	@Test
	public void loginCustomerCriticalErrorTest() {
		Login login = new Login("unknown", "unknown");
		String command = new CommandWrapper(0, CommandDescriptor.LOGIN_CUSTOMER,
				new Gson().toJson(login, Login.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(sqlDatabaseConnection.loginCustomer(login.getUserName(), login.getPassword()))
					.thenThrow(new CriticalError());
		} catch (NumberOfConnectionsExceeded | ClientAlreadyConnected | CriticalError | AuthenticationError e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_ERR, out.getResultDescriptor());
	}
	
	@Test
	public void loginInvalidUserNameTest() {
		assertEquals(ResultDescriptor.SM_INVALID_PARAMETER,
				(new CommandExecuter(new CommandWrapper(0, CommandDescriptor.LOGIN_CUSTOMER,
						new Gson().toJson(new Login("", "admin"), Login.class)).serialize()))
								.execute(sqlDatabaseConnection).getResultDescriptor());
	}
	
	@Test
	public void loginInvalidPasswordTest() {
		assertEquals(ResultDescriptor.SM_INVALID_PARAMETER,
				(new CommandExecuter(new CommandWrapper(0, CommandDescriptor.LOGIN_EMPLOYEE,
						new Gson().toJson(new Login("admin", ""), Login.class)).serialize()))
								.execute(sqlDatabaseConnection).getResultDescriptor());
	}
	
	@Test
	public void loginEmployeeAlreadyConnectedTest() {
		Login login = new Login("unknown", "unknown");
		String command = new CommandWrapper(0, CommandDescriptor.LOGIN_EMPLOYEE,
				new Gson().toJson(login, Login.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(sqlDatabaseConnection.loginWorker(login.getUserName(), login.getPassword()))
					.thenThrow(new ClientAlreadyConnected());
		} catch (NumberOfConnectionsExceeded | ClientAlreadyConnected | CriticalError | AuthenticationError e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_SENDER_IS_ALREADY_CONNECTED, out.getResultDescriptor());
	}
	
	@Test
	public void loginCustomerAlreadyConnectedTest() {
		Login login = new Login("unknown", "unknown");
		String command = new CommandWrapper(0, CommandDescriptor.LOGIN_CUSTOMER,
				new Gson().toJson(login, Login.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(sqlDatabaseConnection.loginCustomer(login.getUserName(), login.getPassword()))
					.thenThrow(new ClientAlreadyConnected());
		} catch (NumberOfConnectionsExceeded | ClientAlreadyConnected | CriticalError | AuthenticationError e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_SENDER_IS_ALREADY_CONNECTED, out.getResultDescriptor());
	}
	
	@Test
	public void loginCustomerIllegalLoginTest() {
		assertEquals(ResultDescriptor.SM_ERR,
				(new CommandExecuter(new CommandWrapper(1, CommandDescriptor.LOGIN_CUSTOMER,
						new Gson().toJson("", String.class)).serialize())).execute(sqlDatabaseConnection)
								.getResultDescriptor());
	}
	

	@Test
	public void loginEmployeeIllegalLoginTest() {
		assertEquals(ResultDescriptor.SM_ERR,
				(new CommandExecuter(new CommandWrapper(0, CommandDescriptor.LOGIN_EMPLOYEE,
						new Gson().toJson("", String.class)).serialize())).execute(sqlDatabaseConnection)
								.getResultDescriptor());
	}
	
}
