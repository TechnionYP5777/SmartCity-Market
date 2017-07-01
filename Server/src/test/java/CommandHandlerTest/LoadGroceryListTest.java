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

import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import CommandHandler.CommandExecuter;
import SQLDatabase.SQLDatabaseConnection;
import SQLDatabase.SQLDatabaseException.CriticalError;
import SQLDatabase.SQLDatabaseException.NoGroceryListToRestore;

/**
 * @author Aviad Cohen
 * @since 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class LoadGroceryListTest {

	@Mock
	private SQLDatabaseConnection sqlDatabaseConnection;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
	}
	
	@Test
	public void loadGroceryListSuccessfulTest() {
		int senderID = 1;
		String command = new CommandWrapper(senderID, CommandDescriptor.LOAD_GROCERY_LIST).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(sqlDatabaseConnection.cartRestoreGroceryList(senderID))
							.thenReturn("");
		} catch (CriticalError | NoGroceryListToRestore e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_OK, out.getResultDescriptor());
	}
	
	@Test
	public void loadGroceryListCriticalErrorTest() {
		int senderID = 1;
		String command = new CommandWrapper(senderID, CommandDescriptor.LOAD_GROCERY_LIST).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(sqlDatabaseConnection.cartRestoreGroceryList(senderID))
							.thenThrow(new CriticalError());
		} catch (CriticalError __) {
			/* Success */
		} catch (NoGroceryListToRestore e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_ERR, out.getResultDescriptor());
	}
	
	@Test
	public void loadGroceryListNoGroceryListToRestoreTest() {
		int senderID = 1;
		String command = new CommandWrapper(senderID, CommandDescriptor.LOAD_GROCERY_LIST).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(sqlDatabaseConnection.cartRestoreGroceryList(senderID))
							.thenThrow(new NoGroceryListToRestore());
		} catch (NoGroceryListToRestore __) {
			/* Success */
		} catch (CriticalError e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED, out.getResultDescriptor());
	}
}