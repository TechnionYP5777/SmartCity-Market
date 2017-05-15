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
import SQLDatabase.SQLDatabaseException.ClientAlreadyExist;
import SQLDatabase.SQLDatabaseException.ClientNotConnected;
import SQLDatabase.SQLDatabaseException.CriticalError;

@RunWith(MockitoJUnitRunner.class)
public class RegisterNewWorkerTest {

	@Mock
	private SQLDatabaseConnection sqlDatabaseConnection;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
	}

	private static final ForgotPasswordData forgotPasswordData = new ForgotPasswordData("question", "answer");
	private static final Login worker = new Login("worker", "worker", forgotPasswordData);
	
	@Test
	public void registerNewWorkerSuccessfulTest() {
		int senderID = 1;
		String command = new CommandWrapper(senderID, CommandDescriptor.REGISTER_NEW_WORKER,
				new Gson().toJson(worker, Login.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doNothing().when(sqlDatabaseConnection).addWorker(senderID, worker, worker.getForgetPassword());
		} catch (CriticalError | ClientAlreadyExist | ClientNotConnected e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_OK, out.getResultDescriptor());
	}

	@Test
	public void registerNewWorkerCriticalErrorTest() {
		int senderID = 1;
		String command = new CommandWrapper(senderID, CommandDescriptor.REGISTER_NEW_WORKER,
				new Gson().toJson(worker, Login.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new CriticalError()).when(sqlDatabaseConnection).addWorker(senderID, worker, worker.getForgetPassword());
		} catch (ClientAlreadyExist | ClientNotConnected e) {
			fail();
		} catch (CriticalError __) {
			/* success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_ERR, out.getResultDescriptor());
	}
	
	@Test
	public void registerNewWorkerClientAlreadyExistTest() {
		int senderID = 1;
		String command = new CommandWrapper(senderID, CommandDescriptor.REGISTER_NEW_WORKER,
				new Gson().toJson(worker, Login.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new ClientAlreadyExist()).when(sqlDatabaseConnection).addWorker(senderID, worker, worker.getForgetPassword());
		} catch (CriticalError | ClientNotConnected e) {
			fail();
		} catch (ClientAlreadyExist __) {
			/* success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_USERNAME_ALREADY_EXISTS, out.getResultDescriptor());
	}
	
	@Test
	public void registerNewWorkerClientNotConnectedTest() {
		int senderID = 1;
		String command = new CommandWrapper(senderID, CommandDescriptor.REGISTER_NEW_WORKER,
				new Gson().toJson(worker, Login.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new ClientNotConnected()).when(sqlDatabaseConnection).addWorker(senderID, worker, worker.getForgetPassword());
		} catch (CriticalError | ClientAlreadyExist e) {
			fail();
		} catch (ClientNotConnected __) {
			/* success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED, out.getResultDescriptor());
	}
	
	@Test
	public void registerNewWorkerIllegalInputTest() {
		int senderID = 1;
		String command = new CommandWrapper(senderID, CommandDescriptor.REGISTER_NEW_WORKER,
				new Gson().toJson("", String.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_ERR, out.getResultDescriptor());
	}
}