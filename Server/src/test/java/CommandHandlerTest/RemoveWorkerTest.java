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
import SQLDatabase.SQLDatabaseException.ClientNotConnected;
import SQLDatabase.SQLDatabaseException.ClientNotExist;
import SQLDatabase.SQLDatabaseException.CriticalError;

/**
 * @author Aviad Cohen
 * @since 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class RemoveWorkerTest {

	@Mock
	private SQLDatabaseConnection sqlDatabaseConnection;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
	}
	
	private static final String username = "username";
	private static final int senderID = 1;
	
	@Test
	public void removeWorkerSuccessfulTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.REMOVE_WORKER,
				new Gson().toJson(username, String.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		
		try {
			Mockito.doNothing().when(sqlDatabaseConnection).removeWorker(senderID, username);
		} catch (CriticalError | ClientNotExist | ClientNotConnected e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_OK, out.getResultDescriptor());
	}
	
	@Test
	public void removeWorkerCriticalErrorTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.REMOVE_WORKER,
				new Gson().toJson(username, String.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		
		try {
			Mockito.doThrow(new CriticalError()).when(sqlDatabaseConnection).removeWorker(senderID, username);
		} catch (ClientNotExist | ClientNotConnected e) {
			fail();
		} catch (CriticalError e) {
			/* success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_ERR, out.getResultDescriptor());
	}
	
	@Test
	public void removeWorkerClientNotExistTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.REMOVE_WORKER,
				new Gson().toJson(username, String.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		
		try {
			Mockito.doThrow(new ClientNotExist()).when(sqlDatabaseConnection).removeWorker(senderID, username);
		} catch (CriticalError | ClientNotConnected e) {
			fail();
		} catch (ClientNotExist e) {
			/* success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_USERNAME_DOES_NOT_EXIST, out.getResultDescriptor());
	}
	
	@Test
	public void removeWorkerClientNotConnectedTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.REMOVE_WORKER,
				new Gson().toJson(username, String.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		
		try {
			Mockito.doThrow(new ClientNotConnected()).when(sqlDatabaseConnection).removeWorker(senderID, username);
		} catch (CriticalError | ClientNotExist e) {
			fail();
		} catch (ClientNotConnected e) {
			/* success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED, out.getResultDescriptor());
	}
}