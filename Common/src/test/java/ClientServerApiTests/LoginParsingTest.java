package ClientServerApiTests;

import org.junit.Test;

import com.google.gson.Gson;

import ClientServerApi.ClientCommands;

public class LoginParsingTest {
	/**
	 * This is a simple test for verifying gson "does the work" and parses the
	 * login command object to the appropriate json format:
	 * 
	 * @author idan atias
	 * @author shimon azulay
	 */
	@Test
	public void LoginParseTest() {
		System.out.println((new Gson()).toJson((new ClientCommands().new LoginCommand("user", "pass"))));
	}

}
