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

import BasicCommonClasses.Manufacturer;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import EmployeeContracts.IManager;
import EmployeeDefs.AEmployeeException.ConnectionFailure;
import EmployeeDefs.AEmployeeException.CriticalError;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeDefs.AEmployeeException.ParamIDAlreadyExists;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;
import EmployeeDefs.WorkerDefs;
import EmployeeImplementations.Manager;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

/**
 * @author Aviad Cohen
 * @since 2016-04-24 */

@RunWith(MockitoJUnitRunner.class)
public class AddManufacturerTest {
	private IManager manager;
	@Mock
	private IClientRequestHandler clientRequestHandler;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
		manager = new Manager(clientRequestHandler);
	}
	
	static Manufacturer newManufacturer = new Manufacturer(0, "Manu");
	
	@Test
	public void AddManufacturerSuccesfulTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					(new CommandWrapper(WorkerDefs.loginCommandSenderId, 
							CommandDescriptor.ADD_MANUFACTURER,
							Serialization.serialize(newManufacturer)).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		try {
			manager.addManufacturer(newManufacturer);
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure | ParamIDAlreadyExists e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void AddManufacturerInvalidParameterTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					(new CommandWrapper(WorkerDefs.loginCommandSenderId, 
							CommandDescriptor.ADD_MANUFACTURER,
							Serialization.serialize(newManufacturer)).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER).serialize());
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		try {
			manager.addManufacturer(newManufacturer);
		} catch (InvalidParameter e) {
			/* success */
		} catch (CriticalError | EmployeeNotConnected | ConnectionFailure | ParamIDAlreadyExists e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void AddManufacturerCriticalErrorTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					(new CommandWrapper(WorkerDefs.loginCommandSenderId, 
							CommandDescriptor.ADD_MANUFACTURER,
							Serialization.serialize(newManufacturer)).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_ERR).serialize());
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		try {
			manager.addManufacturer(newManufacturer);
		} catch (CriticalError e) {
			/* success */
		} catch (InvalidParameter | EmployeeNotConnected | ConnectionFailure | ParamIDAlreadyExists e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void AddManufacturerEmployeeNotConnectedTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					(new CommandWrapper(WorkerDefs.loginCommandSenderId, 
							CommandDescriptor.ADD_MANUFACTURER,
							Serialization.serialize(newManufacturer)).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED).serialize());
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		try {
			manager.addManufacturer(newManufacturer);
		} catch (EmployeeNotConnected e) {
			/* success */
		} catch (InvalidParameter | CriticalError | ConnectionFailure | ParamIDAlreadyExists e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void AddManufacturerParamIDAlreadyExistsTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					(new CommandWrapper(WorkerDefs.loginCommandSenderId, 
							CommandDescriptor.ADD_MANUFACTURER,
							Serialization.serialize(newManufacturer)).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.PARAM_ID_ALREADY_EXISTS).serialize());
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		try {
			manager.addManufacturer(newManufacturer);
		} catch (ParamIDAlreadyExists e) {
			/* success */
		} catch (InvalidParameter | CriticalError | ConnectionFailure | EmployeeNotConnected e) {
			e.printStackTrace();
			fail();
		}
	}
}