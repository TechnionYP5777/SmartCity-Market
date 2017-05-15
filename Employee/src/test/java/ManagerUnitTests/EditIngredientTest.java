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

import BasicCommonClasses.Ingredient;
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
public class EditIngredientTest {
	private IManager manager;
	@Mock
	private IClientRequestHandler clientRequestHandler;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
		manager = new Manager(clientRequestHandler);
	}
	
	static Ingredient newIngredient = new Ingredient(0, "FOLL");
	
	@Test
	public void EditIngredientSuccesfulTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.EDIT_INGREDIENT,
							Serialization.serialize(newIngredient)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());
		} catch (IOException e) {
			
			fail();
		}
		
		try {
			manager.editIngredient(newIngredient);
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure | ParamIDDoesNotExist e1) {
			
			fail();
		}
	}

	@Test
	public void EditIngredientInvalidParameterTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.EDIT_INGREDIENT,
							Serialization.serialize(newIngredient)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER).serialize());
		} catch (IOException e) {
			
			fail();
		}
		
		try {
			manager.editIngredient(newIngredient);
		} catch (InvalidParameter e) {
			/* success */
		} catch (CriticalError | EmployeeNotConnected | ConnectionFailure | ParamIDDoesNotExist e1) {
			
			fail();
		}
	}
	
	@Test
	public void EditIngredientCriticalErrorTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.EDIT_INGREDIENT,
							Serialization.serialize(newIngredient)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_ERR).serialize());
		} catch (IOException e) {
			
			fail();
		}
		
		try {
			manager.editIngredient(newIngredient);
		} catch (CriticalError e) {
			/* success */
		} catch (InvalidParameter | EmployeeNotConnected | ConnectionFailure | ParamIDDoesNotExist e1) {
			
			fail();
		}
	}
	
	@Test
	public void EditIngredientEmployeeNotConnectedTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.EDIT_INGREDIENT,
							Serialization.serialize(newIngredient)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED).serialize());
		} catch (IOException e) {
			
			fail();
		}
		
		try {
			manager.editIngredient(newIngredient);
		} catch (EmployeeNotConnected e) {
			/* success */
		} catch (InvalidParameter | CriticalError | ConnectionFailure | ParamIDDoesNotExist e1) {
			
			fail();
		}
	}
	
	@Test
	public void EditIngredientParamIDDoesNotExistTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.EDIT_INGREDIENT,
							Serialization.serialize(newIngredient)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.PARAM_ID_IS_NOT_EXIST).serialize());
		} catch (IOException e) {
			
			fail();
		}
		
		try {
			manager.editIngredient(newIngredient);
		} catch (ParamIDDoesNotExist e) {
			/* success */
		} catch (InvalidParameter | CriticalError | ConnectionFailure | EmployeeNotConnected e1) {
			
			fail();
		}
	}
	
	@Test
	public void EditIngredientIllegalResultTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.EDIT_INGREDIENT,
							Serialization.serialize(newIngredient)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_MANUFACTURER_STILL_IN_USE).serialize());
		} catch (IOException e) {
			
			fail();
		}
		
		try {
			manager.editIngredient(newIngredient);
		} catch (CriticalError e) {
			/* success */
		} catch (InvalidParameter | ConnectionFailure | EmployeeNotConnected | ParamIDDoesNotExist e1) {
			
			fail();
		}
	}
}