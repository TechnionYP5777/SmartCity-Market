package WorkerUnitTests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import BasicCommonClasses.Login;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import CommonDefs.CLIENT_TYPE;
import EmployeeContracts.IWorker;
import EmployeeDefs.AEmployeeException.AuthenticationError;
import EmployeeDefs.AEmployeeException.ConnectionFailure;
import EmployeeDefs.AEmployeeException.CriticalError;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeDefs.AEmployeeException.EmployeeAlreadyConnected;
import EmployeeDefs.WorkerDefs;
import EmployeeImplementations.Worker;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

@RunWith(MockitoJUnitRunner.class)
public class LoginTest {

	private IWorker worker;
	private  CLIENT_TYPE clType;

	@Mock
	private IClientRequestHandler clientRequestHandler;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");

		worker = new Worker(clientRequestHandler);
	}

	@Test
	public void loginSuccessfulTest() {
		try {
			Mockito.when(
					clientRequestHandler.sendRequestWithRespond((new CommandWrapper(WorkerDefs.loginCommandSenderId,
							CommandDescriptor.LOGIN_EMPLOYEE, Serialization.serialize(new Login("test", "test"))).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK, CLIENT_TYPE.WORKER.serialize()).serialize());
		} catch (IOException ¢) {
			
			fail();
		}
		try {
			clType = worker.login("test", "test", false);
		} catch (InvalidParameter | CriticalError | EmployeeAlreadyConnected | AuthenticationError | ConnectionFailure ¢) {
			
			fail();
		}
		assertEquals("test", worker.getWorkerLoginDetails().getUserName());
		assertEquals("test", worker.getWorkerLoginDetails().getPassword());
		assertEquals(clType, CLIENT_TYPE.WORKER);
	}

	@Test
	public void loginWrongUserOrPasswordTest() {
		try {
			Mockito.when(
					clientRequestHandler.sendRequestWithRespond((new CommandWrapper(WorkerDefs.loginCommandSenderId,
							CommandDescriptor.LOGIN_EMPLOYEE, Serialization.serialize(new Login("test", "test"))).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_USERNAME_DOES_NOT_EXIST_WRONG_PASSWORD).serialize());
		} catch (IOException ¢) {
			
			fail();
		}
		
		try {
			worker.login("test", "test", false);
			
			fail();
		} catch (InvalidParameter | CriticalError | EmployeeAlreadyConnected | ConnectionFailure ¢) {
			
			fail();
		} catch (AuthenticationError e) {
			/* Test Passed */
		}
	}

	@Test
	public void loginAlreadyConnectedTest() {
		try {
			Mockito.when(
					clientRequestHandler.sendRequestWithRespond((new CommandWrapper(WorkerDefs.loginCommandSenderId,
							CommandDescriptor.LOGIN_EMPLOYEE, Serialization.serialize(new Login("test", "test"))).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_SENDER_IS_ALREADY_CONNECTED).serialize());
		} catch (IOException ¢) {
			
			fail();
		}
		
		try {
			worker.login("test", "test", false);
			
			fail();
		} catch (InvalidParameter | CriticalError | AuthenticationError | ConnectionFailure ¢) {
			
			fail();
		} catch (EmployeeAlreadyConnected e) {
			/* Test Passed */
		}
	}

	@Test
	public void loginParseFailuteTest() {
		try {
			Mockito.when(
					clientRequestHandler.sendRequestWithRespond((new CommandWrapper(WorkerDefs.loginCommandSenderId,
							CommandDescriptor.LOGIN_EMPLOYEE, Serialization.serialize(new Login("test", "test"))).serialize())))
					.thenReturn("");
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			worker.login("test", "test", false);
			
			fail();
		} catch (InvalidParameter | EmployeeAlreadyConnected | AuthenticationError | ConnectionFailure ¢) {
			
			fail();
		} catch (CriticalError e) {
			/* Test Passed */
		}
	}
	
	@Test
	public void loginIllegalResultTest() {
		try {
			Mockito.when(
					clientRequestHandler.sendRequestWithRespond((new CommandWrapper(WorkerDefs.loginCommandSenderId,
							CommandDescriptor.LOGIN_EMPLOYEE, Serialization.serialize(new Login("test", "test"))).serialize())))
			.thenReturn(new CommandWrapper(ResultDescriptor.SM_INGREDIENT_STILL_IN_USE).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			worker.login("test", "test", false);
			
			fail();
		} catch (InvalidParameter | EmployeeAlreadyConnected | AuthenticationError | ConnectionFailure ¢) {
			
			fail();
		} catch (CriticalError e) {
			/* Test Passed */
		}
	}
}
