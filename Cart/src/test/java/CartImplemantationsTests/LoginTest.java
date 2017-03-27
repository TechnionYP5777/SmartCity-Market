package CartImplemantationsTests;

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
import CartContracts.ICart;
import CartContracts.ACartExceptions.AuthenticationError;
import CartContracts.ACartExceptions.CriticalError;
import CartImplementations.Cart;
import CartImplementations.CartDefs;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

/**
 * @author Lior Ben Ami
 * @since 2017-03-25
 */

@RunWith(MockitoJUnitRunner.class)
public class LoginTest {
	private ICart cart;

	@Mock
	private IClientRequestHandler clientRequestHandler;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
		cart = new Cart(clientRequestHandler);
	}
	
	@Test
	public void loginSuccessfulTest() {
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond((new CommandWrapper(CartDefs.loginCommandSenderId,
						CommandDescriptor.LOGIN, Serialization.serialize(new Login("test", "test"))).serialize())))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());
			
		} catch (IOException ¢) {
			¢.printStackTrace();
			fail();
		}
		try {
			cart.login("test", "test");
		} catch (CriticalError | AuthenticationError ¢) {
			¢.printStackTrace();
			fail();
		}
		//TODO lior - add getCartLoginDetails - issue #301
//		assertEquals("test", cart.getCartLoginDetails().getUserName());
//		assertEquals("test", cart.getCartLoginDetails().getPassword());
	}
	
	@Test
	public void loginCriticalErrorTest() {
		try {
			Mockito.when(
					clientRequestHandler.sendRequestWithRespond((new CommandWrapper(CartDefs.loginCommandSenderId,
							CommandDescriptor.LOGIN, Serialization.serialize(new Login("test", "test"))).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_ERR).serialize());
		} catch (IOException ¢) {
			¢.printStackTrace();
			fail();
		}
		try {
			cart.login("test", "test");
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
					clientRequestHandler.sendRequestWithRespond((new CommandWrapper(CartDefs.loginCommandSenderId,
							CommandDescriptor.LOGIN, Serialization.serialize(new Login("test", "test"))).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_USERNAME_DOES_NOT_EXIST_WRONG_PASSWORD).serialize());
		} catch (IOException ¢) {
			¢.printStackTrace();
			fail();
		}
		try {
			cart.login("test", "test");
		} catch (CriticalError ¢) {
			¢.printStackTrace();
			fail();
		} catch (AuthenticationError ¢) {
		/* Test Passed */
		}
	}
}
