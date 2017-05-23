package WorkerUnitTests;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import EmployeeContracts.IWorker;
import EmployeeDefs.WorkerDefs;
import EmployeeImplementations.Worker;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.ForgotPasswordHandler.NoSuchUserName;
import UtilsImplementations.Serialization;

/**
 * @author idan atias
 *
 * @since May 23, 2017
 * 
 *        this class is for testing the forgot password mechanism of an employee
 */
@RunWith(MockitoJUnitRunner.class)
public class ForgotPasswordTest {
	private IWorker worker;
	private static String authQuestion = String.valueOf("what is the color of your car?");

	@Mock
	private IClientRequestHandler clientRequestHandler;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
		worker = new Worker(clientRequestHandler);
	}

	@Test
	public void getAuthQuestionTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(new CommandWrapper(WorkerDefs.loginCommandSenderId,
					CommandDescriptor.FORGOT_PASSWORD_GET_QUESTION, Serialization.serialize(worker.getUsername())).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK, authQuestion).serialize());
		} catch (IOException ¢) {
			fail();
		}
		String question = null;
		try {
			question = worker.getForgotPasswordQuestion();
		} catch (NoSuchUserName e) {
			fail();
		}
		assert(question.equals(authQuestion));
	}

}
