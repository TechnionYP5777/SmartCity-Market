package CustomerImplemantationsTests;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.time.LocalDate;
import java.util.HashSet;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import BasicCommonClasses.CustomerProfile;
import BasicCommonClasses.ForgotPasswordData;
import BasicCommonClasses.Ingredient;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import CustomerContracts.ICustomer;
import SMExceptions.CommonExceptions.CriticalError;
import CustomerContracts.ACustomerExceptions.InvalidParameter;
import CustomerContracts.ACustomerExceptions.UsernameAlreadyExists;
import CustomerImplementations.Customer;
import CustomerImplementations.CustomerDefs;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

@RunWith(MockitoJUnitRunner.class)

public class RegisterNewCustomerTest {
	private ICustomer customer;

	private static final CustomerProfile customerProfile = new CustomerProfile("userName", "password",
			"firstName", "lastName", "phoneNumber", "emailAddress",
			"city", "street", LocalDate.now(), new HashSet<Ingredient>(),
			new ForgotPasswordData("q", "a"));
	
	@Mock
	private IClientRequestHandler clientRequestHandler;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
		customer = new Customer(clientRequestHandler);
	}
	
	@Test
	public void registerNewCustomerSuccessfulTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.REGISTER_NEW_CUSTOMER,
						Serialization.serialize(customerProfile)).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.registerNewCustomer(customerProfile);
		} catch (CriticalError | InvalidParameter | UsernameAlreadyExists e) {
			fail();
		}
	}
	
	@Test
	public void registerNewCustomerCriticalErrorTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.REGISTER_NEW_CUSTOMER,
						Serialization.serialize(customerProfile)).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_ERR).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.registerNewCustomer(customerProfile);
		} catch (InvalidParameter | UsernameAlreadyExists e) {
			fail();
		} catch (CriticalError e) {
			/* success */
		}
	}
	
	@Test
	public void registerNewCustomerInvalidParameterTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.REGISTER_NEW_CUSTOMER,
						Serialization.serialize(customerProfile)).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.registerNewCustomer(customerProfile);
		} catch (CriticalError | UsernameAlreadyExists e) {
			fail();
		} catch (InvalidParameter e) {
			/* success */
		}
	}
	
	@Test
	public void registerNewCustomerUsernameAlreadyExistsTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.REGISTER_NEW_CUSTOMER,
						Serialization.serialize(customerProfile)).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_USERNAME_ALREADY_EXISTS).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.registerNewCustomer(customerProfile);
		} catch (CriticalError | InvalidParameter e) {
			fail();
		} catch (UsernameAlreadyExists e) {
			/* success */
		}
	}
	
	@Test
	public void registerNewCustomerConnectionFailureTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.REGISTER_NEW_CUSTOMER,
						Serialization.serialize(customerProfile)).serialize()))
			.thenThrow(new SocketTimeoutException());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.registerNewCustomer(customerProfile);
		} catch (UsernameAlreadyExists | InvalidParameter e) {
			fail();
		} catch (CriticalError e) {
			/* success */
		}
	}
	
	@Test
	public void registerNewCustomerIllegalResultTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.REGISTER_NEW_CUSTOMER,
						Serialization.serialize(customerProfile)).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_GROCERY_LIST_IS_EMPTY).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.registerNewCustomer(customerProfile);
		} catch (UsernameAlreadyExists | InvalidParameter e) {
			fail();
		} catch (CriticalError e) {
			/* success */
		}
	}
}
