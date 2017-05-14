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

@RunWith(MockitoJUnitRunner.class)
public class EditManufacturerTest {

	@Mock
	private SQLDatabaseConnection sqlDatabaseConnection;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
	}
	
	private static final Manufacturer manufacturer = new Manufacturer(0, "Manu");
	private static final int senderID = 1;
	
	@Test
	public void editManufacturerSuccessfulTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.EDIT_MANUFACTURER,
				new Gson().toJson(manufacturer, Manufacturer.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doNothing().when(sqlDatabaseConnection).editManufacturer(senderID, manufacturer);
		} catch (CriticalError | ClientNotConnected | ManufacturerNotExist e1) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_OK, out.getResultDescriptor());
	}
	
	@Test
	public void editManufacturerCriticalErrorTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.EDIT_MANUFACTURER,
				new Gson().toJson(manufacturer, Manufacturer.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new CriticalError()).when(sqlDatabaseConnection).editManufacturer(senderID, manufacturer);
		} catch (ClientNotConnected | ManufacturerNotExist e1) {
			fail();
		} catch (CriticalError e) {
			/* success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_ERR, out.getResultDescriptor());
	}
	
	@Test
	public void editManufacturerClientNotConnectedTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.EDIT_MANUFACTURER,
				new Gson().toJson(manufacturer, Manufacturer.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new ClientNotConnected()).when(sqlDatabaseConnection).editManufacturer(senderID, manufacturer);
		} catch (CriticalError | ManufacturerNotExist e1) {
			fail();
		} catch (ClientNotConnected e) {
			/* success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED, out.getResultDescriptor());
	}
	
	@Test
	public void editManufacturerManufacturerNotExistTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.EDIT_MANUFACTURER,
				new Gson().toJson(manufacturer, Manufacturer.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new ManufacturerNotExist()).when(sqlDatabaseConnection).editManufacturer(senderID, manufacturer);
		} catch (CriticalError | ClientNotConnected e1) {
			fail();
		} catch (ManufacturerNotExist e) {
			/* success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.PARAM_ID_IS_NOT_EXIST, out.getResultDescriptor());
	}
		
	@Test
	public void editManufacturerIllegalIngredientTest() {
		assertEquals(ResultDescriptor.SM_ERR,
				(new CommandExecuter(new CommandWrapper(1, CommandDescriptor.EDIT_MANUFACTURER,
						new Gson().toJson("", String.class)).serialize())).execute(sqlDatabaseConnection)
								.getResultDescriptor());
	}
}