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

import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import CustomerContracts.ICustomer;
import SMExceptions.CommonExceptions.CriticalError;
import CustomerImplementations.Customer;
import CustomerImplementations.CustomerDefs;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

@RunWith(MockitoJUnitRunner.class)

public class IsFreeCustomerNameTest {
	private ICustomer customer;
	
	private static String username = "username";
	
	@Mock
	private IClientRequestHandler clientRequestHandler;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
		customer = new Customer(clientRequestHandler);
	}
	
	@Test
	public void isFreeCustomerNameSuccessfulTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.IS_FREE_CUSTOMER_NAME
						,Serialization.serialize(username)).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.isFreeUsername(username);
		} catch (CriticalError e) {
			fail();
		}

	}

	@Test
	public void isFreeCustomerNameCriticalErrorTest() {	
		try {
			Mockito.when(
					clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.IS_FREE_CUSTOMER_NAME
							,Serialization.serialize(username)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_ERR).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.isFreeUsername(username);
			fail();
		} catch (CriticalError e) {
			/* success */
		}
	}
	
	@Test
	public void isFreeCustomerNameConnectionFailureTest() {	
		try {
			Mockito.when(
					clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.IS_FREE_CUSTOMER_NAME
							,Serialization.serialize(username)).serialize()))
			.thenThrow(new SocketTimeoutException());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.isFreeUsername(username);
			fail();
		} catch (CriticalError e) {
			/* success */
		}
	}
	
	@Test
	public void isFreeCustomerNameIllegalResultTest() {	
		try {
			Mockito.when(
					clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.IS_FREE_CUSTOMER_NAME
							,Serialization.serialize(username)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_GROCERY_LIST_IS_EMPTY).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.isFreeUsername(username);
			fail();
		} catch (CriticalError e) {
			/* success */
		}
	}
}
