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

import BasicCommonClasses.ForgotPasswordData;
import BasicCommonClasses.Login;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import CustomerContracts.ICustomer;
import CustomerImplementations.Customer;
import CustomerImplementations.CustomerDefs;
import UtilsContracts.IClientRequestHandler;
import UtilsContracts.IForgotPasswordHandler;
import UtilsImplementations.Serialization;
import UtilsImplementations.ForgotPasswordHandler.NoSuchUserName;
import UtilsImplementations.ForgotPasswordHandler.WrongAnswer;

/**
 * @author Lior Ben Ami
 * @since  2017-06-30
 */
@RunWith(MockitoJUnitRunner.class)
public class ForgetPasswordQuestionTest {
	private ICustomer customer;
	private static String authQuestion = String.valueOf("what is the color of your car?");
	private static String authQuestionAnswer = String.valueOf("green");
	private static String newPass = String.valueOf("1234");
	private static String username = String.valueOf("user");


	@Mock
	private IClientRequestHandler clientRequestHandler;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
		customer = new Customer(clientRequestHandler);
	}

	@Test
	public void getAuthQuestionTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId,
					CommandDescriptor.FORGOT_PASSWORD_GET_QUESTION, Serialization.serialize(username)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK, authQuestion).serialize());
		} catch (IOException ¢) {
			fail();
		}
		String question = null;
		try {
			question = ((IForgotPasswordHandler)customer).getForgotPasswordQuestion(username);
		} catch (NoSuchUserName e) {
			fail();
		}
		assert(question.equals(authQuestion));
	}
	
	@Test
	public void getAuthQuestionFailTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId,
					CommandDescriptor.FORGOT_PASSWORD_GET_QUESTION, Serialization.serialize(username)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_FOROGT_PASSWORD_WRONG_ANSWER, authQuestion).serialize());
		} catch (IOException ¢) {
			fail();
		}
		String question = null;
		try {
			question = ((IForgotPasswordHandler)customer).getForgotPasswordQuestion(username);
		} catch (NoSuchUserName e) {
			fail();
		}
		assert(question == null);
	}
	
	@Test
	public void sendWrongAnswerWithNewPassword(){
		ForgotPasswordData forgotPassData = new ForgotPasswordData(null, authQuestionAnswer + "bla");
		Login ansAndPassContainer = new Login(null , newPass, forgotPassData);
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId,
					CommandDescriptor.FORGOT_PASSWORD_SEND_ANSWER_WITH_NEW_PASSWORD, Serialization.serialize(ansAndPassContainer)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_FOROGT_PASSWORD_WRONG_ANSWER, null).serialize());
		} catch (IOException ¢) {
			fail();
		}
		try {
			((IForgotPasswordHandler)customer).sendAnswerAndNewPassword(authQuestionAnswer + "bla", newPass);
		} catch (WrongAnswer e) {
			return; //success
		} catch (NoSuchUserName e) {
			fail();
		}
		fail();
	}
	
	@Test
	public void sendCorrectAnswerWithNewPassword(){
		ForgotPasswordData forgotPassData = new ForgotPasswordData(null, authQuestionAnswer);
		Login ansAndPassContainer = new Login(null, newPass, forgotPassData);
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId,
					CommandDescriptor.FORGOT_PASSWORD_SEND_ANSWER_WITH_NEW_PASSWORD, Serialization.serialize(ansAndPassContainer)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK, Serialization.serialize(true)).serialize());
		} catch (IOException ¢) {
			fail();
		}
		try {
			((IForgotPasswordHandler)customer).sendAnswerAndNewPassword(authQuestionAnswer, newPass);
		} catch (NoSuchUserName | WrongAnswer e) {
			fail();
		}
	}
}
