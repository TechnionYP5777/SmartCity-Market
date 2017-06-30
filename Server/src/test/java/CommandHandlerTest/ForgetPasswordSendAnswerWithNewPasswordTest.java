package CommandHandlerTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gson.Gson;

import BasicCommonClasses.ForgotPasswordData;
import BasicCommonClasses.Login;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import CommandHandler.CommandExecuter;
import SQLDatabase.SQLDatabaseConnection;
import SQLDatabase.SQLDatabaseException.ClientNotExist;
import SQLDatabase.SQLDatabaseException.CriticalError;

@RunWith(MockitoJUnitRunner.class)
public class ForgetPasswordSendAnswerWithNewPasswordTest {

	@Mock
	private SQLDatabaseConnection sqlDatabaseConnection;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
	}
	
	private static final String username = "username";
	private static final String answer = "answer";
	private static final int senderIDWorker = 0;
	private static final int senderIDCustomer = 1;
	private static final Login login = new Login(username, username, new ForgotPasswordData(answer, answer));

	@Test
	public void forgetPasswordGetQuestionSuccessfulTest() {
		String command = new CommandWrapper(senderIDWorker, CommandDescriptor.FORGOT_PASSWORD_SEND_ANSWER_WITH_NEW_PASSWORD,
				new Gson().toJson(login, Login.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(sqlDatabaseConnection.verifySecurityAnswerWorker(username, answer))
			.thenReturn(true);
		} catch (CriticalError | ClientNotExist e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_OK, out.getResultDescriptor());
		
		command = new CommandWrapper(senderIDCustomer, CommandDescriptor.FORGOT_PASSWORD_SEND_ANSWER_WITH_NEW_PASSWORD,
				new Gson().toJson(login, Login.class)).serialize();
		commandExecuter = new CommandExecuter(command);
		
		try {
			Mockito.when(sqlDatabaseConnection.verifySecurityAnswerCustomer(username, answer))
			.thenReturn(true);
		} catch (CriticalError | ClientNotExist e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_OK, out.getResultDescriptor());
		
		command = new CommandWrapper(senderIDCustomer, CommandDescriptor.FORGOT_PASSWORD_SEND_ANSWER_WITH_NEW_PASSWORD,
				new Gson().toJson(login, Login.class)).serialize();
		commandExecuter = new CommandExecuter(command);
		
		try {
			Mockito.when(sqlDatabaseConnection.verifySecurityAnswerCustomer(username, answer))
			.thenReturn(false);
		} catch (CriticalError | ClientNotExist e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_FOROGT_PASSWORD_WRONG_ANSWER, out.getResultDescriptor());
	}
	
	@Test
	public void forgetPasswordGetQuestionCriticalErrorTest() {
		String command = new CommandWrapper(senderIDWorker, CommandDescriptor.FORGOT_PASSWORD_SEND_ANSWER_WITH_NEW_PASSWORD,
				new Gson().toJson(login, Login.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(sqlDatabaseConnection.verifySecurityAnswerWorker(username, answer))
			.thenThrow(new CriticalError());
		} catch (CriticalError e) {
			fail();
		} catch (ClientNotExist e) {
			/* success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);

		assertEquals(ResultDescriptor.SM_ERR, out.getResultDescriptor());
	}
}