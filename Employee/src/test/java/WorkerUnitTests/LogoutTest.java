package WorkerUnitTests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import EmployeeContracts.IWorker;
import EmployeeDefs.WorkerDefs;
import EmployeeImplementations.Worker;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

@RunWith(MockitoJUnitRunner.class)
public class LogoutTest {

	private IWorker worker;

	@Mock
	private IClientRequestHandler clientRequestHandler;


	@Before
	public void setup() {
		worker = new Worker(clientRequestHandler);
	}

	@Test
	public void logoutSuccessfulTest() {
		CommandWrapper commandWrapper = new CommandWrapper(ResultDescriptor.SM_OK);
		try {
			Mockito.when(
					clientRequestHandler.sendRequestWithRespond((new CommandWrapper(WorkerDefs.loginCommandSenderId,
							CommandDescriptor.LOGOUT, Serialization.serialize(null)).serialize())))
					.thenReturn(commandWrapper.serialize());
		} catch (IOException e) {
			e.printStackTrace();
		}
		worker.logout();
	}


	@Test
	public void logoutNotConnectedTest() {
		CommandWrapper commandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED);
		try {
			Mockito.when(
					clientRequestHandler.sendRequestWithRespond((new CommandWrapper(WorkerDefs.loginCommandSenderId,
							CommandDescriptor.LOGOUT, Serialization.serialize(null)).serialize())))
					.thenReturn(commandWrapper.serialize());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			worker.logout();
		} catch (RuntimeException e) {
			assertEquals(e.getMessage(), WorkerDefs.loginCmdUserNotConnected);
		}
	}
}
