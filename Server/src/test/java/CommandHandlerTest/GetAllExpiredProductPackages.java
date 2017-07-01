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

import BasicCommonClasses.ProductPackage;
import BasicCommonClasses.SmartCode;
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
public class GetAllExpiredProductPackages {
	@Mock
	private SQLDatabaseConnection sqlDatabaseConnection;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
	}
	
	private static final int senderID = 1;
	private static final List<ProductPackage> expiredes =  Arrays.asList(
			new ProductPackage(new SmartCode(1234567, null), 10, null), new ProductPackage(new SmartCode(987654, null), 10, null));

	@Test
	public void getAllExpiredProductPackagesSuccesfulTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.GET_ALL_EXPIRED_PRODUCT_PACKAGES).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(sqlDatabaseConnection.getExpiredProductPackages())
			.thenReturn(expiredes);
		} catch (CriticalError e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_OK, out.getResultDescriptor());
	}
	
	@Test
	public void getAllSalesExpiredProductPackagesCriticalErrorTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.GET_ALL_EXPIRED_PRODUCT_PACKAGES).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(sqlDatabaseConnection.getExpiredProductPackages())
			.thenThrow(new CriticalError());
		} catch (CriticalError e) {
			//success
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_ERR, out.getResultDescriptor());
	}
}
