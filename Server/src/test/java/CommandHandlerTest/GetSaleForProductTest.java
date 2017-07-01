package CommandHandlerTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gson.Gson;

import BasicCommonClasses.Ingredient;
import BasicCommonClasses.Sale;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import CommandHandler.CommandExecuter;
import SQLDatabase.SQLDatabaseConnection;
import SQLDatabase.SQLDatabaseException.ClientNotConnected;
import SQLDatabase.SQLDatabaseException.CriticalError;


/**
 * @author Lior Ben Ami
 * @since 2017-07-01  
 */
@RunWith(MockitoJUnitRunner.class)
public class GetSaleForProductTest {
	@Mock
	private SQLDatabaseConnection sqlDatabaseConnection;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
	}
	
	private static final int senderID = 1;
	private static final long barcode = 123456789;
	private static final Sale sale = new Sale(1, barcode , 2, 10.0);

	@Test
	public void getSaleForProductSuccesfulTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.GET_SALE_FOR_PRODUCT,
				new Gson().toJson(barcode, long.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(sqlDatabaseConnection.getSaleForProduct(barcode))
			.thenReturn(sale);
		} catch (CriticalError | ClientNotConnected e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_OK, out.getResultDescriptor());
	}
	
	
	@Test
	public void getSaleForProductCriticalErrorTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.GET_SALE_FOR_PRODUCT,
				new Gson().toJson(barcode, long.class)).serialize();		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(sqlDatabaseConnection.getSaleForProduct(barcode))
			.thenThrow(new CriticalError());
		} catch (ClientNotConnected e) {
			fail();
		} catch (CriticalError e) {
			//success
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_ERR, out.getResultDescriptor());
	}
	
	@Test
	public void getSaleForProductClientNotConnectedTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.GET_SALE_FOR_PRODUCT,
				new Gson().toJson(barcode, long.class)).serialize();		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(sqlDatabaseConnection.getSaleForProduct(barcode))
			.thenThrow(new ClientNotConnected());
		} catch (CriticalError e) {
			fail();
		} catch (ClientNotConnected  e) {
			//success
		}
		out = commandExecuter.execute(sqlDatabaseConnection);
		assertEquals(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED, out.getResultDescriptor());
	}

}
