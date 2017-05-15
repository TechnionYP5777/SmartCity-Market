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
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeDefs.WorkerDefs;
import EmployeeImplementations.Manager;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

/**
 * @author Aviad Cohen
 * @since 2016-04-24 */

@RunWith(MockitoJUnitRunner.class)
public class GetAllIngredientsTest {
	private IManager manager;
	@Mock
	private IClientRequestHandler clientRequestHandler;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
		manager = new Manager(clientRequestHandler);
	}
		
	@Test
	public void GetAllIngredientsSuccesfulTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.GET_ALL_INGREDIENTS,
							Serialization.serialize("")).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());
		} catch (IOException e) {
			
			fail();
		}
		
		try {
			manager.getAllIngredients();
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure e) {
			fail();
		}
	}
	
	@Test
	public void GetAllIngredientsCriticalErrorTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.GET_ALL_INGREDIENTS,
							Serialization.serialize("")).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_ERR).serialize());
		} catch (IOException e) {
			
			fail();
		}
		
		try {
			manager.getAllIngredients();
		} catch (InvalidParameter | EmployeeNotConnected | ConnectionFailure e1) {
			fail();
		} catch (CriticalError e) {
			/* success */
		}
	}
	
	@Test
	public void GetAllIngredientsEmployeeNotConnectedTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.GET_ALL_INGREDIENTS,
							Serialization.serialize("")).serialize()))
			.thenReturn(new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED).serialize());
		} catch (IOException e) {
			
			fail();
		}
		
		try {
			manager.getAllIngredients();
		} catch (InvalidParameter | CriticalError | ConnectionFailure e1) {
			fail();
		} catch (EmployeeNotConnected e) {
			/* success */
		}
	}
	
	@Test
	public void GetAllIngredientsIllegalResultTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.GET_ALL_INGREDIENTS,
							Serialization.serialize("")).serialize()))
			.thenReturn(new CommandWrapper(ResultDescriptor.SM_GROCERY_LIST_IS_EMPTY).serialize());
		} catch (IOException e) {
			
			fail();
		}
		
		try {
			manager.getAllIngredients();
		} catch (InvalidParameter | EmployeeNotConnected | ConnectionFailure e1) {
			fail();
		} catch (CriticalError e) {
			/* success */
		}
	}
}