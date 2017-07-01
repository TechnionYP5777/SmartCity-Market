package CustomerImplemantationsTests;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import BasicCommonClasses.GroceryList;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import CustomerContracts.ICustomer;
import SMExceptions.CommonExceptions.CriticalError;
import CustomerContracts.ACustomerExceptions.CustomerNotConnected;
import CustomerImplementations.Customer;
import CustomerImplementations.CustomerDefs;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

/** 
 * @author Lior Ben Ami
 * @since 2017-06-30
 */
@RunWith(MockitoJUnitRunner.class)
public class ResumeTest {
	private ICustomer customer;

	@Mock
	private IClientRequestHandler clientRequestHandler;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
		customer = new Customer(clientRequestHandler);
	}
	
	@Test
	public void resumeSuccessfulTest() {
		GroceryList groceryList = new GroceryList();
		
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.LOAD_GROCERY_LIST).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK, Serialization.serialize(groceryList)).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.resume(CustomerDefs.loginCommandSenderId);
		} catch (CriticalError | CustomerNotConnected e1) {			
			fail();
		}
	}
	
	@Test
	public void resumeCriticalErrorTest() {
		GroceryList groceryList = new GroceryList();
		
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.LOAD_GROCERY_LIST).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_ERR, Serialization.serialize(groceryList)).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.resume(CustomerDefs.loginCommandSenderId);
		} catch (CustomerNotConnected e1) {			
			fail();
		} catch (CriticalError __) {
			/* success */
		}
	}
	
	@Test
	public void resumeCustomerNotConnectedTest() {
		GroceryList groceryList = new GroceryList();
		
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.LOAD_GROCERY_LIST).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED, Serialization.serialize(groceryList)).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.resume(CustomerDefs.loginCommandSenderId);
		} catch (CriticalError e1) {			
			fail();
		} catch (CustomerNotConnected __) {
			/* success */
		}
	}
	
	@Test
	public void resumeConnectionFailureTest() {		
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.LOAD_GROCERY_LIST).serialize()))
			.thenThrow(new SocketTimeoutException());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.resume(CustomerDefs.loginCommandSenderId);
		} catch (CustomerNotConnected e1) {			
			fail();
		} catch (CriticalError __) {
			/* success */
		}
	}
	
	@Test
	public void IllegalResultTest() {
		GroceryList groceryList = new GroceryList();
		
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.LOAD_GROCERY_LIST).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_GROCERY_LIST_IS_EMPTY, Serialization.serialize(groceryList)).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.resume(CustomerDefs.loginCommandSenderId);
		} catch (CustomerNotConnected e1) {			
			fail();
		} catch (CriticalError __) {
			/* success */
		}
	}
}
