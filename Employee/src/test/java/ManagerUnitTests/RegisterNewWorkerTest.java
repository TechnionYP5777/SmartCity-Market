package ManagerUnitTests;

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
import EmployeeContracts.IManager;
import EmployeeDefs.AEmployeeException.ConnectionFailure;
import EmployeeDefs.AEmployeeException.CriticalError;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeDefs.AEmployeeException.WorkerAlreadyExists;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;
import EmployeeDefs.WorkerDefs;
import EmployeeImplementations.Manager;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

/**
 * @author Aviad Cohen
 * @since 2016-04-24 */

@RunWith(MockitoJUnitRunner.class)
public class RegisterNewWorkerTest {
	private IManager manager;
	@Mock
	private IClientRequestHandler clientRequestHandler;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
		manager = new Manager(clientRequestHandler);
	}
	
	static Login newWorker = new Login("MyWorker", "MyWorker");
	
	@Test
	public void RegisterNewWorkerSuccesfulTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.REGISTER_NEW_WORKER,
							Serialization.serialize(newWorker)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());
		} catch (IOException e) {
			
			fail();
		}
		
		try {
			manager.registerNewWorker(newWorker);
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure | WorkerAlreadyExists e) {
			
			fail();
		}
	}
	
	@Test
	public void RegisterNewWorkerInvalidParameterTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.REGISTER_NEW_WORKER,
							Serialization.serialize(newWorker)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER).serialize());
		} catch (IOException e) {
			
			fail();
		}
		
		try {
			manager.registerNewWorker(newWorker);
		} catch (InvalidParameter e) {
			/* success */
		} catch (CriticalError | EmployeeNotConnected | ConnectionFailure | WorkerAlreadyExists e) {
			
			fail();
		}
	}
	
	@Test
	public void RegisterNewWorkerCriticalErrorTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.REGISTER_NEW_WORKER,
							Serialization.serialize(newWorker)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_ERR).serialize());
		} catch (IOException e) {
			
			fail();
		}
		
		try {
			manager.registerNewWorker(newWorker);
		} catch (CriticalError e) {
			/* success */
		} catch (InvalidParameter | EmployeeNotConnected | ConnectionFailure | WorkerAlreadyExists e) {
			
			fail();
		}
	}
	
	@Test
	public void RegisterNewWorkerEmployeeNotConnectedTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.REGISTER_NEW_WORKER,
							Serialization.serialize(newWorker)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED).serialize());
		} catch (IOException e) {
			
			fail();
		}
		
		try {
			manager.registerNewWorker(newWorker);
		} catch (EmployeeNotConnected e) {
			/* success */
		} catch (InvalidParameter | CriticalError | ConnectionFailure | WorkerAlreadyExists e) {
			
			fail();
		}
	}
	
	@Test
	public void RegisterNewWorkerWorkerAlreadyExistsTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.REGISTER_NEW_WORKER,
							Serialization.serialize(newWorker)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_USERNAME_ALREADY_EXISTS).serialize());
		} catch (IOException e) {
			
			fail();
		}
		
		try {
			manager.registerNewWorker(newWorker);
		} catch (WorkerAlreadyExists e) {
			/* success */
		} catch (InvalidParameter | CriticalError | ConnectionFailure | EmployeeNotConnected e) {
			
			fail();
		}
	}
	
	@Test
	public void RegisterNewWorkerIllegalResultTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.REGISTER_NEW_WORKER,
							Serialization.serialize(newWorker)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_GROCERY_LIST_IS_EMPTY).serialize());
		} catch (IOException e) {
			
			fail();
		}
		
		try {
			manager.registerNewWorker(newWorker);
		} catch (CriticalError e) {
			/* success */
		} catch (InvalidParameter | WorkerAlreadyExists | ConnectionFailure | EmployeeNotConnected e) {
			
			fail();
		}
	}
}
