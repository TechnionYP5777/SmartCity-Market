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

import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import EmployeeContracts.IManager;
import EmployeeDefs.AEmployeeException.ConnectionFailure;
import EmployeeDefs.AEmployeeException.CriticalError;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeDefs.AEmployeeException.WorkerDoesNotExist;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;
import EmployeeDefs.WorkerDefs;
import EmployeeImplementations.Manager;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

/**
 * @author Aviad Cohen
 * @since 2016-04-24 */

@RunWith(MockitoJUnitRunner.class)
public class RemoveWorkerTest {
	private IManager manager;
	@Mock
	private IClientRequestHandler clientRequestHandler;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
		manager = new Manager(clientRequestHandler);
	}
	
	static String userToRemove = String.valueOf("MyWorker");
	
	@Test
	public void RemoveWorkerSuccesfulTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					(new CommandWrapper(WorkerDefs.loginCommandSenderId, 
							CommandDescriptor.REMOVE_WORKER,
							Serialization.serialize(userToRemove)).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		try {
			manager.removeWorker(userToRemove);
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure | WorkerDoesNotExist e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void RemoveWorkerInvalidParameterTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					(new CommandWrapper(WorkerDefs.loginCommandSenderId, 
							CommandDescriptor.REMOVE_WORKER,
							Serialization.serialize(userToRemove)).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER).serialize());
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		try {
			manager.removeWorker(userToRemove);
		} catch (InvalidParameter e) {
			/* success */
		} catch (CriticalError | EmployeeNotConnected | ConnectionFailure | WorkerDoesNotExist e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void RemoveWorkerCriticalErrorTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					(new CommandWrapper(WorkerDefs.loginCommandSenderId, 
							CommandDescriptor.REMOVE_WORKER,
							Serialization.serialize(userToRemove)).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_ERR).serialize());
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		try {
			manager.removeWorker(userToRemove);
		} catch (CriticalError e) {
			/* success */
		} catch (InvalidParameter | EmployeeNotConnected | ConnectionFailure | WorkerDoesNotExist e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void RemoveWorkerEmployeeNotConnectedTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					(new CommandWrapper(WorkerDefs.loginCommandSenderId, 
							CommandDescriptor.REMOVE_WORKER,
							Serialization.serialize(userToRemove)).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED).serialize());
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		try {
			manager.removeWorker(userToRemove);
		} catch (EmployeeNotConnected e) {
			/* success */
		} catch (InvalidParameter | CriticalError | ConnectionFailure | WorkerDoesNotExist e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void RemoveWorkerWorkerDoesNotExistTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					(new CommandWrapper(WorkerDefs.loginCommandSenderId, 
							CommandDescriptor.REMOVE_WORKER,
							Serialization.serialize(userToRemove)).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_USERNAME_DOES_NOT_EXIST).serialize());
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		try {
			manager.removeWorker(userToRemove);
		} catch (WorkerDoesNotExist e) {
			/* success */
		} catch (InvalidParameter | CriticalError | ConnectionFailure | EmployeeNotConnected e) {
			e.printStackTrace();
			fail();
		}
	}
}
