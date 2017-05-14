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

import BasicCommonClasses.Manufacturer;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import CommandHandler.CommandExecuter;
import SQLDatabase.SQLDatabaseConnection;
import SQLDatabase.SQLDatabaseException.ClientNotConnected;
import SQLDatabase.SQLDatabaseException.CriticalError;

@RunWith(MockitoJUnitRunner.class)
public class AddNewManufacturerTest {

	@Mock
	private SQLDatabaseConnection sqlDatabaseConnection;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
	}
	
	private static final Manufacturer manufacturer = new Manufacturer(0, "Manu");
	private static final int senderID = 1;
	
	@Test
	public void addNewManufacturerSuccessfulTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.ADD_MANUFACTURER,
				new Gson().toJson(manufacturer, Manufacturer.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(sqlDatabaseConnection.addManufacturer(senderID, manufacturer.getName()))
			.thenReturn("0");
		} catch (CriticalError | ClientNotConnected e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_OK, out.getResultDescriptor());
	}
	
	@Test
	public void addNewManufacturerCriticalErrorTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.ADD_MANUFACTURER,
				new Gson().toJson(manufacturer, Manufacturer.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(sqlDatabaseConnection.addManufacturer(senderID, manufacturer.getName()))
			.thenThrow(new CriticalError());
		} catch (ClientNotConnected e) {
			fail();
		} catch (CriticalError e) {
			/* success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_ERR, out.getResultDescriptor());
	}
	
	@Test
	public void addNewManufacturerClientNotConnectedrTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.ADD_MANUFACTURER,
				new Gson().toJson(manufacturer, Manufacturer.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.when(sqlDatabaseConnection.addManufacturer(senderID, manufacturer.getName()))
			.thenThrow(new ClientNotConnected());
		} catch (CriticalError e) {
			fail();
		} catch (ClientNotConnected e) {
			/* success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED, out.getResultDescriptor());
	}
	
	@Test
	public void addNewManufacturerIllegalManufacturerTest() {
		assertEquals(ResultDescriptor.SM_ERR,
				(new CommandExecuter(new CommandWrapper(1, CommandDescriptor.ADD_MANUFACTURER,
						new Gson().toJson("", String.class)).serialize())).execute(sqlDatabaseConnection)
								.getResultDescriptor());
	}
}