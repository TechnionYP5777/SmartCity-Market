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

import BasicCommonClasses.Ingredient;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import CommandHandler.CommandExecuter;
import SQLDatabase.SQLDatabaseConnection;
import SQLDatabase.SQLDatabaseException.ClientNotConnected;
import SQLDatabase.SQLDatabaseException.CriticalError;
import SQLDatabase.SQLDatabaseException.IngredientNotExist;

/**
 * @author Aviad Cohen
 * @since 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class EditIngredientTest {

	@Mock
	private SQLDatabaseConnection sqlDatabaseConnection;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
	}
	
	private static final Ingredient ingredient = new Ingredient(0, "ingre");
	private static final int senderID = 1;
	
	@Test
	public void editIngredientSuccessfulTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.EDIT_INGREDIENT,
				new Gson().toJson(ingredient, Ingredient.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doNothing().when(sqlDatabaseConnection).editIngredient(senderID, ingredient);
		} catch (CriticalError | ClientNotConnected | IngredientNotExist e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_OK, out.getResultDescriptor());
	}
	
	@Test
	public void editIngredientCriticalErrorTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.EDIT_INGREDIENT,
				new Gson().toJson(ingredient, Ingredient.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new CriticalError()) .when(sqlDatabaseConnection).editIngredient(senderID, ingredient);
		} catch (ClientNotConnected | IngredientNotExist e1) {
			fail();
		} catch (CriticalError e) {
			/* success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_ERR, out.getResultDescriptor());
	}
	
	@Test
	public void editIngredientClientNotConnectedTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.EDIT_INGREDIENT,
				new Gson().toJson(ingredient, Ingredient.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new ClientNotConnected()) .when(sqlDatabaseConnection).editIngredient(senderID, ingredient);
		} catch (CriticalError | IngredientNotExist e1) {
			fail();
		} catch (ClientNotConnected e) {
			/* success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED, out.getResultDescriptor());
	}
	
	@Test
	public void editIngredientIngredientNotExistTest() {
		String command = new CommandWrapper(senderID, CommandDescriptor.EDIT_INGREDIENT,
				new Gson().toJson(ingredient, Ingredient.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new IngredientNotExist()) .when(sqlDatabaseConnection).editIngredient(senderID, ingredient);
		} catch (CriticalError | ClientNotConnected e1) {
			fail();
		} catch (IngredientNotExist e) {
			/* success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.PARAM_ID_IS_NOT_EXIST, out.getResultDescriptor());
	}
	
	@Test
	public void editIngredientIllegalIngredientTest() {
		assertEquals(ResultDescriptor.SM_ERR,
				(new CommandExecuter(new CommandWrapper(1, CommandDescriptor.EDIT_INGREDIENT,
						new Gson().toJson("", String.class)).serialize())).execute(sqlDatabaseConnection)
								.getResultDescriptor());
	}
}