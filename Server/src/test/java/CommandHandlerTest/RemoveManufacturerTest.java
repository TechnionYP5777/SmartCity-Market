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
import SQLDatabase.SQLDatabaseException.ManufacturerNotExist;
import SQLDatabase.SQLDatabaseException.ManufacturerStillUsed;

@RunWith(MockitoJUnitRunner.class)
public class RemoveManufacturerTest {

	@Mock
	private SQLDatabaseConnection sqlDatabaseConnection;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
	}
	
	private static final Manufacturer manufacturer = new Manufacturer(0, "Manu");
	private static final int senderID = 1;
	
	@Test
	public void removeManufacturerSuccessfulTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.REMOVE_MANUFACTURER,
				new Gson().toJson(manufacturer, Manufacturer.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doNothing().when(sqlDatabaseConnection).removeManufacturer(senderID, manufacturer);
		} catch (CriticalError | ClientNotConnected | ManufacturerNotExist | ManufacturerStillUsed e1) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_OK, out.getResultDescriptor());
	}
	
	@Test
	public void removeManufacturerCriticalErrorTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.REMOVE_MANUFACTURER,
				new Gson().toJson(manufacturer, Manufacturer.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new CriticalError()).when(sqlDatabaseConnection).removeManufacturer(senderID, manufacturer);
		} catch (ClientNotConnected | ManufacturerNotExist | ManufacturerStillUsed e1) {
			fail();
		} catch (CriticalError e) {
			/* success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_ERR, out.getResultDescriptor());
	}
	
	@Test
	public void removeManufacturerClientNotConnectedTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.REMOVE_MANUFACTURER,
				new Gson().toJson(manufacturer, Manufacturer.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new ClientNotConnected()).when(sqlDatabaseConnection).removeManufacturer(senderID, manufacturer);
		} catch (CriticalError | ManufacturerNotExist | ManufacturerStillUsed e1) {
			fail();
		} catch (ClientNotConnected e) {
			/* success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED, out.getResultDescriptor());
	}
	
	@Test
	public void removeManufacturerManufacturerNotExistTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.REMOVE_MANUFACTURER,
				new Gson().toJson(manufacturer, Manufacturer.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new ManufacturerNotExist()).when(sqlDatabaseConnection).removeManufacturer(senderID, manufacturer);
		} catch (CriticalError | ClientNotConnected | ManufacturerStillUsed e1) {
			fail();
		} catch (ManufacturerNotExist e) {
			/* success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.PARAM_ID_IS_NOT_EXIST, out.getResultDescriptor());
	}
	
	@Test
	public void removeManufacturerManufacturerStillUsedTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.REMOVE_MANUFACTURER,
				new Gson().toJson(manufacturer, Manufacturer.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new ManufacturerStillUsed()).when(sqlDatabaseConnection).removeManufacturer(senderID, manufacturer);
		} catch (CriticalError | ClientNotConnected | ManufacturerNotExist e1) {
			fail();
		} catch (ManufacturerStillUsed e) {
			/* success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_MANUFACTURER_STILL_IN_USE, out.getResultDescriptor());
	}
	
	@Test
	public void removeManufacturerIllegalIngredientTest() {
		assertEquals(ResultDescriptor.SM_ERR,
				(new CommandExecuter(new CommandWrapper(1, CommandDescriptor.REMOVE_MANUFACTURER,
						new Gson().toJson("", String.class)).serialize())).execute(sqlDatabaseConnection)
								.getResultDescriptor());
	}
}