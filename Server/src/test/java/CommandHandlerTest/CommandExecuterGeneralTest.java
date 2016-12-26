package CommandHandlerTest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gson.Gson;

import BasicCommonClasses.Login;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import CommandHandler.CommandExecuter;

public class CommandExecuterGeneralTest {

	@Test
	public void sqlDatabaseConnectionNullTest() {
		assertEquals(ResultDescriptor.SM_ERR,
				new CommandExecuter(new CommandWrapper(0, CommandDescriptor.LOGIN,
						new Gson().toJson(new Login("admin", "admin"), Login.class)).serialize()).execute(null).getResultDescriptor());
	}
}
