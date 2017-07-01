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

import BasicCommonClasses.CustomerProfile;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import CustomerContracts.ACustomerExceptions.AuthenticationError;
import SMExceptions.CommonExceptions.CriticalError;
import CustomerContracts.ACustomerExceptions.CustomerNotConnected;
import CustomerContracts.ACustomerExceptions.InvalidParameter;
import CustomerImplementations.CustomerDefs;
import CustomerImplementations.RegisteredCustomer;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

/** 
 * @author Lior Ben Ami
 * @since 2017-06-30
 */
@RunWith(MockitoJUnitRunner.class)
public class UpdateCustomerProfileTest {
	private RegisteredCustomer customer;

	static int amount = 10;
	static String username = "username";
	static CustomerProfile customerProfile = new CustomerProfile(username);
	
	@Mock
	private IClientRequestHandler clientRequestHandler;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
		customer = new RegisteredCustomer(clientRequestHandler);

	}
	
	@Test
	public void UpdateCustomerProfileSuccessfulTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.UPDATE_CUSTOMER_PROFILE,
						Serialization.serialize(customerProfile)).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.updateCustomerProfile(customerProfile);
		} catch (CustomerNotConnected | InvalidParameter | AuthenticationError | CriticalError e) {
			fail();
		}
	}
	
	@Test
	public void UpdateCustomerProfileCustomerNotConnectedTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.UPDATE_CUSTOMER_PROFILE,
						Serialization.serialize(customerProfile)).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.updateCustomerProfile(customerProfile);
		} catch (InvalidParameter | AuthenticationError | CriticalError e) {
			fail();
		} catch (CustomerNotConnected e) {
			/* success */
		}
	}
	
	@Test
	public void UpdateCustomerProfileInvalidParameterTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.UPDATE_CUSTOMER_PROFILE,
						Serialization.serialize(customerProfile)).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.updateCustomerProfile(customerProfile);
		} catch (CustomerNotConnected | AuthenticationError | CriticalError e) {
			fail();
		} catch (InvalidParameter e) {
			/* success */
		}
	}
	
	@Test
	public void UpdateCustomerProfileAuthenticationErrorTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.UPDATE_CUSTOMER_PROFILE,
						Serialization.serialize(customerProfile)).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_USERNAME_DOES_NOT_EXIST_WRONG_PASSWORD).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.updateCustomerProfile(customerProfile);
		} catch (CustomerNotConnected | InvalidParameter | CriticalError e) {
			fail();
		} catch (AuthenticationError e) {
			/* success */
		}
	}
	
	@Test
	public void UpdateCustomerProfileCriticalErrorTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.UPDATE_CUSTOMER_PROFILE,
						Serialization.serialize(customerProfile)).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_ERR).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.updateCustomerProfile(customerProfile);
		} catch (CustomerNotConnected | InvalidParameter | AuthenticationError e) {
			fail();
		} catch (CriticalError e) {
			/* success */
		}
	}
	
	@Test
	public void UpdateCustomerProfileIllegalResultTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.UPDATE_CUSTOMER_PROFILE,
						Serialization.serialize(customerProfile)).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_GROCERY_LIST_IS_EMPTY).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.updateCustomerProfile(customerProfile);
		} catch (CustomerNotConnected | InvalidParameter | AuthenticationError e) {
			fail();
		} catch (CriticalError e) {
			/* success */
		}
	}
	
	@Test
	public void UpdateCustomerProfileConnectionFailureTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.UPDATE_CUSTOMER_PROFILE,
						Serialization.serialize(customerProfile)).serialize()))
			.thenThrow(new SocketTimeoutException());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.updateCustomerProfile(customerProfile);
		} catch (CustomerNotConnected | InvalidParameter | AuthenticationError e) {
			fail();
		} catch (CriticalError e) {
			/* success */
		}
	}
}
