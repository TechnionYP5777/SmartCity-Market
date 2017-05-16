package CustomerImplemantationsTests;

import static org.junit.Assert.fail;

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
import CustomerContracts.ICustomer;
import CustomerContracts.ACustomerExceptions.CriticalError;
import CustomerImplementations.Customer;
import CustomerImplementations.CustomerDefs;
import UtilsContracts.IClientRequestHandler;

@RunWith(MockitoJUnitRunner.class)

public class GetAllIngredientsTest {
	private ICustomer customer;
	
	@Mock
	private IClientRequestHandler clientRequestHandler;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
		customer = new Customer(clientRequestHandler);
	}
	
	@Test
	public void getAllIngredientsTestSuccessfulTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.GET_ALL_INGREDIENTS).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.getAllIngredients();
		} catch (CriticalError e) {
			fail();
		}
	}
	
	@Test
	public void getAllIngredientsTestCriticalErrorTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.GET_ALL_INGREDIENTS).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_ERR).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.getAllIngredients();
			fail();
		} catch (CriticalError e) {
			/* success */
		}
	}
	
	@Test
	public void getAllIngredientsTestIllegalResultTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.GET_ALL_INGREDIENTS).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_GROCERY_LIST_IS_EMPTY).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.getAllIngredients();
			fail();
		} catch (CriticalError e) {
			/* success */
		}
	}
}
