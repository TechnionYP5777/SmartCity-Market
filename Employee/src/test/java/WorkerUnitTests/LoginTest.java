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
		CommandWrapper commandWrapper = new CommandWrapper(ResultDescriptor.SM_OK);
		commandWrapper.setSenderID(1234567890);
		try {
			Mockito.when(
					clientRequestHandler.sendRequestWithRespond((new CommandWrapper(WorkerDefs.loginCommandSenderId,
							CommandDescriptor.LOGIN, Serialization.serialize(new Login("test", "test"))).serialize())))
					.thenReturn(commandWrapper.serialize());
		} catch (IOException e) {
			e.printStackTrace();
		}
		worker.login("test", "test");
		assertEquals("test", worker.getWorkerLoginDetails().getUserName());
		assertEquals("test", worker.getWorkerLoginDetails().getPassword());
	}


	@Test
	public void loginWrongUserOrPasswordTest() {
		CommandWrapper commandWrapper = new CommandWrapper(ResultDescriptor.SM_USERNAME_DOES_NOT_EXIST_WRONG_PASSWORD);
		try {
			Mockito.when(
					clientRequestHandler.sendRequestWithRespond((new CommandWrapper(WorkerDefs.loginCommandSenderId,
							CommandDescriptor.LOGIN, Serialization.serialize(new Login("test", "test"))).serialize())))
					.thenReturn(commandWrapper.serialize());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			worker.login("test", "test");
		} catch (RuntimeException e) {
			assertEquals(e.getMessage(), WorkerDefs.loginCmdWrongUserOrPass);
		}
	}

	@Test
	public void loginAlreadyConnectedTest() {
		CommandWrapper commandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_ALREADY_CONNECTED);
		try {
			Mockito.when(
					clientRequestHandler.sendRequestWithRespond((new CommandWrapper(WorkerDefs.loginCommandSenderId,
							CommandDescriptor.LOGIN, Serialization.serialize(new Login("test", "test"))).serialize())))
					.thenReturn(commandWrapper.serialize());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			worker.login("test", "test");
		} catch (RuntimeException e) {
			assertEquals(e.getMessage(), WorkerDefs.loginCmdUserAlreadyConnected);
		}
	}

}
