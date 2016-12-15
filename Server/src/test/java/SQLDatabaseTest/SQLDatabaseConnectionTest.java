package SQLDatabaseTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import SQLDatabase.SQLDatabaseConnection;

/**
 * @author Noam Yefet
 */
public class SQLDatabaseConnectionTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInitialize() {
		@SuppressWarnings("unused")
		SQLDatabaseConnection sqlConnection = new SQLDatabaseConnection();
	}

}
