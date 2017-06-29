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
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;
import EmployeeDefs.WorkerDefs;
import EmployeeImplementations.Worker;
import SMExceptions.CommonExceptions.CriticalError;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

@RunWith(MockitoJUnitRunner.class)
public class LogoutTest {

	private IWorker worker;

	@Mock
	private IClientRequestHandler clientRequestHandler;


	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");

		worker = new Worker(clientRequestHandler);
	}

	@Test
	public void logoutSuccessfulTest() {
		try {
			Mockito.when(
					clientRequestHandler.sendRequestWithRespond(new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.LOGOUT,
							Serialization.serialize(worker.getUsername())).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());
		} catch (IOException ¢) {
			
			fail();
		}
		
		try {
			worker.logout();
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure ¢) {
			
			fail();
		}
	}

	@Test
	public void logoutNotConnectedTest() {
		try {
			Mockito.when(
					clientRequestHandler.sendRequestWithRespond(new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.LOGOUT,
							Serialization.serialize(worker.getUsername())).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED).serialize());
		} catch (IOException ¢) {
			
			fail();
		}
		
		try {
			worker.logout();
			
			fail();
		} catch (InvalidParameter | CriticalError | ConnectionFailure ¢) {
			
			fail();
		} catch (EmployeeNotConnected e) {
			/* Test Passed */
		}
	}
	
	@Test
	public void logoutIllegalResultTest_IngredientStillInUse() {
		try {
			Mockito.when(
					clientRequestHandler.sendRequestWithRespond(new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.LOGOUT,
							Serialization.serialize(null)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_INGREDIENT_STILL_IN_USE).serialize());
		} catch (IOException ¢) {
			
			fail();
		}
		
		try {
			worker.logout();
			
			fail();
		} catch (InvalidParameter | EmployeeNotConnected | ConnectionFailure ¢) {
			
			fail();
		} catch (CriticalError e) {
			/* Test Passed */
		}
	}
	
	@Test
	public void logoutIllegalResultTest_InvalidCommandDescriptor() {
		try {
			Mockito.when(
					clientRequestHandler.sendRequestWithRespond(new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.LOGOUT,
							Serialization.serialize(null)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_INVALID_CMD_DESCRIPTOR).serialize());
		} catch (IOException ¢) {
			
			fail();
		}
		
		try {
			worker.logout();
			
			fail();
		} catch (InvalidParameter | EmployeeNotConnected | ConnectionFailure ¢) {
			
			fail();
		} catch (CriticalError e) {
			/* Test Passed */
		}
	}
	
	
}
