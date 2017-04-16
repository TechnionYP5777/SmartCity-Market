package CustomerImplemantationsTests;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import BasicCommonClasses.CustomerProfile;
import BasicCommonClasses.Login;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import CustomerContracts.IRegisteredCustomer;
import CustomerContracts.ACustomerExceptions.AuthenticationError;
import CustomerContracts.ACustomerExceptions.CriticalError;
import CustomerImplementations.CustomerDefs;
import CustomerImplementations.RegisteredCustomer;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

@RunWith(MockitoJUnitRunner.class)

/**
 * @author idan atias
 *
 */
public class UpdateCustomerProfileTest {
	private IRegisteredCustomer regCustomer;

	@Mock
	private IClientRequestHandler clientRequestHandler;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
		regCustomer = new RegisteredCustomer(clientRequestHandler);
		try {
			Mockito.when(
					clientRequestHandler.sendRequestWithRespond(
							new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.LOGIN_CUSTOMER,
									Serialization.serialize(new Login("kuku", "1234"))).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK, Serialization.serialize(
							new CustomerProfile("kuku", "1234", null, null, null, null, null, null, null, null, null)))
									.serialize());

		} catch (IOException ¢) {
			¢.printStackTrace();
			fail();
		}
		try {
			regCustomer.login("kuku", "1234");
		} catch (CriticalError | AuthenticationError ¢) {
			¢.printStackTrace();
			fail();
		}
	}

	@After
	public void cleanup() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.LOGOUT).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());

		} catch (IOException ¢) {
			¢.printStackTrace();
			fail();
		}
		try {
			regCustomer.login("kuku", "1234");
		} catch (CriticalError | AuthenticationError ¢) {
			¢.printStackTrace();
			fail();
		}
	}

	@Test
	public void updateCustomerProfileFIeldsSuccessfullTest() {
		CustomerProfile cProfile = null;
		try {
			cProfile = regCustomer.getCustomerProfile();
			assert ("kuku".equals(cProfile.getUserName()));
			assert (cProfile.getFirstName() == null);
			cProfile.setFirstName("bla");
		} catch (Exception ex) {
			fail();
		}
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(new CommandWrapper(regCustomer.getId(),
					CommandDescriptor.UPDATE_CUSTOMER_PROFILE, Serialization.serialize(cProfile)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());
		} catch (IOException ¢) {
			¢.printStackTrace();
			fail();
		}

		try {
			regCustomer.updateCustomerProfile(cProfile);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail();
		}
	}

	@Test
	public void validateExcpetionIsThrownIfARegisteredCustomerGetsAuthenticationErrorTest() {
		CustomerProfile cProfile = null;
		try {
			cProfile = regCustomer.getCustomerProfile();
		} catch (CriticalError e) {
			e.printStackTrace();
			fail();
		}
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(new CommandWrapper(regCustomer.getId(),
					CommandDescriptor.UPDATE_CUSTOMER_PROFILE, Serialization.serialize(cProfile)).serialize()))
					.thenReturn(
							new CommandWrapper(ResultDescriptor.SM_USERNAME_DOES_NOT_EXIST_WRONG_PASSWORD).serialize());
		} catch (IOException ¢) {
			¢.printStackTrace();
			fail();
		}

		try {
			regCustomer.updateCustomerProfile(cProfile);
		} catch (Exception ex) {
			/* success */
			return;
		}
		fail();
	}
}
