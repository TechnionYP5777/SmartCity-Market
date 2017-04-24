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
import EmployeeDefs.AEmployeeException.ParamIDDoesNotExist;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;
import EmployeeDefs.WorkerDefs;
import EmployeeImplementations.Manager;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

/**
 * @author Aviad Cohen
 * @since 2016-04-24 */

@RunWith(MockitoJUnitRunner.class)
public class RemoveManufacturerTest {
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
	public void RemoveManufacturerSuccesfulTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					(new CommandWrapper(WorkerDefs.loginCommandSenderId, 
							CommandDescriptor.REMOVE_MANUFACTURER,
							Serialization.serialize(newManufacturer)).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		try {
			manager.removeManufacturer(newManufacturer);
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure | ParamIDDoesNotExist e1) {
			e1.printStackTrace();
			fail();
		}
	}

	@Test
	public void RemoveManufacturerInvalidParameterTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					(new CommandWrapper(WorkerDefs.loginCommandSenderId, 
							CommandDescriptor.REMOVE_MANUFACTURER,
							Serialization.serialize(newManufacturer)).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER).serialize());
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		try {
			manager.removeManufacturer(newManufacturer);
		} catch (InvalidParameter e) {
			/* success */
		} catch (CriticalError | EmployeeNotConnected | ConnectionFailure | ParamIDDoesNotExist e1) {
			e1.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void RemoveManufacturerCriticalErrorTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					(new CommandWrapper(WorkerDefs.loginCommandSenderId, 
							CommandDescriptor.REMOVE_MANUFACTURER,
							Serialization.serialize(newManufacturer)).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_ERR).serialize());
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		try {
			manager.removeManufacturer(newManufacturer);
		} catch (CriticalError e) {
			/* success */
		} catch (InvalidParameter | EmployeeNotConnected | ConnectionFailure | ParamIDDoesNotExist e1) {
			e1.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void RemoveManufacturerEmployeeNotConnectedTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					(new CommandWrapper(WorkerDefs.loginCommandSenderId, 
							CommandDescriptor.REMOVE_MANUFACTURER,
							Serialization.serialize(newManufacturer)).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED).serialize());
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		try {
			manager.removeManufacturer(newManufacturer);
		} catch (EmployeeNotConnected e) {
			/* success */
		} catch (InvalidParameter | CriticalError | ConnectionFailure | ParamIDDoesNotExist e1) {
			e1.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void RemoveManufacturerParamIDDoesNotExistTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					(new CommandWrapper(WorkerDefs.loginCommandSenderId, 
							CommandDescriptor.REMOVE_MANUFACTURER,
							Serialization.serialize(newManufacturer)).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.PARAM_ID_IS_NOT_EXIST).serialize());
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		try {
			manager.removeManufacturer(newManufacturer);
		} catch (ParamIDDoesNotExist e) {
			/* success */
		} catch (InvalidParameter | CriticalError | ConnectionFailure | EmployeeNotConnected e1) {
			e1.printStackTrace();
			fail();
		}
	}
}