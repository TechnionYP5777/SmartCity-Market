package CustomerImplemantationsTests;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import BasicCommonClasses.Sale;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import CustomerContracts.ICustomer;
import SMExceptions.CommonExceptions.CriticalError;
import CustomerContracts.ACustomerExceptions.CustomerNotConnected;
import CustomerContracts.ACustomerExceptions.GroceryListIsEmpty;
import CustomerImplementations.Customer;
import CustomerImplementations.CustomerDefs;
import UtilsContracts.IClientRequestHandler;

@RunWith(MockitoJUnitRunner.class)

public class CheckoutGroceryListTest {
	private ICustomer customer;
	
	@Mock
	private IClientRequestHandler clientRequestHandler;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
		customer = new Customer(clientRequestHandler);
	}
	
	@Test
	public void checkoutGroceryListSuccessfulTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.CHECKOUT_GROCERY_LIST).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.checkOutGroceryList(new HashMap<Sale, Boolean>());
		} catch (CriticalError | CustomerNotConnected | GroceryListIsEmpty e) {
			fail();
		}
	}
	
	@Test
	public void checkoutGroceryListCriticalErrorTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.CHECKOUT_GROCERY_LIST).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_ERR).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.checkOutGroceryList(new HashMap<Sale, Boolean>());
		} catch (CustomerNotConnected | GroceryListIsEmpty e) {
			fail();
		} catch (CriticalError __) {
			/* success */
		}
	}
	
	@Test
	public void checkoutGroceryListCustomerNotConnectedTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.CHECKOUT_GROCERY_LIST).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.checkOutGroceryList(new HashMap<Sale, Boolean>());
		} catch (CriticalError | GroceryListIsEmpty e) {
			fail();
		} catch (CustomerNotConnected __) {
			/* success */
		}
	}
	
	@Test
	public void checkoutGroceryListGroceryListIsEmptyTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.CHECKOUT_GROCERY_LIST).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_GROCERY_LIST_IS_EMPTY).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.checkOutGroceryList(new HashMap<Sale, Boolean>());
		} catch (CriticalError | CustomerNotConnected e) {
			fail();
		} catch (GroceryListIsEmpty __) {
			/* success */
		}
	}
	
	@Test
	public void checkoutGroceryListConnectionFailureTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.CHECKOUT_GROCERY_LIST).serialize()))
			.thenThrow(new SocketTimeoutException());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.checkOutGroceryList(new HashMap<Sale, Boolean>());
		} catch (GroceryListIsEmpty | CustomerNotConnected e) {
			fail();
		} catch (CriticalError __) {
			/* success */
		}
	}
	
	@Test
	public void checkoutGroceryListIllegalResultTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.CHECKOUT_GROCERY_LIST).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_FOROGT_PASSWORD_WRONG_ANSWER).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.checkOutGroceryList(new HashMap<Sale, Boolean>());
		} catch (GroceryListIsEmpty | CustomerNotConnected e) {
			fail();
		} catch (CriticalError __) {
			/* success */
		}
	}
}
