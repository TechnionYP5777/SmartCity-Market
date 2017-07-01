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

import BasicCommonClasses.Sale;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import CommandHandler.CommandExecuter;
import SQLDatabase.SQLDatabaseConnection;
import SQLDatabase.SQLDatabaseException.ClientNotConnected;
import SQLDatabase.SQLDatabaseException.CriticalError;
import SQLDatabase.SQLDatabaseException.SaleAlreadyExist;

/**
 * @author Lior Ben Ami
 * @since 2017-07-01
 */
@RunWith(MockitoJUnitRunner.class)
public class CreateNewSaleTest {
	
	@Mock
	private SQLDatabaseConnection sqlDatabaseConnection;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
	}
	
	private static final Sale sale = new Sale(1, 1L * 12345678, 2, 10.0);
	private static final int senderID = 1;
	
	@Test
	public void createNewSaleSuccessfulTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.CREATE_NEW_SALE,
				new Gson().toJson(sale, Sale.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when((sqlDatabaseConnection).addSale(senderID, sale, true))
			.thenReturn(new Integer(1));
		} catch (CriticalError | ClientNotConnected | SaleAlreadyExist  e1) {
			fail();
		}
	
		out = commandExecuter.execute(sqlDatabaseConnection);
		assertEquals(ResultDescriptor.SM_OK, out.getResultDescriptor());
	}
	
	@Test
	public void createNewSaleCriticalErrorTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.CREATE_NEW_SALE,
				new Gson().toJson(sale, Sale.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when((sqlDatabaseConnection).addSale(senderID, sale, true))
			.thenThrow(new CriticalError());
		} catch ( ClientNotConnected | SaleAlreadyExist  e1) {
			fail();
		} catch (CriticalError e) {
			//Succeed
		}
	
		out = commandExecuter.execute(sqlDatabaseConnection);
		assertEquals(ResultDescriptor.SM_ERR, out.getResultDescriptor());
	}
	
	@Test
	public void createNewSaleClientNotConnectedTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.CREATE_NEW_SALE,
				new Gson().toJson(sale, Sale.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when((sqlDatabaseConnection).addSale(senderID, sale, true))
			.thenThrow(new ClientNotConnected());
		} catch (CriticalError | SaleAlreadyExist e1) {
			fail();
		} catch (ClientNotConnected e) {
			//Succeed
		}
	
		out = commandExecuter.execute(sqlDatabaseConnection);
		assertEquals(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED, out.getResultDescriptor());
	}
	
	@Test
	public void createNewSaleSaleAlreadyExistTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.CREATE_NEW_SALE,
				new Gson().toJson(sale, Sale.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when((sqlDatabaseConnection).addSale(senderID, sale, true))
			.thenThrow(new SaleAlreadyExist());
		} catch (CriticalError | ClientNotConnected  e1) {
			fail();
		} catch (SaleAlreadyExist e) {
			//Succeed
		}
	
		out = commandExecuter.execute(sqlDatabaseConnection);
		assertEquals(ResultDescriptor.PARAM_ID_ALREADY_EXISTS, out.getResultDescriptor());
	}
}
