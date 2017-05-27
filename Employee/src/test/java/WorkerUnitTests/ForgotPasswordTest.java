package WorkerUnitTests;

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
import EmployeeContracts.IWorker;
import EmployeeDefs.WorkerDefs;
import EmployeeImplementations.Worker;
import UtilsContracts.IClientRequestHandler;
import UtilsContracts.IForgotPasswordHandler;
import UtilsImplementations.ForgotPasswordHandler.NoSuchUserName;
import UtilsImplementations.ForgotPasswordHandler.WrongAnswer;
import UtilsImplementations.Serialization;

/**
 * @author idan atias
 *
 * @since May 23, 2017
 * 
 *        this class is for testing the forgot password mechanism of an employee
 */
@RunWith(MockitoJUnitRunner.class)
public class ForgotPasswordTest {
	private IWorker worker;
	private static String authQuestion = String.valueOf("what is the color of your car?");
	private static String authQuestionAnswer = String.valueOf("green");
	private static String newPass = String.valueOf("1234");
	private static String username = String.valueOf("user");


	@Mock
	private IClientRequestHandler clientRequestHandler;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
		worker = new Worker(clientRequestHandler);
	}

	@Test
	public void getAuthQuestionTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(new CommandWrapper(WorkerDefs.loginCommandSenderId,
					CommandDescriptor.FORGOT_PASSWORD_GET_QUESTION, Serialization.serialize(username)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK, authQuestion).serialize());
		} catch (IOException ¢) {
			fail();
		}
		String question = null;
		try {
			// TODO Check this username 
			question = ((IForgotPasswordHandler)worker).getForgotPasswordQuestion(username);
		} catch (NoSuchUserName e) {
			fail();
		}
		assert(question.equals(authQuestion));
	}
	
	@Test
	public void sendWrongAnswerWithNewPassword(){
		ForgotPasswordData forgotPassData = new ForgotPasswordData(null, authQuestionAnswer + "bla");
		Login ansAndPassContainer = new Login(worker.getUsername(), newPass, forgotPassData);
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(new CommandWrapper(WorkerDefs.loginCommandSenderId,
					CommandDescriptor.FORGOT_PASSWORD_SEND_ANSWER_WITH_NEW_PASSWORD, Serialization.serialize(ansAndPassContainer)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_FOROGT_PASSWORD_WRONG_ANSWER, null).serialize());
		} catch (IOException ¢) {
			fail();
		}
		try {
			((IForgotPasswordHandler)worker).sendAnswerAndNewPassword(authQuestionAnswer + "bla", newPass);
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
		Login ansAndPassContainer = new Login(worker.getUsername(), newPass, forgotPassData);
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(new CommandWrapper(WorkerDefs.loginCommandSenderId,
					CommandDescriptor.FORGOT_PASSWORD_SEND_ANSWER_WITH_NEW_PASSWORD, Serialization.serialize(ansAndPassContainer)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK, Serialization.serialize(true)).serialize());
		} catch (IOException ¢) {
			fail();
		}
		try {
			((IForgotPasswordHandler)worker).sendAnswerAndNewPassword(authQuestionAnswer, newPass);
		} catch (NoSuchUserName | WrongAnswer e) {
			fail();
		}
	}

}
