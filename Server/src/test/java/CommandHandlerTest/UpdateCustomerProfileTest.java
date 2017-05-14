package CommandHandlerTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.HashSet;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gson.Gson;

import BasicCommonClasses.CustomerProfile;
import BasicCommonClasses.ForgetPassword;
import BasicCommonClasses.Ingredient;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import CommandHandler.CommandExecuter;
import SQLDatabase.SQLDatabaseConnection;
import SQLDatabase.SQLDatabaseException.ClientNotExist;
import SQLDatabase.SQLDatabaseException.CriticalError;
import SQLDatabase.SQLDatabaseException.IngredientNotExist;

@RunWith(MockitoJUnitRunner.class)
public class UpdateCustomerProfileTest {

	@Mock
	private SQLDatabaseConnection sqlDatabaseConnection;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
	}
	
	private static final CustomerProfile customerProfile = new CustomerProfile("userName", "password",
			"firstName", "lastName", "phoneNumber", "emailAddress",
			"city", "street", LocalDate.now(), new HashSet<Ingredient>(),
			new ForgetPassword("q", "a"));
	private static final int senderID = 1;
	
	@Test
	public void updateCustomerProfileSuccessfulTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.UPDATE_CUSTOMER_PROFILE,
				new Gson().toJson(customerProfile, CustomerProfile.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doNothing().when(sqlDatabaseConnection).setCustomerProfile(customerProfile.getUserName(), customerProfile);
		} catch (CriticalError | ClientNotExist | IngredientNotExist e1) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_OK, out.getResultDescriptor());
	}
	
	@Test
	public void updateCustomerProfileCriticalErrorTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.UPDATE_CUSTOMER_PROFILE,
				new Gson().toJson(customerProfile, CustomerProfile.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new CriticalError()).when(sqlDatabaseConnection).setCustomerProfile(customerProfile.getUserName(), customerProfile);
		} catch (ClientNotExist | IngredientNotExist e1) {
			fail();
		} catch (CriticalError e) {
			/* success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_ERR, out.getResultDescriptor());
	}
	
	@Test
	public void updateCustomerProfileClientNotExistTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.UPDATE_CUSTOMER_PROFILE,
				new Gson().toJson(customerProfile, CustomerProfile.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new ClientNotExist()).when(sqlDatabaseConnection).setCustomerProfile(customerProfile.getUserName(), customerProfile);
		} catch (CriticalError | IngredientNotExist e1) {
			fail();
		} catch (ClientNotExist e) {
			/* success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_USERNAME_DOES_NOT_EXIST, out.getResultDescriptor());
	}
	
	@Test
	public void updateCustomerProfileIngredientNotExistTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.UPDATE_CUSTOMER_PROFILE,
				new Gson().toJson(customerProfile, CustomerProfile.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new IngredientNotExist()).when(sqlDatabaseConnection).setCustomerProfile(customerProfile.getUserName(), customerProfile);
		} catch (CriticalError | ClientNotExist e1) {
			fail();
		} catch (IngredientNotExist e) {
			/* success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_INVALID_PARAMETER, out.getResultDescriptor());
	}
	
	@Test
	public void removeCatalogProductIllegalCatalogProductTest() {
		assertEquals(ResultDescriptor.SM_ERR,
				(new CommandExecuter(new CommandWrapper(1, CommandDescriptor.UPDATE_CUSTOMER_PROFILE,
						new Gson().toJson("", String.class)).serialize())).execute(sqlDatabaseConnection)
								.getResultDescriptor());
	}
}