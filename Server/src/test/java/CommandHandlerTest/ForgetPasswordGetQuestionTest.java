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

import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import CommandHandler.CommandExecuter;
import SQLDatabase.SQLDatabaseConnection;
import SQLDatabase.SQLDatabaseException.ClientNotExist;
import SQLDatabase.SQLDatabaseException.CriticalError;

/**
 * @author Aviad Cohen
 * @since 2017-07-01 
 */
@RunWith(MockitoJUnitRunner.class)
public class ForgetPasswordGetQuestionTest {

	@Mock
	private SQLDatabaseConnection sqlDatabaseConnection;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
	}
	
	private static final String username = "username";
	private static final String ques = "ques";
	private static final int senderIDWorker = 0;
	private static final int senderIDCustomer = 1;

	@Test
	public void forgetPasswordGetQuestionSuccessfulTest() {
		String command = new CommandWrapper(senderIDWorker, CommandDescriptor.FORGOT_PASSWORD_GET_QUESTION,
				new Gson().toJson(username, String.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(sqlDatabaseConnection.getSecurityQuestionWorker(username))
			.thenReturn(ques);
		} catch (CriticalError | ClientNotExist e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_OK, out.getResultDescriptor());
		
		command = new CommandWrapper(senderIDCustomer, CommandDescriptor.FORGOT_PASSWORD_GET_QUESTION,
				new Gson().toJson(username, String.class)).serialize();
		commandExecuter = new CommandExecuter(command);
		
		try {
			Mockito.when(sqlDatabaseConnection.getSecurityQuestionWorker(username))
			.thenReturn(ques);
		} catch (CriticalError | ClientNotExist e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_OK, out.getResultDescriptor());
	}
	
	@Test
	public void forgetPasswordGetQuestionCriticalErrorTest() {
		String command = new CommandWrapper(senderIDWorker, CommandDescriptor.FORGOT_PASSWORD_GET_QUESTION,
				new Gson().toJson(username, String.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(sqlDatabaseConnection.getSecurityQuestionWorker(username))
			.thenThrow(new CriticalError());
		} catch (ClientNotExist e) {
			fail();
		} catch (CriticalError e) {
			/* success */
		}
				
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_ERR, out.getResultDescriptor());
	}
	
	@Test
	public void forgetPasswordGetQuestionClientNotExistTest() {
		String command = new CommandWrapper(senderIDWorker, CommandDescriptor.FORGOT_PASSWORD_GET_QUESTION,
				new Gson().toJson(username, String.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(sqlDatabaseConnection.getSecurityQuestionWorker(username))
			.thenThrow(new ClientNotExist());
		} catch (CriticalError e) {
			fail();
		} catch (ClientNotExist e) {
			/* success */
		}
				
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_USERNAME_DOES_NOT_EXIST, out.getResultDescriptor());
	}
}