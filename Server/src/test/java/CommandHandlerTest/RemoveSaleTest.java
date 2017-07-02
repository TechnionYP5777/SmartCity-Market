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
import SQLDatabase.SQLDatabaseException.CriticalError;
import SQLDatabase.SQLDatabaseException.SaleNotExist;
import SQLDatabase.SQLDatabaseException.SaleStillUsed;

/**
 * @author Lior Ben Ami
 * @since 2017-07-01
 */
@RunWith(MockitoJUnitRunner.class)
public class RemoveSaleTest {
	
	@Mock
	private SQLDatabaseConnection sqlDatabaseConnection;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
	}
	
	private static final int senderID = 1, saleID = 1;

	@Test
	public void removeSaleSuccessfulTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.REMOVE_SALE,
				new Gson().toJson(saleID, Integer.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		try {
			Mockito.doNothing().when(sqlDatabaseConnection).removeSale(senderID, saleID);
		} catch (CriticalError | ClientNotConnected | SaleNotExist | SaleStillUsed e) {
			fail();
		}
		out = commandExecuter.execute(sqlDatabaseConnection);
		assertEquals(ResultDescriptor.SM_OK, out.getResultDescriptor());
	}
	
	@Test
	public void removeSaleCriticalErrorTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.REMOVE_SALE,
				new Gson().toJson(saleID, Integer.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		try {
			Mockito.doThrow(new CriticalError()).when(sqlDatabaseConnection).removeSale(senderID, saleID);
		} catch (ClientNotConnected | SaleNotExist | SaleStillUsed e) {
			fail();
		} catch (CriticalError e) {
			//succeed
		}
		out = commandExecuter.execute(sqlDatabaseConnection);
		assertEquals(ResultDescriptor.SM_ERR, out.getResultDescriptor());
	}
	
	
	@Test
	public void removeSaleClientNotConnectedTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.REMOVE_SALE,
				new Gson().toJson(saleID, Integer.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		try {
			Mockito.doThrow(new ClientNotConnected()).when(sqlDatabaseConnection).removeSale(senderID, saleID);
		} catch (CriticalError | SaleNotExist | SaleStillUsed e) {
			fail();
		} catch (ClientNotConnected e) {
			//succeed
		}
		out = commandExecuter.execute(sqlDatabaseConnection);
		assertEquals(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED, out.getResultDescriptor());
	}
	
	@Test
	public void removeSaleSaleNotExistTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.REMOVE_SALE,
				new Gson().toJson(saleID, Integer.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		try {
			Mockito.doThrow(new SaleNotExist()).when(sqlDatabaseConnection).removeSale(senderID, saleID);
		} catch (CriticalError | ClientNotConnected | SaleStillUsed e) {
			fail();
		} catch (SaleNotExist e) {
			//succeed
		}
		out = commandExecuter.execute(sqlDatabaseConnection);
		assertEquals(ResultDescriptor.PARAM_ID_IS_NOT_EXIST, out.getResultDescriptor());
	}
	
	@Test
	public void removeSaleSaleStillUsedTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.REMOVE_SALE,
				new Gson().toJson(saleID, Integer.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		try {
			Mockito.doThrow(new SaleStillUsed()).when(sqlDatabaseConnection).removeSale(senderID, saleID);
		} catch (CriticalError | ClientNotConnected | SaleNotExist e) {
			fail();
		} catch (SaleStillUsed  e) {
			//succeed
		}
		out = commandExecuter.execute(sqlDatabaseConnection);
		assertEquals(ResultDescriptor.PARAM_ID_STILL_IN_USE, out.getResultDescriptor());
	}
	
}
