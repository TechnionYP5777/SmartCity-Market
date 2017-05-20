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
import CustomerContracts.ACustomerExceptions.CriticalError;
import CustomerContracts.ACustomerExceptions.CustomerNotConnected;
import CustomerContracts.ACustomerExceptions.InvalidParameter;
import CustomerImplementations.CustomerDefs;
import CustomerImplementations.RegisteredCustomer;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

@RunWith(MockitoJUnitRunner.class)

public class RemoveCustomerTest {
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
	public void removeCustomerSuccessfulTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.REMOVE_CUSTOMER,
						Serialization.serialize(username)).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.removeCustomer();
		} catch (CustomerNotConnected | InvalidParameter | AuthenticationError | CriticalError e) {
			fail();
		}
	}
	
	@Test
	public void removeCustomerCustomerNotConnectedTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.REMOVE_CUSTOMER,
						Serialization.serialize(username)).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.removeCustomer();
		} catch (InvalidParameter | AuthenticationError | CriticalError e) {
			fail();
		}
		catch(CustomerNotConnected e) {
			/* success */
		}
	}
	
	@Test
	public void removeCustomerInvalidParameterTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.REMOVE_CUSTOMER,
						Serialization.serialize(username)).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.removeCustomer();
		} catch (CustomerNotConnected | AuthenticationError | CriticalError e) {
			fail();
		}
		catch(InvalidParameter e) {
			/* success */
		}
	}
	
	@Test
	public void removeCustomerAuthenticationErrorTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.REMOVE_CUSTOMER,
						Serialization.serialize(username)).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_USERNAME_DOES_NOT_EXIST_WRONG_PASSWORD).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.removeCustomer();
		} catch (CustomerNotConnected | InvalidParameter | CriticalError e) {
			fail();
		}
		catch(AuthenticationError e) {
			/* success */
		}
	}
	
	@Test
	public void removeCustomerCriticalErrorTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.REMOVE_CUSTOMER,
						Serialization.serialize(username)).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_ERR).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.removeCustomer();
		} catch (CustomerNotConnected | InvalidParameter | AuthenticationError e) {
			fail();
		}
		catch(CriticalError e) {
			/* success */
		}
	}
	
	@Test
	public void removeCustomerIlligalResultTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.REMOVE_CUSTOMER,
						Serialization.serialize(username)).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_CATALOG_PRODUCT_ALREADY_EXISTS).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.removeCustomer();
		} catch (CustomerNotConnected | InvalidParameter | AuthenticationError e) {
			fail();
		}
		catch(CriticalError e) {
			/* success */
		}
	}
	
	@Test
	public void removeCustomerConnectionFailureTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.REMOVE_CUSTOMER,
						Serialization.serialize(username)).serialize()))
				.thenThrow(new SocketTimeoutException());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.removeCustomer();
		} catch (CustomerNotConnected | InvalidParameter | AuthenticationError e) {
			fail();
		}
		catch(CriticalError e) {
			/* success */
		}
	}
}
