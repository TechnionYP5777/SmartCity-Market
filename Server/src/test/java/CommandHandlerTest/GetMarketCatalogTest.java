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

import BasicCommonClasses.CatalogProduct;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import CommandHandler.CommandExecuter;
import SQLDatabase.SQLDatabaseConnection;
import SQLDatabase.SQLDatabaseException.CriticalError;

/**
 * @author Lior Ben Ami
 * @since 2017-07-01  
 */
@RunWith(MockitoJUnitRunner.class)
public class GetMarketCatalogTest {
	
	@Mock
	private SQLDatabaseConnection sqlDatabaseConnection;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
	}
	
	private static final int senderID = 1;
	private static final List<CatalogProduct> catalog =  Arrays.asList(
			new CatalogProduct(1234567, "bamb", null, null, null, 10.0, null, null));

	@Test
	public void getMarketCatalogTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.GET_MARKET_CATALOG).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(sqlDatabaseConnection.getAllProductsInCatalog())
			.thenReturn(catalog);
		} catch (CriticalError e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_OK, out.getResultDescriptor());
	}
	
	@Test
	public void getMarketCatalogCriticaErrorlTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.GET_MARKET_CATALOG).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(sqlDatabaseConnection.getAllProductsInCatalog())
			.thenThrow(new CriticalError());
		} catch (CriticalError e) {
			//success
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_ERR, out.getResultDescriptor());
	}


}
