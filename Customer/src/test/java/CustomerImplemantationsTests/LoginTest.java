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

import BasicCommonClasses.Login;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import CustomerContracts.ICustomer;
import CustomerContracts.ACustomerExceptions.AuthenticationError;
import CustomerContracts.ACustomerExceptions.CriticalError;
import CustomerImplementations.Customer;
import CustomerImplementations.CustomerDefs;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

/**
 * @author Lior Ben Ami
 * @since 2017-03-25
 */

@RunWith(MockitoJUnitRunner.class)
public class LoginTest {
	private ICustomer customer;

	@Mock
	private IClientRequestHandler clientRequestHandler;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
		customer = new Customer(clientRequestHandler);
	}
	
	@Test
	public void loginSuccessfulTest() {
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond((new CommandWrapper(CustomerDefs.loginCommandSenderId,
						CommandDescriptor.LOGIN, Serialization.serialize(new Login("test", "test"))).serialize())))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());
			
		} catch (IOException ¢) {
			¢.printStackTrace();
			fail();
		}
		try {
			customer.login("test", "test");
		} catch (CriticalError | AuthenticationError ¢) {
			¢.printStackTrace();
			fail();
		}
		//TODO lior - add getCartLoginDetails - issue #301
//		assertEquals("test", customer.getCartLoginDetails().getUserName());
//		assertEquals("test", customer.getCartLoginDetails().getPassword());
	}
	
	@Test
	public void loginCriticalErrorTest() {
		try {
			Mockito.when(
					clientRequestHandler.sendRequestWithRespond((new CommandWrapper(CustomerDefs.loginCommandSenderId,
							CommandDescriptor.LOGIN, Serialization.serialize(new Login("test", "test"))).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_ERR).serialize());
		} catch (IOException ¢) {
			¢.printStackTrace();
			fail();
		}
		try {
			customer.login("test", "test");
		} catch (AuthenticationError ¢) {
			¢.printStackTrace();
			fail();
		} catch (CriticalError ¢) {
		/* Test Passed */
		}
	}
	
	@Test
	public void AuthenticationError() {
		try {
			Mockito.when(
					clientRequestHandler.sendRequestWithRespond((new CommandWrapper(CustomerDefs.loginCommandSenderId,
							CommandDescriptor.LOGIN, Serialization.serialize(new Login("test", "test"))).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_USERNAME_DOES_NOT_EXIST_WRONG_PASSWORD).serialize());
		} catch (IOException ¢) {
			¢.printStackTrace();
			fail();
		}
		try {
			customer.login("test", "test");
		} catch (CriticalError ¢) {
			¢.printStackTrace();
			fail();
		} catch (AuthenticationError ¢) {
		/* Test Passed */
		}
	}
}
