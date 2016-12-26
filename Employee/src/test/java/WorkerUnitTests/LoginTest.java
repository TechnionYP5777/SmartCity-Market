package WorkerUnitTests;

import static org.junit.Assert.*;

import java.io.IOException;

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
import EmployeeContracts.IWorker;
import EmployeeDefs.AEmployeeExceptions.AuthenticationError;
import EmployeeDefs.AEmployeeExceptions.CriticalError;
import EmployeeDefs.AEmployeeExceptions.InvalidParameter;
import EmployeeDefs.AEmployeeExceptions.WorkerAlreadyConnected;
import EmployeeDefs.WorkerDefs;
import EmployeeImplementations.Worker;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

@RunWith(MockitoJUnitRunner.class)
public class LoginTest {

	private IWorker worker;

	@Mock
	private IClientRequestHandler clientRequestHandler;

	@Before
	public void setup() {
		worker = new Worker(clientRequestHandler);
	}

	@Test
	public void loginSuccessfulTest() {
		try {
			Mockito.when(
					clientRequestHandler.sendRequestWithRespond((new CommandWrapper(WorkerDefs.loginCommandSenderId,
							CommandDescriptor.LOGIN, Serialization.serialize(new Login("test", "test"))).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		try {
			worker.login("test", "test");
		} catch (InvalidParameter | CriticalError | WorkerAlreadyConnected | AuthenticationError e) {
			e.printStackTrace();
			fail();
		}
		
		assertEquals("test", worker.getWorkerLoginDetails().getUserName());
		assertEquals("test", worker.getWorkerLoginDetails().getPassword());
	}

	@Test
	public void loginWrongUserOrPasswordTest() {
		try {
			Mockito.when(
					clientRequestHandler.sendRequestWithRespond((new CommandWrapper(WorkerDefs.loginCommandSenderId,
							CommandDescriptor.LOGIN, Serialization.serialize(new Login("test", "test"))).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_USERNAME_DOES_NOT_EXIST_WRONG_PASSWORD).serialize());
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		try {
			worker.login("test", "test");
		} catch (InvalidParameter | CriticalError | WorkerAlreadyConnected e) {
			e.printStackTrace();
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
							CommandDescriptor.LOGIN, Serialization.serialize(new Login("test", "test"))).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_SENDER_IS_ALREADY_CONNECTED).serialize());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			worker.login("test", "test");
		} catch (InvalidParameter | CriticalError | AuthenticationError e) {
			e.printStackTrace();
			fail();
		} catch (WorkerAlreadyConnected e) {
			/* Test Passed */
		}
	}

}
