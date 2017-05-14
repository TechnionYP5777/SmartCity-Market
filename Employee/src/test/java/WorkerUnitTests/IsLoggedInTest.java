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

import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import EmployeeContracts.IWorker;
import EmployeeDefs.AEmployeeException.ConnectionFailure;
import EmployeeDefs.AEmployeeException.CriticalError;
import EmployeeDefs.WorkerDefs;
import EmployeeImplementations.Worker;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

@RunWith(MockitoJUnitRunner.class)
public class IsLoggedInTest {

	private IWorker worker;
	private Boolean answer = true;

	@Mock
	private IClientRequestHandler clientRequestHandler;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");

		worker = new Worker(clientRequestHandler);
	}

	@Test
	public void isLoggedInSuccessfulTest() {		
		try {
			Mockito.when(
					clientRequestHandler.sendRequestWithRespond((new CommandWrapper(WorkerDefs.loginCommandSenderId,
							CommandDescriptor.IS_LOGGED_IN).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK, Serialization.serialize(answer)).serialize());
		} catch (IOException ¢) {
			
			fail();
		}
		try {			
			assertEquals(worker.isLoggedIn(), answer);
		} catch (CriticalError | ConnectionFailure e) {
			
			fail();
		}
	}

	@Test
	public void isLoggedInCriticalErrorTest() {		
		try {
			Mockito.when(
					clientRequestHandler.sendRequestWithRespond((new CommandWrapper(WorkerDefs.loginCommandSenderId,
							CommandDescriptor.IS_LOGGED_IN).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_ERR, Serialization.serialize(answer)).serialize());
		} catch (IOException ¢) {
			
			fail();
		}
		try {			
			assertEquals(worker.isLoggedIn(), answer);
		} catch (ConnectionFailure e) {
			
			fail();
		} catch (CriticalError __) {
			/* Successful */
		}
	}
	
	@Test
	public void isLoggedInIllegalResultTest() {		
		try {
			Mockito.when(
					clientRequestHandler.sendRequestWithRespond((new CommandWrapper(WorkerDefs.loginCommandSenderId,
							CommandDescriptor.IS_LOGGED_IN).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_INGREDIENT_STILL_IN_USE, Serialization.serialize(answer)).serialize());
		} catch (IOException ¢) {
			
			fail();
		}
		try {			
			assertEquals(worker.isLoggedIn(), answer);
		} catch (ConnectionFailure e) {
			
			fail();
		} catch (CriticalError __) {
			/* Successful */
		}
	}
}

