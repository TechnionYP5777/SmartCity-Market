package WorkerUnitTests;

import static org.junit.Assert.*;

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
import EmployeeDefs.AEmployeeException.CriticalError;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeDefs.AEmployeeException.UnknownSenderID;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;
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
		try {
			Mockito.when(
					clientRequestHandler.sendRequestWithRespond((new CommandWrapper(WorkerDefs.loginCommandSenderId,
							CommandDescriptor.LOGOUT, Serialization.serialize(null)).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());
		} catch (IOException ¢) {
			¢.printStackTrace();
			fail();
		}
		
		try {
			worker.logout();
		} catch (InvalidParameter | UnknownSenderID | CriticalError | EmployeeNotConnected ¢) {
			¢.printStackTrace();
			fail();
		}
	}


	@Test
	public void logoutNotConnectedTest() {
		try {
			Mockito.when(
					clientRequestHandler.sendRequestWithRespond((new CommandWrapper(WorkerDefs.loginCommandSenderId,
							CommandDescriptor.LOGOUT, Serialization.serialize(null)).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED).serialize());
		} catch (IOException ¢) {
			¢.printStackTrace();
			fail();
		}
		
		try {
			worker.logout();
		} catch (InvalidParameter | UnknownSenderID | CriticalError ¢) {
			¢.printStackTrace();
			fail();
		} catch (EmployeeNotConnected e) {
			/* Test Passed */
		}
	}
	
	//TODO: lior: add tests for the other failures possibilities when its done
}
